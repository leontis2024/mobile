package com.aula.leontis.models.noticia;


public class NewsHeadlines {
    Source source = null;
    String author = "";
    String title = "";
    String description = "";
    String url = "";
    String urlToImage = "";
    String publisheAt = "";
    String content = "";

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public String getPublisheAt() {
        return publisheAt;
    }

    public void setPublisheAt(String publisheAt) {
        this.publisheAt = publisheAt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
