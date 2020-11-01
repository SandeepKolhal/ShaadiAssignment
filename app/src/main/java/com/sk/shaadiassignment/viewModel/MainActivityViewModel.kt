package com.sk.shaadiassignment.viewModel

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sk.shaadiassignment.data.database.AppDatabase
import com.sk.shaadiassignment.model.Result
import com.sk.shaadiassignment.network.RetrofitFactory
import com.sk.shaadiassignment.network.SafeApiRequest
import com.sk.shaadiassignment.repositories.MatchUserRepositories
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {

    val isApiRunning = MutableLiveData<Boolean>()

    var matchListLiveData = MutableLiveData<List<Result>>()

    private val matchList = ArrayList<Result>()

    val updatedPosition = MutableLiveData<Int>()

    private val matchUserRepositories: MatchUserRepositories

    var isMoreOnDatabase = true
    var isMoreOnAPI = true
    var isInternetConnected = true

    init {
        val db = AppDatabase.getDatabase(application)

        matchUserRepositories = MatchUserRepositories(db.matchUserDao())

        matchListLiveData.value = matchList
    }

    fun getMatchersAPICall(context: Context) {
        isApiRunning.value = true
        val call = RetrofitFactory.makeRetrofitService(context)
        SafeApiRequest().apiRequest(
            { call.getShaadiMatchers(10) },
            { response ->
                if (response != null) {
                    response.results?.let {
                        insertUserIntoDatabase(it)
                    }
                }
            },
            { error ->
                isApiRunning.value = false
                Toast.makeText(context, error, Toast.LENGTH_LONG).show()
            },
            { error ->
                isApiRunning.value = false
                Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                isInternetConnected = false
            })
    }

    private fun insertUserIntoDatabase(list: List<Result>) {
        viewModelScope.launch(Dispatchers.IO) {
            for (i in list.indices) {
                list[i].uuid = list[i].login?.uuid ?: ""
                matchList.add(list[i])
            }

            isMoreOnAPI = list.size >= 10

            matchUserRepositories.insertData(list)
            getInsertedUserFromDatabase()
        }
    }

    private fun getInsertedUserFromDatabase() {
        viewModelScope.launch(Dispatchers.IO) {
            val list = matchUserRepositories.getUsers(10, matchList.size)
            matchList.addAll(list)
            withContext(Dispatchers.Main) {
                matchListLiveData.value = matchList
                isApiRunning.value = false
            }
        }
    }

    fun getAllUserFromDatabase(context: Context) {
        isApiRunning.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val list = matchUserRepositories.getUsers(10, matchList.size)
            matchList.addAll(list)
            withContext(Dispatchers.Main) {
                matchListLiveData.value = matchList
                isApiRunning.value = false
                isMoreOnDatabase = list.size >= 10
                if (!isMoreOnDatabase) {
                    getMatchersAPICall(context)
                }
            }
        }
    }

    fun updateUser(user: Result, position: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            matchUserRepositories.updateUser(user)
            withContext(Dispatchers.Main) {
                updatedPosition.value = position
            }
        }
    }

}

