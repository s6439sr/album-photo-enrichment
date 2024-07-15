package com.example.albumphotoenrichment.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AlbumControllerIntegrationTest {

	// Inyecta MockMvc para realizar solicitudes HTTP simuladas
	@Autowired
	private MockMvc mockMvc;

	// Prueba de integración para el endpoint GET /albums
	@Test
	void testGetAlbums() throws Exception {
		// Realiza una solicitud GET a /albums y verifica el resultado
		mockMvc.perform(get("/albums")).andExpect(status().isOk()) // Verifica que el estado de la respuesta sea 200 OK
				.andExpect(jsonPath("$").isArray()); // Verifica que la respuesta sea un array JSON
	}

	// Prueba de integración para el endpoint PUT /albums/refresh
	@Test
	void testRefreshAlbums() throws Exception {
		// Realiza una solicitud PUT a /albums/refresh y verifica el resultado
		mockMvc.perform(put("/albums/refresh")).andExpect(status().isOk()) // Verifica que el estado de la respuesta sea
																			// 200 OK
				.andExpect(jsonPath("$").isArray()); // Verifica que la respuesta sea un array JSON
	}

	// Prueba de integración para el endpoint PUT /albums/refresh-and-save
	@Test
	void testRefreshAndSaveAlbums() throws Exception {
		// Realiza una solicitud PUT a /albums/refresh-and-save y verifica el resultado
		mockMvc.perform(put("/albums/refresh-and-save")).andExpect(status().isOk()) // Verifica que el estado de la
																					// respuesta sea 200 OK
				.andExpect(jsonPath("$").isArray()); // Verifica que la respuesta sea un array JSON
	}
}
