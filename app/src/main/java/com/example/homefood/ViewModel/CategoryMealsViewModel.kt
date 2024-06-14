package com.example.homefood.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.homefood.dtclasses.MealsByCategory
import com.example.homefood.dtclasses.MealsByCategoryList
import com.example.homefood.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryMealsViewModel(): ViewModel() {

    val mealsLiveData = MutableLiveData<List<MealsByCategory>>()

    fun getMealsByCategory(categoryName:String){
        RetrofitInstance.api.getMealsByCategory(categoryName).enqueue(object : Callback<MealsByCategoryList>{
            override fun onResponse(call: Call<MealsByCategoryList>, response: Response<MealsByCategoryList>
            ) {
                response.body()?.let { mealslist ->
                    mealsLiveData.postValue(mealslist.meals)
                }
            }

            override fun onFailure(call: Call<MealsByCategoryList>, t: Throwable) {
                Log.d("MealsByCategoryList: ", t.message.toString())
            }


        })
    }

    //Share data
    fun observeMealsLiveData(): LiveData<List<MealsByCategory>>{
        return mealsLiveData
    }

}