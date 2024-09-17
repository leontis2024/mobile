package com.aula.leontis.models.genero;

public class Genero {
    private long id;
    private String nomeGenero;
    private boolean checkInteresse;
    private String introducao;

    public Genero(long id,String nomeGenero, boolean checkInteresse,String introducao) {
        this.id = id;
        this.nomeGenero = nomeGenero;
        this.introducao = introducao;
        this.checkInteresse = checkInteresse;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nomeGenero;
    }

    public void setNome(String nome) {
        this.nomeGenero = nomeGenero;
    }

    public String getIntroducao() {
        return introducao;
    }

    public void setIntroducao(String introducao) {
        this.introducao = introducao;
    }
    public boolean getCheckInteresse(){
        return checkInteresse;
    }

    public void setCheckInteresse(boolean checkInteresse) {
        this.checkInteresse = checkInteresse;
    }

}

