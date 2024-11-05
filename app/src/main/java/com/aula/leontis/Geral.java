package com.aula.leontis;

public class Geral {
        private static Geral instance;
        private String urlApiSql="http://ec2-52-44-41-122.compute-1.amazonaws.com:8080/";
        private String urlApiMongo="http://ec2-44-207-250-124.compute-1.amazonaws.com:8080/";
        private String urlImagePadrao ="https://firebasestorage.googleapis.com/v0/b/leontisfotos.appspot.com/o/usuarios%2FusuarioDefault.png?alt=media&token=04ef6067-fef3-4a33-9065-8788b3b44f96";
        private String apiModeloPremium = "http://ec2-34-200-118-26.compute-1.amazonaws.com:5000";
        private String urlModeloScanner="http://ec2-100-24-75-249.compute-1.amazonaws.com:5000/";
        private String urlApiRedis="https://apiredis.onrender.com/";
        private boolean primeiroAcesso =false;

        private Geral() { }

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

    public String getApiModeloPremium() {
        return apiModeloPremium;
    }
    public void setApiModeloPremium(String apiModeloPremium) {
        this.apiModeloPremium = apiModeloPremium;
    }
}
