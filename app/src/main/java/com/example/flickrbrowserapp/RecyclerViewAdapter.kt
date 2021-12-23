package com.example.flickrbrowserapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions


import kotlinx.android.synthetic.main.item_row.view.*
import java.util.ArrayList

class RecyclerViewAdapter(
    var title: List<String>,
    var imgUrl: ArrayList<String>,
    var flicker: Flickr
) : RecyclerView.Adapter<RecyclerViewAdapter.ItemViewHolder>() {


    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_row, parent, false)
        )
    }


    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        var title = title[position]
        var url = imgUrl[position]


        holder.itemView.apply {
            textView.text = title

            Glide.with(context)
                .load(url)
                .apply(RequestOptions().override(100,100))
                .into(imageView )


         imageView.setOnClickListener {
            flicker.openImageFull(url)

           }


        }


    }

    override fun getItemCount() = title.size

    fun filterList(filteredTitle: ArrayList<String>) {
        this.title = filteredTitle
        notifyDataSetChanged()
    }

}

