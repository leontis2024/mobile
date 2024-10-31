package com.aula.leontis;

public class Geral {
        private static Geral instance;
        private String urlApiSql="http://ec2-52-44-41-122.compute-1.amazonaws.com:8080/";
        private String urlApiMongo="http://ec2-44-207-250-124.compute-1.amazonaws.com:8080/";
        private String urlImagePadrao ="https://www.buritama.sp.leg.br/imagens/parlamentares-2013-2016/sem-foto.jpg/image";
        private String urlModeloScanner="http://ec2-100-24-75-249.compute-1.amazonaws.com:5000/";
        private String urlApiRedis="https://redisapi-r315.onrender.com/";
        private boolean primeiroAcesso =false;
        private boolean terminou ;
        private boolean predicao;

        private Geral() { }

        // Método para obter a instância da classe
        public static synchronized Geral getInstance() {
            if (instance == null) {
                instance = new Geral();
            }
            return instance;
        }

    public String getUrlApiMongo() {
        return urlApiMongo;
    }

    public String getUrlApiSql() {
        return urlApiSql;
    }

    public String getUrlImagePadrao() {
        return urlImagePadrao;
    }

    public boolean isPrimeiroAcesso() {
        return primeiroAcesso;
    }

    public void setPrimeiroAcesso(boolean primeiroAcesso) {
        this.primeiroAcesso = primeiroAcesso;
    }

    public String getUrlModeloScanner() {
        return urlModeloScanner;
    }

    public String getUrlApiRedis() {
        return urlApiRedis;
    }

    public void setTerminou(boolean terminou) {
        this.terminou = terminou;
    }

    public void setPredicao(boolean predicao) {
        this.predicao = predicao;
    }

    public boolean isTerminou() {
        return terminou;
    }

    public boolean isPredicao() {
        return predicao;
    }
}
