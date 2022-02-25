package com.nmelihsensoy.snowy

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

//am broadcast -a com.nmelihsensoy.snowy.TOASTER -n com.nmelihsensoy.snowy/.SnowyToaster
class SnowyToaster: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Toast.makeText(context, "Snowy: blocking stopped", Toast.LENGTH_SHORT).show()
    }
}
