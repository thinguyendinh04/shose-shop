package com.nqmgaming.shoseshop.ui.fragments.order.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.nqmgaming.shoseshop.adapter.order.OrderTotalAdapter
import com.nqmgaming.shoseshop.databinding.FragmentCancelBinding
import com.nqmgaming.shoseshop.ui.activities.order_detail.OrderDetailActivity
import com.nqmgaming.shoseshop.ui.fragments.cart.CartViewModel
import com.nqmgaming.shoseshop.ui.fragments.order.OrderViewModel
import com.nqmgaming.shoseshop.util.SharedPrefUtils
import dagger.hilt.android.AndroidEntryPoint

private const val ARG_PARAM1 = "param1"

@AndroidEntryPoint
class CancelFragment : Fragment() {
    private var param1: String? = null
    private var _binding: FragmentCancelBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<OrderViewModel>()
    private val cartViewModel by viewModels<CartViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCancelBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val token = SharedPrefUtils.getString(requireContext(), "accessToken", "") ?: ""
        val bearerToken = "Bearer $token"

        viewModel.getOrders("Cancelled") { orders ->
            if (orders.isEmpty()) {
                binding.emptyOrderLayout.visibility = View.VISIBLE
                binding.orderRecyclerView.visibility = View.GONE
            } else {
                binding.emptyOrderLayout.visibility = View.GONE
                binding.orderRecyclerView.visibility = View.VISIBLE
                binding.orderRecyclerView.setHasFixedSize(true)
                binding.orderRecyclerView.adapter = OrderTotalAdapter(
                    viewModel = cartViewModel,
                    token = bearerToken
                ).apply {
                    differ.submitList(orders.reversed())
                    setOnItemClickListener {
                        val intent = Intent(requireContext(), OrderDetailActivity::class.java)
                        intent.putExtra("orderId", it.id)
                        startActivity(intent)
                    }
                }
            }
        }

    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            CancelFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)

                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}