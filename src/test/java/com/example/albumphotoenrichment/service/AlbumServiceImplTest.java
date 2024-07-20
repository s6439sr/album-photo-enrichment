package com.example.albumphotoenrichment.service;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestTemplate;

import com.example.albumphotoenrichment.constant.AlbumPhotoConstants;
import com.example.albumphotoenrichment.model.Album;
import com.example.albumphotoenrichment.model.Photo;
import com.example.albumphotoenrichment.repository.AlbumRepository;

@WebMvcTest(AlbumService.class)
class AlbumServiceImplTest {

	@MockBean
	private AlbumService albumService;

	@MockBean
	private RestTemplate restTemplate;

	@MockBean
	private AlbumRepository albumRepository;

	@Mock
	private ExecutorService executorService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		executorService = Executors.newFixedThreadPool(AlbumPhotoConstants.THREAD_POOL_SIZE);
	}

	@Test
	void testFetchAlbumsAsync() throws Exception {
		Album[] albumArray = { new Album(1L, 1L, "Album 1"), new Album(2L, 1L, "Album 2") };

		when(restTemplate.getForObject(anyString(), eq(Album[].class))).thenReturn(albumArray);

		CompletableFuture<List<Album>> future = albumService.fetchAlbumsAsync();

		List<Album> albums = future.join(); // Esperar a que el CompletableFuture se complete

		assert (albums.size() == 2);
		verify(restTemplate, times(1)).getForObject(AlbumPhotoConstants.ALBUMS_URL, Album[].class);
	}

	@Test
	void testFetchPhotosAsync() throws Exception {
		Photo[] photoArray = { new Photo(1L, 1L, "Photo 1", "URL 1", "Thumbnail URL 1"),
				new Photo(2L, 1L, "Photo 2", "URL 2", "Thumbnail URL 2") };

		when(restTemplate.getForObject(anyString(), eq(Photo[].class))).thenReturn(photoArray);

		CompletableFuture<List<Photo>> future = albumService.fetchPhotosAsync();

		List<Photo> result = future.join();

		assert (result.size() == 2);
		verify(restTemplate, times(1)).getForObject(AlbumPhotoConstants.PHOTOS_URL, Photo[].class);
	}

	@Test
	void testrefreshAlbumsAndSave() {
		// Configurar los mocks para fetchAlbumsAsync y fetchPhotosAsync
		Album album1 = new Album(1L, 1L, "Album 1");
		Album album2 = new Album(2L, 2L, "Album 2");

		Photo photo1 = new Photo(1L, 1L, "Photo 1", "URL 1", "Thumbnail URL 1");
		Photo photo2 = new Photo(2L, 2L, "Photo 2", "URL 2", "Thumbnail URL 2");

		when(albumService.fetchAlbumsAsync())
				.thenReturn(CompletableFuture.completedFuture(Arrays.asList(album1, album2)));
		when(albumService.fetchPhotosAsync())
				.thenReturn(CompletableFuture.completedFuture(Arrays.asList(photo1, photo2)));

		when(albumRepository.saveAll(anyList())).thenReturn(Arrays.asList(album1, album2));

		List<Album> albums = albumService.refreshAlbumsAndSave();

		assert (albums.size() == 2);
		verify(albumRepository, times(1)).saveAll(anyList());
	}

	@Test
	void testEnrichAlbums() {
		// Configurar los mocks para fetchAlbumsAsync y fetchPhotosAsync
		Album album1 = new Album(1L, 1L, "Album 1");
		Album album2 = new Album(2L, 2L, "Album 2");

		Photo photo1 = new Photo(1L, 1L, "Photo 1", "URL 1", "Thumbnail URL 1");
		Photo photo2 = new Photo(2L, 2L, "Photo 2", "URL 2", "Thumbnail URL 2");

		when(albumService.fetchAlbumsAsync())
				.thenReturn(CompletableFuture.completedFuture(Arrays.asList(album1, album2)));
		when(albumService.fetchPhotosAsync())
				.thenReturn(CompletableFuture.completedFuture(Arrays.asList(photo1, photo2)));

		List<Album> albums = albumService.enrichAlbums();

		assert (albums.size() == 2);
	}

	@Test
	void testEvictCache() {
		albumService.evictCache();
		// No se puede verificar directamente el cache evict sin un spy en el objeto
		// CacheManager
		// Pero el método puede ser llamado y no debería lanzar excepciones
	}

	@Test
	void testRefreshAlbumsAndSave() {
		doNothing().when(albumService).evictCache();
		when(albumService.refreshAlbumsAndSave()).thenReturn(Arrays.asList(new Album(), new Album()));

		List<Album> albums = albumService.refreshAlbumsAndSave();

		assert (albums.size() == 2);
		verify(albumService, times(1)).evictCache();
		verify(albumService, times(1)).refreshAlbumsAndSave();
	}

	@Test
	void testRefreshAlbums() {
		doNothing().when(albumService).evictCache();
		when(albumService.enrichAlbums()).thenReturn(Arrays.asList(new Album(), new Album()));

		List<Album> albums = albumService.refreshAlbums();

		assert (albums.size() == 2);
		verify(albumService, times(1)).evictCache();
		verify(albumService, times(1)).enrichAlbums();
	}
}
