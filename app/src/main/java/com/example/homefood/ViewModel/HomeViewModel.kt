package com.example.homefood.ViewModel
import android.media.tv.BroadcastInfoResponse
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.homefood.db.MealDatabase
import com.example.homefood.dtclasses.Category
import com.example.homefood.dtclasses.CategoryList
import com.example.homefood.dtclasses.MealsByCategoryList
import com.example.homefood.dtclasses.MealsByCategory
import com.example.homefood.dtclasses.Meal
import com.example.homefood.dtclasses.MealList
import com.example.homefood.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(

    //Adding the database
    private val mealDatabase: MealDatabase
):ViewModel() {

    private var randomMealLiveData = MutableLiveData<Meal>()
    private var popularItemsLiveData = MutableLiveData<List<MealsByCategory>>()
    private var categoriesLiveData = MutableLiveData<List<Category>>()
    private var favoriteMealsLiveData = mealDatabase.mealDao().getAllMeals()

    fun getRandomMeal(){

        RetrofitInstance.api.getRandomMeal().enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {

                if(response.body() != null){

                    val randomMeal = response.body()!!.meals[0]

                    randomMealLiveData.value = randomMeal

                }else{
                    return
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {

                Log.d("HomeFragment", t.message.toString())
            }
        })

    }

    //Only read
    fun observeRandomMealLiveData():LiveData<Meal>{
        return randomMealLiveData
    }

    fun getPopularItems(){
        RetrofitInstance.api.getPopularItems("SeaFood").enqueue(object : Callback<MealsByCategoryList>{
            override fun onResponse(call: Call<MealsByCategoryList>, response: Response<MealsByCategoryList>) {
                if (response.body() != null){
                    popularItemsLiveData.value = response.body()!!.meals
                }
                else {
                    return
                }
            }

            override fun onFailure(call: Call<MealsByCategoryList>, t: Throwable) {
                Log.d("Category List: ", t.message.toString())
            }

        })
    }

    fun getCategories(){
        RetrofitInstance.api.getCategories().enqueue(object: Callback<CategoryList> {
            override fun onResponse(call: Call<CategoryList>, response: Response<CategoryList>) {
                response.body()?.let { CategoryList ->
                    categoriesLiveData.postValue(CategoryList.categories)
                }
            }

            override fun onFailure(call: Call<CategoryList>, t: Throwable) {
                Log.d("Each meal Category:", t.message.toString())
            }

        })
    }

    fun observePopularItemsLiveData():LiveData<List<MealsByCategory>>{
        return popularItemsLiveData
    }

    fun observeCategoriesLiveData():LiveData<List<Category>>{
        return categoriesLiveData
    }

    fun observeFavoritesMealsLiveData():LiveData<List<Meal>>{
        return favoriteMealsLiveData
    }

}