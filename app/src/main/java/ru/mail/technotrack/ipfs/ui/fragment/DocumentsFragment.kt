package ru.mail.technotrack.ipfs.ui.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.mail.technotrack.ipfs.model.Document

import ru.mail.technotrack.ipfs.R
import ru.mail.technotrack.ipfs.ui.adapter.DocumentsRecyclerViewAdapter


class DocumentsFragment : Fragment() {

    private val documents: MutableList<Document> = mutableListOf(
        Document(
            "1",
            "2",
            "3"
        )
    )

    companion object {
        @JvmStatic
        fun newInstance(): DocumentsFragment {
            return DocumentsFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_documents, container, false) as RecyclerView
        with (view) {
            layoutManager = when {
                resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT -> LinearLayoutManager(context)
                else -> GridLayoutManager(context, 2)
            }
            adapter = DocumentsRecyclerViewAdapter(documents)
        }
        return view
    }
}
