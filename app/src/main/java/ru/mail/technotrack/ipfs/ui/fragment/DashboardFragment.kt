package ru.mail.technotrack.ipfs.ui.fragment

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import io.ipfs.kotlin.IPFS
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_dashboard.view.*
import ru.mail.technotrack.ipfs.viewmodel.DashboardViewModel
import ru.mail.technotrack.ipfs.R
import ru.mail.technotrack.ipfs.data.network.Api
import ru.mail.technotrack.ipfs.data.service.IPFSService
import ru.mail.technotrack.ipfs.di.component.DaggerAppComponent
import javax.inject.Inject

enum class State(val value: Int) {
    START(R.string.start_ipfs),
    STOP(R.string.stop_ipfs);
}

class DashboardFragment : Fragment() {

    companion object {
        fun newInstance() = DashboardFragment()
    }

    @Inject
    lateinit var api: Api
    @Inject
    lateinit var ipfs: IPFS

    private lateinit var viewModel: DashboardViewModel
    private lateinit var runButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DaggerAppComponent.builder().build().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        viewModel = ViewModelProviders.of(this).get(DashboardViewModel::class.java)

        with(view) {
            runButton.text = resources.getString(viewModel.getState().value)
            runButton.setOnClickListener {
                when (viewModel.getState()) {
                    State.START -> {
                        viewModel.setState(State.STOP)
                        IPFSService.start(context, IPFSService.Action.START.toString())
                    }
                    State.STOP -> {
                        viewModel.setState(State.START)
                        IPFSService.start(context, IPFSService.Action.STOP.toString())
                    }
                }
                runButton.text = resources.getString(viewModel.getState().value)
            }
        }

        //            api
//                .getVersion()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnNext { version -> println(version.toString()) }
//                .doOnError { data -> println(data) }
//                .subscribe()
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }
}
