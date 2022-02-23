package com.nmelihsensoy.snowy

import android.graphics.drawable.Icon
import android.service.quicksettings.TileService
import android.widget.Toast
import androidx.preference.PreferenceManager

class SnowyTileService : TileService() {

    override fun onClick() {
        super.onClick()
        Runtime.getRuntime().exec("cmd statusbar collapse")
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        if (prefs.getBoolean("onstart_alert", true)){
            Toast.makeText(this, getString(R.string.onstart_alert_msg), Toast.LENGTH_SHORT).show()
        }

        if (prefs.getBoolean("onstart_vibrate", false)){
            Runtime.getRuntime().exec("cmd vibrator vibrate -f 100 snowy")
        }
    }

    override fun onStartListening() {
        super.onStartListening()
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val tile = qsTile
        val id = resources.getIdentifier(prefs.getString("tile_icon", "ic_baseline_ac_unit_24"),
            "drawable", packageName)
        tile.label = prefs.getString("tile_title", R.string.app_name.toString())
        tile.icon = Icon.createWithResource(this, id)

        tile.updateTile()
    }
}