package com.example.flickrbrowserapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class full_Image_Activity : AppCompatActivity() {
    lateinit var fullImage: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_image)

        fullImage = findViewById(R.id.fullImage)
      var url = intent.extras?.getString("URL")!!

        Log.d("tag","url $url ")

        Glide.with(applicationContext)
            .load(url)
            .apply(RequestOptions().override(400,400))
            .into(fullImage)

    }
}