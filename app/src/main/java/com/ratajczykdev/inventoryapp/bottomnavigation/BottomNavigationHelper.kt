package com.ratajczykdev.inventoryapp.bottomnavigation

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.support.design.widget.BottomNavigationView
import android.view.MenuItem
import com.ratajczykdev.inventoryapp.AboutActivity
import com.ratajczykdev.inventoryapp.CatalogActivity
import com.ratajczykdev.inventoryapp.R
import com.ratajczykdev.inventoryapp.settings.SettingsActivity
import com.ratajczykdev.inventoryapp.statistics.StatisticsActivity

/**
 * Helper class that configures [BottomNavigationView]
 */
class BottomNavigationHelper {

    companion object {
        //  TODO: prevent Activity from launching itself

        /**
         * Configures [BottomNavigationView] with listeners
         *
         * @param bottomNavigationView to be configured with listeners
         * @param currentActivity where BNV is
         */
        fun setBottomNavigationListener(bottomNavigationView: BottomNavigationView, currentActivity: Activity) {
            bottomNavigationView.setOnNavigationItemSelectedListener { currentItem: MenuItem ->
                when (currentItem.itemId) {
                    R.id.settings_button -> startSettingsActivity(currentActivity)
                    R.id.list_button -> startCatalogActivity(currentActivity)
                    R.id.statistics_button -> startStatisticsActivity(currentActivity)
                    R.id.about_button -> startAboutActivity(currentActivity)
                }
                true
            }
        }

        private fun startSettingsActivity(currentActivity: Activity) {
            val intent = Intent(currentActivity, SettingsActivity::class.java)
            currentActivity.startActivity(intent)
        }

        private fun startCatalogActivity(currentActivity: Activity) {
            val intent = Intent(currentActivity, CatalogActivity::class.java)
            currentActivity.startActivity(intent)
        }

        private fun startStatisticsActivity(currentActivity: Activity) {
            val intent = Intent(currentActivity, StatisticsActivity::class.java)
            currentActivity.startActivity(intent)
        }

        private fun startAboutActivity(currentActivity: Activity) {
            val intent = Intent(currentActivity, AboutActivity::class.java)
            //  to start content transition in AboutActivity
            val bundle = ActivityOptions.makeSceneTransitionAnimation(currentActivity).toBundle()
            currentActivity.startActivity(intent, bundle)
        }

        /**
         * Set matching current [Activity] button as checked
         */
        fun setButtonForActivityChecked(bottomNavigationView: BottomNavigationView, currentActivity: Activity) {
            val matchingButton = getMatchingButtonIdForActivity(currentActivity)
            bottomNavigationView.menu.findItem(matchingButton).isChecked = true

        }

        private fun getMatchingButtonIdForActivity(currentActivity: Activity): Int {
            return when (currentActivity) {
                is SettingsActivity -> R.id.settings_button
                is CatalogActivity -> R.id.list_button
                is StatisticsActivity -> R.id.statistics_button
                is AboutActivity -> R.id.about_button
                else -> R.id.list_button    //  because when expression must be exhaustive
            }
        }
    }

}