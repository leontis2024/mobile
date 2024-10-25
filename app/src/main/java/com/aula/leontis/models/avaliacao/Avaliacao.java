package com.aula.leontis.models.avaliacao;

public class Avaliacao {
    private long obraId;
    private double nota;
    private String dataAvaliacao;

    public Avaliacao(long obraId, double nota, String dataAvaliacao) {
        this.obraId = obraId;
        this.nota = nota;
        this.dataAvaliacao = dataAvaliacao;
    }

    public Avaliacao() {
    }

    public long getObraId() {
        return obraId;
    }

    public void setObraId(long obraId) {
        this.obraId = obraId;
    }

    public double getNota() {
        return nota;
    }

    public void setNota(double nota) {
        this.nota = nota;
    }

    public String getDataAvaliacao() {
        return dataAvaliacao;
    }

    public void setDataAvaliacao(String dataAvaliacao) {
        this.dataAvaliacao = dataAvaliacao;
    }
}
