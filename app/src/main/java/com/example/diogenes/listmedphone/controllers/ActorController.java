package com.example.diogenes.listmedphone.controllers;


import android.util.Log;

import com.example.diogenes.listmedphone.interfaces.MedphoneAPI;
import com.example.diogenes.listmedphone.model.Actor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActorController implements Callback<List<Actor>> {

    static final String BASE_URL = "http://5bf57c322a6f080013a34eaf.mockapi.io/api/v1/";
    List<Actor> actorList;

    public static Retrofit start() {
//        try {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            return new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

/*            MedphoneAPI medphoneAPI = retrofit.create(MedphoneAPI.class);

            Call<List<Actor>> call = medphoneAPI.loadActors();
            call.enqueue(this);

            return actorList;*/

/*            return call.execute().body();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }*/

    }

    @Override
    public void onResponse(Call<List<Actor>> call, Response<List<Actor>> response) {
        if (response.isSuccessful()) {
            actorList = response.body();
            actorList.forEach(actor -> Log.v("medphone",actor.name));
        } else {
            System.out.println(response.errorBody());
        }
    }

    @Override
    public void onFailure(Call<List<Actor>> call, Throwable t) {

    }
}
