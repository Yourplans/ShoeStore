package com.shoestore.chat.model;

/**
 * Created by Daniel on 28/04/2017.
 */
public class ContactModel {

    private String name;
    private String image;
    private String desc;
    private String id;
    private String pass;
    private boolean isNew;


    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public ContactModel(){

    }

    public ContactModel(String nombre, String state, String image) {
        this.name = nombre;
        this.image = image;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
