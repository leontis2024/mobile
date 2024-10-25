package com.aula.leontis.models.comentario;


public class Comentario {
    private long obraId;
    private String texto;
    private String dataComentario;

    public Comentario(long obraId, String texto, String dataComentario) {
        this.obraId = obraId;
        this.texto = texto;
        this.dataComentario = dataComentario;
    }

    public Comentario() {
    }

    public long getObraId() {
        return obraId;
    }

    public void setObraId(long obraId) {
        this.obraId = obraId;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getDataComentario() {
        return dataComentario;
    }

    public void setDataComentario(String dataComentario) {
        this.dataComentario = dataComentario;
    }
}

