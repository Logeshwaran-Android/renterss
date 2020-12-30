package com.rent.renters.app.retrofit

import com.rent.renters.BuildConfig
import com.rent.renters.app.ui.base.Iconstants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {

    private var retrofit: Retrofit? = null
    private var retrofitGoogle: Retrofit? = null


    fun logging() : OkHttpClient{

        var interceptor = HttpLoggingInterceptor()
        interceptor.level = if(BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        var client =  OkHttpClient.Builder().addInterceptor(interceptor).build();
        return OkHttpClient.Builder().addInterceptor(interceptor).connectTimeout(180, TimeUnit.SECONDS).writeTimeout(180, TimeUnit.SECONDS).readTimeout(180, TimeUnit.SECONDS)
                .build()
    }

    fun getInstance(): ApiInterface {
        if (retrofit == null)
            retrofit = Retrofit.Builder()
                    .client(logging())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(Iconstants.mainUrl)
                    .build()
        return retrofit!!.create(ApiInterface::class.java)
    }

    fun getGoogleInstance(): ApiInterface {
        if (retrofitGoogle == null)
            retrofitGoogle = Retrofit.Builder()
                    .client(logging())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(Iconstants.google_base_url)
                    .build()
        return retrofitGoogle!!.create(ApiInterface::class.java)
    }


}
