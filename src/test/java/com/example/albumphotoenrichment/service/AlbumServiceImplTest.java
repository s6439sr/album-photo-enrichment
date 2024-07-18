package com.example.albumphotoenrichment.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import com.example.albumphotoenrichment.constant.AlbumPhotoConstants;
import com.example.albumphotoenrichment.model.Album;
import com.example.albumphotoenrichment.model.Photo;
import com.example.albumphotoenrichment.repository.AlbumRepository;

class AlbumServiceImplTest {

	@Mock
	private RestTemplate restTemplate;

	@Mock
	private AlbumRepository albumRepository;

	@InjectMocks
	private AlbumService albumService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testFetchAlbumsAsync() throws ExecutionException, InterruptedException {
		Album[] albums = { new Album(1L, 1L, "Album 1"), new Album(2L, 1L, "Album 2") };
		when(restTemplate.getForObject(AlbumPhotoConstants.ALBUMS_URL, Album[].class)).thenReturn(albums);

		CompletableFuture<List<Album>> futureAlbums = albumService.fetchAlbumsAsync();
		List<Album> result = futureAlbums.get();

		assertEquals(2, result.size());
		assertEquals("Album 1", result.get(0).getTitle());
		assertEquals("Album 2", result.get(1).getTitle());
		verify(restTemplate, times(1)).getForObject(AlbumPhotoConstants.ALBUMS_URL, Album[].class);
	}

	@Test
	void testFetchPhotosAsync() throws ExecutionException, InterruptedException {
		Photo[] photos = { new Photo(1L, 1L, "Photo 1", "URL 1", "Thumbnail URL 1"),
				new Photo(2L, 1L, "Photo 2", "URL 2", "Thumbnail URL 2") };
		when(restTemplate.getForObject(AlbumPhotoConstants.PHOTOS_URL, Photo[].class)).thenReturn(photos);

		CompletableFuture<List<Photo>> futurePhotos = albumService.fetchPhotosAsync();
		List<Photo> result = futurePhotos.get();

		assertEquals(2, result.size());
		assertEquals("Photo 1", result.get(0).getTitle());
		assertEquals("Photo 2", result.get(1).getTitle());
		verify(restTemplate, times(1)).getForObject(AlbumPhotoConstants.PHOTOS_URL, Photo[].class);
	}

	@Test
	void testEnrichAlbumsAndSave() {
		Album album1 = new Album(1L, 1L, "Album 1");
		Album album2 = new Album(2L, 1L, "Album 2");
		List<Album> albums = Arrays.asList(album1, album2);

		Photo photo1 = new Photo(1L, 1L, "Photo 1", "URL 1", "Thumbnail URL 1");
		Photo photo2 = new Photo(2L, 1L, "Photo 2", "URL 2", "Thumbnail URL 2");
		List<Photo> photos = Arrays.asList(photo1, photo2);

		when(restTemplate.getForObject(AlbumPhotoConstants.ALBUMS_URL, Album[].class))
				.thenReturn(albums.toArray(new Album[0]));
		when(restTemplate.getForObject(AlbumPhotoConstants.PHOTOS_URL, Photo[].class))
				.thenReturn(photos.toArray(new Photo[0]));
		when(albumRepository.saveAll(any())).thenReturn(albums);

		List<Album> result = albumService.enrichAlbumsAndSave();

		assertEquals(2, result.size());
		assertEquals(2, result.get(0).getPhotos().size());
		verify(albumRepository, times(1)).saveAll(any());
	}
}
