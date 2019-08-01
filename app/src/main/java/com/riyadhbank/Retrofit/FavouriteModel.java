package com.riyadhbank.Retrofit;


import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FavouriteModel {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("offercontent")
    @Expose
    private List<Offercontent> offercontent = null;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<Offercontent> getOffercontent() {
        return offercontent;
    }

    public void setOffercontent(List<Offercontent> offercontent) {
        this.offercontent = offercontent;
    }

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
}


