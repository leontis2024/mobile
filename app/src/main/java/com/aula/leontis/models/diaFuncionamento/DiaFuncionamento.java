package com.aula.leontis.models.diaFuncionamento;
public class DiaFuncionamento {
    private long id;
    private String hrInicio;
    private String hrTermino;
    private Double precoDiaFuncionamento; // Pode ser null, por isso uso Double (que aceita null)
    private String diaSemana;
    private long idMuseu;

    // Construtor
    public DiaFuncionamento(long id, String hrInicio, String hrTermino, Double precoDiaFuncionamento, String diaSemana, long idMuseu) {
        this.id = id;
        this.hrInicio = hrInicio;
        this.hrTermino = hrTermino;
        this.precoDiaFuncionamento = precoDiaFuncionamento;
        this.diaSemana = diaSemana;
        this.idMuseu = idMuseu;
    }

    // Getters e Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getHrInicio() {
        return hrInicio;
    }

    public void setHrInicio(String hrInicio) {
        this.hrInicio = hrInicio;
    }

    public String getHrTermino() {
        return hrTermino;
    }

    public void setHrTermino(String hrTermino) {
        this.hrTermino = hrTermino;
    }

    public Double getPrecoDiaFuncionamento() {
        return precoDiaFuncionamento;
    }

    public void setPrecoDiaFuncionamento(Double precoDiaFuncionamento) {
        this.precoDiaFuncionamento = precoDiaFuncionamento;
    }

    public String getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(String diaSemana) {
        this.diaSemana = diaSemana;
    }

    public long getIdMuseu() {
        return idMuseu;
    }

    public void setIdMuseu(long idMuseu) {
        this.idMuseu = idMuseu;
    }
}
