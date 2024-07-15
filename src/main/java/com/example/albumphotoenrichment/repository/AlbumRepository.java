package com.example.albumphotoenrichment.repository;

import com.example.albumphotoenrichment.model.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para gestionar las operaciones CRUD en la base de datos para los
 * albumes.
 */
@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {
}