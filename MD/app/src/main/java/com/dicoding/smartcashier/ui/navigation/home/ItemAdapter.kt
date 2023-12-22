package com.dicoding.smartcashier.ui.navigation.home

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.smartcashier.R
import com.dicoding.smartcashier.data.remote.response.ItemsResponse
import com.dicoding.smartcashier.databinding.ItemLayoutBinding
import com.dicoding.smartcashier.ui.detail.DetailItemActivity

class ItemAdapter : ListAdapter<ItemsResponse, ItemAdapter.MyViewHolder>(DIFF_CALLBACK) {
    inner class MyViewHolder(private val binding: ItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ItemsResponse) {
            binding.apply {
                tvId.text = item.idBarang
                tvItemName.text = item.nama
                tvPrice.text = item.hargaJual.toString()
                btnEdit.setOnClickListener {
                    showPopupMenu(btnEdit, adapterPosition)
                }
            }
        }

        private fun showPopupMenu(view: View, position: Int) {
            val popupMenu = PopupMenu(view.context, view)
            popupMenu.inflate(R.menu.more)
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.edit -> {
                        showEditDialog(view.context, position)
                        true
                    }
                    R.id.remove -> {
                        showDeleteDialog(view.context, position)
                        true
                    }
                    else -> false
                }
            }
            popupMenu.show()
        }

        private fun showEditDialog(context: Context, position: Int) {
            val item = getItem(position)

            val editView = LayoutInflater.from(context).inflate(R.layout.edit_item, null)
            val idEditText = editView.findViewById<EditText>(R.id.id)
            val nameEditText = editView.findViewById<EditText>(R.id.name)
            val salesEditText = editView.findViewById<EditText>(R.id.sales)
            val purchaseEditText = editView.findViewById<EditText>(R.id.purchase)
            val stockEditText = editView.findViewById<EditText>(R.id.stock)

            idEditText.setText(item.idBarang)
            nameEditText.setText(item.nama)
            salesEditText.setText(item.hargaJual.toString())
            purchaseEditText.setText(item.hargaBeli.toString())
            stockEditText.setText(item.stock.toString())

            AlertDialog.Builder(context)
                .setView(editView)
                .setPositiveButton("Save") { dialog, _ ->
                    // Update the item
                    val newItem = item.copy(
                        idBarang = idEditText.text.toString(),
                        nama = nameEditText.text.toString(),
                        hargaJual = salesEditText.text.toString().toInt(),
                        hargaBeli = purchaseEditText.text.toString().toInt(),
                        stock = stockEditText.text.toString().toInt()
                    )
                    notifyItemChanged(position, newItem)
                    Toast.makeText(context, "Item Information is Edited", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
        }

        private fun showDeleteDialog(context: Context, position: Int) {
            AlertDialog.Builder(context)
                .setTitle("Delete")
                .setIcon(R.drawable.ic_warning)
                .setMessage("Are you sure to delete this item?")
                .setPositiveButton("Yes") { dialog, _ ->
                    currentList.toMutableList().removeAt(position)
                    notifyItemRemoved(position)
                    Toast.makeText(context, "Deleted this information", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
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
                val intent = Intent(holder.itemView.context, DetailItemActivity::class.java)
                intent.putExtra(DetailItemActivity.ID, dataAdapter.idBarang)
                intent.putExtra(DetailItemActivity.NAME, dataAdapter.nama)
                intent.putExtra(DetailItemActivity.PURCHASE, dataAdapter.hargaBeli)
                intent.putExtra(DetailItemActivity.SALE, dataAdapter.hargaJual)
                intent.putExtra(DetailItemActivity.STOCK, dataAdapter.stock)
                holder.itemView.context.startActivity(intent)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ItemsResponse>() {
            override fun areItemsTheSame(oldItem: ItemsResponse, newItem: ItemsResponse): Boolean {
                return oldItem.idBarang == newItem.idBarang
            }

            override fun areContentsTheSame(oldItem: ItemsResponse, newItem: ItemsResponse): Boolean {
                return oldItem == newItem
            }
        }
    }
}
