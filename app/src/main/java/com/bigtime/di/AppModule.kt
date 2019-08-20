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
import androidx.room.Room
import com.bigtime.common.LiveDataCallAdapterFactory
import com.bigtime.data.api.WebService
import com.bigtime.data.db.AppDb
import com.bigtime.data.db.UMSDao
import com.bigtime.utils.AppConstants
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
/**
 * Created by Shijil Kadambath on 03/08/2018
 * for NewAgeSMB
 * Email : shijil@newagesmb.com
 */

@Module(includes = [ViewModelModule::class])
class AppModule {

    private val BASE_URL = "http://dev4.shoekonnect.com/"



    @Singleton @Provides
    fun provideGithubService(): WebService {

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()


        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(LiveDataCallAdapterFactory())
                .build()
                .create(WebService::class.java)
    }

    @Singleton @Provides
    fun provideDb(app: Application): AppDb {
        return Room
                .databaseBuilder(app, AppDb::class.java, AppConstants.DATABASE)
                .fallbackToDestructiveMigration()
                .build()
    }


    @Singleton @Provides
    fun provideUMSDao(db: AppDb): UMSDao {
        return db.umsDao()
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
