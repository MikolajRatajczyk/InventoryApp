package com.ratajczykdev.inventoryapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.ratajczykdev.inventoryapp.about.AboutFragment
import com.ratajczykdev.inventoryapp.catalog.CatalogFragment
import com.ratajczykdev.inventoryapp.settings.SettingsActivity
import com.ratajczykdev.inventoryapp.statistics.LoadingFragmentWithArgs
import com.ratajczykdev.inventoryapp.statistics.StatisticsFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), LoadingFragmentWithArgs {


    /**
     * Identifier for WRITE permissions request
     * The callback method gets the result of the request
     */
    private val PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE_ID = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            askWriteStoragePermission()
            configureBottomNavigation()
            tapOnCatalogButton()
        }
    }

    private fun configureBottomNavigation() {
        bottom_navigation_view.setOnNavigationItemSelectedListener { currentItem: MenuItem ->
            when (currentItem.itemId) {
                R.id.about_button -> loadFragment(AboutFragment())
                R.id.catalog_button -> loadFragment(CatalogFragment())
                R.id.settings_button -> startSettingsActivity()
                R.id.statistics_button -> loadFragment(StatisticsFragment())
            }
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        when (isFragmentLoaded()) {
            true -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .commit()
            }
            false -> {
                supportFragmentManager.beginTransaction()
                        .add(R.id.fragment_container, fragment)
                        .commit()
            }
        }
    }


    private fun isFragmentLoaded(): Boolean {
        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        return when (fragment) {
            null -> false
            else -> true
        }
    }

    private fun startSettingsActivity() {
        val intent = Intent(this@MainActivity, SettingsActivity::class.java)
        startActivity(intent)
    }

    private fun askWriteStoragePermission() {
        val WRITE_EXTERNAL_STORAGE_PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE

        if (checkSelfPermission(WRITE_EXTERNAL_STORAGE_PERMISSION) != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE_PERMISSION)) {
                //  TODO: add explanation for a user (asynchronously)
            }
            requestPermissions(arrayOf(WRITE_EXTERNAL_STORAGE_PERMISSION), PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE_ID)
        }
    }

    /**
     * Behaves the same as tapping on the Catalog button
     */
    private fun tapOnCatalogButton() {
        bottom_navigation_view.selectedItemId = R.id.catalog_button
    }

    /**
     * Interface implementation required for communicate Fragments
     */
    override fun loadFragmentWithArgs(fragment: Fragment, bundle: Bundle) {
        fragment.arguments = bundle
        if (isFragmentLoaded()) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit()
        } else {
            supportFragmentManager.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit()
        }
    }


    //  TODO: show sorting options on appbar
    //  TODO: move settings button to appbar
}
