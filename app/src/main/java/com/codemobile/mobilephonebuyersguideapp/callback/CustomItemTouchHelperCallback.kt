package com.codemobile.mobilephonebuyersguideapp.callback

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView


class CustomItemTouchHelperCallback(private var listener: CustomItemTouchHelperListener) : ItemTouchHelper.Callback() {
    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        val dragFlags = 0
        val swipeFlags = ItemTouchHelper.LEFT
        return makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        source: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        if (source?.itemViewType != target?.itemViewType) {
            return false
        }
        if (source != null && target != null) {
            return listener.onItemMove(source.adapterPosition, target.adapterPosition)
        }
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        viewHolder?.let {
            listener.onItemDismiss(viewHolder.adapterPosition)
        }
    }

    override fun isLongPressDragEnabled(): Boolean {
        return false
    }
}

interface CustomItemTouchHelperListener {
    fun onItemMove(fromPosition: Int, toPosition: Int) : Boolean

    fun onItemDismiss(position: Int)
}