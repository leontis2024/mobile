package com.aula.leontis.models.guia;

public class Guia {
    private long id;
    private String tituloGuia;
    private String descGuia;
    private long idMuseu;
    private String urlImagem;

    public Guia(long id, String tituloGuia, String descGuia, long idMuseu, String urlImagem) {
        this.id = id;
        this.tituloGuia = tituloGuia;
        this.descGuia = descGuia;
        this.idMuseu = idMuseu;
        this.urlImagem = urlImagem;
    }

    public Guia() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTituloGuia() {
        return tituloGuia;
    }

    public void setTituloGuia(String tituloGuia) {
        this.tituloGuia = tituloGuia;
    }

    public String getDescGuia() {
        return descGuia;
    }

    public void setDescGuia(String descGuia) {
        this.descGuia = descGuia;
    }

    public long getIdMuseu() {
        return idMuseu;
    }

    public void setIdMuseu(long idMuseu) {
        this.idMuseu = idMuseu;
    }

    public String getUrlImagem() {
        return urlImagem;
    }

    public void setUrlImagem(String urlImagem) {
        this.urlImagem = urlImagem;
    }
}
