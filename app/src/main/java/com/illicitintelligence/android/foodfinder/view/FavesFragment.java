package com.illicitintelligence.android.foodfinder.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.illicitintelligence.android.foodfinder.R;
import com.illicitintelligence.android.foodfinder.adapter.RVAdapterFaves;
import com.illicitintelligence.android.foodfinder.adapter.Swiper;
import com.illicitintelligence.android.foodfinder.database.FavoriteEntity;
import com.illicitintelligence.android.foodfinder.model.Result;
import com.illicitintelligence.android.foodfinder.util.Constants;
import com.illicitintelligence.android.foodfinder.viewmodel.MapsViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavesFragment extends Fragment implements RVAdapterFaves.Clicker{

    List<FavoriteEntity> entities;
    MapsViewModel viewModel;
    @BindView(R.id.favorites_rv)
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.favorites_frag,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);

        viewModel = ViewModelProviders.of(this).get(MapsViewModel.class);
        entities = viewModel.getFavorites();
        RVAdapterFaves adapterFaves = new RVAdapterFaves(entities, this, this.getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(adapterFaves);
        Swiper swiper = new Swiper(new Swiper.SwiperDelegate() {
            @Override
            public void getPosition(int position) {
                viewModel.deleteFavorite(position);
                adapterFaves.notifyItemRemoved(position);
            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swiper);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void click(FavoriteEntity entity) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.ENTITY_KEY,entity);
        Fragment foodFragment = new FoodFragment();
        foodFragment.setArguments(bundle);
        getFragmentManager().beginTransaction()
                .addToBackStack(foodFragment.getTag())
                .add(R.id.frag_framelayout,foodFragment)
                .commit();
    }
}
