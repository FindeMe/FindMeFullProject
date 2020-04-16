package com.nyx.fineme.models;

public class DeviceRow {
    public String id ,name ,info ,pic ,family;
    public int distance;


    public DeviceRow(String id, String name, String info, int distance ,String pic ,String fam) {
        this.id = id;
        this.name = name;
        this.info = info;
        this.family = fam;
        this.distance = distance;
        this.pic = pic;
    }
}
