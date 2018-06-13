package com.xamarin.azuredevdays

import com.google.gson.Gson
import com.xamarin.azuredevdays.model.SentimentDocument
import com.xamarin.azuredevdays.model.SentimentRequest

import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody

import java.io.IOException

val HTTP_CLIENT by lazy { OkHttpClient() }

private val JSON = MediaType.parse("application/json; charset=utf-8")

class SentimentClient {
    var sentimentApiRegion = "westus"
    var sentimentApiKey = ""

    private val gson = Gson()

    @Throws(IOException::class)
    fun getKey(callback: Callback): Call {
        val request = Request.Builder()
            .url("https://vsmcsentimentdemo.azurewebsites.net/api/GetSentimentKey")
            .get()
            .build()

        val call = HTTP_CLIENT.newCall(request)
        call.enqueue(callback)
        return call
    }

    @Throws(IOException::class)
    fun getSentimentResult(text: String, callback: Callback): Call {
        val jsonString = sentimentDocumentJson(text)
        val body = RequestBody.create(JSON, jsonString)

        val request = Request.Builder()
            .url("https://$sentimentApiRegion.api.cognitive.microsoft.com/text/analytics/v2.0/sentiment")
            .addHeader("Ocp-Apim-Subscription-Key", sentimentApiKey)
            .post(body)
            .build()

        val call = HTTP_CLIENT.newCall(request)
        call.enqueue(callback)
        return call

    }

    private fun sentimentDocumentJson(text: String): String {
        val reqId = "123"
        val request = SentimentRequest()
        request.documents.add(SentimentDocument(reqId, text))
        return gson.toJson(request)
    }
}
