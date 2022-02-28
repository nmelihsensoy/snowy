package com.nmelihsensoy.snowy

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.telephony.TelephonyManager
import android.widget.Toast


class SnowyToaster: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.extras != null) {
            Toast.makeText(context, intent.getStringExtra("MSG"), Toast.LENGTH_SHORT).show()
            SnowyUtils.disableToaster(context)
        }
    }
}
