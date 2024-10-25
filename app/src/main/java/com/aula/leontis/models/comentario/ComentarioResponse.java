package com.aula.leontis.models.comentario;

public class ComentarioResponse {
    private long usuarioId;
    private Comentario comentario;

    public ComentarioResponse() {
    }

    public ComentarioResponse(long usuarioId, Comentario comentario) {
        this.usuarioId = usuarioId;
        this.comentario = comentario;
    }

    public long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Comentario getComentario() {
        return comentario;
    }

    public void setComentario(Comentario comentario) {
        this.comentario = comentario;
    }
}
