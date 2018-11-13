package ru.mail.technotrack.ipfs

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView


import ru.mail.technotrack.ipfs.HomeFragment.OnListFragmentInteractionListener
import ru.mail.technotrack.ipfs.item.ItemContent.Item

import kotlinx.android.synthetic.main.fragment_home.view.*


class ItemRecyclerViewAdapter(
    private val mValues: List<Item>,
    private val mListener: OnListFragmentInteractionListener?
    ) : RecyclerView.Adapter<ItemRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as Item
            mListener?.onListFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_home, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.mIdView.text = item.id
        holder.mContentView.text = item.content
        holder.mImageView.setImageResource(R.drawable.ic_ipfs)

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mIdView: TextView = mView.item_number
        val mContentView: TextView = mView.content
        val mImageView: ImageView = mView.item_image

        override fun toString(): String {
            return super.toString() + " '" + mContentView.text + "'"
        }
    }
}
