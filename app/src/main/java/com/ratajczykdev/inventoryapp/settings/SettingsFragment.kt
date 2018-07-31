package com.ratajczykdev.inventoryapp.settings

import android.os.Bundle
import android.preference.PreferenceFragment
import com.ratajczykdev.inventoryapp.R

/**
 * Loads preferences from an XML file resource
 *
 * @author Mikolaj Ratajczyk <mikolaj.ratajczyk@gmail.com>
 */
class SettingsFragment : PreferenceFragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //  Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences)
    }

}
