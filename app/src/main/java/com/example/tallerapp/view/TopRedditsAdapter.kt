package com.example.tallerapp.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.tallerapp.R
import com.example.tallerapp.databinding.ItemRedditBinding
import com.example.tallerapp.model.RedditTopChildren
import com.example.tallerapp.model.RedditTopChildrenData

class TopRedditsAdapter
    (val redditTopList: ArrayList<RedditTopChildrenData>?) :
    RecyclerView.Adapter<TopRedditsAdapter.TopRedditsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopRedditsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view =
            DataBindingUtil.inflate<ItemRedditBinding>(inflater, R.layout.item_reddit, parent, false)
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


    class TopRedditsViewHolder(var view: ItemRedditBinding) : RecyclerView.ViewHolder(view.root)
}