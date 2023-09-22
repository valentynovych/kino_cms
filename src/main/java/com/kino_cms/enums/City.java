package com.kino_cms.enums;

public enum City {
    VINNITSIA("Вінниця"),
    KYIV("Київ"),
    LVIV("Львів"),
    KHARKIV("Харків"),
    ODESSA("Одеса"),
    KHERSON("Херсон"),
    ZHYTOMYR("Житомир");
    private String description;

    City() {
    }

    City(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
