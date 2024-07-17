package com.mastercoding.pagingapp.activity;


import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.bumptech.glide.RequestManager;
import com.mastercoding.pagingapp.R;
import com.mastercoding.pagingapp.adapter.MoviesAdapter;
import com.mastercoding.pagingapp.adapter.MoviesLoadStateAdapter;
import com.mastercoding.pagingapp.databinding.ActivityMainBinding;
import com.mastercoding.pagingapp.util.GridSpace;
import com.mastercoding.pagingapp.util.MovieComparator;
import com.mastercoding.pagingapp.util.Utils;
import com.mastercoding.pagingapp.viewmodel.MovieViewModel;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {
    MovieViewModel mainActivityViewModel;
    ActivityMainBinding binding;
    MoviesAdapter moviesAdapter;
    @Inject
    RequestManager requestManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if(Utils.API_KEY == null || Utils.API_KEY.isEmpty()){
            Toast.makeText(this, "Please provide API KEY", Toast.LENGTH_SHORT).show();
        }
        moviesAdapter = new MoviesAdapter(new MovieComparator() , requestManager);
        mainActivityViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        }
         private void initRecyclerViewAndAdapter() {
             GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
            binding.recyclerView.setLayoutManager(gridLayoutManager);
            binding.recyclerView.addItemDecoration(new GridSpace(2, 20, true));
            binding.recyclerView.setAdapter(moviesAdapter.withLoadStateFooter(
                    new MoviesLoadStateAdapter(v -> {
                        moviesAdapter.retry();
                    })
            ));
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return moviesAdapter.getItemViewType(position) == MoviesAdapter.MOVIE_ITEM ? 1 : 2;
            }
        });



    }
}