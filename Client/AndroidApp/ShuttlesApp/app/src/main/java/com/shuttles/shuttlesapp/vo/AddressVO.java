package com.shuttles.shuttlesapp.vo;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by domin on 2018-03-22.
 */

public class AddressVO  extends RealmObject{

    @PrimaryKey
    private int id;
    @Required
    private String addressName;
    @Required
    private String zipcode;
    @Required
    private String address1;
    @Required
    private String address2;
    @Required
    private String addressExtra;

    public AddressVO(){

    }
    public AddressVO(int id, String addressName, String zipcode, String address1, String address2, String addressExtra){
        this.id = id;
        this.addressName = addressName;
        this.zipcode = zipcode;
        this.address1 = address1;
        this.address2 = address2;
        this.addressExtra = addressExtra;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getAddressExtra() {
        return addressExtra;
    }

    public void setAddressExtra(String addressExtra) {
        this.addressExtra = addressExtra;
    }
}
