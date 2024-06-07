package com.example.africanshippingapp.ui.gallery

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.africanshippingapp.databinding.FragmentEnterGoodsBinding

class EnterGoodsFragment : Fragment() {

    private var _binding: FragmentEnterGoodsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val galleryViewModel =
            ViewModelProvider(this)[GoodsEnterViewModel::class.java]

        _binding = FragmentEnterGoodsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val button: Button = binding.btnEnterGoods // assuming you have a button in your layout with id "button"
        button.setOnClickListener {
            val intent = Intent(activity, InsertionActivity::class.java) // replace NextActivity with your desired activity
            startActivity(intent)
        }
        val buttons: Button = binding.btnEnterStoreGoods // assuming you have a button in your layout with id "button"
        buttons.setOnClickListener {
            val intents = Intent(activity, StoreGoodsInsertion::class.java) // replace NextActivity with your desired activity
            startActivity(intents)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}