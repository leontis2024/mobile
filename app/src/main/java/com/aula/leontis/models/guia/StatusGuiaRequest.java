package com.aula.leontis.models.guia;

public class StatusGuiaRequest{
        private long userId;
        private long guiaId;

        private boolean concluido;
        private int numeroPassoAtual;

        public StatusGuiaRequest(long userId, long guiaId, boolean concluido, int numeroPassoAtual) {
            this.userId = userId;
            this.guiaId = guiaId;
            this.concluido = concluido;
            this.numeroPassoAtual = numeroPassoAtual;
        }

        public StatusGuiaRequest() {
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

        public long getUserId() {
            return userId;
        }

        public void setUserId(long userId) {
            this.userId = userId;
        }

        public long getGuiaId() {
            return guiaId;
        }

        public void setGuiaId(long guiaId) {
            this.guiaId = guiaId;
        }
}
