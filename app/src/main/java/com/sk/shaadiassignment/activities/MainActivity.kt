package com.sk.shaadiassignment.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sk.shaadiassignment.R
import com.sk.shaadiassignment.adapter.MatcherListAdapter
import com.sk.shaadiassignment.model.Result
import com.sk.shaadiassignment.viewModel.MainActivityViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MatcherListAdapter.OnClickListener {

    private val mainActivityViewModel: MainActivityViewModel by lazy {
        ViewModelProvider(this).get(MainActivityViewModel::class.java)
    }

    private val matcherListAdapter: MatcherListAdapter by lazy {
        MatcherListAdapter(this, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        title = "HOME"

        setUpViewModelObservers()
        setUpAdapter()

        mainActivityViewModel.getAllUserFromDatabase(this)

        val layoutManager = LinearLayoutManager(this)
        matchesList.layoutManager = layoutManager
        matchesList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisible = layoutManager.findFirstVisibleItemPosition()

                if (!mainActivityViewModel.isApiRunning.value!!
                    && (visibleItemCount + firstVisible) >= totalItemCount
                ) {
                    if (mainActivityViewModel.isMoreOnDatabase) {
                        mainActivityViewModel.getAllUserFromDatabase(this@MainActivity)
                    } else if (mainActivityViewModel.isMoreOnAPI && mainActivityViewModel.isInternetConnected) {
                        mainActivityViewModel.getMatchersAPICall(this@MainActivity)
                    }
                } else if ((visibleItemCount + firstVisible) < totalItemCount && !mainActivityViewModel.isInternetConnected) {
                    mainActivityViewModel.isInternetConnected = true
                }
            }
        })
    }

    private fun setUpAdapter() {
        matchesList.adapter = matcherListAdapter
    }

    private fun setUpViewModelObservers() {

        mainActivityViewModel.isApiRunning.observe(this, { isRunning ->
            if (isRunning) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
        })

        mainActivityViewModel.matchListLiveData.observe(this, { list ->
            matcherListAdapter.setData(list)
        })

        mainActivityViewModel.updatedPosition.observe(this, { position ->
            if (matcherListAdapter.itemCount > position) {
                matcherListAdapter.notifyItemChanged(position)
            }
        })
    }

    override fun onClick(user: Result, position: Int) {
        mainActivityViewModel.updateUser(user, position)
    }
}