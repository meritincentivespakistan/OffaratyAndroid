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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.riyadhbank.Adapter.FeaturedDealsAdapter;
import com.riyadhbank.Async.FavouriteService;
import com.riyadhbank.Async.OfferCategoryService;
import com.riyadhbank.Modal.CategoryModal;
import com.riyadhbank.Modal.OfferModal;
import com.riyadhbank.R;
import com.riyadhbank.Retrofit.ApiRequestHelper;
import com.riyadhbank.Retrofit.App;
import com.riyadhbank.Retrofit.GeneralModel;
import com.riyadhbank.Retrofit.OfferByCatModel;
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

public class CategoryWiseOfferActivity extends AppCompatActivity implements Interface.OnOfferCategory, Interface.OnClickHomeOrCategoryFavourite, Interface.OnFavourite {

    ImageView back;
    TextView txtTitle, txt_home, txt_logout;
    LinearLayout chandLout, lout_home, lout_profile, lout_about_us, lout_logout, lout_favourite;
    ImageView img_home, img_logout;

    RecyclerView offerList;
    ArrayList<OfferModal> offerArrayList;
    FeaturedDealsAdapter offerAdapter;

    RelativeLayout sortLout, btnCancel;
    FrameLayout sortDialogLout;
    Animation slideUp, slideDowm;
    View blackView;
    ImageView imgfeatured, imgNear;
    LinearLayout btnDone, nearLout, featuredLout;
    String SortType, selectionType = "";

