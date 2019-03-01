package dev.ratajczyk.inventoryapp

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import dev.ratajczyk.inventoryapp.about.AboutFragment
import dev.ratajczyk.inventoryapp.catalog.CatalogFragment
import dev.ratajczyk.inventoryapp.dataexport.PdfExportFragment
import dev.ratajczyk.inventoryapp.settings.SettingsActivity
import dev.ratajczyk.inventoryapp.statistics.LoadingFragmentWithArgs
import dev.ratajczyk.inventoryapp.statistics.StatisticsFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), LoadingFragmentWithArgs {

    companion object {
        /**
         * Identifier for WRITE permission request
         * [onRequestPermissionsResult] gets the result of the request
         */
        private const val PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE_ID = 1000
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        configureBottomNavigation()

        if (savedInstanceState == null) {
            askWriteStoragePermission()
            tapOnCatalogButton()
        }
    }

    private fun askWriteStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE_ID)
        }
    }

    private fun configureBottomNavigation() {
        bottom_navigation_view.setOnNavigationItemSelectedListener { currentItem: MenuItem ->
            when (currentItem.itemId) {
                R.id.catalog_button -> loadFragment(CatalogFragment())
                R.id.statistics_button -> loadFragment(StatisticsFragment())
                R.id.export_button -> loadFragment(PdfExportFragment())
                R.id.about_button -> loadFragment(AboutFragment())
            }
            true
        }
    }

    private fun loadFragment(fragmentToLoad: Fragment) {
        when (isFragmentLoaded()) {
            true -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragmentToLoad)
                        .commit()
            }
            false -> {
                supportFragmentManager.beginTransaction()
                        .add(R.id.fragment_container, fragmentToLoad)
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

    /**
     * Behaves the same as tapping on the Catalog button
     */
    private fun tapOnCatalogButton() {
        bottom_navigation_view.selectedItemId = R.id.catalog_button
    }

    /**
     * This method is being invoked when user responds to permission request
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE_ID) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //  permission granted
            } else {
                //  permission denied
                showPermissionDeniedDialog()
            }
        }
    }

    private fun showPermissionDeniedDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(getString(R.string.dialog_description_app_will_not_work))
                .setTitle(getString(R.string.dialog_title_permission_denied))

        //  Handle action when user presses the button
        builder.setPositiveButton(getString(R.string.exit))
        { _: DialogInterface, _: Int ->
            finish()
        }

        val createdDialog = builder.create()
        createdDialog.show()
    }

    /**
     * Interface implementation required to communicate Fragments
     */
    override fun loadFragmentWithArgs(fragment: Fragment, bundle: Bundle) {
        fragment.arguments = bundle
        loadFragment(fragment)
    }

    /**
     * Modifies appbar to have actions
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_main_appbar_actions, menu)
        return true
    }

    /**
     * Handles clicks on appbar's actions
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.activity_main_appbar_actions_settings_action -> {
                startSettingsActivity()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun startSettingsActivity() {
        val intent = Intent(this@MainActivity, SettingsActivity::class.java)
        startActivity(intent)
    }
}
