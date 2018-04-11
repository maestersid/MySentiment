package com.xamarin.azuredevdays

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.View.OnClickListener

import com.google.gson.Gson
import com.xamarin.azuredevdays.model.KeyResponse
import com.xamarin.azuredevdays.model.SentimentResponse

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response

import java.io.IOException
import java.util.HashMap

private val PROPERTIES = HashMap<String, String>()

class MainActivity : AppCompatActivity(), OnClickListener {

    private val sentimentClient = SentimentClient()
    private val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        try {
            sentimentClient.getKey(
                object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        println("Error getting key!")
                    }

                    @Throws(IOException::class)
                    override fun onResponse(call: Call, response: Response) {
                        if (response.isSuccessful) response.body()?.let { responseBody ->
                            gson.fromJson(responseBody.string(), KeyResponse::class.java)?.let {
                                sentimentClient.sentimentApiRegion = it.region ?: ""
                                sentimentClient.sentimentApiKey = it.key ?: ""
                            }
                        }
                    }
                }
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }

        val defaultBkgColor = Color.parseColor(DEFAULT_BACKGROUND_COLOR)
        backgroundLayout?.setBackgroundColor(defaultBkgColor)
        toolbar?.setBackgroundColor(defaultBkgColor)
        window.statusBarColor = darkenColor(defaultBkgColor)

        getSentimentButton?.setOnClickListener(this)
        sendCrashButton?.setOnClickListener(this)
        sendEventButton?.setOnClickListener(this)
        sendColorButton?.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.getSentimentButton -> {
                if (sentimentText?.text?.isBlank() == true) {
                    Snackbar.make(view, "You must enter something!", Snackbar.LENGTH_SHORT).show()
                    return
                }

                getSentimentButton?.text = "Calculating"
                getSentimentButton?.isEnabled = false
                sentimentText?.isEnabled = false

                try {
                    sentimentClient.getSentimentResult(sentimentText?.text.toString(),
                        object : Callback {
                            override fun onFailure(call: Call, e: IOException) {
                                // Something went wrong
                            }

                            @Throws(IOException::class)
                            override fun onResponse(call: Call, response: Response) {

                                if (response.isSuccessful) response.body()?.let { responseBody ->
                                    val sentimentResponse: SentimentResponse = gson.fromJson(
                                        responseBody.string(),
                                        SentimentResponse::class.java
                                    )
                                    val score = sentimentResponse.documents[0].score
                                    this@MainActivity.runOnUiThread { updateUI(score) }
                                } else {
                                    Snackbar.make(view, response.message(), Snackbar.LENGTH_LONG)
                                        .show()
                                    this@MainActivity.runOnUiThread { updateUI(-1f) }
                                }
                            }
                        })
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
            R.id.sendCrashButton -> CrashDialog().show(fragmentManager, "crashDialog")
            R.id.sendEventButton -> EventDialog().show(fragmentManager, "eventDialog")
            R.id.sendColorButton -> ColorDialog().show(fragmentManager, "colorDialog")
        }
    }

    fun updateUI(score: Float) {
        if (score != -1f) {
            val sentimentColor = Color.parseColor(getBackgroundColor(score))
            emojiView?.text = getEmojiString(score)
            backgroundLayout?.setBackgroundColor(sentimentColor)
            toolbar?.setBackgroundColor(sentimentColor)
            this@MainActivity.window.statusBarColor = darkenColor(sentimentColor)
        }

        sentimentText?.isEnabled = true
        getSentimentButton?.text = "Submit"
        getSentimentButton?.isEnabled = true
    }

    private fun getEmojiString(score: Float) = when {
        score < 0.4 -> SAD_FACE_EMOJI
        score in 0.4..0.6 -> NEUTRAL_FACE_EMOJI
        score > 0.6 -> HAPPY_FACE_EMOJI
        else -> ""
    }

    private fun getBackgroundColor(score: Float) = when {
        score <= 0.1 -> EMOTION_COLOR_1
        score > 0.1 && score <= 0.2 -> EMOTION_COLOR_2
        score > 0.2 && score <= 0.3 -> EMOTION_COLOR_3
        score > 0.3 && score <= 0.4 -> EMOTION_COLOR_4
        score > 0.4 && score <= 0.6 -> EMOTION_COLOR_5
        score > 0.6 && score <= 0.7 -> EMOTION_COLOR_6
        score > 0.7 && score <= 0.8 -> EMOTION_COLOR_7
        score > 0.8 && score <= 0.9 -> EMOTION_COLOR_8
        score > 0.9 -> EMOTION_COLOR_9
        else -> ""
    }

    private fun darkenColor(color: Int): Int {
        val hsv = FloatArray(3)
        Color.colorToHSV(color, hsv)
        hsv[2] *= 0.8f
        return Color.HSVToColor(hsv)
    }

    class CrashDialog : DialogFragment() {
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
            AlertDialog.Builder(activity).apply {
                setMessage("A crash report will be sent when you reopen the app.")
                setPositiveButton("Crash app") { _, _ -> throw RuntimeException("crashing") }
                setNegativeButton("Cancel") { _, _ -> }
            }.create()
    }

    class EventDialog : DialogFragment() {
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
            AlertDialog.Builder(activity).apply {
                setMessage("Event sent").setPositiveButton("OK") { _, _ -> }
            }.create()
    }

    class ColorDialog : DialogFragment() {
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
            AlertDialog.Builder(activity).apply {
                val colors = arrayOf<CharSequence>("Yellow", "Blue", "Red")
                setTitle("Pick a color").setItems(colors) { _, index ->
                    when (index) {
                        0 -> PROPERTIES["Color"] = "Yellow"
                        1 -> PROPERTIES["Color"] = "Blue"
                        2 -> PROPERTIES["Color"] = "Red"
                    }
                }
            }.create()
    }
}
