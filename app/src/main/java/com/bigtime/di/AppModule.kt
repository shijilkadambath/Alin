/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bigtime.di

import android.app.Application
import com.bigtime.common.LiveDataCallAdapterFactory
import com.bigtime.data.api.WebService
import com.bigtime.utils.AppConstants
import dagger.Module
import dagger.Provides
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
/**
 * Created by Shijil Kadambath on 03/08/2018
 * Email : shijilkadambath@gmail.com
 */

@Module(includes = [ViewModelModule::class])
class  AppModule {

    private val BASE_URL = "http://localhost/"//"http://dev4.shoekonnect.com/"

   /* var scheme: String = ""
        set(url) {
            field = HttpUrl.parse(url)!!.scheme()
        }


    var host: String = ""
        set(url) {
            field = HttpUrl.parse(url)!!.host()
        }*/

    //@JvmStatic
    @Singleton
    @Provides
    fun provideOkHttpClient() =
            OkHttpClient.Builder()
                    .addInterceptor {
                        val request = it.request()

                        val newUrl: HttpUrl?
                        newUrl = when {
                            AppConstants.SCHEME != null && AppConstants.HOST != null -> request.url().newBuilder()
                                    .scheme( AppConstants.SCHEME)
                                    .host(AppConstants.HOST)
                                    .port(AppConstants.PORT)
                                    //.addQueryParameter("apikey", "something")
                                    .build()
                            else -> request.url().newBuilder()
                                    //.addQueryParameter("apikey", "something")
                                    .build()
                        }

                        it.proceed(
                                request.newBuilder()
                                        .url(newUrl)
                                        .build())

                    }
                    .build()!!
   /* //@JvmStatic
    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit.Builder {

        val okHttpBuilder = provideOkHttpClient().newBuilder()
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        okHttpBuilder.addInterceptor(httpLoggingInterceptor)


        return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                //.addCallAdapterFactory(CoroutineCallAdapterFactory())
                .client(okHttpBuilder.build())
                .baseUrl("http://localhost/")

    }*/


    @Singleton @Provides
    fun provideGithubService(okHttpClient: OkHttpClient): WebService {

        val okHttpBuilder = okHttpClient.newBuilder()
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        okHttpBuilder.addInterceptor(httpLoggingInterceptor)

        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(LiveDataCallAdapterFactory())
                .build()
                .create(WebService::class.java)
    }



    @Singleton @Provides
    fun getBaseUrl(): String {
        return BASE_URL
    }

    @Singleton
    @Provides
    fun getTermsOfServiceUrl(): String {
        return "https://www.google.com"
        //return BASE_URL
    }

    @Singleton
    @Provides
    fun getPrivacyPolicyUrl(): String {
        return "https://www.google.com"
        //return BASE_URL
    }
}
