package com.pineapplesupermarket.tiendaapi.dto;

import java.util.List;
/**
 *Clase de la respuesta del JWT
 *@author Raquel de la Rosa 
 *@version 1.0
 */

public class JwtResponseDTO {
    private String token;
    private String type = "Bearer";
    private long id;
    private String username;
    private List<String> roles;

    public JwtResponseDTO(String accessToken, long id, String username, List<String> roles) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.roles = roles;
    }

    public String getAccessToken() {
        return token;
    }

    public void setAccessToken(String accessToken) {
        this.token = accessToken;
    }

    public String getTokenType() {
        return type;
    }

    public void setTokenType(String tokenType) {
        this.type = tokenType;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getRoles() {
        return roles;
    }

}