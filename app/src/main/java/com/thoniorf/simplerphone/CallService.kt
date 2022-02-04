package com.thoniorf.simplerphone

import android.content.Intent
import android.telecom.Call
import android.telecom.InCallService
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class CallService : InCallService() {

    override fun onCallAdded(call: Call?) {
        super.onCallAdded(call)
        if (call != null) {
            runBlocking { launch { Ongoingcall.produceCall(call) } }

            startActivity(Intent())
            Intent(this, CallActivity::class.java).setData(call.details.handle)
                .let(this::startActivity)
        };
    }

    override fun onCallRemoved(call: Call?) {
        super.onCallRemoved(call)
        runBlocking { launch { Ongoingcall.produceCall(null) } }
    }

}