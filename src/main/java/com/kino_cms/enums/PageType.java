package com.kino_cms.enums;

public enum PageType {
    ABOUT_CINEMA("Про кінотеатр"),
    CAFE_BAR,
    VIP_HALL,
    ADVERTISING,
    CHILD_ROOM,
    HOME_PAGE,
    CONTACT_PAGE,
    OTHER_PAGE;

    String description;

    PageType(String description) {
        this.description = description;
    }

    PageType() {
    }

    public String getDescription() {
        return description;
    }
}
