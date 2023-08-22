package com.immersion.immersionandroid.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.immersion.immersionandroid.databinding.ItemOwnershipBinding
import com.immersion.immersionandroid.domain.IEmployerOwnerShip

class OwnershipAdapter(private val ownerShipList: List<IEmployerOwnerShip>) :
    RecyclerView.Adapter<OwnershipAdapter.OwnershipViewHolder>() {

    private var mListener: OnClickListener? = null

    interface OnClickListener {
        fun onClick(position: Int, item: IEmployerOwnerShip)
    }

    inner class OwnershipViewHolder(val ownerShipView: ItemOwnershipBinding) :
        RecyclerView.ViewHolder(ownerShipView.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OwnershipViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemOwnershipBinding.inflate(layoutInflater, parent, false)
        return OwnershipViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OwnershipViewHolder, position: Int) {

        val item = ownerShipList[position]
        holder.ownerShipView.apply {
            tvBusinessMainInfo.text = item.toString()
        }

        holder.itemView.setOnClickListener {
            if (this.mListener != null)
                this.mListener!!.onClick(position, item)
        }
    }

    override fun getItemCount(): Int {
        return ownerShipList.size
    }

    fun setOnClickListener(listener: OnClickListener) {
        this.mListener = listener
    }
}