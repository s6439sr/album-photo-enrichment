package com.example.albumphotoenrichment.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import com.example.albumphotoenrichment.constant.AlbumPhotoConstants;
import com.example.albumphotoenrichment.factory.ExecutorServiceFactory;
import com.example.albumphotoenrichment.factory.RestTemplateFactory;
import com.example.albumphotoenrichment.model.Album;
import com.example.albumphotoenrichment.model.Photo;
import com.example.albumphotoenrichment.repository.AlbumRepository;

class AlbumServiceTest {

	@Mock
	private RestTemplateFactory restTemplateFactory;

	@Mock
	private AlbumRepository albumRepository;

	@Mock
	private ExecutorServiceFactory executorServiceFactory;

	@Mock
	private RestTemplate restTemplate;

	@InjectMocks
	private AlbumService albumService;

	@BeforeEach
	void setUp() {
		// Inicializa los mocks
		MockitoAnnotations.openMocks(this);

		// Configura los mocks para devolver instancias simuladas de RestTemplate y
		// ExecutorService
		when(restTemplateFactory.createRestTemplate()).thenReturn(restTemplate);
		when(executorServiceFactory.createExecutorService()).thenReturn(Executors.newFixedThreadPool(10));
	}

	@Test
	void testFetchAlbums() throws Exception {
		// Prepara los datos de prueba
		Album[] albumsArray = { new Album(1L, 1L, "Album1"), new Album(2L, 2L, "Album2") };

		// Configura el mock RestTemplate para devolver los datos de prueba
		when(restTemplate.getForObject(AlbumPhotoConstants.ALBUMS_URL, Album[].class)).thenReturn(albumsArray);

		// Llama al método fetchAlbumsAsync y espera a que se complete
		CompletableFuture<List<Album>> albumsFuture = albumService.fetchAlbumsAsync();
		List<Album> albums = albumsFuture.get();

		// Verifica que los datos devueltos sean correctos
		assertEquals(2, albums.size());
		assertEquals(1L, albums.get(0).getId());
		assertEquals("Album1", albums.get(0).getTitle());
	}

	@Test
	void testFetchPhotos() throws Exception {
		// Prepara los datos de prueba
		Photo[] photosArray = { new Photo(1L, 1L, "Photo1", "url1", "thumbnail1"),
				new Photo(2L, 2L, "Photo2", "url2", "thumbnail2") };

		// Configura el mock RestTemplate para devolver los datos de prueba
		when(restTemplate.getForObject(AlbumPhotoConstants.PHOTOS_URL, Photo[].class)).thenReturn(photosArray);

		// Llama al método fetchPhotosAsync y espera a que se complete
		CompletableFuture<List<Photo>> photosFuture = albumService.fetchPhotosAsync();
		List<Photo> photos = photosFuture.get();

		// Verifica que los datos devueltos sean correctos
		assertEquals(2, photos.size());
		assertEquals(1L, photos.get(0).getId());
		assertEquals("Photo1", photos.get(0).getTitle());
	}

	@Test
	void testEnrichAlbums() throws Exception {
		// Prepara los datos de prueba
		Album album1 = new Album(1L, 1L, "Album1");
		Album album2 = new Album(2L, 2L, "Album2");
		Photo photo1 = new Photo(1L, 1L, "Photo1", "url1", "thumbnail1");
		Photo photo2 = new Photo(2L, 2L, "Photo2", "url2", "thumbnail2");

		// Configura los mocks RestTemplate para devolver los datos de prueba
		when(restTemplate.getForObject(AlbumPhotoConstants.ALBUMS_URL, Album[].class))
				.thenReturn(new Album[] { album1, album2 });
		when(restTemplate.getForObject(AlbumPhotoConstants.PHOTOS_URL, Photo[].class))
				.thenReturn(new Photo[] { photo1, photo2 });

		// Llama al método enrichAlbums
		List<Album> enrichedAlbums = albumService.enrichAlbums();

		// Verifica que los álbumes enriquecidos contengan las fotos correctas
		assertEquals(2, enrichedAlbums.size());
		assertEquals(1, enrichedAlbums.get(0).getPhotos().size());
		assertEquals("Photo1", enrichedAlbums.get(0).getPhotos().get(0).getTitle());
	}
}
