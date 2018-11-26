package com.example.diogenes.listmedphone;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diogenes.listmedphone.adapters.ActorsAdapter;
import com.example.diogenes.listmedphone.controllers.ActorController;
import com.example.diogenes.listmedphone.dao.ActorDB;
import com.example.diogenes.listmedphone.interfaces.ClickListener;
import com.example.diogenes.listmedphone.interfaces.MedphoneAPI;
import com.example.diogenes.listmedphone.model.Actor;
import com.example.diogenes.listmedphone.util.DownloadImageTask;
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
    private ActorsAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.list_actors_medphone);

        if (isAvaliable()) {
            mRecyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(mLayoutManager);

            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.show();

            setupRecycler();


        } else {
            alertYesNo();
        }
        Log.v("medphone", "Activity Main criada");
    }

    private void setupRecycler() {
        Retrofit retrofit = ActorController.start();
        MedphoneAPI medphoneAPI = retrofit.create(MedphoneAPI.class);
        Call<List<Actor>> call = medphoneAPI.loadActors();
        call.enqueue(new Callback<List<Actor>>() {
            @Override
            public void onResponse(Call<List<Actor>> call, Response<List<Actor>> response) {
                mProgressDialog.dismiss();
                if (response != null && response.isSuccessful()) {
                    mAdapter = new ActorsAdapter(response.body());
                    try {
                        ActorDB actorDB = new ActorDB(getBaseContext());
                        List<Actor> actorListDB = actorDB.findAll();
                        List<Actor> actorListWS = mAdapter.actorsList;
                        for (Actor actor : actorListDB) {
                            for (Actor actorWS : actorListWS) {
                                if (actor.id == actorWS.id) {
                                    actorWS.favorite = true;
                                    break;
                                }
                            }
                        }
                        mRecyclerView.setAdapter(mAdapter);
                        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(),
                                mRecyclerView, new ClickListener() {
                            @Override
                            public void onClick(View view, final int position) {
                                Intent intent = new Intent(getApplicationContext(), ActorDetailActivity.class);
                                intent.putExtra("actor", mAdapter.getItem(position));
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                            @Override
                            public void onLongClick(View view, int position) {

                            }
                        }));

                        mRecyclerView.addItemDecoration(
                                new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
                    } catch (Exception e) {
                        Log.e("medphone", e.getMessage());
                        e.printStackTrace();
                        Toast.makeText(getBaseContext(), "Loading data fail", Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<List<Actor>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Falha no servidor", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        setupRecycler();
    }

    private boolean isAvaliable() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if (activeNetwork != null) {
            // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
                return true;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to mobile data
                return true;
            }
        } else {
            // not connected to the internet

            return false;
        }
        return false;
    }

    public void alertYesNo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(android.R.drawable.stat_sys_download);
        builder.setTitle("There isn't internet");
        builder.setMessage("Do you want connect?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                WifiManager wifiManager = (WifiManager)getApplication().getSystemService(Context.WIFI_SERVICE);
                wifiManager.setWifiEnabled(true);
                onRestart();
                return;
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertOk();
                return;
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void alertOk() {
        //ALERT DIALOG CRIADO
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //SETAR AS PROPRIEDADES DO ALERTDIALOG
        builder.setIcon(android.R.drawable.stat_sys_download);
        builder.setTitle("Atenção");
        builder.setMessage("Not possible to load actor list");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //FAZ O QUE VOCÊ QUISER
                Toast.makeText(getBaseContext(), "Connect to internet", Toast.LENGTH_LONG).show();
                return;
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();


    }
}
