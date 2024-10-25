package com.aula.leontis.models.museu;

public class Museu {
    private String id;
    private String nomeMuseu;
    private String descMuseu;
    private String idEndereco;
    private String dtInauguracao;
    private String telefoneMuseu;
    private String urlImagem;

    public Museu(String id, String nomeMuseu, String urlImagem) {
        this.id = id;
        this.nomeMuseu = nomeMuseu;
        this.urlImagem = urlImagem;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNomeMuseu() {
        return nomeMuseu;
    }

    public void setNomeMuseu(String nomeMuseu) {
        this.nomeMuseu = nomeMuseu;
    }

    public String getDescMuseu() {
        return descMuseu;
    }

    public void setDescMuseu(String descMuseu) {
        this.descMuseu = descMuseu;
    }

    public String getIdEndereco() {
        return idEndereco;
    }

    public void setIdEndereco(String idEndereco) {
        this.idEndereco = idEndereco;
    }

    public String getDtInauguracao() {
        return dtInauguracao;
    }

    public void setDtInauguracao(String dtInauguracao) {
        this.dtInauguracao = dtInauguracao;
    }

    public String getTelefoneMuseu() {
        return telefoneMuseu;
    }

    public void setTelefoneMuseu(String telefoneMuseu) {
        this.telefoneMuseu = telefoneMuseu;
    }

    public String getUrlImagem() {
        return urlImagem;
    }

    public void setUrlImagem(String urlImagem) {
        this.urlImagem = urlImagem;
    }
}
