package com.ticketpro.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "Tariff", strict = false)
public class CaleTariff {
    @Element(name = "Id", required = false)
    private String id;
    @Element(name = "Name", required = false)
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
