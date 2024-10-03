package com.aula.leontis.models.obra;

public class Obra {

    private String id;
    private String anoInicio;
    private String anoFinal;
    private String descObra;
    private String nomeObra;
    private String idGenero;
    private String idArtista;
    private String idMuseu;
    private String urlImagem;

    public Obra(String id, String anoInicio, String anoFinal, String descObra, String nomeObra, String idGenero, String idArtista, String idMuseu, String urlImagem) {
        this.id = id;
        this.anoInicio = anoInicio;
        this.anoFinal = anoFinal;
        this.descObra = descObra;
        this.nomeObra = nomeObra;
        this.idGenero = idGenero;
        this.idArtista = idArtista;
        this.idMuseu = idMuseu;
        this.urlImagem = urlImagem;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAnoInicio() {
        return anoInicio;
    }

    public void setAnoInicio(String anoInicio) {
        this.anoInicio = anoInicio;
    }

    public String getAnoFinal() {
        return anoFinal;
    }

    public void setAnoFinal(String anoFinal) {
        this.anoFinal = anoFinal;
    }

    public String getDescObra() {
        return descObra;
    }

    public void setDescObra(String descObra) {
        this.descObra = descObra;
    }

    public String getNomeObra() {
        return nomeObra;
    }

    public void setNomeObra(String nomeObra) {
        this.nomeObra = nomeObra;
    }

    public String getIdGenero() {
        return idGenero;
    }

    public void setIdGenero(String idGenero) {
        this.idGenero = idGenero;
    }

    public String getIdArtista() {
        return idArtista;
    }

    public void setIdArtista(String idArtista) {
        this.idArtista = idArtista;
    }

    public String getIdMuseu() {
        return idMuseu;
    }

    public void setIdMuseu(String idMuseu) {
        this.idMuseu = idMuseu;
    }

    public String getUrlImagem() {
        return urlImagem;
    }

    public void setUrlImagem(String urlImagem) {
        this.urlImagem = urlImagem;
    }
}
