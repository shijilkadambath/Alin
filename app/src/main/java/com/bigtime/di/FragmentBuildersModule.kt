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


import com.bigtime.ui.add_product.*
import com.bigtime.ui.approved_product.ApprovedProductFragment
import com.bigtime.ui.home.HomeFragment
import com.bigtime.ui.login.*
import com.bigtime.ui.order.OrderFragment
import com.bigtime.ui.pending_product.PendingProductFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector
/**
 * Created by Shijil Kadambath on 03/08/2018
 * Email : shijilkadambath@gmail.com
 */

@Suppress("unused")
@Module
abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeLoginFragment(): LoginFragment

    @ContributesAndroidInjector
    abstract fun contributeForgotPasswordFragmentt(): ForgotPasswordFragment

    @ContributesAndroidInjector
    abstract fun contributeLegalAgreementFragment(): LegalAgreementFragment

    @ContributesAndroidInjector
    abstract fun contributeResetPasswordFragment(): ResetPasswordFragment

    @ContributesAndroidInjector
    abstract fun contributeVerifyPhoneFragment(): VerifyPhoneFragment

    @ContributesAndroidInjector
    abstract fun contributeHomeFragment(): HomeFragment

    @ContributesAndroidInjector
    abstract fun contributeChooseFragment(): ChooseFragment

    @ContributesAndroidInjector
    abstract fun contributeAddProductUploadFragment(): AddProductUploadFragment

    @ContributesAndroidInjector
    abstract fun contributeAddProductDetailFragment(): AddProductDetailFragment

    @ContributesAndroidInjector
    abstract fun contributeOrderFragment(): OrderFragment

    @ContributesAndroidInjector
    abstract fun contributeApprovedProductFragment(): ApprovedProductFragment

    @ContributesAndroidInjector
    abstract fun contributePendingProductFragment(): PendingProductFragment

    @ContributesAndroidInjector
    abstract fun contributeChooseBrandFragment(): ChooseBrandDialogFragment


    @ContributesAndroidInjector
    abstract fun contributeConfirmFragment(): ConfirmFragment
}
