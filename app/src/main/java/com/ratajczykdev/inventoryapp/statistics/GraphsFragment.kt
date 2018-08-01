package com.ratajczykdev.inventoryapp.statistics


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.helper.StaticLabelsFormatter
import com.jjoe64.graphview.series.BarGraphSeries
import com.jjoe64.graphview.series.DataPoint

import com.ratajczykdev.inventoryapp.R
import com.ratajczykdev.inventoryapp.statistics.StatisticsContract
import kotlinx.android.synthetic.main.fragment_graphs.*

/**
 * Shows graphs with statistics data about products
 *
 * @author Mikołaj Ratajczyk <mikolaj.ratajczyk@gmail.com>
 */
class GraphsFragment : Fragment() {

    private var statistics: HashMap<String, Float> = hashMapOf()

    private var itemsNumber: Double = 0.0
    private var productsNumber: Double = 0.0
    private var productsMaxPrice: Double = 0.0
    private var productsMinPrice: Double = 0.0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_graphs, container, false)

        if (arguments != null) {
            statistics = arguments!!.getSerializable(StatisticsContract.STATISTICS_MAP_NAME) as HashMap<String, Float>
            setNumbersData(statistics)
            setPricesData(statistics)
        }
        // Inflate the layout for this fragment
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureGraphItemsAndProductsNumber()
        configureGraphMaxAndMinPrice()
    }

    private fun setNumbersData(statistics: HashMap<String, Float>) {
        itemsNumber = statistics.get(StatisticsContract.ITEMS_NUMBER_KEY)?.toDouble() ?: 0.0
        productsNumber = statistics.get(StatisticsContract.PRODUCTS_NUMBER_KEY)?.toDouble() ?: 0.0
    }

    private fun setPricesData(statistics: HashMap<String, Float>) {
        productsMaxPrice = statistics.get(StatisticsContract.PRODUCTS_MAX_PRICE_KEY)?.toDouble() ?: 0.0
        productsMinPrice = statistics.get(StatisticsContract.PRODUCTS_MIN_PRICE_KEY)?.toDouble() ?: 0.0
    }

    private fun configureGraphItemsAndProductsNumber() {
        val dataPointArray = arrayOf(
                DataPoint(0.0, productsNumber),
                DataPoint(1.0, itemsNumber)
        )

        val barGraphSeries = BarGraphSeries<DataPoint>(dataPointArray)
        barGraphSeries.isAnimated = true
        barGraphSeries.color = context!!.getColor(R.color.colorAccent)

        items_products_number_graphview.addSeries(barGraphSeries)
        items_products_number_graphview.title = context!!.getString(R.string.items_products_graph_title)
        hideGraphXLabels(items_products_number_graphview)
    }

    private fun configureGraphMaxAndMinPrice() {
        val dataPointArray = arrayOf(
                DataPoint(0.0, productsMaxPrice),
                DataPoint(1.0, productsMinPrice)
        )

        val barGraphSeries = BarGraphSeries<DataPoint>(dataPointArray)
        barGraphSeries.isAnimated = true
        barGraphSeries.color = context!!.getColor(R.color.colorPrimary)

        max_min_price_graphview.addSeries(barGraphSeries)
        max_min_price_graphview.title = context!!.getString(R.string.max_min_price_graph_title)
        hideGraphXLabels(max_min_price_graphview)
    }

    private fun hideGraphXLabels(graph: GraphView) {
        val staticLabelsFormatter = StaticLabelsFormatter(graph)
        //  must have more than one element, if not it will cause crash
        val EMPTY_LABEL_ARRAY = arrayOf("", "")
        staticLabelsFormatter.setHorizontalLabels(EMPTY_LABEL_ARRAY)

        graph.gridLabelRenderer.labelFormatter = staticLabelsFormatter
    }


}
