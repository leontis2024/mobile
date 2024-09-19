package com.aula.leontis.models.usuario;

public class UsuarioGenero {

    private long idUsuario;
    private long idGenero;

    public UsuarioGenero(long idUsuario, long idGenero) {
        this.idUsuario = idUsuario;
        this.idGenero = idGenero;
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
