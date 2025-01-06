package com.ticketpro.vendors.cubtrack.cbt_model;

import android.os.Build;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class CubTracZone {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("assigned_id")
    @Expose
    private String assignedId;
    @SerializedName("lat")
    @Expose
    private Object lat;
    @SerializedName("lng")
    @Expose
    private Object lng;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAssignedId() {
        return assignedId;
    }

    public void setAssignedId(String assignedId) {
        this.assignedId = assignedId;
    }

    public Object getLat() {
        return lat;
    }

    public void setLat(Object lat) {
        this.lat = lat;
    }

    public Object getLng() {
        return lng;
    }

    public void setLng(Object lng) {
        this.lng = lng;
    }


    /**
     * Sorts a list of Person objects by their name in ascending order.
     * @param zoneListName the list to be sorted
     */
    public static void sortZoneListByName(List<CubTracZone> zoneListName) {
        if (zoneListName == null || zoneListName.isEmpty()) {
            // Handle empty or null list
            System.out.println("The list is either null or empty.");
            return;
        }

        // Using Java 8's Comparator with null checks
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Collections.sort(zoneListName, Comparator.comparing(CubTracZone::getName,
                    Comparator.nullsLast(String::compareToIgnoreCase)));
        }
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CubTracZone that = (CubTracZone) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
