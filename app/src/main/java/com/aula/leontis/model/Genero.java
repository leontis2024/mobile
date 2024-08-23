package com.aula.leontis.model;

public class Genero {
    private long id;
    private String nome;
    private boolean checkInteresse;
    private String introducao;

    public Genero(long id,String nome, boolean checkInteresse,String introducao) {
        this.id = id;
        this.nome = nome;
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
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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
