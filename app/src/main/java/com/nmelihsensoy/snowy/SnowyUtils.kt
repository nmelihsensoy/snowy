package com.nmelihsensoy.snowy

import android.content.SharedPreferences
import android.content.res.Resources

class SnowyUtils {
    fun getTileIconRes(res: Resources, prefs: SharedPreferences, packageName: String): Int{
        return res.getIdentifier(prefs.getString("tile_icon", "ic_baseline_ac_unit_24"),
            "drawable", packageName)
    }

    fun blockTouchScreen(libPath: String, stopTrigger: String){
        ProcessBuilder("su", "-c", "cmd", "statusbar", "collapse",
            ";", "($libPath/blockevent.so", "-t", "-s", stopTrigger,
            "&&", "cmd", "vibrator", "vibrate", "-f", "100", "blockevent)").start()
    }
}