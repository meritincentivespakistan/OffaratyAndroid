package com.riyadhbank.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.riyadhbank.Adapter.FeaturedDealsAdapter;
import com.riyadhbank.Async.FavouriteService;
import com.riyadhbank.Async.SearchOfferService;
import com.riyadhbank.Fragment.HomeFragment;
import com.riyadhbank.Modal.OfferModal;
import com.riyadhbank.R;
import com.riyadhbank.Retrofit.ApiRequestHelper;
import com.riyadhbank.Retrofit.App;
import com.riyadhbank.Retrofit.DashboardModel;
import com.riyadhbank.Retrofit.GeneralModel;
import com.riyadhbank.Retrofit.Offercontent;
import com.riyadhbank.Retrofit.SearchModel;
import com.riyadhbank.Utility.Constants;
import com.riyadhbank.Utility.CustomDialog;
import com.riyadhbank.Utility.GlobalClass;
import com.riyadhbank.Utility.Interface;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchActivity extends AppCompatActivity implements Interface.OnSearch, Interface.OnClickHomeOrCategoryFavourite, Interface.OnFavourite {

    ImageView back;
    TextView txtTitle;
    LinearLayout chandLout, lout_home, lout_profile, lout_about_us, lout_logout, lout_favourite;
    ImageView img_home, img_logout;
    TextView txt_home, txt_logout;

    RecyclerView searchList;
    ImageView btnSearch;
    EditText edtSearch;
    String UserId, LangType;
    FeaturedDealsAdapter dealsAdapter;
    ArrayList<OfferModal> searchArrayList;
    SearchModel searchModel;
    CustomDialog customDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        app = (App) getApplication();

        FindById();
        customDialog = new CustomDialog(SearchActivity.this);

        SharedPreferences shared1 = getSharedPreferences(Constants.UserDetailPref, MODE_PRIVATE);
        UserId = shared1.getString(Constants.userid, "");
        SharedPreferences shared = getSharedPreferences(Constants.LanguagePref, MODE_PRIVATE);
        GlobalClass.languageCode = shared.getString(Constants.LanguageCode, "en");

        if(GlobalClass.languageCode.equals("en")){
            LangType = "english";
        }else {
            LangType = "arabic";
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        txtTitle.setText(getResources().getString(R.string.search));

        img_home.setImageDrawable(getResources().getDrawable(R.drawable.ic_home_select));
        txt_home.setTextColor(getResources().getColor(R.color.themeColor));

        clickMenuType();

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String SearchSrt = edtSearch.getText().toString().trim();

                if (GlobalClass.isNetworkConnected(SearchActivity.this)) {
                    getSearchedData(SearchSrt);
                   /* new SearchOfferService(SearchActivity.this, SearchActivity.this,
                            UserId, SearchSrt, LangType).execute();*/
                } else {
                    Toast.makeText(SearchActivity.this, getResources().getString(R.string.internet_msg), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
    App app;

    void getSearchedData(String searchSrt) {
        if (GlobalClass.isNetworkConnected(SearchActivity.this)) {
            customDialog.show();

            Map<String, String> params = new HashMap<>();

            params.put(Constants.action, Constants.searchby);
            params.put(Constants.language, LangType);
            params.put(Constants.userid, UserId);
            params.put(Constants.searchstring, searchSrt);

            app.getApiRequestHelper ().getOfferBySearch (params, new ApiRequestHelper.OnRequestComplete () {
                @Override
                public void onSuccess(Object object) {
                    customDialog.dismiss();
                    searchModel = (SearchModel) object;
                    Log.e ("in", "success");
                    if (searchModel != null) {
                        dealsAdapter = new FeaturedDealsAdapter(SearchActivity.this, searchModel.getOffercontent(), GlobalClass.HOME, SearchActivity.this);
                        searchList.setAdapter(dealsAdapter);
                    } else {
                        Toast.makeText (SearchActivity.this, "UNPROPER_RESPONSE",Toast.LENGTH_LONG).show();
                    }
                    // dismisProgressHUD ();
                }

                @Override
                public void onFailure(String apiResponse) {
                    //  dismisProgressHUD ();
                    customDialog.dismiss();
                    Log.e ("in", "error " + apiResponse);
                    Toast.makeText (SearchActivity.this, apiResponse,Toast.LENGTH_LONG).show();
                }
            });

        }else {
            Toast.makeText(SearchActivity.this, (getResources().getString(R.string.internet_msg)), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onSearch(JSONObject jsonObject) {

        try {

            JSONArray offerArray = jsonObject.getJSONArray("offercontent");

            searchArrayList = new ArrayList<>();
            for (int i = 0; i < offerArray.length(); i++) {
                JSONObject offerInnerObject = offerArray.getJSONObject(i);
                OfferModal offerModal = new OfferModal();
                offerModal.setId(offerInnerObject.getString("id"));
                offerModal.setOffertitle(offerInnerObject.getString("offertitle"));
                offerModal.setDiscount(offerInnerObject.getString("discount"));
                offerModal.setImage(offerInnerObject.getString("image"));
                offerModal.setSelected(offerInnerObject.getString("selected"));
                offerModal.setBusinessname(offerInnerObject.getString("businessname"));
                searchArrayList.add(offerModal);
            }

           // dealsAdapter = new FeaturedDealsAdapter(this, searchArrayList, GlobalClass.HOME, this);
           // searchList.setAdapter(dealsAdapter);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void FindById() {
        back = (ImageView) findViewById(R.id.back);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        edtSearch = (EditText) findViewById(R.id.edtSearch);
        btnSearch = (ImageView) findViewById(R.id.btnSearch);

        searchList = (RecyclerView) findViewById(R.id.searchList);
        searchList.setLayoutManager(new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false));

        img_home = (ImageView) findViewById(R.id.img_home);
        txt_home = (TextView) findViewById(R.id.txt_home);

        img_logout = (ImageView) findViewById(R.id.img_logout);
        txt_logout = (TextView) findViewById(R.id.txt_logout);

        chandLout = (LinearLayout) findViewById(R.id.chandLout);
        lout_home = (LinearLayout) findViewById(R.id.lout_home);
        lout_profile = (LinearLayout) findViewById(R.id.lout_profile);
        lout_about_us = (LinearLayout) findViewById(R.id.lout_about_us);
        lout_logout = (LinearLayout) findViewById(R.id.lout_logout);
        lout_favourite = (LinearLayout) findViewById(R.id.lout_favourite);

    }

    private void clickMenuType() {

        lout_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateMenu(GlobalClass.HOME);
            }
        });

        lout_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateMenu(GlobalClass.PROFILE);
            }
        });

        lout_favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateMenu(GlobalClass.FAVOURITE);
            }
        });

        lout_about_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateMenu(GlobalClass.ABOUT_US);
            }
        });

        lout_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img_logout.setImageDrawable(getResources().getDrawable(R.drawable.ic_logout_select));
                txt_logout.setTextColor(getResources().getColor(R.color.themeColor));
                img_home.setImageDrawable(getResources().getDrawable(R.drawable.ic_home));
                txt_home.setTextColor(getResources().getColor(R.color.grey));
                GlobalClass.logOutUser(SearchActivity.this, GlobalClass.HOME, img_logout, txt_logout, img_home, txt_home, chandLout);
            }
        });

    }

    private void navigateMenu(String type) {
        Intent i = new Intent(SearchActivity.this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.putExtra("isFrom", type);
        startActivity(i);
    }

    String isSelected;

    @Override
    public void onClickHomeOrCategoryFavourite(Offercontent offerModal, int position) {
        if (offerModal.getSelected()==1) {
            isSelected = "0";
        } else {
            isSelected = "1";
        }
        if (GlobalClass.isNetworkConnected(this)) {
            setAsFvrt(offerModal.getId(), isSelected,position);
           // new FavouriteService(this, this, UserId, offerModal.getId(), isSelected, position).execute();
        } else {
            Toast.makeText(this, (getResources().getString(R.string.internet_msg)), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFavourite(int position) {
        searchArrayList.get(position).setSelected(isSelected);
        dealsAdapter.notifyItemChanged(position);
    }

    void setAsFvrt(String id, final String isSelected, final int position) {
        if (GlobalClass.isNetworkConnected(SearchActivity.this)) {
            customDialog.show();

            Map<String, String> params = new HashMap<>();

            params.put(Constants.action, Constants.favouriteselected);
            params.put(Constants.userid, UserId);
            params.put(Constants.offerid, id);
            params.put(Constants.selected, isSelected);
            params.put(Constants.appid, GlobalClass.Riyadh);


            app.getApiRequestHelper ().setAsFvrt (params, new ApiRequestHelper.OnRequestComplete () {
                @Override
                public void onSuccess(Object object) {
                    customDialog.dismiss();
                    GeneralModel generalModel = (GeneralModel) object;
                    Log.e ("in", "success");
                    if (generalModel != null) {
                        Toast.makeText (SearchActivity.this, generalModel.getMsg(),Toast.LENGTH_LONG).show();
                        searchModel.getOffercontent().get(position).setSelected(Integer.valueOf(isSelected));
                        dealsAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText (SearchActivity.this, "UNPROPER_RESPONSE",Toast.LENGTH_LONG).show();
                    }
                    // dismisProgressHUD ();
                }

                @Override
                public void onFailure(String apiResponse) {
                    //  dismisProgressHUD ();
                    customDialog.dismiss();
                    Log.e ("in", "error " + apiResponse);
                    Toast.makeText (SearchActivity.this, apiResponse,Toast.LENGTH_LONG).show();
                }
            });

        }else {
            Toast.makeText(SearchActivity.this, (getResources().getString(R.string.internet_msg)), Toast.LENGTH_SHORT).show();
        }
    }

}
