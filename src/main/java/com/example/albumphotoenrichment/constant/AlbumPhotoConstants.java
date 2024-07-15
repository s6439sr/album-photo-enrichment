package com.example.albumphotoenrichment.constant;

public final class AlbumPhotoConstants {

	// Instanciacion preventiva
	public AlbumPhotoConstants() {
		throw new UnsupportedOperationException("This is a constants class and cannot be instantiated");
	}

	// API URLs
	public static final String ALBUMS_URL = "https://jsonplaceholder.typicode.com/albums";
	public static final String PHOTOS_URL = "https://jsonplaceholder.typicode.com/photos";

	// Nombres de la Cache
	public static final String CACHE_ALBUMS = "albums";
	public static final String CACHE_ALBUMS_NO_DB = "albums_no_db";
	public static final String CACHE_PHOTOS = "photos";

	// Configuracion Thread pool
	public static final int THREAD_POOL_SIZE = 10;
	public static final int TIMEOUT_MS = 5000;

	// Other constants
	public static final String ALBUMS = "albums";
	public static final String PHOTOS = "photos";
	public static final int MINIMUM_ARRAY_SIZE = 0;

	// Mensajes de error
	public static final String ERROR_FETCH_ALBUMS = "Failed to fetch albums";
	public static final String ERROR_FETCH_PHOTOS = "Failed to fetch photos";
	public static final String ERROR_PROCESS_ALBUMS_PHOTOS = "Failed to fetch or process albums and photos";

}
