package com.aula.leontis.models.usuario;


public class Usuario {
    private String nome;
    private String sobrenome;
    private String email;
    private String telefone;
    private String dataNascimento;
    private String biografia;
    private String sexo;
    private String apelido;
    private String senha;
    private String urlImagem;

    public Usuario(String nm_usuario, String sobrenome, String email_usuario, String nr_tel_usuario, String dt_nasci_usuario, String biografia, String sexo, String apelido, String senha_usuario, String url_imagem) {
        this.nome = nm_usuario;
        this.sobrenome = sobrenome;
        this.email = email_usuario;
        this.telefone = nr_tel_usuario;
        this.dataNascimento = dt_nasci_usuario;
        this.biografia = biografia;
        this.sexo = sexo;
        this.apelido = apelido;
        this.senha = senha_usuario;
        this.urlImagem = url_imagem;
    }


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getBiografia() {
        return biografia;
    }

    public void setBiografia(String biografia) {
        this.biografia = biografia;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getApelido() {
        return apelido;
    }

    public void setApelido(String apelido) {
        this.apelido = apelido;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getUrlImagem() {
        return urlImagem;
    }

    public void setUrlImagem(String urlImagem) {
        this.urlImagem = urlImagem;
    }
}
