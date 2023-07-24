package com.shahareinisim.tzachiapp;

public class TfilahTitlePart {

    int position;
    String title;

    TfilahTitlePart(int position, String title) {
        this.position = position;
        this.title = title.replace("[t]", "");
    }

    public int getPosition() {
        return position;
    }

    public String getTitle() {
        return title;
    }
}
