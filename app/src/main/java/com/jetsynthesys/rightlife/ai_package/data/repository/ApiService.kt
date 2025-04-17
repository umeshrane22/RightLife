package com.jetsynthesys.rightlife.ai_package.data.repository

import com.jetsynthesys.rightlife.ai_package.model.ActiveCaloriesResponse
import com.jetsynthesys.rightlife.ai_package.model.AddToolRequest
import com.jetsynthesys.rightlife.ai_package.model.MealLogsResponseModel
import com.jetsynthesys.rightlife.ai_package.model.MealsResponse
import com.jetsynthesys.rightlife.ai_package.model.NutritionResponse
import com.jetsynthesys.rightlife.ai_package.model.RecipeResponseModel
import com.jetsynthesys.rightlife.ai_package.model.RestorativeSleepResponse
import com.jetsynthesys.rightlife.ai_package.model.AnalysisRequest
import com.jetsynthesys.rightlife.ai_package.model.AssignRoutineRequest
import com.jetsynthesys.rightlife.ai_package.model.AssignRoutineResponse
import com.jetsynthesys.rightlife.ai_package.model.BaseResponse
import com.jetsynthesys.rightlife.ai_package.model.CalculateCaloriesRequest
import com.jetsynthesys.rightlife.ai_package.model.CalculateCaloriesResponse
import com.jetsynthesys.rightlife.ai_package.model.CreateRoutineRequest
import com.jetsynthesys.rightlife.ai_package.model.CreateRoutineResponse
import com.jetsynthesys.rightlife.ai_package.model.DeleteCalorieResponse
import com.jetsynthesys.rightlife.ai_package.model.FitnessResponse
import com.jetsynthesys.rightlife.ai_package.model.FoodDetailsResponse
import com.jetsynthesys.rightlife.ai_package.model.MealLogRequest
import com.jetsynthesys.rightlife.ai_package.model.MealLogResponse
import com.jetsynthesys.rightlife.ai_package.model.FrequentlyLoggedResponse
import com.jetsynthesys.rightlife.ai_package.model.HeartRateResponse
import com.jetsynthesys.rightlife.ai_package.model.HeartRateVariabilityResponse
import com.jetsynthesys.rightlife.ai_package.model.MindfullResponse
import com.jetsynthesys.rightlife.ai_package.model.ModuleResponse
import com.jetsynthesys.rightlife.ai_package.model.MoodTrackerMonthlyResponse
import com.jetsynthesys.rightlife.ai_package.model.MoodTrackerWeeklyResponse
import com.jetsynthesys.rightlife.ai_package.model.RestingHeartRateResponse
import com.jetsynthesys.rightlife.ai_package.model.RoutineResponse
import com.jetsynthesys.rightlife.ai_package.model.ScanMealNutritionResponse
import com.jetsynthesys.rightlife.ai_package.model.SleepConsistencyResponse
import com.jetsynthesys.rightlife.ai_package.model.SleepIdealActualResponse
import com.jetsynthesys.rightlife.ai_package.model.SleepLandingResponse
import com.jetsynthesys.rightlife.ai_package.model.SleepPerformanceResponse
import com.jetsynthesys.rightlife.ai_package.model.SleepStageResponse
import com.jetsynthesys.rightlife.ai_package.model.StoreHealthDataRequest
import com.jetsynthesys.rightlife.ai_package.model.StoreHealthDataResponse
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
import com.jetsynthesys.rightlife.ai_package.model.request.MealPlanLogRequest
import com.jetsynthesys.rightlife.ai_package.model.request.MealPlanRequest
import com.jetsynthesys.rightlife.ai_package.model.response.MealLogPlanResponse
import com.jetsynthesys.rightlife.ai_package.model.response.MealPlanResponse
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
    fun getMealList(@Query("user_id") userId: String,
                @Query("date") startDate: String): Call<MealsResponse>

    @POST("eat/log-meal/")
    fun createLogDish(@Body request: MealLogRequest): Call<MealLogResponse>

    @POST("eat/meal-plans/")
    fun createLogMeal(@Query("user_id") userId: String,@Body request: MealPlanRequest): Call<MealPlanResponse>

    @GET("eat/meal-plans/")
    fun getLogMealList(@Query("user_id") userId: String): Call<MealLogPlanResponse>

    @POST("eat/meal-plans/log/")
    fun createMealPlanLog(@Query("user_id") userId: String,@Query("meal_plan_id") mealPlanId: String,@Body request: MealPlanLogRequest): Call<MealPlanResponse>

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

    @POST("move/store/")
    suspend fun storeHealthData(
        @Body request: StoreHealthDataRequest
    ): Response<StoreHealthDataResponse>

    @POST("move/routine/create/")
    suspend fun createRoutine(
        @Body request: CreateRoutineRequest
    ): Response<CreateRoutineResponse>

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

    @GET("move/fetch_resting_heart_rate/")
    suspend fun getRestingHeartRate(
        @Query("user_id") userId: String,
        @Query("period") period: String,
        @Query("date") date: String
    ): Response<RestingHeartRateResponse>

    @GET("move/fetch_heart_rate/")
    suspend fun getHeartRate(
        @Query("user_id") userId: String,
        @Query("period") period: String,
        @Query("date") date: String
    ): Response<HeartRateResponse>

    @POST("move/data/calculate_calories/")
    suspend fun calculateCalories(
        @Body request: CalculateCaloriesRequest
    ): Response<CalculateCaloriesResponse>

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
        @Query("user_id") userId: String): Call<LandingPageResponse>

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

    @GET("app/api/mindFull")
    fun fetchMindFull(@Header("Authorization") authToken: String,@Query("startDate") startDate: String,
                      @Query("endDate") endDate: String): Call<MindfullResponse>

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

    @GET("app/api/journalNew/recordEmotion")
    fun fetchMoodTrackerPercentage(@Header("Authorization") authToken: String,
        @Query("type") type: String,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String
    ): Call<MoodTrackerWeeklyResponse>

    @GET("app/api/journalNew/recordEmotion")
    fun fetchMoodTrackerMonthly(@Header("Authorization") authToken: String, @Query("type") type: String,
                                   @Query("startDate") startDate: String, @Query("endDate") endDate: String
    ): Call<MoodTrackerMonthlyResponse>

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

    @GET("move/routine/")
    suspend fun getMoveRoutine(
        @Query("user_id") userId: String,
       // @Query("provided_date") providedDate: String
    ): Response<WorkoutResponseRoutine>

    @GET("move/routine/")
    suspend fun getRoutines(
        @Query("user_id") userId: String
    ): Response<List<RoutineResponse>>

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


