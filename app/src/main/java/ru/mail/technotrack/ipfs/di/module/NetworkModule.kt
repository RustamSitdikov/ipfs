package ru.mail.technotrack.ipfs.di.module

import retrofit2.Retrofit
import okhttp3.OkHttpClient
import javax.inject.Singleton
import dagger.Provides
import dagger.Module
import android.app.Application
import android.preference.PreferenceManager
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.reactivex.schedulers.Schedulers
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.mail.technotrack.ipfs.data.network.IPFSApi
import ru.mail.technotrack.ipfs.data.network.EndPoints


@Module
class NetworkModule {

    companion object {
        const val BASE_URL: String = "http://127.0.0.1:5001/${EndPoints.API}"
    }

    @Provides
    @Singleton
    internal fun provideSharedPreferences(application: Application): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(application)
    }

    @Provides
    @Singleton
    internal fun provideGson(): Gson {
        return GsonBuilder().create()
    }

    @Provides
    @Singleton
    internal fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().build()
    }

    @Provides
    @Singleton
    internal fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    internal fun provideApi(retrofit: Retrofit): IPFSApi {
        return retrofit.create(IPFSApi::class.java)
    }
}