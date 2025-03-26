package com.example.rlapp.ai_package.data.repository

import com.example.rlapp.ai_package.model.MealLogsResponseModel
import com.example.rlapp.ai_package.model.RecipeResponseModel
import com.example.rlapp.ai_package.model.WorkoutResponseModel
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("app/api/activityMaster")
    fun getWorkoutList(
        @Header("Authorization") authToken: String): Call<WorkoutResponseModel>

    @GET("app/api/meal-plan/meal-recipes-lists")
    fun getMealRecipesList(
        @Header("Authorization") authToken: String): Call<RecipeResponseModel>

    @GET("app/api/meal-plan/meal-logs")
     fun getMealLogLists(
        @Header("Authorization") authToken: String): Call<MealLogsResponseModel>
}