package com.example.music.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Logger;

import com.example.music.config.MySQLConnection;
import com.example.music.model.Artista;

public class ArtistaDaoImpl implements ArtistaDao {

	private static final Logger log = Logger.getLogger(ArtistaDaoImpl.class.getName());

	@Override
	public Artista save(Artista artista) throws SQLException {
		Artista inserito = null;

		String query = "insert into artisti (nome, nazione, anno_inizio, data_inserimento, data_aggiornamento) values (?, ?, ?, ?, ?)";

		// try with resource
		try (Connection connection = MySQLConnection.open();
				PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
			statement.setString(1, artista.getNome());
			statement.setString(2, artista.getNazione());
			statement.setInt(3, artista.getAnnoInizio());
			statement.setTimestamp(4, Timestamp.valueOf(artista.getDataInserimento()));
			statement.setTimestamp(5, null);

			int righeCoinvolte = statement.executeUpdate();

			Integer artistaId = -1;
			if (righeCoinvolte > 0) {
				ResultSet chiavi = statement.getGeneratedKeys();
				if (chiavi.next()) {
					artistaId = chiavi.getInt(1);

					inserito = new Artista();
					inserito.setArtistaId(artistaId);
					inserito.setNome(artista.getNome());
					inserito.setNazione(artista.getNazione());
					inserito.setAnnoInizio(artista.getAnnoInizio());
					inserito.setDataInserimento(artista.getDataInserimento());

					log.info("Creato l'artista con il seguente ID: " + artistaId);
				} else {
					log.warning("Non è stato possibile recuperare la chiave primaria");
				}
				chiavi.close();
			} else {
				log.severe("Non è stato possibile inserire l'artista");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return inserito;
	}

	@Override
	public Artista findById(Integer artistaId) throws SQLException {
		Artista trovato = null;

		String query = "select nome, nazione, anno_inizio, data_inserimento, data_aggiornamento from artisti where artista_id = ?";
		try (Connection connection = MySQLConnection.open();
				PreparedStatement statement = creaPerFindbyId(connection, query, artistaId);
				ResultSet result = statement.executeQuery()) {
			if (result.next()) {
				trovato = new Artista();
				trovato.setArtistaId(artistaId);
				trovato.setNome(result.getString(1));
				trovato.setNazione(result.getString(2));
				trovato.setAnnoInizio(result.getInt(3));
				trovato.setDataInserimento(result.getTimestamp(4).toLocalDateTime());
				trovato.setDataAggiornamento(result.getTimestamp(5).toLocalDateTime());

				log.info("Trovato l'artista con ID: " + artistaId);
			} else {
				log.warning("Non è stato possibilo ritrovare l'artista con l'ID: " + artistaId);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return trovato;
	}

	private PreparedStatement creaPerFindbyId(Connection connection, String query, Integer artistaId)
			throws SQLException {
		PreparedStatement statement = connection.prepareStatement(query);
		statement.setInt(1, artistaId);

		return statement;
	}

	@Override
	public Artista update(Integer artistaId, Artista artista) throws SQLException {
		Artista aggiornato = null;
		
		String query = "update artisti set nome = ?, nazione = ?, anno_inizio = ?, data_inserimento = ?, data_aggiornamento = ? where artista_id = ?";
		try (Connection connection = MySQLConnection.open();
				PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, artista.getNome());
			statement.setString(2, artista.getNazione());
			statement.setInt(3, artista.getAnnoInizio());
			statement.setTimestamp(4, Timestamp.valueOf(artista.getDataInserimento()));
			statement.setTimestamp(5, Timestamp.valueOf(artista.getDataAggiornamento()));
			statement.setInt(6, artistaId);
			
			int righeCoinvolte = statement.executeUpdate();
			if (righeCoinvolte > 0) {
				aggiornato = new Artista();
				aggiornato.setArtistaId(artistaId);
				aggiornato.setNome(artista.getNome());
				aggiornato.setNazione(artista.getNazione());
				aggiornato.setAnnoInizio(artista.getAnnoInizio());
				aggiornato.setDataInserimento(artista.getDataInserimento());
				aggiornato.setDataAggiornamento(artista.getDataAggiornamento());

				log.info("Aggiornato l'artista con il seguente ID: " + artistaId);
			} else {
				log.warning("Non è stato possibile aggiornare l'artista");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return aggiornato;
	}

	@Override
	public Artista deleteById(Integer artistaId) throws SQLException {
		Artista rimosso = null;
		
		String query1 = "select nome, nazione, anno_inizio, data_inserimento, data_aggiornamento from artisti where artista_id = ?";
		String query2 = "delete from artisti where artista_id = ?";
		try (Connection connection = MySQLConnection.open();
				PreparedStatement statement1 = creaPerFindbyId(connection, query1, artistaId);
				PreparedStatement statement2 = creaPerDeleteById(connection, query2, artistaId);
				ResultSet result = statement1.executeQuery()) {
			if (result.next()) {
				rimosso = new Artista();
				rimosso.setArtistaId(artistaId);
				rimosso.setNome(result.getString(1));
				rimosso.setNazione(result.getString(2));
				rimosso.setAnnoInizio(result.getInt(3));
				rimosso.setDataInserimento(result.getTimestamp(4).toLocalDateTime());
				rimosso.setDataAggiornamento(result.getTimestamp(5).toLocalDateTime());
				
				statement2.executeUpdate();

				log.info("Rimosso l'artista con ID: " + artistaId);
			} else {
				log.warning("Non è stato possibilo rimmuovere l'artista con l'ID: " + artistaId);
			}
		}
		
		return rimosso;
	}

	private PreparedStatement creaPerDeleteById(Connection connection, String query, Integer artistaId) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(query);
		statement.setInt(1, artistaId);
		
		return statement;
	}

	@Override
	public Set<Artista> findAll() throws SQLException {
		Set<Artista> artisti = new LinkedHashSet<Artista>();
		
		String query = "select * from artisti order by anno_inizio";
		try (Connection connection = MySQLConnection.open();
				PreparedStatement statement = connection.prepareStatement(query);
				ResultSet result = statement.executeQuery()) {
			while (result.next()) {
				Artista artista = new Artista();
				artista.setArtistaId(result.getInt(1));
				artista.setNome(result.getString(2));
				artista.setNazione(result.getString(3));
				artista.setAnnoInizio(result.getInt(4));
				artista.setDataInserimento(result.getTimestamp(5).toLocalDateTime());
				Timestamp timestamp = result.getTimestamp(6);
				if (timestamp != null)
					artista.setDataAggiornamento(result.getTimestamp(6).toLocalDateTime());
				
				artisti.add(artista);
			}
			
			log.info("Sono stati trovati " + artisti.size() + " artisti");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return artisti;
	}

}
