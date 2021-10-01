package ru.intersvyaz.course.android.retrofit.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.intersvyaz.course.android.retrofit.data.api.ApiHelper
import ru.intersvyaz.course.android.retrofit.data.database.AppDatabase
import ru.intersvyaz.course.android.retrofit.data.model.User

class MainRepository(
    private val apiHelper: ApiHelper,
    appDatabase: AppDatabase?
) {

    private val userDao = appDatabase?.userDao()

    suspend fun getUsers(): List<User>? = withContext(Dispatchers.IO) {
        val apiResult = apiHelper.getUsers()
        userDao?.insertAllUsers(apiResult)

        userDao?.getAllUsers()
    }

    suspend fun getUser(id: String): User? = apiHelper.getUser(id)

    suspend fun getUser2(id: String): User? = withContext(Dispatchers.IO) { userDao?.getUser(id) }
}