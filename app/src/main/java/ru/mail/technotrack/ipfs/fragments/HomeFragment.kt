package ru.mail.technotrack.ipfs.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

import ru.mail.technotrack.ipfs.viewAdapters.HomeRecyclerViewAdapter
import ru.mail.technotrack.ipfs.R
import ru.mail.technotrack.ipfs.api.DTO.FileInfo as FileInfoDto
import ru.mail.technotrack.ipfs.api.*
import ru.mail.technotrack.ipfs.database.FileInfo as FileInfoEntity
import ru.mail.technotrack.ipfs.database.Model
import ru.mail.technotrack.ipfs.database.converters.fromDtoToModelFileInfo

class HomeFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private var columnCount = 2
    private var listener: OnListFragmentInteractionListener? = null
    private var filesInfoList = ArrayList<FileInfoEntity>()

    private var dataLoaded = false
    private val DATA_LOADED = "dataLoaded"
    private lateinit var model: Model
    private var currentPath = "/"

    private lateinit var viewAdapter: HomeRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val swipeContainer = inflater.inflate(R.layout.fragment_home_list, container, false) as SwipeRefreshLayout
        val view = swipeContainer.findViewById<RecyclerView>(R.id.list)

        viewAdapter = HomeRecyclerViewAdapter(
            filesInfoList,
            listener,
            container
        )

        with(view) {
            this?.layoutManager = when {
                columnCount <= 1 -> LinearLayoutManager(context)
                else -> GridLayoutManager(context, columnCount)
            }
            this?.adapter = viewAdapter
        }

        return swipeContainer
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (view as SwipeRefreshLayout).setOnRefreshListener(this)
    }

    /**
     * when item is clicked
     * should data from current folder be loaded
     */
    fun changeCurrentPath(path: String) {
        currentPath = path
        loadDataSet()
    }

    override fun onRefresh() {
        loadDataSet()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dataLoaded = savedInstanceState?.getBoolean(DATA_LOADED) ?: false
        model = Model(this.context!!)
        onAttachData()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(DATA_LOADED, dataLoaded)
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * In the begining get data from db
     */
    fun onAttachData() {
        filesInfoList.addAll(model.getFiles(currentPath))
        if(filesInfoList.isEmpty()) {
            loadDataSet()
        }
    }

    /**
     * Download data via ApiExecutor method
     */
    private fun loadDataSet() {
        val files = model
        model.deleteFilesByPath(currentPath)
        getFilesInfo({ it, path ->
            run {
                it.entries?.let {
                    model.updateFiles(path, fromDtoToModelFileInfo(it, path))
                    filesInfoList.clear()
                    filesInfoList.addAll(model.getFiles(currentPath))
                }
                viewAdapter.notifyDataSetChanged()
                (this.view as SwipeRefreshLayout).isRefreshing = false
            }
        }, currentPath)
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson
     * [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onListFragmentInteraction(item: FileInfoEntity?)
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}