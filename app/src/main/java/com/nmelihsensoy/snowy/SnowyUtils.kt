package com.nmelihsensoy.snowy

import android.content.ComponentName
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Build
import java.util.*

class SnowyUtils {
    companion object {
        enum class Mode {
            VIBRATE_ONSTART, VIBRATE_ONSTOP, TOAST_ONSTART, TOAST_ONSTOP
        }
        private val collapseCmd = mutableListOf("cmd", "statusbar", "collapse")
        private val vibrateCmd = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) {
            mutableListOf("cmd", "vibrator", "vibrate", "-f", "100", "snowy")
        } else {
            mutableListOf("cmd", "vibrator_manager", "synced", "oneshot", "100", "snowy")
        }
        private val stopAlertCmd = mutableListOf("am", "broadcast", "-a",
            "com.nmelihsensoy.snowy.TOASTER", "-n", "com.nmelihsensoy.snowy/.SnowyToaster",
            "-e", "MSG")

        fun getTileIconRes(res: Resources, prefs: SharedPreferences, packageName: String): Int{
            return res.getIdentifier(prefs.getString("tile_icon", "ic_baseline_ac_unit_24"),
                "drawable", packageName)
        }

        fun initFlags(): EnumSet<Mode>{
            return EnumSet.noneOf(Mode::class.java)
        }

        fun blockTouchScreen(libPath: String, stopTrigger: String, flags: EnumSet<Mode>,
                             onStopMsg: String = ""){
            var blockCmd = mutableListOf("su", "-c")
            val blockeventCmd = mutableListOf("$libPath/blockevent.so", "-d", "0", "-s", stopTrigger, "-v", "0")
            blockCmd.addAll(collapseCmd)
            blockCmd.add(";")

            if (flags.contains(Mode.VIBRATE_ONSTART)) {
                blockCmd.addAll(vibrateCmd)
                blockCmd.add(";")
            }

            if (flags.contains(Mode.VIBRATE_ONSTOP)) {
                blockCmd.add("(")
                blockCmd.addAll(blockeventCmd)
                blockCmd.add("&&")
                blockCmd.addAll(vibrateCmd)
                blockCmd.add(")")
            }else {
                blockCmd.addAll(blockeventCmd)
            }

            if (flags.contains(Mode.TOAST_ONSTOP)) {
                blockCmd.add(";")
                blockCmd.addAll(stopAlertCmd)
                blockCmd.add("\"$onStopMsg\"")
            }
            ProcessBuilder(blockCmd).start()
        }

        private fun enableToaster(cn: ComponentName, pm: PackageManager){
            pm.setComponentEnabledSetting(
                cn,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP)
        }

        fun enableToaster(cxt: Context){
            val receiver = ComponentName(cxt.applicationContext, SnowyToaster::class.java)
            val pm = cxt.applicationContext?.packageManager
            if (pm != null) {
                enableToaster(receiver, pm)
            }
        }

        private fun disableToaster(cn: ComponentName, pm: PackageManager){
            pm.setComponentEnabledSetting(
                cn,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP)
        }

        fun disableToaster(cxt: Context){
            val receiver = ComponentName(cxt.applicationContext, SnowyToaster::class.java)
            val pm = cxt.applicationContext?.packageManager
            if (pm != null) {
                disableToaster(receiver, pm)
            }
        }

    }
}