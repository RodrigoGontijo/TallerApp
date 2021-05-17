package com.example.tallerapp.view

import android.Manifest
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.tallerapp.R
import com.example.tallerapp.RedditTopConstants.Companion.LIMIT
import com.example.tallerapp.RedditTopConstants.Companion.NUM_COLLUMS
import com.example.tallerapp.viewmodel.RedditTopViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {

    private val picsListAdapter = TopRedditsAdapter(arrayListOf(), this)
    private val viewModel: RedditTopViewModel by viewModel()

    private var isLoading = true
    private var after = ""
    private var currentPage = 1
    private var firstVisibleItem = 0
    private var visibleItemCount = 0
    private val visibleThreshold = 5
    private var totalItemCount = 0
    private var previousTotal = 0 // The total number of items in the dataset after the last load


    private var staggeredGridLayoutManager =
        StaggeredGridLayoutManager(NUM_COLLUMS, LinearLayout.VERTICAL)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        redditTopList.apply {
            layoutManager = staggeredGridLayoutManager
            adapter = picsListAdapter
        }

        observeVielModel()

        viewModel.fetchRedditTopList(LIMIT, after)
        swipeRefresh()
        endlessScroll()
        isStoragePermissionGranted()
    }

    override fun onDestroy() {
        super.onDestroy()
        removeObservers()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.v(
                TAG,
                "Permission: " + permissions[0] + "was " + grantResults[0]
            )
            //resume tasks needing this permission
        }
    }

    private fun swipeRefresh() {
        refreshLayout.setOnRefreshListener {
            currentPage = 1
            after = ""
            redditTopList.visibility = View.GONE
            listError.visibility = View.GONE
            loadingView.visibility = View.VISIBLE
            viewModel.fetchRedditTopList(LIMIT, after)
            refreshLayout.isRefreshing = false
        }
    }

    private fun endlessScroll() {
        redditTopList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                visibleItemCount = staggeredGridLayoutManager.childCount
                totalItemCount = staggeredGridLayoutManager.itemCount
                var firstVisibleItems: IntArray? = null
                firstVisibleItems =
                    staggeredGridLayoutManager.findFirstVisibleItemPositions(firstVisibleItems)
                firstVisibleItem = firstVisibleItems[1]

                if (isLoading) {

                    if (totalItemCount > previousTotal) {
                        isLoading = false;
                        previousTotal = totalItemCount;
                    }
                }
                if (!isLoading && (totalItemCount - visibleItemCount)
                    <= (firstVisibleItem + visibleThreshold)
                ) {

                    currentPage += 1
                    viewModel.fetchRedditTopList(LIMIT, after)
                    isLoading = true;
                }
            }
        })
    }

    fun observeVielModel() {

        viewModel.redditTopList.observe(this, Observer {
            it?.let {

                var list = it.data?.childrenData
                after = list?.get(list.size-1)?.childrenDataSub?.name.toString()
                redditTopList.visibility = View.VISIBLE
                picsListAdapter.updatePicList(list)
            }
        })

        viewModel.redditTopError.observe(this, Observer {
            it?.let {
                listError.visibility = if (it) View.VISIBLE else View.GONE
            }
        })

        viewModel.loading.observe(this, Observer {
            it?.let {
                if (it) {
                    loadingView.visibility = View.VISIBLE
                    listError.visibility = View.GONE
                } else {
                    loadingView.visibility = View.GONE
                }
            }
        })
    }

    fun removeObservers() {
        viewModel.loading.removeObservers(this)
        viewModel.redditTopError.removeObservers(this)
        viewModel.redditTopList.removeObservers(this)
    }

    fun isStoragePermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted")
                true
            } else {
                Log.v(TAG, "Permission is revoked")
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE),
                    1
                )
                false
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted")
            true
        }
    }
}