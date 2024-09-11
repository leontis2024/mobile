package com.aula.leontis.models.usuario;

public class UsuarioGenero {

    private long id_user;
    private long id_genero;

    public UsuarioGenero(long id_user, long id_genero) {
        this.id_user = id_user;
        this.id_genero = id_genero;
    }

    public long getId_user() {
        return id_user;
    }

    public void setId_user(long id_user) {
        this.id_user = id_user;
    }

    public long getId_genero() {
        return id_genero;
    }

    public void setId_genero(long id_genero) {
        this.id_genero = id_genero;
    }
}
