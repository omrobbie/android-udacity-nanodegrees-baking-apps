package com.omrobbie.bakingapp.util.rest;

import com.omrobbie.bakingapp.database.pojo.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface APIService {

    @GET(".")
    Call<List<Recipe>> getRecipes();

}
