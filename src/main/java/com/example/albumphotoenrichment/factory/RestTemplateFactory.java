package com.example.albumphotoenrichment.factory;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RestTemplateFactory {

	private static final RestTemplate INSTANCE = new RestTemplate();

	public RestTemplate createRestTemplate() {
		return INSTANCE;
	}
}
