package com.thoniorf.simplerphone

import android.telecom.Call
import android.telecom.VideoProfile
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow


object Ongoingcall {
    private val callsFlow = MutableSharedFlow<Call?>()
    val calls = callsFlow.asSharedFlow();

    var call: Call? = null;

    suspend fun produceCall(call: Call?) {
        this.call = call;
        callsFlow.emit(call)
    }

    fun answer() {
        call!!.answer(VideoProfile.STATE_AUDIO_ONLY)
    }

    fun hangup() {
        call!!.disconnect()
    }
}