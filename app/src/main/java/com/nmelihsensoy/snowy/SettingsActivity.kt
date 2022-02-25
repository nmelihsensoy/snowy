package com.nmelihsensoy.snowy

import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_action, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //val id = item.itemId
        Toast.makeText(this, "About:", Toast.LENGTH_LONG).show()

        return super.onOptionsItemSelected(item)
    }

    class SettingsFragment : PreferenceFragmentCompat() {

        lateinit var prefs: SharedPreferences

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
            prefs = PreferenceManager.getDefaultSharedPreferences(this.requireContext())
            updatePrefIcon()
        }

        private fun updatePrefIcon(){
            val prefCat: ListPreference? = findPreference("tile_icon") as ListPreference?
            val id = SnowyUtils().getTileIconRes(resources, prefs, requireActivity().packageName)
            prefCat?.setIcon(id)
        }

        private var listener: SharedPreferences.OnSharedPreferenceChangeListener =
            SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
                when (key) {
                    "tile_icon" ->{
                        updatePrefIcon()
                    }
                    "tile_title" ->{

                    }
                }
            }

        override fun onResume() {
            super.onResume()
            prefs.registerOnSharedPreferenceChangeListener(listener)
        }

        override fun onPause() {
            super.onPause()
            prefs.unregisterOnSharedPreferenceChangeListener(listener)
        }
    }
}