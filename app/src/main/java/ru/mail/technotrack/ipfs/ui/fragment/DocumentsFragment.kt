package ru.mail.technotrack.ipfs.ui.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.mail.technotrack.ipfs.model.Document

import ru.mail.technotrack.ipfs.R
import ru.mail.technotrack.ipfs.di.component.DaggerAppComponent
import ru.mail.technotrack.ipfs.ui.adapter.DocumentsRecyclerViewAdapter
import ru.mail.technotrack.ipfs.viewmodel.IPFSViewModel


class DocumentsFragment : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance(): DocumentsFragment {
            return DocumentsFragment()
        }
    }

    private val LOG_TAG: String = DocumentsFragment::class::simpleName.toString()

    private lateinit var viewModel: IPFSViewModel
    private lateinit var adapter: DocumentsRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DaggerAppComponent.builder().build().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_documents, container, false) as RecyclerView

        with(view) {
            layoutManager = when {
                resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT -> LinearLayoutManager(context)
                else -> GridLayoutManager(context, 2)
            }
            adapter = DocumentsRecyclerViewAdapter(emptyList<Document>().toMutableList())
        }
        this.adapter = view.adapter as DocumentsRecyclerViewAdapter
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(IPFSViewModel::class.java)

        viewModel.getDocuments().observe(this, Observer {
            adapter.setItems(it)
        })
    }
}
