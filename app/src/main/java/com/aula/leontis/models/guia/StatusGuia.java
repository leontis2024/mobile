package com.aula.leontis.models.guia;

public class StatusGuia {

    private boolean concluido;
    private int numeroPassoAtual;

    public StatusGuia(boolean concluido, int numeroPassoAtual) {
        this.concluido = concluido;
        this.numeroPassoAtual = numeroPassoAtual;
    }

    public StatusGuia() {
    }

    public boolean isConcluido() {
        return concluido;
    }

    public void setConcluido(boolean concluido) {
        this.concluido = concluido;
    }

    public int getNumeroPassoAtual() {
        return numeroPassoAtual;
    }

    public void setNumeroPassoAtual(int numeroPassoAtual) {
        this.numeroPassoAtual = numeroPassoAtual;
    }
}
