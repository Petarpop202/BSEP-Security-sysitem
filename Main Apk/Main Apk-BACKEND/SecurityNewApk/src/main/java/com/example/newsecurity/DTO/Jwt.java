package com.example.newsecurity.DTO;

public class Jwt {

    private String jwt;

    public String getRefreshJwt() {
        return refreshJwt;
    }

    public void setRefreshJwt(String refreshJwt) {
        this.refreshJwt = refreshJwt;
    }

    private String refreshJwt;

    public Jwt() {
        this.jwt = null;
        this.refreshJwt = null;
    }

    public Jwt(String jwt, String refreshJwt, long expiresIn) {
        this.jwt = jwt;
        this.refreshJwt = refreshJwt;
    }
    public String getJwt() {
        return jwt;
    }
    public void setJwt(String accessToken) {
        this.jwt = accessToken;
    }


}
