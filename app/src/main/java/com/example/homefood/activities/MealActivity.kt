package com.example.homefood.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.homefood.R
import com.example.homefood.ViewModel.MealViewModel
import com.example.homefood.ViewModel.MealViewModelFactory
import com.example.homefood.databinding.ActivityMealBinding
import com.example.homefood.db.MealDatabase
import com.example.homefood.dtclasses.Meal
import com.example.homefood.fragments.HomeFragment

class MealActivity : AppCompatActivity() {
    //Instance
    private lateinit var binding: ActivityMealBinding
    private lateinit var mealMvvm: MealViewModel

    //data
    private lateinit var mealID:String
    private lateinit var mealName:String
    private lateinit var mealThumb:String
    private lateinit var youtubeLink:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(binding.root)


        val mealDatabase = MealDatabase.getInstance(this)
        val viewModelFactory = MealViewModelFactory(mealDatabase)
        mealMvvm = ViewModelProvider(this, viewModelFactory)[MealViewModel::class.java]

        //New Function created here
        getMealInformationFromApi()

        //New Function created here
        setInformationInViews()

        //Loading bar status
        loadingCase()

        //Get meal details by ID
        mealMvvm.getMealDetail(mealID)

        //New Function created here
        observerMealDetailsLiveData()

        onYoutubeImageClick()

        onFavoriteClick()
    }

    private fun onFavoriteClick() {
        binding.btnFav.setOnClickListener{
            mealToSave?.let {
                mealMvvm.insertMeal(it)
                Toast.makeText(this, "Meal saved!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onYoutubeImageClick() {

        //Get the control of the view and set an action from link type
        binding.imgYoutube.setOnClickListener{
            val intent = Intent(
                Intent.ACTION_VIEW, Uri.parse(youtubeLink)
            )

            startActivity(intent)
        }

    }

    private var mealToSave:Meal?=null
    private fun observerMealDetailsLiveData() {
        mealMvvm.observeMealDetailLiveData().observe(this, object : Observer<Meal>{
            override fun onChanged(t: Meal) {
                //loading
                onResponseCase()
                val meal = t
                mealToSave = meal

                binding.tvCategory.text = "Category : ${meal!!.strCategory}"
                binding.tvArea.text = "Area : ${meal!!.strArea}"
                binding.tvIngredients.text = meal.strInstructions

                youtubeLink = meal.strYoutube.toString()
            }

        })
    }

    private fun setInformationInViews() {
        //set image
        Glide.with(applicationContext)
            .load(mealThumb)
            .into(binding.imgMealDetail)

        //set title
        binding.collapsingToolbar.title = mealName
        binding.collapsingToolbar.setCollapsedTitleTextColor(resources.getColor(R.color.white))
        binding.collapsingToolbar.setExpandedTitleColor(resources.getColor(R.color.white))
    }

    //Get the meal whole details from API
    private fun getMealInformationFromApi() {
        val intent = intent
        mealID = intent.getStringExtra(HomeFragment.MEAL_ID)!!
        mealName = intent.getStringExtra(HomeFragment.MEAL_NAME)!!
        mealThumb = intent.getStringExtra(HomeFragment.MEAL_THUMB)!!
    }

    //loading bar activity
    private fun loadingCase(){

        binding.progressBar.visibility = View.VISIBLE
        binding.btnFav.visibility = View.INVISIBLE
        binding.tvInstructions.visibility = View.INVISIBLE
        binding.tvCategory.visibility = View.INVISIBLE
        binding.tvArea.visibility = View.INVISIBLE
        binding.imgYoutube.visibility = View.INVISIBLE


    }

    //loading bar on Response activity
    private fun onResponseCase(){
        binding.progressBar.visibility = View.INVISIBLE
        binding.btnFav.visibility = View.VISIBLE
        binding.tvInstructions.visibility = View.VISIBLE
        binding.tvCategory.visibility = View.VISIBLE
        binding.tvArea.visibility = View.VISIBLE
        binding.imgYoutube.visibility = View.VISIBLE
    }
}