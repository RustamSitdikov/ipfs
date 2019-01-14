package ru.mail.technotrack.ipfs.ui.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*
import ru.mail.technotrack.ipfs.R
import ru.mail.technotrack.ipfs.di.component.DaggerAppComponent
import ru.mail.technotrack.ipfs.ui.fragment.DashboardFragment
import ru.mail.technotrack.ipfs.ui.fragment.DocumentsFragment
import ru.mail.technotrack.ipfs.data.service.IPFSService
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import android.content.IntentFilter
import android.util.Log


class MainActivity : AppCompatActivity() {

    companion object {
        const val DASHBOARD_FRAGMENT_TAG = "DashboardFragment"
        const val DOCUMENT_FRAGMENT_TAG = "DocumentFragment"

        fun start(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }

    private val LOG_TAG: String = "ru.mail.technotrack.ipfs.ui.activity.MainActivity"

    private val dashboardFragment: Fragment = DashboardFragment.newInstance()
    private val documentsFragment: Fragment = DocumentsFragment.newInstance()
    var activeFragment: Fragment = dashboardFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        DaggerAppComponent.builder().build().inject(this)

        // Create fragments
        createFragments()

        // Create bottom navigation listener
        mainNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

//        IPFSService.start(this, IPFSService.Action.START.toString())
    }

    private fun createFragments() {
        supportFragmentManager.beginTransaction().add(R.id.mainContent, documentsFragment, DOCUMENT_FRAGMENT_TAG).hide(documentsFragment).commit()
        supportFragmentManager.beginTransaction().add(R.id.mainContent, dashboardFragment, DASHBOARD_FRAGMENT_TAG).commit()
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_dashboard -> {
                supportFragmentManager.beginTransaction().hide(activeFragment).show(dashboardFragment).commit()
                activeFragment = dashboardFragment
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_documents -> {
                supportFragmentManager.beginTransaction().hide(activeFragment).show(documentsFragment).commit()
                activeFragment = documentsFragment
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private val ipfsReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val message = intent.getStringExtra(IPFSService.MESSAGE)
            Log.d(LOG_TAG, message)
        }
    }

    override fun onResume() {
        super.onResume()

        val intentFilter = IntentFilter(IPFSService.ACTION)
        LocalBroadcastManager.getInstance(this).registerReceiver(ipfsReceiver, intentFilter)
    }

    override fun onPause() {
        super.onPause()

        LocalBroadcastManager.getInstance(this).unregisterReceiver(ipfsReceiver)
    }
}
