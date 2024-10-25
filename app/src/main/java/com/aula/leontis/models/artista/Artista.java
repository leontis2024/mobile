package com.aula.leontis.models.artista;
public class Artista {
    private long id;
    private String nomeArtista;
    private String dtNascArtista; // Pode ser null
    private String dtFalecimento; // Pode ser null
    private String localNasc; // Pode ser null
    private String localMorte; // Pode ser null
    private String descArtista; // Pode ser null
    private String urlImagem; // Pode ser null

    // Construtor
    public Artista(long id, String nomeArtista, String dtNascArtista, String dtFalecimento, String localNasc, String localMorte, String descArtista, String urlImagem) {
        this.id = id;
        this.nomeArtista = nomeArtista;
        this.dtNascArtista = dtNascArtista;
        this.dtFalecimento = dtFalecimento;
        this.localNasc = localNasc;
        this.localMorte = localMorte;
        this.descArtista = descArtista;
        this.urlImagem = urlImagem;
    }

    // Getters e Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNomeArtista() {
        return nomeArtista;
    }

    public void setNomeArtista(String nomeArtista) {
        this.nomeArtista = nomeArtista;
    }

    public String getDtNascArtista() {
        return dtNascArtista;
    }

    public void setDtNascArtista(String dtNascArtista) {
        this.dtNascArtista = dtNascArtista;
    }

    public String getDtFalecimento() {
        return dtFalecimento;
    }

    public void setDtFalecimento(String dtFalecimento) {
        this.dtFalecimento = dtFalecimento;
    }

    public String getLocalNasc() {
        return localNasc;
    }

    public void setLocalNasc(String localNasc) {
        this.localNasc = localNasc;
    }

    public String getLocalMorte() {
        return localMorte;
    }

    public void setLocalMorte(String localMorte) {
        this.localMorte = localMorte;
    }

    public String getDescArtista() {
        return descArtista;
    }

    public void setDescArtista(String descArtista) {
        this.descArtista = descArtista;
    }

    public String getUrlImagem() {
        return urlImagem;
    }

    public void setUrlImagem(String urlImagem) {
        this.urlImagem = urlImagem;
    }
}
