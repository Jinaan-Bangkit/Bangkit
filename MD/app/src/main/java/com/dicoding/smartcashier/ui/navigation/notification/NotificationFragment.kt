package com.dicoding.smartcashier.ui.navigation.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.smartcashier.databinding.FragmentNotificationBinding

class NotificationFragment : Fragment() {

    private lateinit var binding: FragmentNotificationBinding
    private lateinit var viewModel: NotificationViewModel
    private val notificationAdapter: NotificationAdapter by lazy {
        NotificationAdapter { notification ->
//            val action = NotificationFragmentDirections.actionNotificationFragmentToDetailNotificationFragment(
//                notification.namaBarang,
//                notification.keterangan
//            )
//            findNavController().navigate(action)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotificationBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this)[NotificationViewModel::class.java]

        binding.rvShowList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvShowList.adapter = notificationAdapter

        viewModel.listNotif.observe(viewLifecycleOwner, { listNotif ->
            notificationAdapter.submitList(listNotif)
        })

        notificationAdapter.setOnItemClickedListener { notification->
            viewModel.getNotifClick(notification.namaBarang, notification.keterangan)
        }

//        viewModel.clickedNotification.observe(viewLifecycleOwner, { clickedNotification ->
//            // Navigasi ke halaman detail notifikasi
//            val action = NotificationFragmentDirections
//                .actionNotificationFragmentToDetailNotificationFragment(
//                    clickedNotification.namaBarang,
//                    clickedNotification.keterangan
//                )
//            findNavController().navigate(action)
//        })
        return binding.root
    }


}