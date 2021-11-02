package com.example.medibank.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.medibank.R
import com.example.medibank.adapter.SourceAdapter
import com.example.medibank.model.Source
import com.example.medibank.util.Consts
import com.example.medibank.util.Consts.Companion.GET_ALL_SOURCES
import com.example.medibank.util.Preferences
import kotlinx.android.synthetic.main.headlines_fragment.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.ArrayList

/*
    Displays all sources by calling a webservice.
    The user can select multiple sources
*/
class SourcesFragment : BaseFragment() {

    private lateinit var rootView: View
    private var qetSources: RequestQueue? = null
    val aList = arrayListOf<Source>()
    private val prefs = Preferences()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.sources_fragment, container, false)
        qetSources = Volley.newRequestQueue(context!!.applicationContext)


        if (isNetworkConnected(rootView.context)) {
            getSources()
        }else{
            Toast.makeText(rootView.context, Consts.INTERNET_NOT_AVAILABLE, Toast.LENGTH_LONG).show()
        }
        return rootView
    }

    //Webservice call to get the sources
    private fun getSources(){

        val request = object: JsonObjectRequest(Method.GET, GET_ALL_SOURCES, null, Response.Listener<JSONObject> { response ->
            val dObject = JSONObject(response.toString())
            val dStations = JSONArray(dObject.get("sources").toString())

            for (i in 0 until dStations.length()) {
                var dataInner: JSONObject = dStations.getJSONObject(i)
                aList.add(
                    Source(
                        dataInner.getString("id"),
                        dataInner.getString("name"),
                        dataInner.getString("description"),
                        dataInner.getString("url"),
                        dataInner.getString("category"),
                        dataInner.getString("language"),
                        dataInner.getString("country"),
                    )
                )
            }
            //populates the UI
            val appContext = context!!.applicationContext
            val adapter = SourceAdapter(appContext,  aList)
            list_articles.adapter = adapter
            progress_bar.visibility = View.GONE
            list_articles.visibility = View.VISIBLE


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
        qetSources!!.add(request)
    }

    override fun onPause() {
        var savedSourceList: ArrayList<String?>? = ArrayList()
        //find the user selected sources.
        for (i in 0 until aList.size) {
            if (aList[i].isChecked == true){
                savedSourceList?.add(aList[i].id);
            }
        }

        //save the selected sources to shared preferences
        if (savedSourceList != null) {
            prefs.setArrayPrefs("OrderList", savedSourceList, context!!.applicationContext)
        }


        super.onPause()
    }


}