package com.example.albumphotoenrichment.factory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.stereotype.Service;

import com.example.albumphotoenrichment.constant.AlbumPhotoConstants;

/**
 * Fábrica para crear instancias de ExecutorService. Esta clase sigue el
 * patrón Singleton para proporcionar una instancia compartida de
 * ExecutorService. La instancia creada utiliza un pool de hilos fijo de
 * tamaño 10.
 */
@Service
public class ExecutorServiceFactory {

	// Instancia única de ExecutorService que se reutiliza, con un pool de hilos
	// fijo de tamaño 10.
	private static final ExecutorService INSTANCE = Executors.newFixedThreadPool(AlbumPhotoConstants.THREAD_POOL_SIZE);

	/**
	 * Crea o devuelve una instancia de ExecutorService. Este método siempre
	 * devuelve la misma instancia de ExecutorService, asegurando que solo se
	 * utilice una instancia compartida en toda la aplicación.
	 * 
	 * @return la instancia compartida de ExecutorService.
	 */
	public ExecutorService createExecutorService() {
		return INSTANCE;
	}
}