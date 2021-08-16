package com.example.radioapp

import android.media.AudioManager
import android.media.MediaPlayer
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private var isPrepared = false
    private var isStarted = false

    private lateinit var button: Button
    private lateinit var mediaPlayer: MediaPlayer

    private var stream = "http://stream.radioreklama.bg:80/radio1rock128"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button = findViewById(R.id.button)
        button.isEnabled  = false
        button.text = "Loading..."

        mediaPlayer = MediaPlayer()
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)

        PlayerTask().execute(stream)

        button.setOnClickListener {
            if (isStarted){
                isStarted = false
                mediaPlayer.pause()
                button.text = "Play"
            }else{
                isStarted = true
                mediaPlayer.start()
                button.text = "pause"
            }
        }

    }

    inner class PlayerTask: AsyncTask<String, Unit, Boolean>() {

        override fun doInBackground(vararg params: String?): Boolean? {
            try {
                mediaPlayer.setDataSource(params[0])
                mediaPlayer.prepare()
                isPrepared = true
            }catch (e: Exception){
                e.printStackTrace()
            }
            return isPrepared
        }

        override fun onPostExecute(result: Boolean?) {
            super.onPostExecute(result)
            button.isEnabled = true
            button.text = "play"
        }
    }

    override fun onPause() {
        super.onPause()
        if(isStarted)
            mediaPlayer.pause()
    }

    override fun onResume() {
        super.onResume()
        if(isStarted)
            mediaPlayer.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        if(isPrepared)
            mediaPlayer.release()
    }
}