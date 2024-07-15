package com.example.albumphotoenrichment.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

/**
 * Modelo que representa una foto.
 */
@Entity
public class Photo {
	@Id
	private Long id;
	private Long albumId;
	private String title;
	private String url;
	private String thumbnailUrl;

	/**
	 * Constructor por defecto
	 */
	public Photo() {
	}

	public Photo(Long id, Long albumId, String title, String url, String thumbnailUrl) {
		this.id = id;
		this.albumId = albumId;
		this.title = title;
		this.url = url;
		this.thumbnailUrl = thumbnailUrl;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAlbumId() {
		return albumId;
	}

	public void setAlbumId(Long albumId) {
		this.albumId = albumId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

}
