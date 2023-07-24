package com.shahareinisim.tzachiapp;

public class TfilahPart {

    String key;
    String part;

    TfilahPart(String key, String part) {
        this.key = key;
        this.part = part.replace("[n]", "").replace("[t]","");
    }

    public String getKey() {
        return key;
    }

    public String getPart() {
        return part;
    }
}
