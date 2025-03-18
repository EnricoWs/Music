package com.example.music.app;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Set;

import com.example.music.dao.AlbumDao;
import com.example.music.dao.AlbumDaoImpl;
import com.example.music.dao.ArtistaDao;
import com.example.music.dao.ArtistaDaoImpl;
import com.example.music.model.Album;
import com.example.music.model.Artista;

public class MusicApp {

	public static void main(String[] args) throws SQLException {
		Artista artista = new Artista();
		artista.setArtistaId(2);
		artista.setNome("Metallica");
		artista.setNazione("USA");
		artista.setAnnoInizio(1986);
		artista.setDataInserimento(LocalDateTime.now());

		Album album = new Album();
		album.setTitolo("Master of Puppets");
		album.setDataUscita(new Date(86,2,3).toLocalDate());
		album.setGenere("ROCK");
		album.setArtista(artista);
		album.setDataInserimento(LocalDateTime.now());

		AlbumDao albumDao = new AlbumDaoImpl();
		albumDao.update(1, album);
/*


		// findAll
		ArtistaDao artistaDao = new ArtistaDaoImpl();
		try {
			Set<Artista> artisti = artistaDao.findAll();
			for (Artista artista : artisti) {
				System.out.println(artista);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

 */
	}
}
