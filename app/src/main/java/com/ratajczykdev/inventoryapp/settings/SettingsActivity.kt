package com.ratajczykdev.inventoryapp.settings

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.ratajczykdev.inventoryapp.R

/**
 * Displays Fragment with preferences
 *
 * @author Mikolaj Ratajczyk <mikolaj.ratajczyk@gmail.com>
 */
class SettingsActivity : AppCompatActivity() {
    //  TODO: support back arrow

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        title = getString(R.string.settings_label)

        //  display the fragment as settings_view
        fragmentManager.beginTransaction()
                .replace(R.id.settings_view, SettingsFragment())
                .commit()
    }

}
