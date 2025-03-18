package com.example.music.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Logger;

import com.example.music.config.MySQLConnection;
import com.example.music.model.Album;
import com.example.music.model.Artista;

public class AlbumDaoImpl implements AlbumDao {

    private static final Logger log = Logger.getLogger(AlbumDaoImpl.class.getName());

    @Override
    public Album save(Album album) throws SQLException {
        Album inserito = null;
        String query = "INSERT INTO album (titolo, data_uscita, genere, artista_id, data_inserimento, data_aggiornamento) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = MySQLConnection.open();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, album.getTitolo());
            statement.setDate(2, Date.valueOf(album.getDataUscita()));
            statement.setString(3, album.getGenere());

            if (album.getArtista() != null && album.getArtista().getArtistaId() != null) {
                statement.setInt(4, album.getArtista().getArtistaId());
            } else {
                statement.setNull(4, java.sql.Types.INTEGER);
            }

            statement.setTimestamp(5, Timestamp.valueOf(album.getDataInserimento()));
            if (album.getDataAggiornamento() != null) {
                statement.setTimestamp(6, Timestamp.valueOf(album.getDataAggiornamento()));
            } else {
                statement.setTimestamp(6, null);
            }

            int righeCoinvolte = statement.executeUpdate();
            Integer albumId = -1;
            if (righeCoinvolte > 0) {
                ResultSet chiavi = statement.getGeneratedKeys();
                if (chiavi.next()) {
                    albumId = chiavi.getInt(1);
                    inserito = new Album();
                    inserito.setAlbumId(albumId);
                    inserito.setTitolo(album.getTitolo());
                    inserito.setDataUscita(album.getDataUscita());
                    inserito.setGenere(album.getGenere());
                    inserito.setArtista(album.getArtista());
                    inserito.setDataInserimento(album.getDataInserimento());
                    inserito.setDataAggiornamento(album.getDataAggiornamento());
                    log.info("Creato l'album con ID: " + albumId);
                } else {
                    log.warning("Non è stato possibile recuperare la chiave primaria");
                }
                chiavi.close();
            } else {
                log.severe("Non è stato possibile inserire l'album");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return inserito;
    }

    @Override
    public Album findById(Integer albumId) throws SQLException {
        Album trovato = null;
        String query = "SELECT titolo, data_uscita, genere, artista_id, data_inserimento, data_aggiornamento FROM album WHERE album_id = ?";
        try (Connection connection = MySQLConnection.open();
             PreparedStatement statement = creaPerFindById(connection, query, albumId);
             ResultSet result = statement.executeQuery()) {

            if (result.next()) {
                trovato = new Album();
                trovato.setAlbumId(albumId);
                trovato.setTitolo(result.getString("titolo"));
                java.sql.Date sqlDate = result.getDate("data_uscita");
                trovato.setDataUscita(sqlDate != null ? sqlDate.toLocalDate() : null);
                trovato.setGenere(result.getString("genere"));

                int artistaId = result.getInt("artista_id");
                Artista artista = new Artista();
                artista.setArtistaId(artistaId);
                trovato.setArtista(artista);

                Timestamp tsInserimento = result.getTimestamp("data_inserimento");
                trovato.setDataInserimento(tsInserimento != null ? tsInserimento.toLocalDateTime() : null);
                Timestamp tsAggiornamento = result.getTimestamp("data_aggiornamento");
                trovato.setDataAggiornamento(tsAggiornamento != null ? tsAggiornamento.toLocalDateTime() : null);
                log.info("Trovato l'album con ID: " + albumId);
            } else {
                log.warning("Non è stato possibile trovare l'album con ID: " + albumId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return trovato;
    }

    private PreparedStatement creaPerFindById(Connection connection, String query, Integer albumId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, albumId);
        return statement;
    }

    @Override
    public Album update(Integer albumId, Album album) throws SQLException {
        Album aggiornato = null;
        String query = "UPDATE album SET titolo = ?, data_uscita = ?, genere = ?, artista_id = ?, data_inserimento = ?, data_aggiornamento = ? WHERE album_id = ?";
        try (Connection connection = MySQLConnection.open();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, album.getTitolo());
            statement.setDate(2, Date.valueOf(album.getDataUscita()));
            statement.setString(3, album.getGenere());

            if (album.getArtista() != null && album.getArtista().getArtistaId() != null) {
                statement.setInt(4, album.getArtista().getArtistaId());
            } else {
                statement.setNull(4, java.sql.Types.INTEGER);
            }

            statement.setTimestamp(5, Timestamp.valueOf(album.getDataInserimento()));
            if (album.getDataAggiornamento() != null) {
                statement.setTimestamp(6, Timestamp.valueOf(album.getDataAggiornamento()));
            } else {
                statement.setTimestamp(6, null);
            }
            statement.setInt(7, albumId);

            int righeCoinvolte = statement.executeUpdate();
            if (righeCoinvolte > 0) {
                aggiornato = new Album();
                aggiornato.setAlbumId(albumId);
                aggiornato.setTitolo(album.getTitolo());
                aggiornato.setDataUscita(album.getDataUscita());
                aggiornato.setGenere(album.getGenere());
                aggiornato.setArtista(album.getArtista());
                aggiornato.setDataInserimento(album.getDataInserimento());
                aggiornato.setDataAggiornamento(album.getDataAggiornamento());
                log.info("Aggiornato l'album con ID: " + albumId);
            } else {
                log.warning("Non è stato possibile aggiornare l'album con ID: " + albumId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return aggiornato;
    }

    @Override
    public Album deleteById(Integer albumId) throws SQLException {
        Album rimosso = null;
        String querySelect = "SELECT titolo, data_uscita, genere, artista_id, data_inserimento, data_aggiornamento FROM album WHERE album_id = ?";
        String queryDelete = "DELETE FROM album WHERE album_id = ?";

        try (Connection connection = MySQLConnection.open();
             PreparedStatement statementSelect = creaPerFindById(connection, querySelect, albumId);
             PreparedStatement statementDelete = creaPerDeleteById(connection, queryDelete, albumId);
             ResultSet result = statementSelect.executeQuery()) {

            if (result.next()) {
                rimosso = new Album();
                rimosso.setAlbumId(albumId);
                rimosso.setTitolo(result.getString("titolo"));
                java.sql.Date sqlDate = result.getDate("data_uscita");
                rimosso.setDataUscita(sqlDate != null ? sqlDate.toLocalDate() : null);
                rimosso.setGenere(result.getString("genere"));

                int artistaId = result.getInt("artista_id");
                Artista artista = new Artista();
                artista.setArtistaId(artistaId);
                rimosso.setArtista(artista);

                Timestamp tsInserimento = result.getTimestamp("data_inserimento");
                rimosso.setDataInserimento(tsInserimento != null ? tsInserimento.toLocalDateTime() : null);
                Timestamp tsAggiornamento = result.getTimestamp("data_aggiornamento");
                rimosso.setDataAggiornamento(tsAggiornamento != null ? tsAggiornamento.toLocalDateTime() : null);

                statementDelete.executeUpdate();
                log.info("Rimosso l'album con ID: " + albumId);
            } else {
                log.warning("Non è stato possibile trovare l'album con ID: " + albumId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rimosso;
    }

    private PreparedStatement creaPerDeleteById(Connection connection, String query, Integer albumId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, albumId);
        return statement;
    }

    @Override
    public Set<Album> findAll() throws SQLException {
        Set<Album> albums = new LinkedHashSet<>();
        String query = "SELECT * FROM album ORDER BY data_uscita";

        try (Connection connection = MySQLConnection.open();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                Album album = new Album();
                album.setAlbumId(result.getInt("album_id"));
                album.setTitolo(result.getString("titolo"));
                java.sql.Date sqlDate = result.getDate("data_uscita");
                album.setDataUscita(sqlDate != null ? sqlDate.toLocalDate() : null);
                album.setGenere(result.getString("genere"));

                int artistaId = result.getInt("artista_id");
                Artista artista = new Artista();
                artista.setArtistaId(artistaId);
                album.setArtista(artista);

                Timestamp tsInserimento = result.getTimestamp("data_inserimento");
                album.setDataInserimento(tsInserimento != null ? tsInserimento.toLocalDateTime() : null);
                Timestamp tsAggiornamento = result.getTimestamp("data_aggiornamento");
                album.setDataAggiornamento(tsAggiornamento != null ? tsAggiornamento.toLocalDateTime() : null);
                albums.add(album);
            }
            log.info("Trovati " + albums.size() + " album");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return albums;
    }
}
