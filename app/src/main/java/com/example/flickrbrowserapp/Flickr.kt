package com.example.flickrbrowserapp

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.Resource
import kotlinx.coroutines.*
import org.json.JSONObject
import java.net.URL
import android.content.Context

class Flickr : AppCompatActivity() {
    var tag = "Flickr"
    var tags = "city"
    var base_Url = "https://www.flickr.com/services/rest/"
    val api_key = "6cbd737dc96c0be4a4892c3a36e23730"
    private lateinit var search : Drawable
    private lateinit var recView: RecyclerView
    private lateinit var rvAdapter: RecyclerViewAdapter
    lateinit var searchEditT: EditText
    var  title=  ArrayList<String>()
    var imgUrls =  ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flickr)
        recView = findViewById(R.id.recView)
        rvAdapter = RecyclerViewAdapter(title,imgUrls,this)
        recView.adapter = rvAdapter
        recView.layoutManager = LinearLayoutManager(this)
        searchEditT = findViewById(R.id.searchEditT)
        searchEditT.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun afterTextChanged(editable: Editable) {
                //after the change calling the method and passing the search input
                tags = editable.toString()
                Log.d(tag,"suss$tags")
                requestAPI()

               // filter(editable.toString())

            }
        })


        requestAPI()

    }

    private fun requestAPI() {
    title.clear()
        imgUrls.clear()
        CoroutineScope(Dispatchers.IO).launch {
            // we fetch the data
            val data = async { fetchData() }.await()

            if (data.isNotEmpty()) {
                getData(data)
                Log.d("able", "requestAPI: ")
            } else {
                Log.d(tag, "Unable to get data")
            }
        }
    }

    private fun fetchData(): String {
        var response = ""
        try {
            response =
                URL("$base_Url?method=flickr.photos.search&api_key=$api_key&tags=$tags&format=json&nojsoncallback=1&extras=url_s&safe_search=3").readText()
        } catch (e: Exception) {
            Log.d(tag, "ISSUE: $e")
        }
        // our response is saved as a string and returned
        return response
    }

    private suspend fun getData(result: String) {
        withContext(Dispatchers.Main) {
            val jsonObject = JSONObject(result)

            var photos = jsonObject.getJSONObject("photos")
            val photo = photos.getJSONArray("photo")

            for (i in 0 until photo.length()) {
                val title1 = photo.getJSONObject(i).getString("title")
                val url = photo.getJSONObject(i).getString("url_s")
                title.add(title1)
                imgUrls.add(url)


            }
            rvAdapter.notifyDataSetChanged()

        }

    }
    private fun filter(text: String) {

        //new array list that will hold the filtered data
        val filteredNames = ArrayList<String>()
        //looping through existing elements and adding the element to filtered list
        title.filterTo(filteredNames) {
            //if the existing elements contains the search input
            it.toLowerCase().contains(text.toLowerCase())
        }
        //calling a method of the adapter class and passing the filtered list
        rvAdapter!!.filterList(filteredNames)
    }

fun openImageFull(url:String){
    val intent = Intent(applicationContext, full_Image_Activity::class.java)
    intent.putExtra("URL", url)
    startActivity(intent)
}

}