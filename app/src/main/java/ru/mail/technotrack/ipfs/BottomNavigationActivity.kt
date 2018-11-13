//package ru.mail.technotrack.ipfs
//
//import android.content.Context
//import android.content.Intent
//import android.os.Bundle
//import com.google.android.material.bottomnavigation.BottomNavigationView
//import androidx.appcompat.app.AppCompatActivity
//import kotlinx.android.synthetic.main.activity_bottom_navigation.*
//
//class BottomNavigationActivity : AppCompatActivity() {
//
//    companion object {
//        fun start(context: Context) {
//            context.startActivity(Intent(context, BottomNavigationActivity::class.java))
//        }
//    }
//
//    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
//        when (item.itemId) {
//            R.id.navigation_home -> {
//                supportFragmentManager.beginTransaction().replace(R.id.content_bottom_navigation_view, HomeFragment()).commit()
//                return@OnNavigationItemSelectedListener true
//            }
//            R.id.navigation_dashboard -> {
//                return@OnNavigationItemSelectedListener true
//            }
//        }
//        false
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_bottom_navigation)
//
//        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
//    }
//}
