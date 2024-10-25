package com.aula.leontis.models.guia;

public class ObraGuia {
    private Long id;
    private int nrOrdem;
    private String descLocalizacao;
    private Long idGuia;
    private Long idObra;

    public ObraGuia() {
    }

    public ObraGuia(Long id, int nrOrdem, String descLocalizacao, Long idGuia, Long idObra) {
        this.id = id;
        this.nrOrdem = nrOrdem;
        this.descLocalizacao = descLocalizacao;
        this.idGuia = idGuia;
        this.idObra = idObra;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getNrOrdem() {
        return nrOrdem;
    }

    public void setNrOrdem(int nrOrdem) {
        this.nrOrdem = nrOrdem;
    }

    public String getDescLocalizacao() {
        return descLocalizacao;
    }

    public void setDescLocalizacao(String descLocalizacao) {
        this.descLocalizacao = descLocalizacao;
    }

    public Long getIdGuia() {
        return idGuia;
    }

    public void setIdGuia(Long idGuia) {
        this.idGuia = idGuia;
    }

    public Long getIdObra() {
        return idObra;
    }

    public void setIdObra(Long idObra) {
        this.idObra = idObra;
    }
}
