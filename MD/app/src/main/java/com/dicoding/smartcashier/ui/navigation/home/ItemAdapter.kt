package com.dicoding.smartcashier.ui.navigation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.smartcashier.data.remote.response.ItemsResponse
import com.dicoding.smartcashier.databinding.ItemLayoutBinding

class ItemAdapter : ListAdapter<ItemsResponse, ItemAdapter.MyViewHolder>(DIFF_CALLBACK){
    private val listItem = ArrayList<ItemsResponse>()

    inner class MyViewHolder(private val binding: ItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ItemsResponse) {
            binding.apply {
                tvId.text = item.idBarang
                tvItemName.text = item.nama
                tvPrice.text = item.hargaJual.toString()
            }
        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemLayoutBinding.inflate(inflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val dataAdapter = getItem(position)
        if (dataAdapter != null) {
            holder.bind(dataAdapter)
            holder.itemView.setOnClickListener {

            }
        }
    }

    override fun getItemCount() = listItem.size

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ItemsResponse>() {
            override fun areItemsTheSame(oldItem: ItemsResponse, newItem: ItemsResponse): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ItemsResponse, newItem: ItemsResponse): Boolean {
                return oldItem == newItem
            }
        }
    }

}