package com.example.medibank.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.example.medibank.adapter.ArticleAdapter
import com.example.medibank.model.Article
import org.json.JSONArray
import org.json.JSONObject
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.example.medibank.R
import com.example.medibank.util.Consts.Companion.INITIAL_HEADLINE_URL
import com.example.medibank.util.Consts.Companion.INTERNET_NOT_AVAILABLE
import com.example.medibank.util.Preferences
import kotlinx.android.synthetic.main.headlines_fragment.*
import java.util.ArrayList

/*
    Displays all headlines in a list along with the image, title and description.
    Communicates to the server, loads the data after parsing
    On click of a row will take the user to article details page
*/

class HeadlinesFragment : BaseFragment() {

    private lateinit var rootView: View
    private var qStation: RequestQueue? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView=inflater.inflate(R.layout.headlines_fragment,container,false)
        qStation = Volley.newRequestQueue(context!!.applicationContext)
        return rootView
    }

    //webservice that retrieves headings from the server
    private fun getArticles(url : String){
        val aList = arrayListOf<Article>()
        val request = object: JsonObjectRequest(Method.GET, url, null, Response.Listener<JSONObject> { response ->
            val dObject = JSONObject(response.toString())
            val articleJasonArr = JSONArray(dObject.get("articles").toString())
            //loop the json array
            for (i in 0 until articleJasonArr.length()) {
                var dataInner: JSONObject = articleJasonArr.getJSONObject(i)
                aList.add(
                    Article(
                        dataInner.getString("author"),
                        dataInner.getString("title"),
                        dataInner.getString("description"),
                        dataInner.getString("url"),
                        dataInner.getString("urlToImage"),
                        dataInner.getString("publishedAt"),
                        dataInner.getString("content"),
                    )
                )
            }
            //set up UI
            val appContext = context!!.applicationContext
            val adapter = ArticleAdapter(appContext,  aList)
            list_articles.adapter = adapter
            progress_bar.visibility = View.GONE
            list_articles.visibility = View.VISIBLE
            //onclickItem action of the list
            list_articles.setOnItemClickListener { _, _, position, _ ->
                var intent: Intent? = Intent(rootView.context, ArticleDetails::class.java)

                // Pass the values to next activity (ArticleDetails)
                intent!!.putExtra("author",aList[position].author)
                intent!!.putExtra("title",aList[position].title)
                intent!!.putExtra("description",aList[position].description)
                intent!!.putExtra("url",aList[position].url)
                intent!!.putExtra("urlToImage",aList[position].urlToImage)
                intent!!.putExtra("content",aList[position].content)
                intent!!.putExtra("urlToImage",aList[position].urlToImage)

                startActivity(intent)
            }

        }, Response.ErrorListener { e ->
            e.message?.let { Log.d(">>>>>", it) }
        }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                headers["Accept"] = "application/json"
                headers["User-Agent"]="Mozilla/5.0"
                return headers
            }
        }
       qStation!!.add(request)

    }


    //if there are no saved sources, load all headings
    //if there are saved sources load only the headings related to the selected sources
    override fun onResume() {

        val prefs = Preferences()
        var savedSourceList: ArrayList<String?>? = ArrayList()
        savedSourceList = prefs.getArrayPrefs("OrderList", rootView.context)

        if (isNetworkConnected(rootView.context)) {
            if(savedSourceList!!.size != 0){
                val sources = savedSourceList?.joinToString().toString().trim();
                getArticles("https://newsapi.org/v2/top-headlines?sources=$sources&apiKey=84005b7ec0ba4bc3a15b5234cd2e7448")
            }else{
                getArticles(INITIAL_HEADLINE_URL)
            }
        }else{
            progress_bar.visibility = View.GONE
            Toast.makeText(rootView.context, INTERNET_NOT_AVAILABLE, Toast.LENGTH_LONG).show()
        }

        super.onResume()
    }

}
