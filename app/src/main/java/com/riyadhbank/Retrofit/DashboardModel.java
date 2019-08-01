package com.riyadhbank.Retrofit;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;



public class DashboardModel {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("slider")
    @Expose
    private Slider slider;
    @SerializedName("category")
    @Expose
    private Category category;
    @SerializedName("offer")
    @Expose
    private Offer offer;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Slider getSlider() {
        return slider;
    }

    public void setSlider(Slider slider) {
        this.slider = slider;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Offer getOffer() {
        return offer;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }


    public class Offer {

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

    }



    public class Slider {

        @SerializedName("status")
        @Expose
        private Integer status;
        @SerializedName("count")
        @Expose
        private Integer count;
        @SerializedName("msg")
        @Expose
        private String msg;
        @SerializedName("slidercontent")
        @Expose
        private List<Slidercontent> slidercontent = null;

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

        public List<Slidercontent> getSlidercontent() {
            return slidercontent;
        }

        public void setSlidercontent(List<Slidercontent> slidercontent) {
            this.slidercontent = slidercontent;
        }

    }

    public class Slidercontent {

        @SerializedName("sr")
        @Expose
        private Integer sr;
        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("appid")
        @Expose
        private String appid;
        @SerializedName("language")
        @Expose
        private String language;
        @SerializedName("url")
        @Expose
        private String url;

        public Integer getSr() {
            return sr;
        }

        public void setSr(Integer sr) {
            this.sr = sr;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

    }
    public class Category {

        @SerializedName("status")
        @Expose
        private Integer status;
        @SerializedName("count")
        @Expose
        private Integer count;
        @SerializedName("msg")
        @Expose
        private String msg;
        @SerializedName("categorycontent")
        @Expose
        private List<Categorycontent> categorycontent = null;

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

        public List<Categorycontent> getCategorycontent() {
            return categorycontent;
        }

        public void setCategorycontent(List<Categorycontent> categorycontent) {
            this.categorycontent = categorycontent;
        }

    }

    public class Categorycontent {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("image")
        @Expose
        private String image;

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

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

    }
}
