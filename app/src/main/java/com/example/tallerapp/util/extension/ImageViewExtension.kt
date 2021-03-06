package com.example.tallerapp.util.extension

import android.content.ContentValues
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.tallerapp.R

fun getProgressDrawable(context: Context): CircularProgressDrawable {
    return CircularProgressDrawable(context).apply {
        strokeWidth = 10f
        centerRadius = 50f
        start()
    }
}

fun ImageView.loadImage(
    uri: String?,
    progressDrawable: CircularProgressDrawable,
    centerCropEnabled: Boolean
) {
    val options = RequestOptions()
        .placeholder(progressDrawable)
        .error(R.mipmap.ic_launcher)
        .priority(Priority.HIGH)
        .skipMemoryCache(true)

    if (centerCropEnabled)
        Glide.with(context)
            .applyDefaultRequestOptions(options)
            .load(uri)
            .placeholder(progressDrawable)
            .centerCrop()
            .dontAnimate()
            .override(Target.SIZE_ORIGINAL)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(this)
    else
        Glide.with(context)
            .setDefaultRequestOptions(options)
            .load(uri)
            .placeholder(progressDrawable)
            .fitCenter()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(p0: GlideException?, p1: Any?, p2: Target<Drawable>?, p3: Boolean): Boolean {
                    Log.e("IMAGE_EXCEPTION", "Exception " + p0.toString());
                    return false
                }
                override fun onResourceReady(resource: Drawable?, p1: Any?, p2: Target<Drawable>?, p3: DataSource?, p4: Boolean): Boolean {
                    Log.d(ContentValues.TAG, "OnResourceReady")
                    //do something when picture already loaded
                    return false
                }
            })
            .into(this)
}

@BindingAdapter(value = ["android:imageUrl", "android:centerCropEnabled"], requireAll = true)
fun loadImage(view: ImageView, imageUrl: String?, centerCropEnabled: Boolean) {
    view.loadImage(imageUrl, getProgressDrawable(view.context), centerCropEnabled)
}