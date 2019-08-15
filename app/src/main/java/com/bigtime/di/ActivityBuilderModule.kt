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

import com.bigtime.ui.add_product.AddProductActivity
import com.bigtime.ui.approved_product.ApprovedProductActivity
import com.bigtime.ui.home.HomeActivity
import com.bigtime.ui.login.LoginActivity
import com.bigtime.ui.order.OrderActivity
import com.bigtime.ui.pending_product.PendingProductActivity

import dagger.Module
import dagger.android.ContributesAndroidInjector
/**
 * Created by Shijil Kadambath on 03/08/2018
 * for NewAgeSMB
 * Email : shijil@newagesmb.com
 */
@Suppress("unused")
@Module
abstract class ActivityBuilderModule {

    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeLoginActivity(): LoginActivity


    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeHomeActivity(): HomeActivity


    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeAddProductActivity(): AddProductActivity


    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeOrderActivity(): OrderActivity


    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributePendingProductActivity(): PendingProductActivity


    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeApprovedProductActivity(): ApprovedProductActivity
}
