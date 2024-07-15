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

	@Autowired
	private MockMvc mockMvc;

	@Test
	void testGetAlbums() throws Exception {
		mockMvc.perform(get("/albums")).andExpect(status().isOk()).andExpect(jsonPath("$").isArray());
	}

	@Test
	void testRefreshAlbums() throws Exception {
		mockMvc.perform(put("/albums/refresh")).andExpect(status().isOk()).andExpect(jsonPath("$").isArray());
	}

	@Test
	void testRefreshAndSaveAlbums() throws Exception {
		mockMvc.perform(put("/albums/refresh-and-save")).andExpect(status().isOk()).andExpect(jsonPath("$").isArray());
	}
}
