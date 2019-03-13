package com.pratham.assessment.domain;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 8/31/2015.
 */
public class RaspVillage {
   /* @SerializedName("id")
    public String id;
    @SerializedName("data")
    public ArrayList<Village> data;
    @SerializedName("filter_name")
    public String filter_name;
    @SerializedName("table_name")
    public String table_name;
    @SerializedName("facility")
    public String facility;

    @Override
    public String toString() {
        return "RaspVillage{" +
                "id='" + id + '\'' +
                ", data=" + data +
                ", filter_name='" + filter_name + '\'' +
                ", table_name='" + table_name + '\'' +
                ", facility='" + facility + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<Village> getData() {
        return data;
    }

    public void setData(ArrayList<Village> data) {
        this.data = data;
    }

    public String getFilter_name() {
        return filter_name;
    }

    public void setFilter_name(String filter_name) {
        this.filter_name = filter_name;
    }

    public String getTable_name() {
        return table_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }

    public String getFacility() {
        return facility;
    }

    public void setFacility(String facility) {
        this.facility = facility;
    }*/


    @SerializedName("id")
    public String id;
    @SerializedName("data")
    public Village data;
    @SerializedName("filter_name")
    public String filter_name;
    @SerializedName("table_name")
    public String table_name;
    @SerializedName("facility")
    public String facility;
    @Override
    public String toString() {
        return "RaspVillage{" +
                "id='" + id + '\'' +
                ", data=" + data +
                ", filter_name='" + filter_name + '\'' +
                ", table_name='" + table_name + '\'' +
                ", facility='" + facility + '\'' +
                '}';
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    //
// public ArrayList<Modal_Village> getData() {
// return data;
// }
//
// public void setData(ArrayList<Modal_Village> data) {
// this.data = data;
// }
    public Village getData() {
        return data;
    }
    public void setData(Village data) {
        this.data = data;
    }
    public String getFilter_name() {
        return filter_name;
    }
    public void setFilter_name(String filter_name) {
        this.filter_name = filter_name;
    }
    public String getTable_name() {
        return table_name;
    }
    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }
    public String getFacility() {
        return facility;
    }
    public void setFacility(String facility) {
        this.facility = facility;
    }
}