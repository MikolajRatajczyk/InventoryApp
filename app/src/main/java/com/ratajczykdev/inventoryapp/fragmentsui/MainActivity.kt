package com.ratajczykdev.inventoryapp.fragmentsui

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.ratajczykdev.inventoryapp.R
import com.ratajczykdev.inventoryapp.settings.SettingsActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            configureBottomNavigation()
        }
    }

    private fun configureBottomNavigation() {
        bottom_navigation_view.setOnNavigationItemSelectedListener { currentItem: MenuItem ->
            when (currentItem.itemId) {
            //  TODO: complete for all cases
            //  TODO: make universal loader for all Fragments
                R.id.about_button -> loadAboutFragment()
                R.id.catalog_button -> loadCatalogFragment()
                R.id.settings_button -> startSettingsActivity()

            }
            true
        }
    }

    private fun loadAboutFragment() {
        val aboutFragment = AboutFragment()
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction()
                .add(R.id.fragment_container, aboutFragment)
                .commit()
    }

    private fun loadCatalogFragment() {
        val catalogFragment = CatalogFragment();
        val fragmentManager = supportFragmentManager
        //  TODO: consider replace instead of add
        fragmentManager.beginTransaction()
                .add(R.id.fragment_container, catalogFragment)
                .commit()
    }

    private fun startSettingsActivity() {
        val intent = Intent(this@MainActivity, SettingsActivity::class.java)
        startActivity(intent)
    }


}
