package com.cjzq.family.domainService.appModel.bean;

public class LanguageBean {
    public String name;
    public String value;

    public LanguageBean(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
