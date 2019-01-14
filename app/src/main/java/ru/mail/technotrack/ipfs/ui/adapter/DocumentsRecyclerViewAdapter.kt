package ru.mail.technotrack.ipfs.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_document.view.*
import ru.mail.technotrack.ipfs.model.Document
import ru.mail.technotrack.ipfs.R


class DocumentsRecyclerViewAdapter(
    val items: MutableList<Document>
) : RecyclerView.Adapter<DocumentsRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_document, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun dispatch(newList: MutableList<Document>) {
        val diffUtilCallback = DiffUtilCallback(newList, items)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallback)
        items.clear()
        items.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: Document) = with(view) {
            document_id.text = item.id
            document_title.text = item.title
        }
    }

    inner class DiffUtilCallback(private val newList: List<Document>, private val oldList: List<Document>) : DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}