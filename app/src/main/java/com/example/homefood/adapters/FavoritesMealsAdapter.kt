package com.example.homefood.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.homefood.databinding.MealItemBinding
import com.example.homefood.dtclasses.Meal

class FavoritesMealsAdapter : RecyclerView.Adapter<FavoritesMealsAdapter.FavoritesMealsAdapterViewHolder>() {

    inner class FavoritesMealsAdapterViewHolder(val binding:MealItemBinding) : RecyclerView.ViewHolder(binding.root)

    //improve performance on recycler views
    private val diffUtil = object : DiffUtil.ItemCallback<Meal>(){
        override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            //si es true solo regresa si estos dos objetos son iguales
            return oldItem.idMeal == newItem.idMeal
        }

        override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtil)

    //Recycler view Functions
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoritesMealsAdapterViewHolder {
        return FavoritesMealsAdapterViewHolder(
            MealItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(
        holder: FavoritesMealsAdapterViewHolder,
        position: Int
    ) {
        val meal = differ.currentList[position]

        Glide.with(holder.itemView)
            .load(meal.strMealThumb)
            .into(holder.binding.ivImageMeal)

        holder.binding.tvName.text = meal.strMeal
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}