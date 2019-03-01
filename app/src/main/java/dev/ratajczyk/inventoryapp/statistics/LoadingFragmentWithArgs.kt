package dev.ratajczyk.inventoryapp.statistics

import android.os.Bundle
import androidx.fragment.app.Fragment
import dev.ratajczyk.inventoryapp.MainActivity

/**
 * Required for transfer data between Fragments (communicate between Fragments)
 *
 * Data flow example: [StatisticsFragment] -> [MainActivity] -> [GraphsFragment]
 *
 * @author Mikolaj Ratajczyk <mikolaj.ratajczyk(AT)gmail.com>
 */
interface LoadingFragmentWithArgs {

    fun loadFragmentWithArgs(fragment: Fragment, bundle: Bundle)
}