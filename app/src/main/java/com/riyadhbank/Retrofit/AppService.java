package com.riyadhbank.Retrofit;


import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface AppService {

    @FormUrlEncoded
    @POST("/admin/process/dashboardprocess.php")
    Call<DashboardModel> getDashboard(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("/admin/process/contentprocess.php")
    Call<WelcomeMsgModel> getWelcomeMsg(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("/admin/process/dashboardprocess.php")
    Call<FavouriteModel> getFavourite(@FieldMap Map<String, String> params);

   @FormUrlEncoded
    @POST("/admin/process/contentprocess.php")
    Call<AboutUsModel> getabout(@FieldMap Map<String, String> params);

   @FormUrlEncoded
    @POST("/admin/process/dashboardprocess.php")
    Call<OfferByCatModel> getOfferByCat(@FieldMap Map<String, String> params);

   @FormUrlEncoded
    @POST("/admin/process/dashboardprocess.php")
    Call<SearchModel> getOfferBySearch(@FieldMap Map<String, String> params);

   @FormUrlEncoded
    @POST("/admin/process/dashboardprocess.php")
    Call<GeneralModel> setAsFvrt(@FieldMap Map<String, String> params);

  @FormUrlEncoded
    @POST("/admin/process/userprocess.php")
    Call<LoginModel> login(@FieldMap Map<String, String> params);



/*    @FormUrlEncoded
    //@POST("/mxprm/services/")
    @POST("/demos-mxprm/services/")
    Call<AttendanceData> getAttendanceList(@FieldMap Map<String, String> params);


/*    @GET("/json/")
    Call<LocationData> getLocationData();*/
}
