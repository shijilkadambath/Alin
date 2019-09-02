/*
 * Copyright (C) 2018 The Android Open Source Project
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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bigtime.common.ViewModelFactory
import com.bigtime.ui.add_product.AddProductViewModel
import com.bigtime.ui.add_product.ChooseBrandViewModel
import com.bigtime.ui.approved_product.ApprovedProductViewModel
import com.bigtime.ui.home.HomeViewModel
import com.bigtime.ui.login.LoginViewModel
import com.bigtime.ui.order.OrderViewModel
import com.bigtime.ui.pending_product.PendingProductViewModel


import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
/**
 * Created by Shijil Kadambath on 03/08/2018
 * Email : shijilkadambath@gmail.com
 */


@Suppress("unused")
@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
   @IntoMap
   @ViewModelKey(LoginViewModel::class)
   abstract fun bindLoginViewModel(viewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindHomeViewModel(viewModel: HomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddProductViewModel::class)
    abstract fun bindAddProductViewModel(viewModel: AddProductViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(OrderViewModel::class)
    abstract fun bindOrderViewModel(viewModel: OrderViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PendingProductViewModel::class)
    abstract fun bindPendingProductViewModel(viewModel: PendingProductViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ApprovedProductViewModel::class)
    abstract fun bindApprovedProductViewModel(viewModel: ApprovedProductViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChooseBrandViewModel::class)
    abstract fun bindChooseBrandViewModel(viewModel: ChooseBrandViewModel): ViewModel
}
