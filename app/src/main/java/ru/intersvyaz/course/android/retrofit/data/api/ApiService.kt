package ru.intersvyaz.course.android.retrofit.data.api

import retrofit2.http.GET
import retrofit2.http.Path
import ru.intersvyaz.course.android.retrofit.data.model.User

interface ApiService {

    @GET("users")
    suspend fun getUsers(): List<User>

    @GET("users/{userId}")
    suspend fun getUser(@Path("userId") userId: String): User

}