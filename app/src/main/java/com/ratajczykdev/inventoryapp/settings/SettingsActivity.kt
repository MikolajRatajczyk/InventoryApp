package com.ratajczykdev.inventoryapp.settings

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.ratajczykdev.inventoryapp.R

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        title = getString(R.string.settings_label)

        //  display the fragment as the main content
        fragmentManager.beginTransaction()
                .replace(android.R.id.content, SettingsFragment())
                .commit()
    }
}
