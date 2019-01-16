package ru.mail.technotrack.ipfs.ui.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import kotlinx.android.synthetic.main.fragment_dashboard.*
import ru.mail.technotrack.ipfs.R
import ru.mail.technotrack.ipfs.data.network.IPFSApi
import ru.mail.technotrack.ipfs.data.service.IPFSService
import ru.mail.technotrack.ipfs.di.component.DaggerAppComponent
import ru.mail.technotrack.ipfs.viewmodel.IPFSViewModel
import javax.inject.Inject

enum class State(val value: Int) {
    START(R.string.start_ipfs),
    STOP(R.string.stop_ipfs);
}

class DashboardFragment : Fragment() {

    companion object {
        fun newInstance() = DashboardFragment()
    }

    private val LOG_TAG: String = DashboardFragment::class::simpleName.toString()

    @Inject
    lateinit var api: IPFSApi

    private lateinit var viewModel: IPFSViewModel

    private val ipfsReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val message = intent.getStringExtra(IPFSService.MESSAGE)
            Log.d(LOG_TAG, message)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DaggerAppComponent.builder().build().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(IPFSViewModel::class.java)

        viewModel.getInfo().observe(this, Observer {
            peerIdTextView.text = it.peerId
            versionTextView.text = it.agentVersion
            apiTextView.text = it.protocolVersion
        })
    }

    override fun onResume() {
        super.onResume()

        val intentFilter = IntentFilter(IPFSService.ACTION)
        LocalBroadcastManager.getInstance(activity!!.baseContext).registerReceiver(ipfsReceiver, intentFilter)
    }

    override fun onPause() {
        super.onPause()

        LocalBroadcastManager.getInstance(activity!!.baseContext).unregisterReceiver(ipfsReceiver)
    }
}
