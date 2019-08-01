package com.riyadhbank.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.riyadhbank.Adapter.FavouriteAdapter;
import com.riyadhbank.Async.FavouriteListService;
import com.riyadhbank.Async.FavouriteService;
import com.riyadhbank.Modal.CategoryModal;
import com.riyadhbank.Modal.FavouriteModal;
import com.riyadhbank.R;
import com.riyadhbank.Retrofit.ApiRequestHelper;
import com.riyadhbank.Retrofit.App;
import com.riyadhbank.Retrofit.DashboardModel;
import com.riyadhbank.Retrofit.FavouriteModel;
import com.riyadhbank.Retrofit.GeneralModel;
import com.riyadhbank.Utility.Constants;
import com.riyadhbank.Utility.CustomDialog;
import com.riyadhbank.Utility.GlobalClass;
import com.riyadhbank.Utility.Interface;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class FavouriteFragment extends Fragment implements Interface.OnFavouriteList, Interface.OnClickFavourite, Interface.OnFavourite {

    View view;
    ImageView back;
    TextView txtTitle;
    RecyclerView favouriteList;
    ArrayList<FavouriteModal> favouriteArrayList;
    FavouriteAdapter favouriteAdapter;
    String Language, UserId;
    CustomDialog customDialog;
    FavouriteModel favouriteModal;
App app;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_favourite, container, false);

        FindById();
        app = (App) getActivity().getApplication();
        customDialog = new CustomDialog(getContext());

        back.setVisibility(View.GONE);
        txtTitle.setText(getActivity().getResources().getString(R.string.my_fav));

        if (GlobalClass.languageCode.equals("") || GlobalClass.languageCode.equals("en")) {
            Language = "english";
        } else {
            Language = "arabic";
        }

        SharedPreferences shared = getActivity().getSharedPreferences(Constants.UserDetailPref, MODE_PRIVATE);
        UserId = shared.getString(Constants.userid, "");



        if (GlobalClass.isNetworkConnected(getActivity())) {
            getfvrtData();
            //new FavouriteListService(getActivity(), FavouriteFragment.this, Language, UserId).execute();
        } else {
            Toast.makeText(getActivity(), (getResources().getString(R.string.internet_msg)), Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    @Override
    public void onFavouriteList(String json) {
        try {

            JSONObject jsonObject = new JSONObject(json);
            if (jsonObject.getInt("status") == Constants.Success) {

                JSONArray favouriteArray = jsonObject.getJSONArray("offercontent");
                favouriteArrayList = new ArrayList<>();
                for (int i = 0; i < favouriteArray.length(); i++) {
                    JSONObject offerInnerObject = favouriteArray.getJSONObject(i);
                    FavouriteModal favouriteModal = new FavouriteModal();
                    favouriteModal.setId(offerInnerObject.getString("id"));
                    favouriteModal.setOffertitle(offerInnerObject.getString("offertitle"));
                    favouriteModal.setDiscount(offerInnerObject.getString("discount"));
                    favouriteModal.setBusinessname(offerInnerObject.getString("businessname"));
                    favouriteModal.setImage(offerInnerObject.getString("image"));
                    favouriteModal.setSelected(offerInnerObject.getString("selected"));
                    favouriteArrayList.add(favouriteModal);
                }

                /*favouriteAdapter = new FavouriteAdapter(getActivity(), favouriteArrayList, GlobalClass.FAVOURITE, FavouriteFragment.this);
                favouriteList.setAdapter(favouriteAdapter);
                favouriteList.setVisibility(View.VISIBLE);
*/
            } else {
                favouriteList.setVisibility(View.GONE);
                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.no_favourite), Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void getfvrtData() {
        if (GlobalClass.isNetworkConnected(getActivity())) {
            customDialog.show();

            Map<String, String> params = new HashMap<>();

            params.put(Constants.action, Constants.myfavourite);
            params.put(Constants.language, Language);
            params.put(Constants.userid, UserId);
            params.put(Constants.appid, GlobalClass.Riyadh);

            app.getApiRequestHelper ().getFavourate (params, new ApiRequestHelper.OnRequestComplete () {
                @Override
                public void onSuccess(Object object) {
                    customDialog.dismiss();
                    favouriteModal = (FavouriteModel) object;
                    Log.e ("in", "success");
                    if (favouriteModal != null) {
                        if (favouriteModal.getStatus()==1) {
                            favouriteAdapter = new FavouriteAdapter(getActivity(), favouriteModal.getOffercontent(), GlobalClass.FAVOURITE, FavouriteFragment.this);
                            favouriteList.setAdapter(favouriteAdapter);
                            favouriteList.setVisibility(View.VISIBLE);
                        }
                        else {
                            Toast.makeText (getContext(), favouriteModal.getMsg(),Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText (getContext(), "UNPROPER_RESPONSE",Toast.LENGTH_LONG).show();
                    }
                    // dismisProgressHUD ();
                }

                @Override
                public void onFailure(String apiResponse) {
                    //  dismisProgressHUD ();
                    customDialog.dismiss();
                    Log.e ("in", "error " + apiResponse);
                    Toast.makeText (getActivity(), apiResponse,Toast.LENGTH_LONG).show();
                }
            });

        }else {
            Toast.makeText(getActivity(), (getResources().getString(R.string.internet_msg)), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClickFavourite(FavouriteModal favouriteModal, int position) {
        if (GlobalClass.isNetworkConnected(getActivity())) {

            setAsFvrt(favouriteModal.getId(), "0",position);
            // new FavouriteService(getActivity(), this, UserId, favouriteModal.getId(), "0", position).execute();
        } else {
            Toast.makeText(getActivity(), (getResources().getString(R.string.internet_msg)), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFavourite(int position) {
        favouriteArrayList.remove(position);
        favouriteAdapter.notifyDataSetChanged();
    }

    private void FindById() {
        back = (ImageView) view.findViewById(R.id.back);
        txtTitle = (TextView) view.findViewById(R.id.txtTitle);

        favouriteList = (RecyclerView) view.findViewById(R.id.favouriteList);
        favouriteList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

    }

    void setAsFvrt(String id, final String isSelected, final int position) {
        if (GlobalClass.isNetworkConnected(getContext())) {
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
                    GeneralModel searchModel = (GeneralModel) object;
                    Log.e ("in", "success");
                    if (searchModel != null) {
                        Toast.makeText (getContext(), searchModel.getMsg(),Toast.LENGTH_LONG).show();
                        //favouriteAdapter.getItemId(position)
                        favouriteModal.getOffercontent().get(position).setSelected(0);
                        favouriteAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText (getContext(), "UNPROPER_RESPONSE",Toast.LENGTH_LONG).show();
                    }
                    // dismisProgressHUD ();
                }

                @Override
                public void onFailure(String apiResponse) {
                    //  dismisProgressHUD ();
                    customDialog.dismiss();
                    Log.e ("in", "error " + apiResponse);
                    Toast.makeText (getContext(), apiResponse,Toast.LENGTH_LONG).show();
                }
            });

        }else {
            Toast.makeText(getContext(), (getResources().getString(R.string.internet_msg)), Toast.LENGTH_SHORT).show();
        }
    }

}
