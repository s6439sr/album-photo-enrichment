package com.example.albumphotoenrichment.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.albumphotoenrichment.constant.AlbumPhotoConstants;
import com.example.albumphotoenrichment.factory.ExecutorServiceFactory;
import com.example.albumphotoenrichment.factory.RestTemplateFactory;
import com.example.albumphotoenrichment.model.Album;
import com.example.albumphotoenrichment.model.Photo;
import com.example.albumphotoenrichment.repository.AlbumRepository;

/**
 * Servicio para gestionar álbumes y fotos, proporcionando funcionalidades de
 * enriquecimiento de datos y manejo de caché.
 */
@Service
public class AlbumService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AlbumService.class);

	private final RestTemplate restTemplate;
	private final AlbumRepository albumRepository;
	private final ExecutorService executorService;

	/**
	 * Constructor que inicializa el servicio con las fábricas de RestTemplate y
	 * ExecutorService, y el repositorio de álbumes.
	 * 
	 * @param restTemplateFactory    la fábrica para crear RestTemplate
	 * @param albumRepository        el repositorio de álbumes
	 * @param executorServiceFactory la fábrica para crear ExecutorService
	 */
	@Autowired
	public AlbumService(RestTemplateFactory restTemplateFactory, AlbumRepository albumRepository,
			ExecutorServiceFactory executorServiceFactory) {
		this.restTemplate = restTemplateFactory.createRestTemplate();
		this.albumRepository = albumRepository;
		this.executorService = executorServiceFactory.createExecutorService();
	}

	/**
	 * Obtiene los álbumes de manera asíncrona y los almacena en caché.
	 * 
	 * @return un CompletableFuture con la lista de álbumes
	 */
	@Cacheable(AlbumPhotoConstants.CACHE_ALBUMS)
	CompletableFuture<List<Album>> fetchAlbumsAsync() {
		return CompletableFuture.supplyAsync(() -> {
			Album[] albumArray = restTemplate.getForObject(AlbumPhotoConstants.ALBUMS_URL, Album[].class);
			return Arrays.asList(albumArray);
		}, executorService);
	}

	/**
	 * Obtiene las fotos de manera asíncrona y las almacena en caché.
	 * 
	 * @return un CompletableFuture con la lista de fotos
	 */
	@Cacheable(AlbumPhotoConstants.CACHE_PHOTOS)
	CompletableFuture<List<Photo>> fetchPhotosAsync() {
		return CompletableFuture.supplyAsync(() -> {
			Photo[] photoArray = restTemplate.getForObject(AlbumPhotoConstants.PHOTOS_URL, Photo[].class);
			return Arrays.asList(photoArray);
		}, executorService);
	}

	/**
	 * Enriquecimiento y guardado de los álbumes con las fotos correspondientes.
	 * 
	 * @return la lista de álbumes enriquecidos y guardados
	 */
	public List<Album> enrichAlbumsAndSave() {
		long iniTime = System.nanoTime();
		List<Album> albums = saveAlbums(enrichAlbums(fetchAlbumsAsync(), fetchPhotosAsync()));
		LOGGER.info("FIN EJECUCION DEL METODO enrichAlbumsAndSave() - TIEMPO: {} ns", System.nanoTime() - iniTime);
		return albums;
	}

	/**
	 * Enriquecimiento de los álbumes con las fotos correspondientes.
	 * 
	 * @return la lista de álbumes enriquecidos
	 */
	public List<Album> enrichAlbums() {
		long iniTime = System.nanoTime();
		List<Album> albums = enrichAlbums(fetchAlbumsAsync(), fetchPhotosAsync());
		LOGGER.info("FIN EJECUCION DEL METODO enrichAlbums() - TIEMPO: {} ns", System.nanoTime() - iniTime);
		return albums;
	}

	/**
	 * Limpia todas las entradas de caché para álbumes y fotos.
	 */
	@CacheEvict(value = { AlbumPhotoConstants.CACHE_ALBUMS, AlbumPhotoConstants.CACHE_PHOTOS }, allEntries = true)
	public void evictCache() {
		// Este método limpia la caché
	}

	/**
	 * Actualiza y guarda los álbumes después de limpiar la caché.
	 * 
	 * @return la lista de álbumes actualizados y guardados
	 */
	public List<Album> refreshAlbumsAndSave() {
		evictCache(); // Limpia la caché antes de recargar
		return enrichAlbumsAndSave(); // Recarga y procesa los datos
	}

	/**
	 * Actualiza los álbumes después de limpiar la caché.
	 * 
	 * @return la lista de álbumes actualizados
	 */
	public List<Album> refreshAlbums() {
		evictCache(); // Limpia la caché antes de recargar
		return enrichAlbums(); // Recarga y procesa los datos
	}

	/**
	 * Enriquecimiento de los álbumes con las fotos correspondientes.
	 * 
	 * @param albumsFuture un CompletableFuture con la lista de álbumes
	 * @param photosFuture un CompletableFuture con la lista de fotos
	 * @return la lista de álbumes enriquecidos
	 */
	private List<Album> enrichAlbums(CompletableFuture<List<Album>> albumsFuture,
			CompletableFuture<List<Photo>> photosFuture) {
		// Espera a que ambas tareas se completen
		CompletableFuture<Void> allOf = CompletableFuture.allOf(albumsFuture, photosFuture);
		allOf.join();

		List<Album> albums;
		try {
			albums = albumsFuture.get();
			List<Photo> photos = photosFuture.get();

			// Agrupa las fotos por id de álbum
			Map<Long, List<Photo>> photosByAlbum = photos.parallelStream()
					.collect(Collectors.groupingByConcurrent(Photo::getAlbumId));

			// Asigna las fotos correspondientes a cada álbum
			albums.parallelStream().forEach(album -> album.setPhotos(photosByAlbum.get(album.getId())));
			return albums;
		} catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException(AlbumPhotoConstants.ERROR_PROCESS_ALBUMS_PHOTOS, e);
		} catch (Exception ex) {
			throw new RuntimeException(AlbumPhotoConstants.ERROR_GENERIC, ex);
		}
	}

	/**
	 * Guarda la lista de álbumes en el repositorio.
	 * 
	 * @param albums la lista de álbumes a guardar
	 * @return la lista de álbumes guardados
	 */
	private List<Album> saveAlbums(List<Album> albums) {
		return albumRepository.saveAll(albums);
	}
}
