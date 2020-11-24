package com.example.rockpaperscissors.repository

import android.content.Context
import android.util.Log
import com.example.rockpaperscissors.database.SPreferences
import com.example.rockpaperscissors.notifications.Push
import com.facebook.applinks.AppLinkData

class LinksUtills(val context: Context) {
    var url : String? = null
    var mainActivity : ResponseApi? = null
    var exec = false
    val sPrefUrl = SPreferences(context).apply { getSharedPref("fb") }

    init{
        url = sPrefUrl.getStroke("url")
        Log.e("Links", url.toString())
        if(url == null) tree()
    }

    fun attachWeb(api : ResponseApi){
        mainActivity = api
    }

    private fun tree() {
        AppLinkData.fetchDeferredAppLinkData(context
        ) { appLinkData: AppLinkData? ->
            if (appLinkData != null && appLinkData.targetUri != null) {
                if (appLinkData.argumentBundle["target_url"] != null) {
                    Log.e("DEEP", "SRABOTAL")
                    Push().schedule(context)
                    exec = true
                    val tree = appLinkData.argumentBundle["target_url"].toString()
                    val uri = tree.split("$")
                    url = "https://" + uri[1]
                    if(url != null){
                        sPrefUrl.putStroke("url", url!!)
                        mainActivity?.callResponse(url!!)
                    }
                }
            }
        }
    }}