package com.kino_cms.enums;

public enum FilmType {
    D2("2D"),
    D3("3D"),
    IMAX("IMAX");

    private String description;

    FilmType() {
    }

    FilmType(String description){
        this.description = description;
    }
    public String getDescription() {
        return description;
    }
}
