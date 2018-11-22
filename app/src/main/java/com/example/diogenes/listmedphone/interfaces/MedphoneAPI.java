package com.example.diogenes.listmedphone.interfaces;

import com.example.diogenes.listmedphone.model.Actor;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MedphoneAPI {

    @GET("entity/")
    Call<List<Actor>> loadActors();
}
