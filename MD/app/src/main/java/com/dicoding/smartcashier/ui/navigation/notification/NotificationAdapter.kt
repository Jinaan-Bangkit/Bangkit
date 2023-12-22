package com.dicoding.smartcashier.ui.navigation.notification

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.smartcashier.data.remote.response.NotificationResponseItem
import com.dicoding.smartcashier.databinding.ListNotificationBinding

class NotificationAdapter(private var onItemClicked: (NotificationResponseItem) -> Unit) : ListAdapter<NotificationResponseItem, NotificationAdapter.NotificationViewHolder>(NotificationDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val binding = ListNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = getItem(position)
        holder.bind(notification)
    }

    fun setOnItemClickedListener(listener: (NotificationResponseItem) -> Unit) {
        onItemClicked = listener
    }

    inner class NotificationViewHolder(private val binding: ListNotificationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(notification: NotificationResponseItem) {
            binding.apply {
                nameItems.text = notification.namaBarang
                desc.text = notification.keterangan

                root.setOnClickListener {
                    onItemClicked.invoke(notification)
                }
            }
        }
    }

    private class NotificationDiffCallback : DiffUtil.ItemCallback<NotificationResponseItem>() {
        override fun areItemsTheSame(oldItem: NotificationResponseItem, newItem: NotificationResponseItem): Boolean {
            return oldItem.namaBarang == newItem.namaBarang
        }

        override fun areContentsTheSame(oldItem: NotificationResponseItem, newItem: NotificationResponseItem): Boolean {
            return oldItem == newItem
        }
    }
}
