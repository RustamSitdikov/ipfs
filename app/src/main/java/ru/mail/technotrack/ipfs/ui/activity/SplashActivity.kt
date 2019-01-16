package ru.mail.technotrack.ipfs.ui.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.airbnb.lottie.LottieAnimationView
import ru.mail.technotrack.ipfs.R
import ru.mail.technotrack.ipfs.data.network.IPFSApi
import ru.mail.technotrack.ipfs.di.component.DaggerAppComponent
import ru.mail.technotrack.ipfs.data.service.IPFSService
import javax.inject.Inject

class SplashActivity : AppCompatActivity() {

    private val LOG_TAG: String = SplashActivity::class::simpleName.toString()

    @Inject
    lateinit var api: IPFSApi

    lateinit var progressAnimationView: LottieAnimationView

    private val ipfsReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            MainActivity.start(this@SplashActivity)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_main)

        progressAnimationView = findViewById(R.id.progress_view)

        DaggerAppComponent.builder().build().inject(this)
    }

    override fun onResume() {
        super.onResume()

        val intentFilter = IntentFilter(IPFSService.ACTION)
        LocalBroadcastManager.getInstance(this).registerReceiver(ipfsReceiver, intentFilter)

        progressAnimationView.playAnimation()

        IPFSService.start(this, IPFSService.Action.START.toString())
    }

    override fun onPause() {
        super.onPause()

        LocalBroadcastManager.getInstance(this).unregisterReceiver(ipfsReceiver)

        progressAnimationView.pauseAnimation()
    }
}
