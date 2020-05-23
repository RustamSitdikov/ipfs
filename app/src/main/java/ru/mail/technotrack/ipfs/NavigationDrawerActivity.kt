package ru.mail.technotrack.ipfs

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import com.google.android.material.navigation.NavigationView
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_navigation_drawer.*
import kotlinx.android.synthetic.main.app_bar_navigation_drawer.*
import kotlinx.android.synthetic.main.fragment_dashboard.view.*
import ru.mail.technotrack.ipfs.fragments.UploadMenuFragment

class NavigationDrawerActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, NavigationDrawerActivity::class.java))
        }
    }

    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation_drawer)
        setSupportActionBar(toolbar_navigation_drawer)

        fab.setOnClickListener {
            val uploadMenuFragment = UploadMenuFragment()
            uploadMenuFragment.show(supportFragmentManager, uploadMenuFragment.tag)
        }

        val toggle = ActionBarDrawerToggle(
            this, layout_navigation_drawer, toolbar_navigation_drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        layout_navigation_drawer.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        view_pager.adapter = mSectionsPagerAdapter

        view_pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(view_pager))

//        this.supportFragmentManager.beginTransaction().replace(R.id.content_bottom_navigation_view, HomeFragment()).commit()
    }

    override fun onBackPressed() {
        if (layout_navigation_drawer.isDrawerOpen(GravityCompat.START)) {
            layout_navigation_drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.navigation_drawer, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        layout_navigation_drawer.closeDrawer(GravityCompat.START)
        return true
    }

    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            // getItem is called to instantiate the fragment for the given page.
            // Return a DashboardTabFragment (defined as a static inner class below).
            if (position == 0) return HomeFragment()
            else return DashboardTabFragment.newInstance(position + 1)
        }

        override fun getCount(): Int {
            // Show 3 total pages.
            return 2
        }
    }

    class DashboardTabFragment : Fragment() {

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val rootView = inflater.inflate(R.layout.fragment_dashboard, container, false)
            rootView.section_label.text = getString(R.string.section_format, arguments?.getInt(ARG_SECTION_NUMBER))
            return rootView
        }

        companion object {
            /**
             * The fragment argument representing the section number for this
             * fragment.
             */
            private val ARG_SECTION_NUMBER = "section_number"

            /**
             * Returns a new instance of this fragment for the given section
             * number.
             */
            fun newInstance(sectionNumber: Int): DashboardTabFragment {
                val fragment = DashboardTabFragment()
                val args = Bundle()
                args.putInt(ARG_SECTION_NUMBER, sectionNumber)
                fragment.arguments = args
                return fragment
            }
        }
    }
}
