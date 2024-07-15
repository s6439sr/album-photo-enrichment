package com.example.albumphotoenrichment.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Controlador REST para gestionar las solicitudes relacionadas con albumes y
 * fotos.
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.albumphotoenrichment.model.Album;
import com.example.albumphotoenrichment.service.AlbumService;

@RestController
@RequestMapping("/albums")
public class AlbumController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AlbumController.class);

	private final AlbumService albumService;

	@Autowired
	public AlbumController(AlbumService albumService) {
		this.albumService = albumService;
	}

	/**
	 * Obtiene los álbumes con las fotos enriquecidas. Usa el método enrichAlbums
	 * del servicio
	 * 
	 * @return Lista de albumes enriquecidos
	 */
	@GetMapping
	public List<Album> getAlbums() {
		Long iniTime = System.nanoTime();
		List<Album> albums = albumService.enrichAlbums();
		LOGGER.info("FIN EJECUCION DEL METODO getAlbums() - TIEMPO: {} ns", System.nanoTime() - iniTime);
		return albums;
	}

	/**
	 * Refresca los datos en caché y los devuelve sin guardarlos en la base de
	 * datos. Usa el método refreshAlbums del servicio
	 * 
	 * @return Lista de albumes enriquecidos
	 */
	@PutMapping("/refresh")
	public List<Album> refreshAlbums() {
		Long iniTime = System.nanoTime();
		List<Album> albums = albumService.refreshAlbums();
		LOGGER.info("FIN EJECUCION DEL METODO refreshAlbums() - TIEMPO: {} ns", System.nanoTime() - iniTime);
		return albums;
	}

	/**
	 * Refresca los datos en caché y los guarda en la base de datos. Usa el método
	 * refreshAlbumsAndSave del servicio
	 * 
	 * @return Lista de albumes enriquecidos
	 */
	@PutMapping("/refresh-and-save")
	public List<Album> refreshAndSaveAlbums() {
		Long iniTime = System.nanoTime();
		List<Album> albums = albumService.refreshAlbumsAndSave();
		LOGGER.info("FIN EJECUCION DEL METODO refreshAndSaveAlbums() - TIEMPO: {} ns", System.nanoTime() - iniTime);
		return albums;
	}
}
