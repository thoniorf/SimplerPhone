package com.thoniorf.simplerphone

import android.os.Build
import android.os.Bundle
import android.telecom.Call
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.coroutineScope
import com.thoniorf.simplerphone.databinding.ActivityCallBinding
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.S)
class CallActivity(): AppCompatActivity() {
    private lateinit var phoneNumber: String;
    private lateinit var binding: ActivityCallBinding;

    init {
        lifecycle.coroutineScope.launch {
            // Listen for calls  updates
            Ongoingcall.calls.collect {
                updateUi(it)
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        phoneNumber = intent.data?.schemeSpecificPart ?: "";
        binding = ActivityCallBinding.inflate(layoutInflater);
        val view = binding.root;
        setContentView(view);
    }


    override fun onStart() {
        super.onStart()
        binding.number.text = phoneNumber;

        binding.answer.setOnClickListener { Ongoingcall.answer() }
        binding.hangup.setOnClickListener { Ongoingcall.hangup() }
    }

    fun updateUi(call: Call?) {
        if(call == null) return
        binding.callerDisplayName.text = call.details?.callerDisplayName

        binding.answer.isVisible = call.details.state === Call.STATE_RINGING
        binding.hangup.isVisible = call.details.state in listOf(
            Call.STATE_DIALING,
            Call.STATE_RINGING,
            Call.STATE_ACTIVE
        )
    }
}