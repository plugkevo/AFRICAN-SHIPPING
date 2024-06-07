package com.example.africanshippingapp.ui.slideshow

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.africanshippingapp.databinding.FragmentViewGoodsBinding

class GoodsViewFragment : Fragment() {

    private var _binding: FragmentViewGoodsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val slideshowViewModel =
            ViewModelProvider(this).get(GoodsViewModel::class.java)

        _binding = FragmentViewGoodsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val button: Button = binding.btnViewTruckGoods // assuming you have a button in your layout with id "button"
        button.setOnClickListener {
            val intent = Intent(activity, ViewGoods::class.java) // replace NextActivity with your desired activity
            startActivity(intent)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}