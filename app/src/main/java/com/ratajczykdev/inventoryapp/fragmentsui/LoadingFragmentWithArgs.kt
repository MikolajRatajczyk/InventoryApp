package com.ratajczykdev.inventoryapp.fragmentsui

import android.os.Bundle
import android.support.v4.app.Fragment

/**
 * Required for transfer data between Fragments (communicate between Fragments)
 *
 * Data flow example: [StatisticsFragment] -> [MainActivity] -> [GraphsFragment]
 *
 * @author Mikolaj Ratajczyk <mikolaj.ratajczyk@gmail.com>
 */
interface LoadingFragmentWithArgs {

    fun loadFragmentWithArgs(fragment: Fragment, bundle: Bundle)
}