package ru.mail.technotrack.ipfs

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_navigation.*

class NavigationActivity : AppCompatActivity() {

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, NavigationActivity::class.java))
        }
    }

    private lateinit var fragmentHome: Fragment
    private lateinit var fragmentDashboard: Fragment
    private lateinit var fragmentActive: Fragment

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                supportFragmentManager.beginTransaction().hide(fragmentActive).show(fragmentHome).commit()
                fragmentActive = fragmentHome
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                supportFragmentManager.beginTransaction().hide(fragmentActive).show(fragmentDashboard).commit()
                fragmentActive = fragmentDashboard
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)

        fragmentHome = HomeFragment()
        fragmentDashboard = Fragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.content_bottom_navigation_view, fragmentDashboard, "Dashboard")
            .hide(fragmentDashboard)
            .commit()
        supportFragmentManager.beginTransaction()
            .add(R.id.content_bottom_navigation_view, fragmentHome, "Home")
            .commit()
        fragmentActive = fragmentHome

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }
}
