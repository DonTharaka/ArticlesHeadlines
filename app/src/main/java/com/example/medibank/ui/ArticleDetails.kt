package com.example.medibank.ui


import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.medibank.R
import com.example.medibank.model.Article
import com.example.medibank.util.Preferences

/*
    Loads article URL in a webview
    User can add to or delete from favourites
 */
class ArticleDetails : AppCompatActivity() {

    private lateinit var webView: WebView
    private val prefs = Preferences()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.article_details)
        webView = findViewById(R.id.webview)
        webView.settings.setJavaScriptEnabled(true)

        val articleUrl = intent.getStringExtra("url")

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (url != null) {
                    view?.loadUrl(url)
                }
                return true
            }
        }
        //load web url
        if (articleUrl != null) {
            webView.loadUrl(articleUrl)
        }

        val btnSave = findViewById<Button>(R.id.btn_save)
        val btnDelete = findViewById<Button>(R.id.btn_delete)
        //if the user is coming from saved screen
        if(intent.getStringExtra("author") == null){
            btnSave.visibility = View.GONE
            btnDelete.visibility = View.VISIBLE
        }

        //save saved article
        btnSave.setOnClickListener{
            Toast.makeText(this, "Article saved", Toast.LENGTH_SHORT).show()
            prefs.saveArticleInArray(Article(
                intent.getStringExtra("author"),
                intent.getStringExtra("title"),
                intent.getStringExtra("description"),
                intent.getStringExtra("url"),
                intent.getStringExtra("urlToImage"),
                intent.getStringExtra("publishedAt"),
                intent.getStringExtra("content")
            ), this)
        }

        //delete saved article
        btnDelete.setOnClickListener {
            val builder = AlertDialog.Builder(this@ArticleDetails)
            builder.setMessage("Are you sure you want to Delete?")
                .setCancelable(false)
                .setPositiveButton("Yes") { dialog, id ->
                    // Delete selected note from shared prefs
                    prefs.removeArticlesFromSharedPreferences(intent.getIntExtra("arrayPosition", 0), this)
                    finish()
                }
                .setNegativeButton("No") { dialog, id ->
                    // Dismiss the dialog
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()

        }
    }
}