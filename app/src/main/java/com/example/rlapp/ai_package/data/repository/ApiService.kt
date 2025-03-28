package com.example.rlapp.ai_package.data.repository

import com.example.rlapp.ai_package.model.MealLogsResponseModel
import com.example.rlapp.ai_package.model.RecipeResponseModel
import com.example.rlapp.ai_package.model.SleepConsistencyResponse
import com.example.rlapp.ai_package.model.SleepIdealActualResponse
import com.example.rlapp.ai_package.model.SleepPerformanceResponse
import com.example.rlapp.ai_package.model.SleepStageResponse
import com.example.rlapp.ai_package.model.WorkoutResponse
import com.example.rlapp.ai_package.model.WorkoutResponseModel
import com.example.rlapp.ai_package.ui.eatright.model.LandingPageResponse
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

     @GET("app/api/meal-plan/meal-logs")
     fun getWorkout(@Header("Authorization") authToken: String): Call<MealLogsResponseModel>

    @GET("move/data/user_workouts/")
    suspend fun getUserWorkouts(
        @Query("user_id") userId: String,
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Response<WorkoutResponse>

    @GET("eat/landing-page/64763fe2fa0e40d9c0bc8264/")
    fun getMealSummary(
        @Query("date") startDate: String): Call<LandingPageResponse>

    @GET("sleep/fetch_sleep_stage/")
    suspend fun fetchSleepStage(
        @Query("user_id") userId: String,
        @Query("source") source: String,
        @Query("date") date: String
    ): Response<SleepStageResponse>

    @GET("sleep/fetch_sleep_performance_data/")
    suspend fun fetchSleepPerformance(
        @Query("user_id") userId: String,
        @Query("source") source: String,
        @Query("period") date: String
    ): Response<SleepPerformanceResponse>

    @GET("sleep/ideal_vs_actual_sleepTime_detail/")
    suspend fun fetchSleepIdealActual(
        @Query("user_id") userId: String,
        @Query("source") source: String,
        @Query("period") date: String
    ): Response<SleepIdealActualResponse>

    @GET("sleep/sleep_consistency_details/")
    suspend fun fetchSleepConsistencyDetail(
        @Query("user_id") userId: String,
        @Query("source") source: String,
        @Query("period") date: String
    ): Response<SleepConsistencyResponse>

    @GET("sleep/restorative_sleep_detail/")
    suspend fun fetchSleepRestorativeDetail(
        @Query("user_id") userId: String,
        @Query("source") source: String,
        @Query("period") date: String
    ): Response<SleepConsistencyResponse>
}


