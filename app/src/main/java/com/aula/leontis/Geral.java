package com.aula.leontis;

public class Geral {
        private static Geral instance;
        private String urlApiSql="http://ec2-52-44-41-122.compute-1.amazonaws.com:8080/";
        private String urlApiMongo="http://ec2-44-207-250-124.compute-1.amazonaws.com:8080/";

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
}
