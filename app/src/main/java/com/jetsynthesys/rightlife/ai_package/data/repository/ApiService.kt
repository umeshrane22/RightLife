package com.jetsynthesys.rightlife.ai_package.data.repository

import com.jetsynthesys.rightlife.ai_package.model.ActiveCaloriesResponse
import com.jetsynthesys.rightlife.ai_package.model.AddToolRequest
import com.jetsynthesys.rightlife.ai_package.model.HealthSummaryResponse
import com.jetsynthesys.rightlife.ai_package.model.MealLogsResponseModel
import com.jetsynthesys.rightlife.ai_package.model.MealsResponse
import com.jetsynthesys.rightlife.ai_package.model.NutritionResponse
import com.jetsynthesys.rightlife.ai_package.model.RecipeResponseModel
import com.jetsynthesys.rightlife.ai_package.model.RestorativeSleepResponse
import com.jetsynthesys.rightlife.ai_package.model.AnalysisRequest
import com.jetsynthesys.rightlife.ai_package.model.AssignRoutineRequest
import com.jetsynthesys.rightlife.ai_package.model.AssignRoutineResponse
import com.jetsynthesys.rightlife.ai_package.model.BaseResponse
import com.jetsynthesys.rightlife.ai_package.model.DeleteCalorieResponse
import com.jetsynthesys.rightlife.ai_package.model.FitnessResponse
import com.jetsynthesys.rightlife.ai_package.model.FoodDetailsResponse
import com.jetsynthesys.rightlife.ai_package.model.FrequentlyLoggedResponse
import com.jetsynthesys.rightlife.ai_package.model.HeartRateVariabilityResponse
import com.jetsynthesys.rightlife.ai_package.model.ModuleResponse
import com.jetsynthesys.rightlife.ai_package.model.ScanMealNutritionResponse
import com.jetsynthesys.rightlife.ai_package.model.SleepConsistencyResponse
import com.jetsynthesys.rightlife.ai_package.model.SleepIdealActualResponse
import com.jetsynthesys.rightlife.ai_package.model.SleepLandingResponse
import com.jetsynthesys.rightlife.ai_package.model.SleepPerformanceResponse
import com.jetsynthesys.rightlife.ai_package.model.SleepStageResponse
import com.jetsynthesys.rightlife.ai_package.model.ThinkQuoteResponse
import com.jetsynthesys.rightlife.ai_package.model.ToolsGridResponse
import com.jetsynthesys.rightlife.ai_package.model.ToolsResponse
import com.jetsynthesys.rightlife.ai_package.model.UpdateCalorieRequest
import com.jetsynthesys.rightlife.ai_package.model.UpdateCalorieResponse
import com.jetsynthesys.rightlife.ai_package.model.WorkoutMoveMainResponseRoutine
import com.jetsynthesys.rightlife.ai_package.model.WorkoutMoveResponseRoutine
import com.jetsynthesys.rightlife.ai_package.model.WorkoutResponse
import com.jetsynthesys.rightlife.ai_package.model.WorkoutResponseModel
import com.jetsynthesys.rightlife.ai_package.model.WorkoutResponseRoutine
import com.jetsynthesys.rightlife.ai_package.ui.eatright.model.LandingPageResponse
import com.jetsynthesys.rightlife.ai_package.ui.sleepright.model.AssessmentResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Url

interface ApiService {

    @GET("app/api/activityMaster")
    fun getWorkoutList(
        @Header("Authorization") authToken: String): Call<WorkoutResponseModel>

    @GET("app/api/meal-plan/meal-recipes-lists")
    fun getMealRecipesList(
        @Header("Authorization") authToken: String): Call<RecipeResponseModel>

    @GET("app/api/meal-plan/meal-recipes-lists/{foodId}")
    fun getMealRecipesDetails(
        @Path("foodId") foodId: String,
        @Header("Authorization") authToken: String): Call<FoodDetailsResponse>

    @GET("app/api/tools")
    fun getToolList(
        @Header("Authorization") authToken: String,
        @Query("userId") userId: String
    ): Call<ModuleResponse>

    @GET("app/api/meal-plan/meal-logs")
    fun getMealLogLists(
        @Header("Authorization") authToken: String): Call<MealLogsResponseModel>

    @GET("eat/get-meals/")
    fun getMeal(@Query("user_id") userId: String,
                @Query("date") startDate: String): Call<MealsResponse>

    @GET("eat/log-meal/")
    fun logMeal(@Query("user_id") userId: String,
                @Query("date") startDate: String): Call<MealsResponse>

