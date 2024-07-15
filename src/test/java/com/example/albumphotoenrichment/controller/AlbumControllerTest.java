package com.example.albumphotoenrichment.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.albumphotoenrichment.service.AlbumService;

@WebMvcTest(AlbumController.class)
public class AlbumControllerTest {

	// Mock para el servicio AlbumService
	@Mock
	private AlbumService albumService;

	// Inyecta el controlador con los mocks
	@InjectMocks
	private AlbumController albumController;

	// Objeto MockMvc para realizar solicitudes HTTP simuladas
	private MockMvc mockMvc;

	// Configura los mocks y MockMvc antes de cada prueba
	@BeforeEach
	void setUp() {
		// Inicializa los mocks
		MockitoAnnotations.openMocks(this);

		// Configura MockMvc con el controlador inyectado
		mockMvc = MockMvcBuilders.standaloneSetup(albumController).build();
	}

	// Prueba para el endpoint GET /albums
	@Test
	void testGetAlbums() throws Exception {
		// Configura el mock del servicio para devolver una lista vacía
		when(albumService.enrichAlbums()).thenReturn(Collections.emptyList());

		// Realiza una solicitud GET a /albums y verifica el resultado
		mockMvc.perform(get("/albums")).andExpect(status().isOk()) // Verifica que el estado de la respuesta sea 200 OK
				.andExpect(jsonPath("$").isArray()) // Verifica que la respuesta sea un array JSON
				.andExpect(jsonPath("$").isEmpty()); // Verifica que el array JSON esté vacío
	}

	// Prueba para el endpoint PUT /albums/refresh
	@Test
	void testRefreshAlbums() throws Exception {
		// Configura el mock del servicio para devolver una lista vacía
		when(albumService.refreshAlbums()).thenReturn(Collections.emptyList());

		// Realiza una solicitud PUT a /albums/refresh y verifica el resultado
		mockMvc.perform(put("/albums/refresh")).andExpect(status().isOk()) // Verifica que el estado de la respuesta sea
																			// 200 OK
				.andExpect(jsonPath("$").isArray()) // Verifica que la respuesta sea un array JSON
				.andExpect(jsonPath("$").isEmpty()); // Verifica que el array JSON esté vacío
	}

	// Prueba para el endpoint PUT /albums/refresh-and-save
	@Test
	void testRefreshAndSaveAlbums() throws Exception {
		// Configura el mock del servicio para devolver una lista vacía
		when(albumService.refreshAlbumsAndSave()).thenReturn(Collections.emptyList());

		// Realiza una solicitud PUT a /albums/refresh-and-save y verifica el resultado
		mockMvc.perform(put("/albums/refresh-and-save")).andExpect(status().isOk()) // Verifica que el estado de la
																					// respuesta sea 200 OK
				.andExpect(jsonPath("$").isArray()) // Verifica que la respuesta sea un array JSON
				.andExpect(jsonPath("$").isEmpty()); // Verifica que el array JSON esté vacío
	}
}
