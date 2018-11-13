package ru.mail.technotrack.ipfs

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout


class DashboardActivity : AppCompatActivity() {

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, DashboardActivity::class.java))
        }
    }

    lateinit var viewPager: ViewPager
    lateinit var tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        viewPager = findViewById(R.id.viewpager_dashboard)
        tabLayout = findViewById(R.id.tablayout_dashboard)

        val adapter = DashboardViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(DashboardTabFragment(), "Tab 1")
        adapter.addFragment(DashboardTabFragment(), "Tab 2")
        adapter.addFragment(DashboardTabFragment(), "Tab 3")

        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)
    }
}