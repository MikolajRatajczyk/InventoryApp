package com.ratajczykdev.inventoryapp.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.ratajczykdev.inventoryapp.R

/**
 * Loads preferences from an XML file resource
 *
 * @author Mikolaj Ratajczyk <mikolaj.ratajczyk(AT)gmail.com>
 */
class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(p0: Bundle?, p1: String?) {

        //  Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences)
    }

}
