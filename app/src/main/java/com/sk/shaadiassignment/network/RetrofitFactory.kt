package com.sk.shaadiassignment.network

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


/**
 * This singleton is use for creating retrofit builder
 */
object RetrofitFactory {

    /**
     * Creating retrofit builder and retuning AppApis to perform network call
     *
     * @param context
     * @return
     */
    fun makeRetrofitService(context: Context): AppAPIs {

        val mContext = context.applicationContext

        val builder = OkHttpClient.Builder()
            .addInterceptor(makeLoggingInterceptor())
            .addInterceptor(NetworkConnectionInterceptor(mContext))
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(90, TimeUnit.SECONDS)

        return Retrofit.Builder()
            .baseUrl("https://randomuser.me/")
            .client(builder.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(AppAPIs::class.java)
    }

    /**
     * Retrofit Network call Logging in Logcat
     *
     * @return
     */
    private fun makeLoggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level =
            HttpLoggingInterceptor.Level.BODY
        return logging
    }
}