package com.example.homefood.retrofit
import com.example.homefood.dtclasses.CategoryList
import com.example.homefood.dtclasses.MealsByCategoryList
import com.example.homefood.dtclasses.MealList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MealAPI {


    //GET method to get the meal details
    @GET("lookup.php")
    fun getMealDetails(@Query("i") id:String) : Call<MealList>

    //GET method to get random meal with MealList
    @GET("random.php")
    fun getRandomMeal():Call<MealList>

    //GET method to get popular items
    @GET("filter.php?")
    fun getPopularItems(@Query("c") category:String):Call<MealsByCategoryList>

    //GET method to get categories
    @GET("categories.php")
    fun getCategories():Call<CategoryList>

    //GET method to get meals by category
    @GET("filter.php")
    fun getMealsByCategory(@Query("c") categoryName:String) : Call<MealsByCategoryList>
}