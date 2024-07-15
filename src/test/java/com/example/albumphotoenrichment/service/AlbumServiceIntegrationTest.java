package com.example.albumphotoenrichment.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.albumphotoenrichment.model.Album;

@SpringBootTest
@ActiveProfiles("test")
class AlbumServiceIntegrationTest {

	// Inyecta la instancia de AlbumService
	@Autowired
	private AlbumService albumService;

	// Prueba para verificar que el método enrichAlbums() funciona correctamente
	@Test
	void testFetchAlbums() {
		// Llama al método enrichAlbums() para obtener la lista de álbumes enriquecidos
		List<Album> albums = albumService.enrichAlbums();

		// Verifica que la lista de álbumes no sea nula
		assertNotNull(albums);

		// Verifica que la lista de álbumes no esté vacía
		assertFalse(albums.isEmpty());
	}

	// Prueba para verificar que el método enrichAlbumsAndSave() funciona
	// correctamente
	@Test
	void testEnrichAlbumsAndSave() {
		// Llama al método enrichAlbumsAndSave() para obtener y guardar la lista de
		// álbumes enriquecidos
		List<Album> albums = albumService.enrichAlbumsAndSave();

		// Verifica que la lista de álbumes no sea nula
		assertNotNull(albums);

		// Verifica que la lista de álbumes no esté vacía
		assertFalse(albums.isEmpty());

		// Verifica que el primer álbum de la lista contenga al menos una foto
		assertTrue(albums.get(0).getPhotos().size() > 0);
	}
}
