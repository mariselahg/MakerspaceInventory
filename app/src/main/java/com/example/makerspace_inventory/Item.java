package com.example.makerspace_inventory;

import org.bson.types.ObjectId;

public class Item {
    private ObjectId id;
    public int listID;
    private String itemname;
    private int quantity;
    private String unit;
    private String room;
    private String locker;
    private String compartment;
    private String tags;
    private String info;
    private int serialnumber;
    private String image_url;

    // empty constructor required for MongoDB Data Access POJO codec compatibility
    public Item() {}
    public Item(ObjectId id, int listID, String itemname, int quantity, String unit, String room, String locker, String compartment, String tags, String info, int serialnumber, String image_url) {
        this.id = id;
        this.listID = listID;
        this.itemname = itemname;
        this.quantity = quantity;
        this.unit = unit;
        this.room = room;
        this.locker = locker;
        this.compartment = compartment;
        this.tags = tags;
        this.serialnumber = serialnumber;
        this.image_url = image_url;
    }

    public ObjectId getId() { return id; }
    public void setId(ObjectId id) { this.id = id; }

    public int getListId() { return listID; }
    public void setListId(int listID) { this.listID = listID; }

    public String getItemname() { return itemname; }
    public void setItemname(String itemname) { this.itemname = itemname; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public String getRoom() { return room; }
    public void setRoom(String room) { this.room = room; }

    public String getLocker() { return locker; }
    public void setLocker(String locker) { this.locker = locker; }

    public String getCompartment() { return compartment; }
    public void setCompartment(String compartment) { this.compartment = compartment; }

    public String getTags() { return tags; }
    public void setTags(String tags) {this.tags = tags; }

    public String getInfo() { return info; }
    public void setInfo(String info) { this.info = info; }

    public int getSerialnumber() { return serialnumber; }
    public void setSerialnumber(int serialnumber) { this.serialnumber = serialnumber; }

    public String getImage_url() { return image_url; }
    public void setImage_url(String image_url) { this.image_url =image_url; }

    @Override
    public String toString() {
        return "Item [id=" + id + ", name=" + itemname + ", quantity=" + quantity + ", unit=" + unit + ", room=" + room + ", locker=" + locker + ", compartment=" + compartment + "]";
    }
}
