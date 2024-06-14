package com.example.homefood.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.homefood.ViewModel.HomeViewModel
import com.example.homefood.activities.CategoryMealsActivity
import com.example.homefood.activities.MainActivity
import com.example.homefood.activities.MealActivity
import com.example.homefood.adapters.CategoriesAdapter
import com.example.homefood.adapters.MostPopularAdapter
import com.example.homefood.databinding.FragmentHomeBinding
import com.example.homefood.dtclasses.MealsByCategory
import com.example.homefood.dtclasses.Meal


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel:HomeViewModel
    private lateinit var randomMeal:Meal

    private lateinit var popularItemsAdapter:MostPopularAdapter
    private lateinit var categoriesAdapter: CategoriesAdapter

    companion object{

        const val MEAL_ID = "com.example.homefood.fragments.idMeal"
        const val MEAL_NAME = "com.example.homefood.fragments.nameMeal"
        const val MEAL_THUMB = "com.example.homefood.fragments.thumbMeal"
        const val CATEGORY_NAME = "com.example.homefood.fragments.categoryName"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ( activity as MainActivity).viewModel
//        homeMvvm = ViewModelProviders.of(this)[HomeViewModel::class.java]

        popularItemsAdapter = MostPopularAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root


    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Get random meal from ViewModel
        viewModel.getRandomMeal()
        ObserverRandomMeal()

        //Get popular items from ViewModel
        viewModel.getPopularItems()
        ObserverPopularItemsLiveData()

        //Instance the ItemRecyclerView
        preparePopularItemsRecyclerView()

        //Local functions to show views on click
        onRandonMealClick()
        OnPopularItemClick()

        //Get popular items from ViewModel
        viewModel.getCategories()
        observeCategoriesLiveData()

        //Instance the Categories RecyclerView
        prepareCategoriesRecyclerView()

        onCategoryClick()
    }

    private fun onCategoryClick() {
        categoriesAdapter.onItemClick = {
            category ->
            val intent = Intent(activity,CategoryMealsActivity::class.java)
            intent.putExtra(CATEGORY_NAME,category.strCategory)

            startActivity(intent)
        }
    }

    private fun prepareCategoriesRecyclerView() {
        categoriesAdapter = CategoriesAdapter()

        binding.recViewCategories.apply {
            layoutManager = GridLayoutManager(context,3,GridLayoutManager.VERTICAL,false)
            adapter = categoriesAdapter
        }
    }

    //Get categories from liveData
    private fun observeCategoriesLiveData() {

        viewModel.observeCategoriesLiveData().observe(viewLifecycleOwner, Observer{ categories ->

            categories.forEach{ category ->
                categoriesAdapter.setCategoryList(categories)
            }

        })
    }


    //Button function
    private fun OnPopularItemClick() {
        popularItemsAdapter.onItemClick = { meal ->
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, meal.idMeal)
            intent.putExtra(MEAL_NAME, meal.strMeal)
            intent.putExtra(MEAL_THUMB, meal.strMealThumb)

            //Instance of the MealActivityView
            startActivity(intent)
        }
    }

    private fun preparePopularItemsRecyclerView() {
        binding.recViewMealsPopular.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = popularItemsAdapter
        }
    }

    private fun ObserverPopularItemsLiveData() {
        viewModel.observePopularItemsLiveData().observe(viewLifecycleOwner,
         { mealList ->

            popularItemsAdapter.setMeals(mealsList = mealList as ArrayList<MealsByCategory>)

        })
    }

    private fun ObserverRandomMeal() {
        viewModel.observeRandomMealLiveData().observe(viewLifecycleOwner)
        { meal ->

            Glide.with(this@HomeFragment)
                .load(meal!!.strMealThumb)
                .into(binding.imgRandomMeal)

            this.randomMeal = meal
        }
    }

    //button function
    private fun onRandonMealClick() {
    binding.randomMealCard.setOnClickListener{
        val intent = Intent(activity,MealActivity::class.java)
        intent.putExtra(MEAL_ID,randomMeal.idMeal)
        intent.putExtra(MEAL_NAME,randomMeal.strMeal)
        intent.putExtra(MEAL_THUMB,randomMeal.strMealThumb)

        //Instance of the MealActivityView
        startActivity(intent)
    }

    }
}
