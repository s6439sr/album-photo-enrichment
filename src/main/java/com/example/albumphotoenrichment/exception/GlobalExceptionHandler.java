package com.example.albumphotoenrichment.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleGlobalException(Exception ex, WebRequest request) {
		LOGGER.error("Error interno no esperado: {}", ex.getMessage(), ex);
		String errorMessage = "Ocurrió un error interno. Por favor, contacte al administrador.";

		return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(AlbumServiceException.class)
	public ResponseEntity<Object> handleAlbumServiceException(AlbumServiceException ex, WebRequest request) {
		LOGGER.error("Error en el servicio de álbumes: {}", ex.getMessage(), ex);
		String errorMessage = ex.getMessage();

		return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
