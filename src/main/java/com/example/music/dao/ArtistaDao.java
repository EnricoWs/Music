package com.example.music.dao;

import java.sql.SQLException;
import java.util.Set;

import com.example.music.model.Artista;

public interface ArtistaDao {

	Artista save(Artista artista) throws SQLException;

	Artista findById(Integer artistaId) throws SQLException;

	Artista update(Integer artistaId, Artista artista) throws SQLException;

	Artista deleteById(Integer artistaId) throws SQLException;

	Set<Artista> findAll() throws SQLException;

}
