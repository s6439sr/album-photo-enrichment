package com.example.albumphotoenrichment.controller;

import com.example.albumphotoenrichment.model.Album;

import java.util.List;

public interface AlbumController {

	List<Album> getAlbums();

	List<Album> refreshAlbums();

	List<Album> refreshAndSaveAlbums();
}
