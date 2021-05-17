package com.example.tallerapp.view

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.transition.Transition
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.example.tallerapp.R
import com.example.tallerapp.databinding.ItemRedditBinding
import com.example.tallerapp.model.RedditTopChildrenData
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class TopRedditsAdapter
    (val redditTopList: ArrayList<RedditTopChildrenData>?, val context: Context) :
    RecyclerView.Adapter<TopRedditsAdapter.TopRedditsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopRedditsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view =
            DataBindingUtil.inflate<ItemRedditBinding>(inflater, R.layout.item_reddit, parent, false)
        view.callback = this
        return TopRedditsViewHolder(view)
    }

    override fun getItemCount() : Int {
        redditTopList?.apply {
            return this.size
        }
        return 0
    }


    override fun onBindViewHolder(holder: TopRedditsViewHolder, position: Int) {
        holder.view.redditTopModelObject = redditTopList?.get(position)?.childrenDataSub
    }


    fun updatePicList(newRateList: List<RedditTopChildrenData>?) {
        newRateList?.apply {
            redditTopList?.addAll(this)
        }
        notifyDataSetChanged()
    }

    fun onThumbnailClick(uri: String?) {

            Glide.with(context)
                .asBitmap()
                .load(uri)
                .into(object : CustomTarget<Bitmap>(){
                    override fun onLoadCleared(placeholder: Drawable?) {
                    }

                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?
                    ) {
                        saveMediaToStorage(resource, context)
                    }
                })
    }

    fun saveMediaToStorage(bitmap: Bitmap, context: Context) {
        //Generating a file name
        val filename = "${System.currentTimeMillis()}.jpg"

        //Output stream
        var fos: OutputStream? = null

        //For devices running android >= Q
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            //getting the contentResolver
            context?.contentResolver?.also { resolver ->

                //Content resolver will process the contentvalues
                val contentValues = ContentValues().apply {

                    //putting file information in content values
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }

                //Inserting the contentValues to contentResolver and getting the Uri
                val imageUri: Uri? =
                    resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

                //Opening an outputstream with the Uri that we got
                fos = imageUri?.let { resolver.openOutputStream(it) }
            }
        } else {
            //These for devices running on android < Q
            //So I don't think an explanation is needed here
            val imagesDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image = File(imagesDir, filename)
            fos = FileOutputStream(image)
        }

        fos?.use {
            //Finally writing the bitmap to the output stream that we opened
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            Toast.makeText(context, "Saved to gallery", Toast.LENGTH_SHORT).show()
        }
    }


    class TopRedditsViewHolder(var view: ItemRedditBinding) : RecyclerView.ViewHolder(view.root)
}