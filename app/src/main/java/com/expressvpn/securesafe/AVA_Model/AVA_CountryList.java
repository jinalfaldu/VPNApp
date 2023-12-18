package com.expressvpn.securesafe.AVA_Model;

public class AVA_CountryList
{
    String name;
    String code;
    int icon;

    public AVA_CountryList(String ca, String canada, int icon) {
        this.code=ca;
        this.name=canada;
        this.icon=icon;
    }
    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
