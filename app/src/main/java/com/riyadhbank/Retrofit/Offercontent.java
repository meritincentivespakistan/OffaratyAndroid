package com.riyadhbank.Retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Offercontent {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("offertitle")
    @Expose
    private String offertitle;
    @SerializedName("discount")
    @Expose
    private String discount;
    @SerializedName("businessname")
    @Expose
    private String businessname;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("selected")
    @Expose
    private Integer selected;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOffertitle() {
        return offertitle;
    }

    public void setOffertitle(String offertitle) {
        this.offertitle = offertitle;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getBusinessname() {
        return businessname;
    }

    public void setBusinessname(String businessname) {
        this.businessname = businessname;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getSelected() {
        return selected;
    }

    public void setSelected(Integer selected) {
        this.selected = selected;
    }

}
