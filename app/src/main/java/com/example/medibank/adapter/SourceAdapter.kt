package com.example.medibank.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.TextView
import com.example.medibank.R
import com.example.medibank.model.Source
import com.example.medibank.util.Preferences
import java.util.ArrayList


/*
    Populates the sources listview
 */

class SourceAdapter (context: Context, private val dataSource: ArrayList<Source>) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val source = getItem(position) as Source
        val rowView = inflater.inflate(R.layout.source_row, parent, false)


        // Get title
        val name = rowView.findViewById(R.id.txt_source_name) as TextView
        name.text = source.name


        // Get description
        val description = rowView.findViewById(R.id.txt_source_desc) as TextView
        description.text = source.description

        val check = rowView.findViewById(R.id.checkBox) as CheckBox

        val prefs = Preferences()
        var savedSourceList: ArrayList<String?>? = ArrayList()
        savedSourceList = prefs.getArrayPrefs("OrderList", rowView.context)

        //set the status of the source to check if it was previously selected
        if (savedSourceList != null) {
            for (i in 0 until savedSourceList.size){
                if (savedSourceList[i].equals(source.id)){
                    source.isChecked = true
                    check.isChecked = true;
                }

            }
        }

        check.setOnCheckedChangeListener { buttonView, isChecked ->
            source.isChecked = isChecked
            //Toast.makeText(rowView.context,isChecked.toString(),Toast.LENGTH_SHORT).show()
        }

        return rowView
    }

    override fun getItem(position: Int): Any {
        return dataSource[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return dataSource.size
    }

    private val inflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

}