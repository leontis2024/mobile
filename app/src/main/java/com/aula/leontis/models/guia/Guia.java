package com.aula.leontis.models.guia;

public class Guia {
    private long id;
    private String titulo_guia;
    private String desc_guia;
    private long id_museu;
    private String url_imagem;

    public Guia(long id, String titulo_guia, String desc_guia, long id_museu, String url_imagem) {
        this.id = id;
        this.titulo_guia = titulo_guia;
        this.desc_guia = desc_guia;
        this.id_museu = id_museu;
        this.url_imagem = url_imagem;
    }

    public Guia() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitulo_guia() {
        return titulo_guia;
    }

    public void setTitulo_guia(String titulo_guia) {
        this.titulo_guia = titulo_guia;
    }

    public String getDesc_guia() {
        return desc_guia;
    }

    public void setDesc_guia(String desc_guia) {
        this.desc_guia = desc_guia;
    }

    public long getId_museu() {
        return id_museu;
    }

    public void setId_museu(long id_museu) {
        this.id_museu = id_museu;
    }

    public String getUrl_imagem() {
        return url_imagem;
    }

    public void setUrl_imagem(String url_imagem) {
        this.url_imagem = url_imagem;
    }
}
