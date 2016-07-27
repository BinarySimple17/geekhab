package ru.binarysimple.geekhab;

public class User {
    private String login;
    private String id;
    private String score;
    private String avatar_url;
    private String url;
    private int likes;

    public String getLogin() {
        return login;
    }

    public String getId() {
        return id;
    }

    public String getScore() {
        return score;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public String getUrl() {
        return url;
    }

    public int getLikes() {
        return likes;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }
}

