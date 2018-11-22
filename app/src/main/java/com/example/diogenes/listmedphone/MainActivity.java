package com.example.diogenes.listmedphone;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.diogenes.listmedphone.adapters.ActorsAdapter;
import com.example.diogenes.listmedphone.controllers.ActorController;
import com.example.diogenes.listmedphone.interfaces.ClickListener;
import com.example.diogenes.listmedphone.interfaces.MedphoneAPI;
import com.example.diogenes.listmedphone.model.Actor;
import com.example.diogenes.listmedphone.util.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity /*implements RecyclerView.OnItemTouchListener */ {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.list_actors_medphone);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        final ProgressDialog mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();


        Retrofit retrofit = ActorController.start();
        MedphoneAPI medphoneAPI = retrofit.create(MedphoneAPI.class);
        Call<List<Actor>> call = medphoneAPI.loadActors();
        call.enqueue(new Callback<List<Actor>>() {
            @Override
            public void onResponse(Call<List<Actor>> call, Response<List<Actor>> response) {
                mProgressDialog.dismiss();
                if(response!=null && response.isSuccessful()) {
                    mAdapter = new ActorsAdapter(response.body());
                    setupRecycler();
                }
            }

            @Override
            public void onFailure(Call<List<Actor>> call, Throwable t) {

            }
        });

//        mAdapter = new ActorsAdapter(actorController.start());


    }

    private void setupRecycler() {

        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                mRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                //Values are passing to activity & to fragment as well
                Toast.makeText(MainActivity.this, "Single Click on position        :" + position,
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {
                Toast.makeText(MainActivity.this, "Long press on position :" + position,
                        Toast.LENGTH_LONG).show();
            }
        }));

        // Configurando um dividir entre linhas, para uma melhor visualização.
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

}
