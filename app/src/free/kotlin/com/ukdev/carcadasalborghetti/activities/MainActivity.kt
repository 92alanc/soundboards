package com.ukdev.carcadasalborghetti.activities

import android.os.Bundle
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.ukdev.carcadasalborghetti.R

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        findViewById<AdView>(R.id.ad_view).loadAd(AdRequest.Builder().build())
    }

}