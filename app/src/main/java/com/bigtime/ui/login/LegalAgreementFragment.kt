package com.bigtime.ui.login

import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.bigtime.R
import com.bigtime.databinding.FragmentLegalAgreementBinding
import com.bigtime.di.AppModule
import com.bigtime.di.AppModule_GetPrivacyPolicyUrlFactory
import com.bigtime.di.AppModule_GetTermsOfServiceUrlFactory
import com.bigtime.ui.BaseFragment

/**
 * Created by Shijil Kadambath on 03/08/2018
 * for NewAgeSMB
 * Email : shijil@newagesmb.com
 *
 */

class LegalAgreementFragment : BaseFragment<FragmentLegalAgreementBinding>() {

    companion object {
        const val TAG: String = "LegalAgreementFragment"
        const val TYPE_TERMS_OF_SERVICE: Int = 1
        const val TYPE_PRIVACY_POLICY: Int = 2
        const val TYPE_ABOUT: Int = 3
    }


    override fun getLayoutId(): Int {
        return R.layout.fragment_legal_agreement
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mBinding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        mBinding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }


        mBinding.swipeRefresh.setProgressBackgroundColorSchemeResource(R.color.colorAccent)
        mBinding.swipeRefresh.setColorSchemeColors(ContextCompat.getColor(activity!!, R.color.white))
        mBinding.swipeRefresh.setOnRefreshListener {
            mBinding.webView.reload()
            mBinding.vProgress.progressBar.visibility = View.VISIBLE
        }

        val type = LegalAgreementFragmentArgs.fromBundle(arguments!!).agreementType

        when (type) {
            TYPE_TERMS_OF_SERVICE -> {
                mBinding.webView.loadUrl(AppModule_GetTermsOfServiceUrlFactory.create(AppModule()).get())
                mBinding.webView.webChromeClient = object : WebChromeClient() {
                    override fun onProgressChanged(view: WebView?, newProgress: Int) {
                        super.onProgressChanged(view, newProgress)
                        mBinding.swipeRefresh.isRefreshing = false
                        mBinding.vProgress.progressBar.isIndeterminate = false
                        mBinding.vProgress.progressBar.progress = newProgress
                        if (newProgress == 100) mBinding.vProgress.progressBar.visibility = View.GONE
                    }
                }
            }
            TYPE_PRIVACY_POLICY -> {
                mBinding.webView.loadUrl(AppModule_GetPrivacyPolicyUrlFactory.create(AppModule()).get())
                mBinding.webView.webChromeClient = object : WebChromeClient() {
                    override fun onProgressChanged(view: WebView?, newProgress: Int) {
                        super.onProgressChanged(view, newProgress)
                        mBinding.swipeRefresh.isRefreshing = false
                        mBinding.vProgress.progressBar.isIndeterminate = false
                        mBinding.vProgress.progressBar.progress = newProgress
                        if (newProgress == 100) mBinding.vProgress.progressBar.visibility = View.GONE
                    }
                }
            }

            TYPE_ABOUT -> {
                mBinding.webView.loadUrl(AppModule_GetPrivacyPolicyUrlFactory.create(AppModule()).get())
                mBinding.webView.webChromeClient = object : WebChromeClient() {
                    override fun onProgressChanged(view: WebView?, newProgress: Int) {
                        super.onProgressChanged(view, newProgress)
                        mBinding.swipeRefresh.isRefreshing = false
                        mBinding.vProgress.progressBar.isIndeterminate = false
                        mBinding.vProgress.progressBar.progress = newProgress
                        if (newProgress == 100) mBinding.vProgress.progressBar.visibility = View.GONE
                    }
                }
            }
        }

    }
}
