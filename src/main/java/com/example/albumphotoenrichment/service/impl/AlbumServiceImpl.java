package com.example.albumphotoenrichment.service.impl;

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
import com.example.albumphotoenrichment.exception.AlbumServiceException;
import com.example.albumphotoenrichment.factory.ExecutorServiceFactory;
import com.example.albumphotoenrichment.factory.RestTemplateFactory;
import com.example.albumphotoenrichment.model.Album;
import com.example.albumphotoenrichment.model.Photo;
import com.example.albumphotoenrichment.repository.AlbumRepository;
import com.example.albumphotoenrichment.service.AlbumService;

@Service
public class AlbumServiceImpl implements AlbumService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AlbumServiceImpl.class);

	private final RestTemplate restTemplate;
	private final AlbumRepository albumRepository;
	private final ExecutorService executorService;

	@Autowired
	public AlbumServiceImpl(RestTemplateFactory restTemplateFactory, AlbumRepository albumRepository,
			ExecutorServiceFactory executorServiceFactory) {
		this.restTemplate = restTemplateFactory.createRestTemplate();
		this.albumRepository = albumRepository;
		this.executorService = executorServiceFactory.createExecutorService();
	}

	@Override
	@Cacheable(AlbumPhotoConstants.CACHE_ALBUMS)
	public CompletableFuture<List<Album>> fetchAlbumsAsync() {
		return CompletableFuture.supplyAsync(() -> {
			Album[] albumArray = restTemplate.getForObject(AlbumPhotoConstants.ALBUMS_URL, Album[].class);
			return Arrays.asList(albumArray);
		}, executorService);
	}

	@Override
	@Cacheable(AlbumPhotoConstants.CACHE_PHOTOS)
	public CompletableFuture<List<Photo>> fetchPhotosAsync() {
		return CompletableFuture.supplyAsync(() -> {
			Photo[] photoArray = restTemplate.getForObject(AlbumPhotoConstants.PHOTOS_URL, Photo[].class);
			return Arrays.asList(photoArray);
		}, executorService);
	}

	@Override
	public List<Album> enrichAlbumsAndSave() {
		long iniTime = System.nanoTime();
		List<Album> albums = saveAlbums(enrichAlbums(fetchAlbumsAsync(), fetchPhotosAsync()));
		LOGGER.info("FIN EJECUCION DEL METODO enrichAlbumsAndSave() - TIEMPO: {} ns", System.nanoTime() - iniTime);
		return albums;
	}

	@Override
	public List<Album> enrichAlbums() {
		long iniTime = System.nanoTime();
		List<Album> albums = enrichAlbums(fetchAlbumsAsync(), fetchPhotosAsync());
		LOGGER.info("FIN EJECUCION DEL METODO enrichAlbums() - TIEMPO: {} ns", System.nanoTime() - iniTime);
		return albums;
	}

	@Override
	@CacheEvict(value = { AlbumPhotoConstants.CACHE_ALBUMS, AlbumPhotoConstants.CACHE_PHOTOS }, allEntries = true)
	public void evictCache() {
		// Este método limpia la caché
	}

	@Override
	public List<Album> refreshAlbumsAndSave() {
		evictCache(); // Limpia la caché antes de recargar
		return enrichAlbumsAndSave(); // Recarga y procesa los datos
	}

	@Override
	public List<Album> refreshAlbums() {
		evictCache(); // Limpia la caché antes de recargar
		return enrichAlbums(); // Recarga y procesa los datos
	}

	private List<Album> enrichAlbums(CompletableFuture<List<Album>> albumsFuture,
			CompletableFuture<List<Photo>> photosFuture) {
		CompletableFuture<Void> allOf = CompletableFuture.allOf(albumsFuture, photosFuture);
		allOf.join(); // Espera a que ambas tareas se completen

		List<Album> albums;
		try {
			albums = albumsFuture.get();
			List<Photo> photos = photosFuture.get();

			Map<Long, List<Photo>> photosByAlbum = photos.parallelStream()
					.collect(Collectors.groupingByConcurrent(Photo::getAlbumId));

			albums.parallelStream().forEach(album -> album.setPhotos(photosByAlbum.get(album.getId())));
			return albums;
		} catch (InterruptedException | ExecutionException e) {
			throw new AlbumServiceException(AlbumPhotoConstants.ERROR_PROCESS_ALBUMS_PHOTOS, e);
		} catch (Exception ex) {
			throw new AlbumServiceException(AlbumPhotoConstants.ERROR_GENERIC, ex);
		}
	}

	private List<Album> saveAlbums(List<Album> albums) {
		return albumRepository.saveAll(albums);
	}
}
