package com.dicoding.smartcashier.ui.navigation.addtransaction

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dicoding.smartcashier.R
import com.dicoding.smartcashier.databinding.FragmentTransactionAddBinding

class AddTransactionFragment : Fragment() {

    private lateinit var binding: FragmentTransactionAddBinding
    private lateinit var transactionViewModel: AddTransactionViewModel
    private lateinit var notificationManager: NotificationManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTransactionAddBinding.inflate(inflater, container, false)


        transactionViewModel = ViewModelProvider(this)[AddTransactionViewModel::class.java]

        notificationManager =
            requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        binding.btnSend.setOnClickListener {
            val idBarang = binding.edId.text.toString()
            val jumlah = binding.edStockItem.text.toString()

            transactionViewModel.addTransaction(idBarang, jumlah)

            showNotification(idBarang, jumlah)

            binding.edId.text.clear()
            binding.edStockItem.text.clear()

        }

        return binding.root
    }

    private fun showNotification(idBarang: String, jumlah: String) {
        createNotificationChannel()

        val notificationContent = "Restock notification for $idBarang. Quantity: $jumlah"

        val notificationManager =
            requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channelId = CHANNEL_ID
        val notificationId = System.currentTimeMillis().toInt()

        val notificationBuilder = NotificationCompat.Builder(requireContext(), channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Restock Notification")
            .setContentText(notificationContent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        notificationManager.notify(notificationId, notificationBuilder.build())

    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_TOTAL,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                lightColor = Color.BLUE
                enableLights(true)
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        val CHANNEL_ID = "channel_id"
        val CHANNEL_TOTAL = "channel_total"
    }

}