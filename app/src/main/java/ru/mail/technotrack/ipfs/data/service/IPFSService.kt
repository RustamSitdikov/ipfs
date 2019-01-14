package ru.mail.technotrack.ipfs.data.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.mail.technotrack.ipfs.R
import ru.mail.technotrack.ipfs.ui.activity.MainActivity
import ru.mail.technotrack.ipfs.IPFSDaemon
import ru.mail.technotrack.ipfs.utils.NotificationId


class IPFSService @JvmOverloads constructor(name: String = "IPFS Service") : IntentService(name) {

    private val LOG_TAG: String = "ru.mail.technotrack.ipfs.data.service.IPFSService"

    companion object {
        const val ACTION = "action"
        const val MESSAGE = "message"
        const val CHANNEL_ID = "IPFS_SERVICE_CHANNEL_ID"

        fun start(context: Context, action: String) {
            val createIntent = Intent(context, IPFSService::class.java)
            createIntent.action = action
            context.startService(createIntent)
        }
    }

    enum class Action(val action: String?) {
        CREATE("CREATE"),
        START("START"),
        STOP("STOP")
    }

    lateinit var notificationManager: NotificationManager

    override fun onHandleIntent(intent: Intent) {
        Log.d(LOG_TAG, "intent service ${intent.action}")

        Observable
            .fromCallable { execute(intent) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { it ->
                run {
                    val notificationText: String = it
                    val notificationTitle: String

                    val action: Action = Action.valueOf(intent.action!!)
                    when (action) {
                        Action.CREATE -> {
                            createNotification("", notificationText)
                        }
                        Action.START -> {
                            notificationTitle = resources.getString(R.string.notification_ipfs_created)
                            createNotification(notificationTitle, notificationText)
                        }
                        Action.STOP -> {
                            notificationTitle = resources.getString(R.string.notification_ipfs_stopped)
                            createNotification(notificationTitle, notificationText)
                        }
                    }

                    val broadcastIntent = Intent(ACTION)
                    broadcastIntent.putExtra(MESSAGE, action.toString())
                    LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(broadcastIntent)
                }
            }.subscribe()
    }

    private fun execute(intent: Intent) : String {
        val action: Action = Action.valueOf(intent.action!!)
        return when (action) {
            Action.CREATE -> IPFSDaemon.create(applicationContext)
            Action.START -> IPFSDaemon.start()
            Action.STOP -> IPFSDaemon.stop()
        }
    }

    override fun onCreate() {
        Log.d(LOG_TAG, "create service")

        super.onCreate()

        createNotificationManager()
    }

    override fun onDestroy() {
        Log.d(LOG_TAG, "destroy service")

        super.onDestroy()
//        destroyNotificationManager()
    }

    private fun createNotificationManager() {
        val name = getString(R.string.ipfs_channel_name)
        val descriptionText = getString(R.string.ipfs_channel_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun destroyNotificationManager() {
        notificationManager.cancelAll()
    }

    private fun createNotification(notificationTitle: String, notificationText: String) {
        Log.d(LOG_TAG, notificationText)
        val notificationIntent = Intent(this, MainActivity::class.java)//.apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_ipfs)
            .setContentTitle(notificationTitle)
            .setContentText(notificationText)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            notify(NotificationId.id, builder.build())
        }
    }
}
