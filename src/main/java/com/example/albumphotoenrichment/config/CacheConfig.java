package com.example.albumphotoenrichment.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.albumphotoenrichment.constant.AlbumPhotoConstants;

import org.springframework.cache.concurrent.ConcurrentMapCacheManager;

/**
 * Configuracion de cache para la aplicacion. Habilita la cache en la aplicacion
 * y define el gestor de cache.
 */
@Configuration
@EnableCaching
public class CacheConfig {
	/**
	 * Define un gestor de cache que utiliza un ConcurrentMap para almacenar datos
	 * en cache.
	 * 
	 * @return ConcurrentMapCacheManager gestor de cache
	 */
	@Bean
	public ConcurrentMapCacheManager cacheManager() {
		return new ConcurrentMapCacheManager(AlbumPhotoConstants.CACHE_ALBUMS, AlbumPhotoConstants.CACHE_PHOTOS,
				AlbumPhotoConstants.CACHE_ALBUMS_NO_DB);
	}
}
