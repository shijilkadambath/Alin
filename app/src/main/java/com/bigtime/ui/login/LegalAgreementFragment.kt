package com.bigtime.ui.login

import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.bigtime.databinding.FragmentLegalAgreementBinding
import com.bigtime.ui.BaseFragment

/**
 * Created by Shijil Kadambath on 03/08/2018
 * for NewAgeSMB
 * Email : shijil@newagesmb.com
 *
 *
 * Updated by Ȿ₳Ɲ @ NEWAGESMB on Thursday, April 11, 2019
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

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener { findNavController().navigateUp() }


        swipe_refresh.setProgressBackgroundColorSchemeResource(R.color.colorAppGreen)
        swipe_refresh.setColorSchemeColors(ContextCompat.getColor(activity!!, R.color.colorWhite))
        swipe_refresh.setOnRefreshListener {
            web_view.reload()
            progress_bar.visibility = View.VISIBLE
        }

        val type = LegalAgreementFragmentArgs.fromBundle(arguments!!).agreementType

        when (type) {
            TYPE_TERMS_OF_SERVICE -> {
                web_view.loadUrl(AppModule_GetTermsOfServiceUrlFactory.create(AppModule()).get())
                web_view.webChromeClient = object : WebChromeClient() {
                    override fun onProgressChanged(view: WebView?, newProgress: Int) {
                        super.onProgressChanged(view, newProgress)
                        swipe_refresh.isRefreshing = false
                        progress_bar.isIndeterminate = false
                        progress_bar.progress = newProgress
                        if (newProgress == 100) progress_bar.visibility = View.GONE
                    }
                }
            }
            TYPE_PRIVACY_POLICY -> {
                web_view.loadUrl(AppModule_GetPrivacyPolicyUrlFactory.create(AppModule()).get())
                web_view.webChromeClient = object : WebChromeClient() {
                    override fun onProgressChanged(view: WebView?, newProgress: Int) {
                        super.onProgressChanged(view, newProgress)
                        swipe_refresh.isRefreshing = false
                        progress_bar.isIndeterminate = false
                        progress_bar.progress = newProgress
                        if (newProgress == 100) progress_bar.visibility = View.GONE
                    }
                }
            }

            TYPE_ABOUT -> {
                web_view.loadUrl(AppModule_GetPrivacyPolicyUrlFactory.create(AppModule()).get())
                web_view.webChromeClient = object : WebChromeClient() {
                    override fun onProgressChanged(view: WebView?, newProgress: Int) {
                        super.onProgressChanged(view, newProgress)
                        swipe_refresh.isRefreshing = false
                        progress_bar.isIndeterminate = false
                        progress_bar.progress = newProgress
                        if (newProgress == 100) progress_bar.visibility = View.GONE
                    }
                }
            }
        }

    }
}
