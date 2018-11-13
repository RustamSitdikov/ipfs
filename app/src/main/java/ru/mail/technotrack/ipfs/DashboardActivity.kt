package ru.mail.technotrack.ipfs

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager

class DashboardActivity : AppCompatActivity() {

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, DashboardActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val viewPager = findViewById<ViewPager>(R.id.viewpager_dashboard)
        if (viewPager != null) {
            val adapter = DashboardViewPagerAdapter(supportFragmentManager)
            viewPager.adapter = adapter
        }
    }
}