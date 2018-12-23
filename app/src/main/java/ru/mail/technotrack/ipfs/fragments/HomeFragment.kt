package ru.mail.technotrack.ipfs.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import retrofit2.Call
import retrofit2.Response

import ru.mail.technotrack.ipfs.viewAdapters.HomeRecyclerViewAdapter
import ru.mail.technotrack.ipfs.R
import ru.mail.technotrack.ipfs.api.DTO.FileInfo
import ru.mail.technotrack.ipfs.api.DTO.FileInfoList
import ru.mail.technotrack.ipfs.api.RetrofitClient

import javax.security.auth.callback.Callback

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [HomeFragment.OnListFragmentInteractionListener] interface.
 */
class HomeFragment : Fragment() {

    // TODO: Customize parameters
    private var columnCount = 2
    private var listener: OnListFragmentInteractionListener? = null
    private var filesInfoList = ArrayList<FileInfo>()
    private lateinit var viewAdapter: HomeRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home_list, container, false)
        viewAdapter = HomeRecyclerViewAdapter(
            filesInfoList,
            listener,
            container
        )

        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = viewAdapter
            }
            loadDataSet()
        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    private fun loadDataSet() {
        RetrofitClient.create().getFilesInfo().enqueue(object : Callback,
            retrofit2.Callback<FileInfoList> {
            // TODO add exception catch
            override fun onFailure(call: Call<FileInfoList>, t: Throwable) {
                println("LOADING FAILED")
            }

            override fun onResponse(call: Call<FileInfoList>, response: Response<FileInfoList>?) {
                if (response != null) {
                    response.body()?.entries?.let { filesInfoList.addAll(it) }
                    viewAdapter.notifyDataSetChanged()
                }
            }
        })
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
        fun onListFragmentInteraction(item: FileInfo?)
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
