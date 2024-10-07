package com.aula.leontis.models.endereco;

public class EnderecoMuseu {
    private long id;
    private String rua;
    private String cep;
    private String numeroMuseu;
    private String cidade;
    private String estado;
    private String pontoReferencia; // Pode ser null se não for obrigatório

    // Construtor
    public EnderecoMuseu(long id, String rua, String cep, String numeroMuseu, String cidade, String estado, String pontoReferencia) {
        this.id = id;
        this.rua = rua;
        this.cep = cep;
        this.numeroMuseu = numeroMuseu;
        this.cidade = cidade;
        this.estado = estado;
        this.pontoReferencia = pontoReferencia;
    }

    // Getters e Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getNumeroMuseu() {
        return numeroMuseu;
    }

    public void setNumeroMuseu(String numeroMuseu) {
        this.numeroMuseu = numeroMuseu;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getPontoReferencia() {
        return pontoReferencia;
    }

    public void setPontoReferencia(String pontoReferencia) {
        this.pontoReferencia = pontoReferencia;
    }
    public String formatarEndereco() {
        StringBuilder enderecoFormatado = new StringBuilder();

        if (this.rua != null && !this.rua.isEmpty()) {
            enderecoFormatado.append(this.rua);
        }

        if (this.numeroMuseu != null && !this.numeroMuseu.isEmpty()) {
            if (enderecoFormatado.length() > 0) {
                enderecoFormatado.append(", ");
            }
            enderecoFormatado.append(this.numeroMuseu);
        }

        if (this.pontoReferencia != null && !this.pontoReferencia.isEmpty()) {
            if (enderecoFormatado.length() > 0) {
                enderecoFormatado.append(" - ");
            }
            enderecoFormatado.append(this.pontoReferencia);
        }

        if (this.cidade != null && !this.cidade.isEmpty()) {
            if (enderecoFormatado.length() > 0) {
                enderecoFormatado.append(", ");
            }
            enderecoFormatado.append(this.cidade);
        }

        if (this.estado != null && !this.estado.isEmpty()) {
            if (enderecoFormatado.length() > 0) {
                enderecoFormatado.append(" - ");
            }
            enderecoFormatado.append(this.estado.toUpperCase());
        }

        if (this.cep != null && !this.cep.isEmpty()) {
            if (enderecoFormatado.length() > 0) {
                enderecoFormatado.append(", ");
            }
            enderecoFormatado.append(this.cep);
        }

        return enderecoFormatado.toString();
    }

}

