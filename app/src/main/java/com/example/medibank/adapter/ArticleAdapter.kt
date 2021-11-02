package com.example.medibank.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.medibank.model.Article
import com.example.medibank.R.layout
import com.example.medibank.R.id
import com.squareup.picasso.Picasso

/*
    Adapter that populates the article listview
 */
class ArticleAdapter(context: Context, private val dataSource: ArrayList<Article>) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val article = getItem(position) as Article
        val rowView = inflater.inflate(layout.article_row, parent, false)


        // Get title
        val title = rowView.findViewById(id.txt_title) as TextView
        title.text = article.title

        // Get author
        val author = rowView.findViewById(id.txt_author) as TextView
        author.text = "By "+article.author

        // Get description
        val description = rowView.findViewById(id.txt_desc) as TextView
        description.text = article.description

        //load image into imageview
        Picasso.get()
            .load(article.urlToImage)
            .resize(100, 100)
            .centerCrop()
            .into(rowView.findViewById(id.article_img) as ImageView)


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

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

}