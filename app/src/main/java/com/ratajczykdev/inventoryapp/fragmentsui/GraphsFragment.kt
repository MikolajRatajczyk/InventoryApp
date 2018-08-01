package com.ratajczykdev.inventoryapp.fragmentsui


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.ratajczykdev.inventoryapp.R

class GraphsFragment : Fragment() {

    private val statistics: HashMap<String, Float> = hashMapOf()

    private var itemsNumber: Float = 0F
    private var productsNumber: Float = 0F
    private var productsMaxPrice: Float = 0F
    private var productsMinPrice: Float = 0F

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_graphs, container, false)
    }


}
