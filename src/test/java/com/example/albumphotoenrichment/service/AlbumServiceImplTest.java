package com.example.albumphotoenrichment.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import com.example.albumphotoenrichment.factory.ExecutorServiceFactory;
import com.example.albumphotoenrichment.factory.RestTemplateFactory;
import com.example.albumphotoenrichment.model.Album;
import com.example.albumphotoenrichment.repository.AlbumRepository;
import com.example.albumphotoenrichment.service.impl.AlbumServiceImpl;

class AlbumServiceImplTest {

	@Mock
	private RestTemplateFactory restTemplateFactory;

	@Mock
	private AlbumRepository albumRepository;

	@Mock
	private RestTemplate restTemplate;

	@Mock
	private ExecutorServiceFactory executorServiceFactory; // Mock del factory

	@InjectMocks
	private AlbumServiceImpl albumServiceImpl;

	private ExecutorService realExecutorService; // Usamos un executor real

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		// Simulamos la creación del RestTemplate
		when(restTemplateFactory.createRestTemplate()).thenReturn(restTemplate);

		// Creamos un ExecutorService real
		realExecutorService = Executors.newSingleThreadExecutor();

		// Simulamos que el factory retorna el ExecutorService real
		when(executorServiceFactory.createExecutorService()).thenReturn(realExecutorService);

		// Inyectamos el mock del factory y los otros dependientes en AlbumServiceImpl
		albumServiceImpl = new AlbumServiceImpl(restTemplateFactory, albumRepository, executorServiceFactory);
	}

	@AfterEach
	void tearDown() {
		// Cerramos el executor después de la prueba para evitar fugas de recursos
		if (realExecutorService != null && !realExecutorService.isShutdown()) {
			realExecutorService.shutdown();
		}
	}

	@Test
	void fetchAlbumsAsync_Successful() throws Exception {
		// Arrange
		Album[] mockAlbums = { new Album(1L, 1L, "Album 1"), new Album(2L, 2L, "Album 2") };
		when(restTemplate.getForObject(anyString(), eq(Album[].class))).thenReturn(mockAlbums);

		// Act
		CompletableFuture<List<Album>> albumsFuture = albumServiceImpl.fetchAlbumsAsync();
		List<Album> albums = albumsFuture.get(); // Obtener el resultado de CompletableFuture

		// Assert
		assertNotNull(albums);
		assertEquals(2, albums.size());
		assertEquals("Album 1", albums.get(0).getTitle());
		verify(restTemplate, times(1)).getForObject(anyString(), eq(Album[].class));
	}
}
