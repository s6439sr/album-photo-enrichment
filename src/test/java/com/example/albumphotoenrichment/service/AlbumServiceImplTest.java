package com.example.albumphotoenrichment.service;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;

import com.example.albumphotoenrichment.model.Photo;
import com.example.albumphotoenrichment.repository.AlbumRepository;
import com.example.albumphotoenrichment.service.impl.AlbumServiceImpl;

@SpringBootTest
class AlbumServiceImplTest {

	@MockBean
	private RestTemplate restTemplate;

	@MockBean
	private AlbumRepository albumRepository;

	@Mock
	private ExecutorService executorService;

	@Autowired
	private AlbumServiceImpl albumService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		// Configurar el ExecutorService para las pruebas
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setCorePoolSize(1);
		taskExecutor.setMaxPoolSize(1);
		taskExecutor.initialize();
		this.executorService = taskExecutor.getThreadPoolExecutor();
	}

	@Test
	void testFetchPhotosAsync() throws Exception {
		Photo[] photoArray = { new Photo(1L, 1L, "Photo 1", "URL 1", "Thumbnail URL 1"),
				new Photo(2L, 1L, "Photo 2", "URL 2", "Thumbnail URL 2") };

		when(restTemplate.getForObject(anyString(), eq(Photo[].class))).thenReturn(photoArray);

		CompletableFuture<List<Photo>> future = albumService.fetchPhotosAsync();

		List<Photo> result = future.get();
		assert (result.size() == 2);
		verify(restTemplate, times(1)).getForObject(anyString(), eq(Photo[].class));
	}
}
