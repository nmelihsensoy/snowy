package com.nmelihsensoy.snowy

import android.graphics.drawable.Icon
import android.service.quicksettings.TileService
import android.widget.Toast
import androidx.preference.PreferenceManager

class SnowyTileService : TileService() {

    override fun onClick() {
        super.onClick()
        val nativeLibPath = this.applicationContext.applicationInfo.nativeLibraryDir
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val trigger = prefs.getString("stop_trigger", "v_up")

        SnowyUtils().blockTouchScreen(nativeLibPath, trigger!!)
        if (prefs.getBoolean("onstart_alert", true)){
            Toast.makeText(this, getString(R.string.onstart_alert_msg),
                Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStartListening() {
        super.onStartListening()
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val tile = qsTile
        val id = SnowyUtils().getTileIconRes(resources, prefs, packageName)
        tile.label = prefs.getString("tile_title", R.string.app_name.toString())
        tile.icon = Icon.createWithResource(this, id)

        tile.updateTile()
    }
}