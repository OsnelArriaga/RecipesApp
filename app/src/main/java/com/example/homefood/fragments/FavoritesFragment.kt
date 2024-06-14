package com.example.homefood.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.homefood.R
import com.example.homefood.R.layout
import com.example.homefood.ViewModel.HomeViewModel
import com.example.homefood.activities.MainActivity
import com.example.homefood.adapters.FavoritesMealsAdapter
import com.example.homefood.databinding.FragmentFavoritesBinding


class FavoritesFragment : Fragment() {
    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var viewModel:HomeViewModel
    private lateinit var favoritesMealsAdapter:FavoritesMealsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //viewmodel instance
        viewModel = ( activity as MainActivity).viewModel

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(layout.fragment_favorites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRecyclerView()
        observeFavorites()

    }

    private fun prepareRecyclerView() {
        favoritesMealsAdapter = FavoritesMealsAdapter()

        binding.rvFavorites.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = favoritesMealsAdapter
        }
    }

    private fun observeFavorites() {
        viewModel.observeFavoritesMealsLiveData().observe(requireActivity(), Observer {meals ->
            favoritesMealsAdapter.differ.submitList(meals)

            //Test for call data
            meals.forEach {
                Log.d("mealsFav", it.idMeal)
            }
        })
    }
}