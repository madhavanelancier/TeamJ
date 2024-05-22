package com.elancier.team_j.retrofit


import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitCli private constructor() {
    private val retrofit: Retrofit
    internal var gson = GsonBuilder()
        .setLenient()
        .create()

    val api: usr
        get() = retrofit.create(usr::class.java)

    init {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .readTimeout(15, TimeUnit.MINUTES)
            .writeTimeout(15, TimeUnit.MINUTES)
            .connectTimeout(15, TimeUnit.MINUTES)
            .addInterceptor(httpLoggingInterceptor)
            .build()
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            //.addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    companion object {
        private val BASE_URL = Appconstants.Domin
        private var mInstance: RetrofitCli? = null

        val instance: RetrofitCli
            @Synchronized get() {
                if (mInstance == null) {
                    mInstance = RetrofitCli()
                }
                return mInstance!!
            }
    }
}
