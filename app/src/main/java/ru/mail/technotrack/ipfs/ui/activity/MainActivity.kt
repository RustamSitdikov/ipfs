package ru.mail.technotrack.ipfs.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*
import ru.mail.technotrack.ipfs.App
import ru.mail.technotrack.ipfs.R
import ru.mail.technotrack.ipfs.di.component.DaggerAppComponent
import ru.mail.technotrack.ipfs.ui.fragment.DashboardFragment
import ru.mail.technotrack.ipfs.ui.fragment.DocumentsFragment


class MainActivity : AppCompatActivity() {

    companion object {
        const val DASHBOARD_FRAGMENT_TAG = "DashboardFragment"
        const val DOCUMENT_FRAGMENT_TAG = "DocumentFragment"

        fun start(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }

    private val LOG_TAG: String = MainActivity::class::simpleName.toString()

    private val dashboardFragment: Fragment = DashboardFragment.newInstance()
    private val documentsFragment: Fragment = DocumentsFragment.newInstance()
    var activeFragment: Fragment = dashboardFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        (application as App).appComponent.inject(this)

        // Create fragments
        createFragments()

        // Create bottom navigation listener
        mainNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
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

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }
}
