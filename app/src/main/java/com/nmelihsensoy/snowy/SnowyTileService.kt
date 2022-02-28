package com.nmelihsensoy.snowy

import android.content.SharedPreferences
import android.graphics.drawable.Icon
import android.service.quicksettings.TileService
import android.widget.Toast
import androidx.preference.PreferenceManager

class SnowyTileService : TileService() {

    lateinit var prefs : SharedPreferences

    override fun onClick() {
        super.onClick()
        val modes = SnowyUtils.initFlags()

        if (prefs.getBoolean("onstart_alert", true)){
            Toast.makeText(this, getString(R.string.onstart_alert_msg),
                Toast.LENGTH_SHORT).show()
            modes.add(SnowyUtils.Companion.Mode.TOAST_ONSTART)
        }
        if (prefs.getBoolean("onstart_vibrate", true))
            modes.add(SnowyUtils.Companion.Mode.VIBRATE_ONSTART)

        if (prefs.getBoolean("onstop_alert", true))
            modes.add(SnowyUtils.Companion.Mode.TOAST_ONSTOP)

        if (prefs.getBoolean("onstop_vibrate", true))
            modes.add(SnowyUtils.Companion.Mode.VIBRATE_ONSTOP)

        SnowyUtils.blockTouchScreen(
            this.applicationContext.applicationInfo.nativeLibraryDir,
            prefs.getString("stop_trigger", "v_up")!!,
            modes,
            getString(R.string.onstop_alert_msg))

        if (modes.contains(SnowyUtils.Companion.Mode.TOAST_ONSTOP)) {
            SnowyUtils.enableToaster(this)
        }
    }

    override fun onStartListening() {
        super.onStartListening()
        prefs = PreferenceManager.getDefaultSharedPreferences(this)

        val tile = qsTile
        tile.label = prefs.getString("tile_title", R.string.app_name.toString())
        tile.icon = Icon.createWithResource(this, prefs.getInt("tile_icon_drawable", 0))
        tile.updateTile()
    }

    override fun onTileAdded() {
        super.onTileAdded()
    }
}