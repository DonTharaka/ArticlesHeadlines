package com.example.medibank.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import com.example.medibank.R
import com.example.medibank.adapter.ArticleAdapter
import com.example.medibank.model.Article
import com.example.medibank.util.Preferences

/*
    This fragment will load the saved headlines
 */
class SavedFragment : BaseFragment() {

    private val prefs = Preferences()
    private lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         rootView = inflater.inflate(R.layout.saved_fragment, container, false)
        return rootView
    }

    //load all saved headlines to the list
    override fun onResume() {
        if(prefs.getArticlesFromSharedPreferences(rootView.context) != null){
            val adapter = ArticleAdapter(rootView.context, prefs.getArticlesFromSharedPreferences(rootView.context) as ArrayList<Article>)
            val listSavedArticles = rootView.findViewById<ListView>(R.id.list_articles_saved)
            listSavedArticles.adapter = adapter

            listSavedArticles.setOnItemClickListener { _, _, position, _ ->
                var intent: Intent? = Intent(rootView.context, ArticleDetails::class.java)

                // Pass the values to next activity (ArticleDetails)
                intent!!.putExtra("isSaved", true)
                intent!!.putExtra("arrayPosition", position)
                intent!!.putExtra("url", (prefs.getArticlesFromSharedPreferences(rootView.context) as ArrayList<Article>)[position].url)

                startActivity(intent)
            }

        }else{
            Toast.makeText(rootView.context, "No saved articles found", Toast.LENGTH_SHORT).show()
        }
        super.onResume()
    }

}