package com.example.albumphotoenrichment.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.example.albumphotoenrichment.model.Album;
import com.example.albumphotoenrichment.model.Photo;

public interface AlbumService {

	CompletableFuture<List<Album>> fetchAlbumsAsync();

	CompletableFuture<List<Photo>> fetchPhotosAsync();

	List<Album> enrichAlbumsAndSave();

	List<Album> enrichAlbums();

	void evictCache();

	List<Album> refreshAlbumsAndSave();

	List<Album> refreshAlbums();
}