    @GET("move/data/user_workouts/")
    suspend fun getUserWorkouts(
        @Query("user_id") userId: String,
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Response<WorkoutResponse>

    @GET("move/data/new_user_workouts/")
    suspend fun getNewUserWorkouts(
        @Query("user_id") userId: String,
        @Query("range_type") rangeType: String,
        @Query("date") date: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Response<WorkoutResponse>

    @GET("move/fetch_active_burned/")
    suspend fun getActiveCalories(
        @Query("user_id") userId: String,
        @Query("period") period: String,
        @Query("date") date: String
    ): Response<ActiveCaloriesResponse>

    @GET("move/fetch_heart_rate_variabililty/")
    suspend fun getHeartRateVariability(
        @Query("user_id") userId: String,
        @Query("period") period: String,
        @Query("date") date: String
    ): Response<HeartRateVariabilityResponse>

    @GET("move/data/user_workouts/frequent/")
    suspend fun getUserFrequentlyWorkouts(
        @Query("user_id") userId: String,
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("min_count") minCount: Int? = null
    ): Response<FrequentlyLoggedResponse>

    @GET("eat/landing-page/")
    fun getMealSummary(
        @Query("user_id") userId: String,
        @Query("date") startDate: String): Call<LandingPageResponse>

    @GET("sleep/fetch_sleep_stage/")
    fun fetchSleepStage(
        @Query("user_id") userId: String,
        @Query("source") source: String,
        @Query("date") date: String
    ): Call<SleepStageResponse>

    @GET("app/api/quoteOfDay")
    fun quoteOfDay(@Header("Authorization") authToken: String): Call<ThinkQuoteResponse>

    @GET("app/api/mind-audit/q/get-assessment-result")
    fun getAssessmentResult(@Header("Authorization") authToken: String): Call<AssessmentResponse>

    @GET("app/api/tools")
    fun fetchToolsList(@Header("Authorization") authToken: String,@Query("userId") userId: String,@Query("filteredKey") filteredKey: String): Call<ToolsResponse>

    @GET("app/api/tools")
    fun fetchToolsListAll(@Header("Authorization") authToken: String,@Query("filteredKey") filteredKey: String): Call<ToolsResponse>


    @POST("app/api/tools")
    fun selectTools(@Header("Authorization") authToken: String, @Body addToolRequest: AddToolRequest,): Call<BaseResponse>

    @GET("app/api/tools")
    fun thinkTools(@Header("Authorization") authToken: String): Call<ToolsGridResponse>

    @GET("sleep/fetch_sleep_performance_data/")
    fun fetchSleepPerformance(
        @Query("user_id") userId: String,
        @Query("source") source: String,
        @Query("period") period: String
    ): Call<SleepPerformanceResponse>

    @GET("sleep/ideal_vs_actual_sleepTime_detail/")
    fun fetchSleepIdealActual(
        @Query("user_id") userId: String,
        @Query("source") source: String,
        @Query("period") period: String
    ): Call<SleepIdealActualResponse>

    @GET("sleep/sleep_consistency_details/")
    fun fetchSleepConsistencyDetail(
        @Query("user_id") userId: String,
        @Query("source") source: String,
        @Query("period") period: String
    ): Call<SleepConsistencyResponse>

    @GET("sleep/restorative_sleep_detail/")
    fun fetchSleepRestorativeDetail(
        @Query("user_id") userId: String,
        @Query("source") source: String,
        @Query("period") period: String,
        @Query("date") date: String
    ): Call<RestorativeSleepResponse>

    @GET("sleep/landing_page/")
    fun fetchSleepLandingPage(
        @Query("user_id") userId: String,
        @Query("source") source: String,
        @Query("date") date: String,
        @Query("user_preferences") preferences: String,
    ): Call<SleepLandingResponse>

    @GET("move/landing_page/")
    suspend fun getMoveLanding(
        @Query("user_id") userId: String,
        @Query("date") date: String
    ): Response<FitnessResponse>


    @DELETE("move/data/delete_calories/")
    suspend fun deleteCalorie(
        @Query("calorie_id") calorieId: String,
        @Query("user_id") userId: String
    ): Response<DeleteCalorieResponse>

    @PUT("move/data/update_calories/")
    suspend fun updateCalorie(
        @Query("calorie_id") calorieId: String,
        @Body request: UpdateCalorieRequest
    ): Response<UpdateCalorieResponse>

    @GET("move/fetch_routines/")
    suspend fun getMoveRoutine(
        @Query("user_id") userId: String,
        @Query("provided_date") providedDate: String
    ): Response<WorkoutResponseRoutine>

    @POST("move/assign_routine/")
    suspend fun assignRoutine(
        @Query("_id") id: String,
        @Query("user_id") userId: String,
        @Body request: AssignRoutineRequest
    ): Response<AssignRoutineResponse>

    @GET("move/data/get_calories/")
    suspend fun getFetchWorkouts(
        @Query("user_id") userId: String,
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("include_stats") includeStats: Boolean // <-- Added this
    ): Response<WorkoutMoveResponseRoutine>

    @GET("move/fetch_calorie_analysis/")
    suspend fun getFetchCalorieAnalysis(
        @Query("user_id") userId: String,
        @Query("source") source: String,
        @Query("period") period: String
    ): Response<WorkoutMoveMainResponseRoutine>

    @Multipart
    @POST("food/images/analyze/")
    fun uploadFoodFile(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Query("apiKey") apiKey: String
    ): Call<NutritionResponse>

    @Multipart
    @POST("analysis/")
    fun uploadFoodImageFile(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Query("apiKey") apiKey: String
    ): Call<NutritionResponse>

    @PUT
    fun analyzeFoodImage(
        @Url url: String,
        @Body request: AnalysisRequest
    ): Call<ScanMealNutritionResponse>
}


