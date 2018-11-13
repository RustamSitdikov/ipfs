package ru.mail.technotrack.ipfs.item

import java.util.ArrayList
import java.util.HashMap

object ItemContent {

    val ITEMS: MutableList<Item> = ArrayList()

    val ITEM_MAP: MutableMap<String, Item> = HashMap()

    private val COUNT = 25

    init {
        for (i in 1..COUNT) {
            addItem(createItem(i))
        }
    }

    private fun addItem(item: Item) {
        ITEMS.add(item)
        ITEM_MAP.put(item.id, item)
    }

    private fun createItem(position: Int): Item {
        return Item(position.toString(), "Item " + position, makeDetails(position))
    }

    private fun makeDetails(position: Int): String {
        val builder = StringBuilder()
        builder.append("Details about Item: ").append(position)
        for (i in 0..position - 1) {
            builder.append("\nMore details information here.")
        }
        return builder.toString()
    }

    /**
     * A dummy item representing a piece of content.
     */
    data class Item(val id: String, val content: String, val details: String) {
        override fun toString(): String = content
    }
}