    String Language, UserId, CategoryId, CategoryTitle, Filter;
    App app;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_wise_offer);

        FindById();

        CategoryId = getIntent().getStringExtra("CategoryId");
        CategoryTitle = getIntent().getStringExtra("CategoryTitle");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        txtTitle.setText(CategoryTitle);
        app = (App) getApplication();

        img_home.setImageDrawable(getResources().getDrawable(R.drawable.ic_home_select));
        txt_home.setTextColor(getResources().getColor(R.color.themeColor));
        clickMenuType();

        if (GlobalClass.languageCode.equals("") || GlobalClass.languageCode.equals("en")) {
            Language = "english";
        } else {
            Language = "arabic";
        }

        SharedPreferences shared = getSharedPreferences(Constants.UserDetailPref, MODE_PRIVATE);
        UserId = shared.getString(Constants.userid, "");

        slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        slideDowm = AnimationUtils.loadAnimation(this, R.anim.slide_down);

        changeSortType("1");
        selectionType = "1";
        Filter = "featured";

        if (GlobalClass.isNetworkConnected(CategoryWiseOfferActivity.this)) {
            offerCategoryService();
//  new OfferCategoryService(CategoryWiseOfferActivity.this, CategoryWiseOfferActivity.this, Language, UserId, CategoryId, Filter).execute();
        } else {
            Toast.makeText(CategoryWiseOfferActivity.this, (getResources().getString(R.string.internet_msg)), Toast.LENGTH_SHORT).show();
        }

        sortLout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                blackView.setVisibility(View.VISIBLE);
                sortDialogLout.setVisibility(View.VISIBLE);
                sortDialogLout.clearAnimation();
                sortDialogLout.startAnimation(slideUp);

                if (selectionType.equals("")) {
                    imgfeatured.setBackground(getResources().getDrawable(R.drawable.radio));
                    imgNear.setBackground(getResources().getDrawable(R.drawable.radio));
                } else if (selectionType.equals("0")) {
                    changeSortType("0");
                } else {
                    changeSortType("1");
                }

            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectionType = SortType;

                if (selectionType.equals("0")) {
                    Filter = "nearby";
                } else {
                    Filter = "featured";
                }

                blackView.setVisibility(View.GONE);
                sortDialogLout.setVisibility(View.GONE);
                sortDialogLout.clearAnimation();
                sortDialogLout.startAnimation(slideDowm);

                if (GlobalClass.isNetworkConnected(CategoryWiseOfferActivity.this)) {
                    offerCategoryService();
                    // new OfferCategoryService(CategoryWiseOfferActivity.this, CategoryWiseOfferActivity.this, Language, UserId, CategoryId, Filter).execute();
                } else {
                    Toast.makeText(CategoryWiseOfferActivity.this, (getResources().getString(R.string.internet_msg)), Toast.LENGTH_SHORT).show();
                }

            }
        });

        nearLout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeSortType("0");
            }
        });

        featuredLout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeSortType("1");
            }
        });

        blackView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                blackView.setVisibility(View.GONE);
                sortDialogLout.setVisibility(View.GONE);
                sortDialogLout.clearAnimation();
                sortDialogLout.startAnimation(slideDowm);
            }
        });

    }

    OfferByCatModel offerByCatModel;
    void offerCategoryService() {
        final CustomDialog customDialog;
        if (GlobalClass.isNetworkConnected(CategoryWiseOfferActivity.this)) {
            customDialog = new CustomDialog(CategoryWiseOfferActivity.this);
            customDialog.show();

            Map<String, String> params = new HashMap<>();

            params.put(Constants.action, Constants.offerbycategory);
            params.put(Constants.language, Language);
            params.put(Constants.userid, UserId);
            params.put(Constants.category, CategoryId);
            params.put(Constants.filter, Filter);
            params.put(Constants.appid, GlobalClass.Riyadh);


            app.getApiRequestHelper ().getOfferbyCat (params, new ApiRequestHelper.OnRequestComplete () {
                @Override
                public void onSuccess(Object object) {
                    customDialog.dismiss();
                    offerByCatModel= (OfferByCatModel) object;
                    Log.e ("in", "success");
                    if (offerByCatModel != null) {
                        if (offerByCatModel.getStatus()==1) {
                            offerAdapter = new FeaturedDealsAdapter(CategoryWiseOfferActivity.this, offerByCatModel.getOffercontent(), GlobalClass.HOME, CategoryWiseOfferActivity.this);
                            offerList.setAdapter(offerAdapter);
                            offerList.setVisibility(View.VISIBLE);
                        }
                        else {
                            Toast.makeText (CategoryWiseOfferActivity.this, offerByCatModel.getMsg(),Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText (CategoryWiseOfferActivity.this, "UNPROPER_RESPONSE",Toast.LENGTH_LONG).show();
                    }
                    // dismisProgressHUD ();
                }

                @Override
                public void onFailure(String apiResponse) {
                    //  dismisProgressHUD ();
                    customDialog.dismiss();
                    Log.e ("in", "error " + apiResponse);
                    Toast.makeText (CategoryWiseOfferActivity.this, apiResponse,Toast.LENGTH_LONG).show();
                }
            });

        }else {
            Toast.makeText(CategoryWiseOfferActivity.this, (getResources().getString(R.string.internet_msg)), Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void onOfferCategory(String json) {
        try {

            JSONObject jsonObject = new JSONObject(json);
            if (jsonObject.getInt("status") == Constants.Success) {

                JSONArray offerArray = jsonObject.getJSONArray("offercontent");
                offerArrayList = new ArrayList<>();
                for (int i = 0; i < offerArray.length(); i++) {
                    JSONObject offerInnerObject = offerArray.getJSONObject(i);
                    OfferModal offerModal = new OfferModal();
                    offerModal.setId(offerInnerObject.getString("id"));
                    offerModal.setOffertitle(offerInnerObject.getString("offertitle"));
                    offerModal.setDiscount(offerInnerObject.getString("discount"));
                    offerModal.setImage(offerInnerObject.getString("image"));
                    offerModal.setSelected(offerInnerObject.getString("selected"));
                    offerModal.setBusinessname(offerInnerObject.getString("businessname"));
                    offerArrayList.add(offerModal);
                }

              /*  offerAdapter = new FeaturedDealsAdapter(this, offerArrayList, GlobalClass.HOME, this);
                offerList.setAdapter(offerAdapter);
                offerList.setVisibility(View.VISIBLE);*/

            } else {
                offerList.setVisibility(View.GONE);
                Toast.makeText(CategoryWiseOfferActivity.this, jsonObject.get("msg").toString(), Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    String isSelected;

    @Override
    public void onClickHomeOrCategoryFavourite(Offercontent offerModal, int position) {
        if (offerModal.getSelected()== 1) {
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
        offerArrayList.get(position).setSelected(isSelected);
        offerAdapter.notifyItemChanged(position);
    }

    private void changeSortType(String type) {
        SortType = type;
        if (type.equals("0")) {
            imgfeatured.setBackground(getResources().getDrawable(R.drawable.radio));
            imgNear.setBackground(getResources().getDrawable(R.drawable.radio_select));
        } else {
            imgfeatured.setBackground(getResources().getDrawable(R.drawable.radio_select));
            imgNear.setBackground(getResources().getDrawable(R.drawable.radio));
        }
    }

    private void FindById() {

        back = (ImageView) findViewById(R.id.back);
        txtTitle = (TextView) findViewById(R.id.txtTitle);

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

        nearLout = (LinearLayout) findViewById(R.id.nearLout);
        featuredLout = (LinearLayout) findViewById(R.id.featuredLout);

        imgfeatured = (ImageView) findViewById(R.id.imgfeatured);
        imgNear = (ImageView) findViewById(R.id.imgNear);

        blackView = (View) findViewById(R.id.blackView);
        btnDone = (LinearLayout) findViewById(R.id.btnDone);
        sortDialogLout = (FrameLayout) findViewById(R.id.sortDialogLout);
        btnCancel = (RelativeLayout) findViewById(R.id.btnCancel);
        sortLout = (RelativeLayout) findViewById(R.id.sortLout);
        offerList = (RecyclerView) findViewById(R.id.offerList);
        offerList.setLayoutManager(new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false));

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
                GlobalClass.logOutUser(CategoryWiseOfferActivity.this, GlobalClass.HOME, img_logout, txt_logout, img_home, txt_home, chandLout);
            }
        });

    }

    private void navigateMenu(String type) {
        Intent i = new Intent(CategoryWiseOfferActivity.this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.putExtra("isFrom", type);
        startActivity(i);
    }
    void setAsFvrt(String id, final String isSelected, final int position) {
        final CustomDialog customDialog;
        if (GlobalClass.isNetworkConnected(CategoryWiseOfferActivity.this)) {
            customDialog = new CustomDialog(CategoryWiseOfferActivity.this);
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
                        Toast.makeText (CategoryWiseOfferActivity.this, generalModel.getMsg(),Toast.LENGTH_LONG).show();
                        offerByCatModel.getOffercontent().get(position).setSelected(Integer.valueOf(isSelected));
                        offerAdapter.notifyItemChanged(position);
                    } else {
                        Toast.makeText (CategoryWiseOfferActivity.this, "UNPROPER_RESPONSE",Toast.LENGTH_LONG).show();
                    }
                    // dismisProgressHUD ();
                }

                @Override
                public void onFailure(String apiResponse) {
                    //  dismisProgressHUD ();
                    customDialog.dismiss();
                    Log.e ("in", "error " + apiResponse);
                    Toast.makeText (CategoryWiseOfferActivity.this, apiResponse,Toast.LENGTH_LONG).show();
                }
            });

        }else {
            Toast.makeText(CategoryWiseOfferActivity.this, (getResources().getString(R.string.internet_msg)), Toast.LENGTH_SHORT).show();
        }
    }

}
