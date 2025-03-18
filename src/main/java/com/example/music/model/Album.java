package com.example.music.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Questo è un javadoc
 * 
 * Creo una classe che rappresenta la tabella album e lo faccio sfruttando la
 * convenzione detta POJO che sarebbe la convenzione Java Beans
 * 
 * Una classe è detta Beans se ha almeno il costruttore di default e i getter e
 * setter
 */
public class Album {

	private Integer albumId;
	private String titolo;
	private LocalDate dataUscita;
	private String genere;
	private LocalDateTime dataInserimento;
	private LocalDateTime dataAggiornamento;

	private Artista artista;

	public Album() {
	}

	public Album(Integer albumId, String titolo, LocalDate dataUscita, String genere, LocalDateTime dataInserimento,
				 LocalDateTime dataAggiornamento, Artista artista) {
		this.albumId = albumId;
		this.titolo = titolo;
		this.dataUscita = dataUscita;
		this.genere = genere;
		this.dataInserimento = dataInserimento;
		this.dataAggiornamento = dataAggiornamento;
		this.artista = artista;
	}

	public Integer getAlbumId() {
		return albumId;
	}

	public void setAlbumId(Integer albumId) {
		this.albumId = albumId;
	}

	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public LocalDate getDataUscita() {
		return dataUscita;
	}

	public void setDataUscita(LocalDate dataUscita) {
		this.dataUscita = dataUscita;
	}

	public String getGenere() {
		return genere;
	}

	public void setGenere(String genere) {
		this.genere = genere;
	}

	public LocalDateTime getDataInserimento() {
		return dataInserimento;
	}

	public void setDataInserimento(LocalDateTime dataInserimento) {
		this.dataInserimento = dataInserimento;
	}

	public LocalDateTime getDataAggiornamento() {
		return dataAggiornamento;
	}

	public void setDataAggiornamento(LocalDateTime dataAggiornamento) {
		this.dataAggiornamento = dataAggiornamento;
	}

	public Artista getArtista() {
		return artista;
	}

	public void setArtista(Artista artista) {
		this.artista = artista;
	}

	@Override
	public int hashCode() {
		return Objects.hash(albumId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Album other = (Album) obj;

		return Objects.equals(albumId, other.albumId);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Album [albumId=");
		builder.append(albumId);
		builder.append(", titolo=");
		builder.append(titolo);
		builder.append(", dataUsicta=");
		builder.append(dataUscita);
		builder.append(", genere=");
		builder.append(genere);
		builder.append(", datainserimento=");
		builder.append(dataInserimento);
		builder.append(", dataAggiornamento=");
		builder.append(dataAggiornamento);
		builder.append(", artista=");
		builder.append(artista);
		builder.append("]");

		return builder.toString();
	}

}
