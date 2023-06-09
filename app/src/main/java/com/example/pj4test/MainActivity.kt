package com.example.pj4test

import android.Manifest.permission.BLUETOOTH_CONNECT
import android.Manifest.permission.CAMERA
import android.Manifest.permission.RECORD_AUDIO
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.pj4test.fragment.CameraFragment
import com.example.pj4test.fragment.HonkFragment
import java.util.*


class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    // permissions
    private val permissions = arrayOf(RECORD_AUDIO, CAMERA, BLUETOOTH_CONNECT)
    private val PERMISSIONS_REQUEST = 0x0000001;

    // mp3 alert
    lateinit var mMediaPlayer: MediaPlayer

    //timer
    private val mainTimer: CountDownTimer = object : CountDownTimer(10000, 10000) {
        override fun onTick(millisUntilFinished: Long) {

        }

        override fun onFinish() {
            val cf: CameraFragment? =
                supportFragmentManager.findFragmentById(R.id.cameraFragmentContainerView) as CameraFragment?
            cf!!.unbind()

            val hf: HonkFragment? =
                supportFragmentManager.findFragmentById(R.id.honkFragmentContainerView) as HonkFragment?
            hf!!.recording = false
            hf!!.honkClassifier.audioInitialize()
            hf!!.honkClassifier.startRecording()
            hf!!.honkClassifier.startInferencing()
        }
    }
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkPermissions() // check permissions

        mMediaPlayer = MediaPlayer.create(this, R.raw.alert)
    }

    private fun checkPermissions() {
        if (permissions.all{ActivityCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED}){
            Log.d(TAG, "All Permission Granted")
        }
        else{
            requestPermissions(permissions, PERMISSIONS_REQUEST)
        }
    }
    fun alert(){
          Log.d("ALERT START", "ALERT START")
          mMediaPlayer.start()
          Log.d("ALERT FIN", "ALERT FIN")
      }

    fun cameraStart(){
        val cf: CameraFragment? =
            supportFragmentManager.findFragmentById(R.id.cameraFragmentContainerView) as CameraFragment?
        cf!!.fragmentCameraBinding.viewFinder.post{
            cf!!.setUpCamera()
        }
        mainTimer.start()
    }

}