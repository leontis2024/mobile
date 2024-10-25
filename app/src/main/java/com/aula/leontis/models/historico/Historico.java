package com.aula.leontis.models.historico;

public class Historico {
    private Long obraId;

    private String dataEscaneada;

    public Historico(Long obraId, String dataEscaneada) {
        this.obraId = obraId;
        this.dataEscaneada = dataEscaneada;
    }

    public Historico() {
    }

    public Long getObraId() {
        return obraId;
    }

    public void setObraId(Long obraId) {
        this.obraId = obraId;
    }

    public String getDataEscaneada() {
        return dataEscaneada;
    }

    public void setDataEscaneada(String dataEscaneada) {
        this.dataEscaneada = dataEscaneada;
    }
}
