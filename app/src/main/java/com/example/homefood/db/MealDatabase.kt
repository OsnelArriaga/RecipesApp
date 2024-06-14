package com.example.homefood.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.homefood.dtclasses.Meal

@Database(entities = [Meal::class], version = 2, exportSchema = false)
@TypeConverters(MealTypeConvertor::class)
abstract class MealDatabase : RoomDatabase() {
    abstract fun mealDao(): MealDAO

    companion object{
        //All changes is visible on any thread
        @Volatile
        var INSTANCE:MealDatabase? = null

        //get instance from database
        @Synchronized //Only one thread can have an instance from this database
        fun getInstance(context: Context):MealDatabase{
            if (INSTANCE == null){
                INSTANCE = Room.databaseBuilder(
                    context,
                    MealDatabase::class.java,
                    "meal.db"
                ).fallbackToDestructiveMigration()//What we want to do when we upgrade the database
                    .build()
            }
            return INSTANCE as MealDatabase
        }

    }



}