package com.example.albumphotoenrichment.factory;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Fábrica para crear instancias de RestTemplate. Esta clase sigue el patrón
 * Singleton para proporcionar una instancia compartida de RestTemplate.
 */
@Service
public class RestTemplateFactory {

	// Instancia única de RestTemplate que se reutiliza.
	private static final RestTemplate INSTANCE = new RestTemplate();

	/**
	 * Crea o devuelve una instancia de RestTemplate. Este método siempre devuelve
	 * la misma instancia de RestTemplate, asegurando que solo se utilice una
	 * instancia compartida en toda la aplicación.
	 * 
	 * @return la instancia compartida de RestTemplate.
	 */
	public RestTemplate createRestTemplate() {
		return INSTANCE;
	}
}
