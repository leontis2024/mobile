package com.aula.leontis.models.usuario;

public class UsuarioGenero {
    private long id;
    private long idUsuario;
    private long idGenero;

    public UsuarioGenero(long id, long idUsuario, long idGenero) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.idGenero = idGenero;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public long getIdGenero() {
        return idGenero;
    }

    public void setIdGenero(long idGenero) {
        this.idGenero = idGenero;
    }
}
