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

	@Mock
	private AlbumService albumService;

	@InjectMocks
	private AlbumController albumController;

	private MockMvc mockMvc;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(albumController).build();
	}

	@Test
	void testGetAlbums() throws Exception {
		when(albumService.enrichAlbums()).thenReturn(Collections.emptyList());

		mockMvc.perform(get("/albums")).andExpect(status().isOk()).andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$").isEmpty());
	}

	@Test
	void testRefreshAlbums() throws Exception {
		when(albumService.refreshAlbums()).thenReturn(Collections.emptyList());

		mockMvc.perform(put("/albums/refresh")).andExpect(status().isOk()).andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$").isEmpty());
	}

	@Test
	void testRefreshAndSaveAlbums() throws Exception {
		when(albumService.refreshAlbumsAndSave()).thenReturn(Collections.emptyList());

		mockMvc.perform(put("/albums/refresh-and-save")).andExpect(status().isOk()).andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$").isEmpty());
	}
}
