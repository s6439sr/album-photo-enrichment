package com.example.albumphotoenrichment.factory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.stereotype.Service;

@Service
public class ExecutorServiceFactory {

	private static final ExecutorService INSTANCE = Executors.newFixedThreadPool(10);

	public ExecutorService createExecutorService() {
		return INSTANCE;
	}
}