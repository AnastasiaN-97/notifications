package ru.intersvyaz.course.android.retrofit.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.intersvyaz.course.android.retrofit.data.api.ApiHelper
import ru.intersvyaz.course.android.retrofit.data.api.RetrofitBuilder
import ru.intersvyaz.course.android.retrofit.data.database.AppDatabase
import ru.intersvyaz.course.android.retrofit.data.model.User
import ru.intersvyaz.course.android.retrofit.data.repository.MainRepository

class MainViewModel : ViewModel() {

    private val apiHelper = ApiHelper(RetrofitBuilder.apiService)
    private val appDatabase = AppDatabase.getDatabase()
    private val mainRepository: MainRepository = MainRepository(apiHelper, appDatabase)

    private val _users = MutableLiveData<List<User>?>(null)
    val users: LiveData<List<User>?> = _users

    init {
        viewModelScope.launch {
            val result = mainRepository.getAllUsers()
            _users.postValue(result)
        }
    }
}