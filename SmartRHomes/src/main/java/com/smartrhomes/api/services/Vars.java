package com.smartrhomes.api.services;

public enum Vars {

	nonzerotime("00:00:01"),
    one("1"),
    zero("0"),
    ;
    String s;

    private Vars(String s) {
        this.s = s;
    }
    public String val() {
        return s;
    }
    
}
