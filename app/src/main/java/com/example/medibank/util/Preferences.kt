package com.example.medibank.util

import android.content.Context
import java.util.ArrayList
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.example.medibank.model.Article
import com.example.medibank.util.Consts.Companion.SAVED_ARTICLES
import com.example.medibank.util.Consts.Companion.SHARED_PREFERENCE
import com.example.medibank.util.Consts.Companion.SHARED_PREFERENCE_ARRAY
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.lang.reflect.Type

/*
    Used to save and retrieve arrays from shared preference
 */
class Preferences {

    //Saves the selected sources
    fun setArrayPrefs(arrayName: String, array: ArrayList<String?>, mContext: Context) {
        val prefs: SharedPreferences = mContext.getSharedPreferences(SHARED_PREFERENCE_ARRAY, 0)
        val editor = prefs.edit()
        editor.putInt(arrayName + "_size", array.size)
        for (i in array.indices) editor.putString(arrayName + "_" + i, array[i])
        editor.apply()
    }
    //Gets the selected sources
     fun getArrayPrefs(arrayName: String, mContext: Context): ArrayList<String?>? {
        val prefs: SharedPreferences = mContext.getSharedPreferences(SHARED_PREFERENCE_ARRAY, 0)
        val size = prefs.getInt(arrayName + "_size", 0)
        val array = ArrayList<String?>(size)
        for (i in 0 until size) array.add(prefs.getString(arrayName + "_" + i, null))
        return array
    }
    //Adds new saved article  to array
     fun saveArticleInArray(article: Article, mContext: Context) {
        val gson = Gson()
        val sharedPref = mContext.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE)
        val jsonSaved = sharedPref.getString(SAVED_ARTICLES, "")
        val jsonNewpArticleToAdd: String = gson.toJson(article)
        var jsonArrayProduct = JSONArray()
        try {
            if (jsonSaved!!.isNotEmpty()) {
                jsonArrayProduct = JSONArray(jsonSaved)
            }
            jsonArrayProduct.put(JSONObject(jsonNewpArticleToAdd))
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        //SAVE NEW ARRAY
        val editor = sharedPref.edit()
        editor.putString(SAVED_ARTICLES, jsonArrayProduct.toString())
        editor.commit()
    }
    //gets all saved items
     fun getArticlesFromSharedPreferences(mContext: Context): List<Article?>? {
        val gson = Gson()
        var productFromShared: List<Article?>? = ArrayList<Article?>()
        val sharedPref = mContext.getSharedPreferences(SHARED_PREFERENCE,
            AppCompatActivity.MODE_PRIVATE
        )
        val jsonPreferences = sharedPref.getString(SAVED_ARTICLES, "")
        val type: Type? = object : TypeToken<List<Article?>?>() {}.type
        productFromShared = gson.fromJson<List<Article?>>(jsonPreferences, type)
        return productFromShared
    }
    //deletes the selected articles from saved
    fun removeArticlesFromSharedPreferences(position: Int, mContext: Context) {

        val sharedPref = mContext.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE)
        val jsonSaved = sharedPref.getString(SAVED_ARTICLES, "")
        var jsonArrayProduct = JSONArray()
        try {
            if (jsonSaved!!.isNotEmpty()) {
                jsonArrayProduct = JSONArray(jsonSaved)
            }
            jsonArrayProduct.remove(position)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        //SAVE NEW ARRAY
        val editor = sharedPref.edit()
        editor.putString(SAVED_ARTICLES, jsonArrayProduct.toString())
        editor.commit()

    }

}