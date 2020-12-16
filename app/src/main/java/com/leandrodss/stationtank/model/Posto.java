package com.leandrodss.stationtank.model;

import java.io.Serializable;

public class Posto implements Serializable {
    private String id;
    private String nome;
    private String preco;
    private Double latitude;
    private Double longitude;

    public Posto(){

    }

    public Posto(String id, String nome, String preco, Double latitude, Double longitude) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getPreco() {
        return preco;
    }

    public void setPreco(String preco) {
        this.preco = preco;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /*@Override
    public String toString() {
        return "Posto{" +
                "nome='" + nome + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                '}';
    }*/
}
