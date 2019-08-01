package com.riyadhbank.Retrofit;


import android.support.annotation.NonNull;
import android.text.Html;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.riyadhbank.Utility.Constants;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiRequestHelper {

    static Gson gson;
    private static ApiRequestHelper instance;
    private App app;
    private AppService appService;

    public static synchronized ApiRequestHelper init(App app) {
        if (null == instance) {
            instance = new ApiRequestHelper ();
            instance.setApplication (app);
            gson = new GsonBuilder ().setLenient ()
                    .registerTypeAdapterFactory (new NullStringToEmptyAdapterFactory ())
                    //.registerTypeAdapter (LoginDataModel.class, new LoginResponseModelDeserializer ())
                    .create ();
            instance.createRestAdapter ();
        }
        return instance;
    }

    /**
     * REST Adapter Configuration
     */
    private void createRestAdapter() {
        Retrofit.Builder builder = new Retrofit.Builder ().baseUrl ("http://cms.meritincentives.com/").addConverterFactory (GsonConverterFactory.create (gson));
        Retrofit retrofit = builder.client (getClient ().build ()).build ();
        appService = retrofit.create (AppService.class);
    }

    @NonNull
    public OkHttpClient.Builder getClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor ();//9276
// set your desired log level
        logging.setLevel (HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder ();
        httpClient.readTimeout (60, TimeUnit.SECONDS);
        httpClient.connectTimeout (60, TimeUnit.SECONDS);
// add your other interceptors …

// add logging as last interceptor
        httpClient.interceptors ().add (logging);
        return httpClient;
    }

    /**
     * End REST Adapter Configuration
     */

    public App getApplication() {
        return app;
    }

    private void handle_fail_response(Throwable t, OnRequestComplete onRequestComplete) {
        if (t != null && t.getMessage () != null)
            Log.e ("server msg", Html.fromHtml (t.getMessage ()) + "");
        if (t != null && t.getMessage () != null) {
            if (t.getMessage ().contains ("Unable to resolve host")) {
                onRequestComplete.onFailure ("No internet");
            } else {
                onRequestComplete.onFailure (Html.fromHtml (t.getMessage ()) + "");
            }
        } else {
            onRequestComplete.onFailure ("UNPROPER_RESPONSE");
        }
    }

    /**
     * API Calls
     */

    public void getDashboard(Map<String, String> params, final OnRequestComplete onRequestComplete) {
        Call<DashboardModel> call = appService.getDashboard (params);
        call.enqueue (new Callback<DashboardModel> () {
            @Override
            public void onResponse(Call<DashboardModel> call, Response<DashboardModel> response) {
                if (response.isSuccessful ()) {
                    onRequestComplete.onSuccess (response.body ());
                } else {
                    onRequestComplete.onFailure (Html.fromHtml (response.message ()) + "");
                }
            }

            @Override
            public void onFailure(Call<DashboardModel> call, Throwable t) {
                handle_fail_response (t, onRequestComplete);
            }
        });
    }
    public void getWelcomeMsg(Map<String, String> params, final OnRequestComplete onRequestComplete) {
        Call<WelcomeMsgModel> call = appService.getWelcomeMsg (params);
        call.enqueue (new Callback<WelcomeMsgModel> () {
            @Override
            public void onResponse(Call<WelcomeMsgModel> call, Response<WelcomeMsgModel> response) {
                if (response.isSuccessful ()) {
                    onRequestComplete.onSuccess (response.body ());
                } else {
                    onRequestComplete.onFailure (Html.fromHtml (response.message ()) + "");
                }
            }

            @Override
            public void onFailure(Call<WelcomeMsgModel> call, Throwable t) {
                handle_fail_response (t, onRequestComplete);
            }
        });
    }
    public void getOfferbyCat(Map<String, String> params, final OnRequestComplete onRequestComplete) {
        Call<OfferByCatModel> call = appService.getOfferByCat (params);
        call.enqueue (new Callback<OfferByCatModel> () {
            @Override
            public void onResponse(Call<OfferByCatModel> call, Response<OfferByCatModel> response) {
                if (response.isSuccessful ()) {
                    onRequestComplete.onSuccess (response.body ());
                } else {
                    onRequestComplete.onFailure (Html.fromHtml (response.message ()) + "");
                }
            }

            @Override
            public void onFailure(Call<OfferByCatModel> call, Throwable t) {
                handle_fail_response (t, onRequestComplete);
            }
        });
    }
   public void getOfferBySearch(Map<String, String> params, final OnRequestComplete onRequestComplete) {
        Call<SearchModel> call = appService.getOfferBySearch(params);
        call.enqueue (new Callback<SearchModel> () {
            @Override
            public void onResponse(Call<SearchModel> call, Response<SearchModel> response) {
                if (response.isSuccessful ()) {
                    onRequestComplete.onSuccess (response.body ());
                } else {
                    onRequestComplete.onFailure (Html.fromHtml (response.message ()) + "");
                }
            }

            @Override
            public void onFailure(Call<SearchModel> call, Throwable t) {
                handle_fail_response (t, onRequestComplete);
            }
        });
    }
   public void setAsFvrt(Map<String, String> params, final OnRequestComplete onRequestComplete) {
        Call<GeneralModel> call = appService.setAsFvrt(params);
        call.enqueue (new Callback<GeneralModel> () {
            @Override
            public void onResponse(Call<GeneralModel> call, Response<GeneralModel> response) {
                if (response.isSuccessful ()) {
                    onRequestComplete.onSuccess (response.body ());
                } else {
                    onRequestComplete.onFailure (Html.fromHtml (response.message ()) + "");
                }
            }

            @Override
            public void onFailure(Call<GeneralModel> call, Throwable t) {
                handle_fail_response (t, onRequestComplete);
            }
        });
    }
   public void login(Map<String, String> params, final OnRequestComplete onRequestComplete) {
        Call<LoginModel> call = appService.login(params);
        call.enqueue (new Callback<LoginModel> () {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                if (response.isSuccessful ()) {
                    onRequestComplete.onSuccess (response.body ());
                } else {
                    onRequestComplete.onFailure (Html.fromHtml (response.message ()) + "");
                }
            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {
                handle_fail_response (t, onRequestComplete);
            }
        });
    }
    public void getFavourate(Map<String, String> params, final OnRequestComplete onRequestComplete) {
        Call<FavouriteModel> call = appService.getFavourite (params);
        call.enqueue (new Callback<FavouriteModel> () {
            @Override
            public void onResponse(Call<FavouriteModel> call, Response<FavouriteModel> response) {
                if (response.isSuccessful ()) {
                    onRequestComplete.onSuccess (response.body ());
                } else {
                    onRequestComplete.onFailure (Html.fromHtml (response.message ()) + "");
                }
            }

            @Override
            public void onFailure(Call<FavouriteModel> call, Throwable t) {
                handle_fail_response (t, onRequestComplete);
            }
        });
    }
    public void getAboutUs(Map<String, String> params, final OnRequestComplete onRequestComplete) {
        Call<AboutUsModel> call = appService.getabout (params);
        call.enqueue (new Callback<AboutUsModel> () {
            @Override
            public void onResponse(Call<AboutUsModel> call, Response<AboutUsModel> response) {
                if (response.isSuccessful ()) {
                    onRequestComplete.onSuccess (response.body ());
                } else {
                    onRequestComplete.onFailure (Html.fromHtml (response.message ()) + "");
                }
            }

            @Override
            public void onFailure(Call<AboutUsModel> call, Throwable t) {
                handle_fail_response (t, onRequestComplete);
            }
        });
    }


    /**
     * End API Calls
     */

    public void setApplication(App app) {
        this.app = app;
    }

    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create (MultipartBody.FORM, descriptionString);
    }

    public interface OnRequestComplete {
        void onSuccess(Object object);

        void onFailure(String apiResponse);
    }

    public static class NullStringToEmptyAdapterFactory<T> implements TypeAdapterFactory {
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {

            Class<T> rawType = (Class<T>) type.getRawType ();
            if (rawType != String.class) {
                return null;
            }
            return (TypeAdapter<T>) new StringAdapter ();
        }
    }

    public static class StringAdapter extends TypeAdapter<String> {
        public String read(JsonReader reader) throws IOException {
            if (reader.peek () == JsonToken.NULL) {
                reader.nextNull ();
                return "";
            }
            return reader.nextString ();
        }

        public void write(JsonWriter writer, String value) throws IOException {
            if (value == null) {
                writer.nullValue ();
                return;
            }
            writer.value (value);
        }
    }
}
