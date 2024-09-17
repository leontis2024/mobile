// GeneroCompleto.java
package com.aula.leontis.models.genero;

public class GeneroCompleto {
    private String id;
    private String nomeGenero;
    private String introducao;
    private String descGenero;
    private String urlImagem;

    // Construtores
    public GeneroCompleto(String id, String nomeGenero, String introducao, String descGenero, String urlImagem) {
        this.id = id;
        this.nomeGenero = nomeGenero;
        this.introducao = introducao;
        this.descGenero = descGenero;
        this.urlImagem = urlImagem;
    }

    // Getters e Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNomeGenero() {
        return nomeGenero;
    }

    public void setNomeGenero(String nomeGenero) {
        this.nomeGenero = nomeGenero;
    }

    public String getIntroducao() {
        return introducao;
    }

    public void setIntroducao(String introducao) {
        this.introducao = introducao;
    }

    public String getDescGenero() {
        return descGenero;
    }

    public void setDescGenero(String descGenero) {
        this.descGenero = descGenero;
    }

    public String getUrlImagem() {
        return urlImagem;
    }

    public void setUrlImagem(String urlImagem) {
        this.urlImagem = urlImagem;
    }
}
