package ru.mail.technotrack.ipfs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.airbnb.lottie.LottieAnimationView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    companion object {
        const val ARGS_DELAY_TIME: String = "delay_time"
        const val ARGS_TIMER_IS_RUNNING: String = "timer_is_running"
        const val SECONDS_TO_DELAY: Long = 5
    }

    private val LOG_TAG: String = MainActivity::class.java::getName.toString()

    private lateinit var mTimer: Disposable
    private var mDelayTime: Long = SECONDS_TO_DELAY
    private var mTimerIsRunning: Boolean = true

    lateinit var progressAnimationView: LottieAnimationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progressAnimationView = findViewById(R.id.progress_view)
    }

    override fun onStart() {
        super.onStart()

        progressAnimationView.playAnimation()

        Thread.sleep(5000)
        NavigationActivity.start(this)
    }

    override fun onStop() {
        super.onStop()

        progressAnimationView.pauseAnimation()
    }

    private fun startTimer(time: Long) {
//        if (mTimerIsRunning) {
//            mTimer = Observable.timer(time, TimeUnit.SECONDS)
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnComplete { NavigationActivity.start(this) }
//                .subscribe(this::setTime)
//        }
    }

    private fun stopTimer() {
//        mTimer.dispose()
    }

    private fun setTime(time: Long) {
        mDelayTime = SECONDS_TO_DELAY - time
    }

    override fun onResume() {
        super.onResume()

        Log.i(LOG_TAG, "onResume")
        startTimer(mDelayTime)
    }

    override fun onPause() {
        super.onPause()

        Log.i(LOG_TAG, "onPause")
        stopTimer()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        Log.i(LOG_TAG, "onRestoreInstanceState")
        mDelayTime = savedInstanceState.getLong(ARGS_DELAY_TIME)
        mTimerIsRunning = savedInstanceState.getBoolean(ARGS_TIMER_IS_RUNNING)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        Log.i(LOG_TAG, "onSaveInstanceState")
        outState?.run {
            putLong(ARGS_DELAY_TIME, mDelayTime)
            putBoolean(ARGS_TIMER_IS_RUNNING, mTimerIsRunning)
        }

        super.onSaveInstanceState(outState)

    }
}
