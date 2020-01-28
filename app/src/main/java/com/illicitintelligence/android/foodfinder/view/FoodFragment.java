package com.illicitintelligence.android.foodfinder.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.illicitintelligence.android.foodfinder.R;
import com.illicitintelligence.android.foodfinder.adapter.RVAdapterFrag;
import com.illicitintelligence.android.foodfinder.database.FavoriteEntity;
import com.illicitintelligence.android.foodfinder.model.Result;
import com.illicitintelligence.android.foodfinder.util.Constants;
import com.illicitintelligence.android.foodfinder.viewmodel.MapsViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FoodFragment extends Fragment {

    private Result result;
    private FavoriteEntity entity;
    private MapsViewModel viewModel;
    @BindView(R.id.food_pictures)
    ImageView foodpic;
    @BindView(R.id.rest_name)
    TextView restName;
    private static final String TAG = "TAG_X";
    @BindView(R.id.add_to_favorites)
    Button favoritesButton;


    String latlong = "";
    String reference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_layout,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this,view);
        viewModel = ViewModelProviders.of(this).get(MapsViewModel.class);

        Bundle bundle = getArguments();
        result = bundle.getParcelable(Constants.RESULT_KEY);
        entity = bundle.getParcelable(Constants.ENTITY_KEY);
        if(result!=null){
            latlong += result.getGeometry().getLocation().getLat();
            latlong += ',';
            latlong += result.getGeometry().getLocation().getLng();
            reference = String.format(Constants.SAMPLE,
                    200,
                    200,
                    result.getPhotos().get(0).getPhotoReference(),
                    Constants.API_KEY);
            restName.setText(result.getName());
        }else if(entity!=null){
            latlong = entity.getLocation();
            reference = entity.getImageURL();
            favoritesButton.setVisibility(View.INVISIBLE);
            restName.setText(entity.getRestaurantName());
        }
        Glide.with(this).load(reference).into(foodpic);
    }

    @OnClick(R.id.directions)
    public void onClick(View v) {
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + latlong);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        Log.d(TAG, "onClick: "+gmmIntentUri.toString());
        if (mapIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(mapIntent);
            Log.d("TAG_X", "onClick: success");
        } else {
            Log.d("TAG_X", "onClick: failed");
        }
    }

    @OnClick(R.id.add_to_favorites)
    public void addToFavorites(View view){
        Log.d(TAG, "addToFavorites: ");
        FavoriteEntity entity = new FavoriteEntity(result.getName(),reference, System.currentTimeMillis(),latlong);
        Log.d(TAG, "addToFavorites: "+entity.toString());
        viewModel.addFavorite(entity);
        favoritesButton.setVisibility(View.INVISIBLE);
    }

}
