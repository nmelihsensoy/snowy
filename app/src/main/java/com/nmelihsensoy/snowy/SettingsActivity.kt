package com.nmelihsensoy.snowy

import android.content.ComponentName
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.ListPreference
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
            updatePrefUiIcon()
        }

        private var listener: SharedPreferences.OnSharedPreferenceChangeListener =
            SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
                when (key) {
                    "tile_icon" -> updatePrefUiIcon()
                    "onstop_alert" -> updateToaster()
                }
            }

        private fun updatePrefUiIcon(){
            val prefCat: ListPreference? = findPreference("tile_icon") as ListPreference?
            val id = SnowyUtils.getTileIconRes(resources, prefs, requireActivity().packageName)
            prefCat?.setIcon(id)

            with (prefs.edit()) {
                putInt("tile_icon_drawable", id)
                apply()
            }
        }

        private fun updateToaster(){
            val receiver = context?.let { ComponentName(it.applicationContext, SnowyToaster::class.java) }
            val pm = context?.applicationContext?.packageManager

            val onstopAlertVal = prefs.getBoolean("onstop_alert", false)
            if (onstopAlertVal){
                if (receiver != null) {
                    pm?.setComponentEnabledSetting(
                        receiver,
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP)
                }
            }else{
                if (receiver != null) {
                    pm?.setComponentEnabledSetting(
                        receiver,
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP)
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