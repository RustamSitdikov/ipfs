package ru.mail.technotrack.ipfs.viewAdapters

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView


import ru.mail.technotrack.ipfs.fragments.HomeFragment.OnListFragmentInteractionListener
import ru.mail.technotrack.ipfs.dummy.DummyContent.DummyItem

import kotlinx.android.synthetic.main.fragment_home.view.*
import ru.mail.technotrack.ipfs.R
import ru.mail.technotrack.ipfs.activities.ScrollingActivity
import ru.mail.technotrack.ipfs.api.DTO.FileInfo
import ru.mail.technotrack.ipfs.utils.getTypeFile

/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class HomeRecyclerViewAdapter(
    private val mValues: List<FileInfo>,
    private val mListener: OnListFragmentInteractionListener?,
    private val viewgroup: ViewGroup?
) : RecyclerView.Adapter<HomeRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as FileInfo
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            val bundle = Bundle()
            val intent = Intent(viewgroup?.context, ScrollingActivity::class.java)
            bundle.putSerializable("item", item)
            intent.putExtras(bundle)
            mListener?.onListFragmentInteraction(item)
            viewgroup?.context?.startActivity(intent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_home, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.mIdView.text = item.name
        holder.mContentView.text = item.type?.let { getTypeFile(it) }

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mIdView: TextView = mView.item_number
        val mContentView: TextView = mView.content

        override fun toString(): String {
            return super.toString() + " '" + mContentView.text + "'"
        }
    }
}
