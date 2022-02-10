package com.thoniorf.simplerphone

import android.Manifest.permission.*
import android.app.role.RoleManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.telecom.PhoneAccountHandle
import android.telecom.TelecomManager
import android.telecom.VideoProfile
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import androidx.core.net.toUri
import com.thoniorf.simplerphone.databinding.ActivityMainBinding


@RequiresApi(Build.VERSION_CODES.Q)
class DialerActivity : AppCompatActivity() {
    private lateinit var phoneNumber: String;

    private lateinit var binding: ActivityMainBinding;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater);
        val view = binding.root;
        setContentView(view);
        binding.editPhoneNumber.setText(intent.data?.schemeSpecificPart ?: "")

    }

    override fun onStart() {
        super.onStart()
        requestRole();
        binding.editPhoneNumber.setOnEditorActionListener { _, _, _ ->
            makeCall()
            true
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_PERMISSION && PERMISSION_GRANTED in grantResults){
            makeCall();
        }
    }

    private fun makeCall() {
        if(checkSelfPermission(CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
            val uri ="tel:${binding.editPhoneNumber.text}".toUri();
            val extras = Bundle()
            extras.putInt(TelecomManager.EXTRA_START_CALL_WITH_VIDEO_STATE, VideoProfile.STATE_AUDIO_ONLY);
            val telecomManager = getSystemService(TelecomManager::class.java)
            val callCapablePhoneAccounts = telecomManager.callCapablePhoneAccounts
            val phoneAccountHandle:PhoneAccountHandle = callCapablePhoneAccounts[0];
            extras.putParcelable(TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE, phoneAccountHandle)
            telecomManager.placeCall(uri, extras);

        } else {
            requestPermissions(arrayOf(CALL_PHONE,MANAGE_OWN_CALLS, READ_PHONE_STATE),REQUEST_PERMISSION);
        }
    }


    private fun requestRole() {
        val roleManager: RoleManager = this.getSystemService(ROLE_SERVICE) as RoleManager;
        val isDialerRoleAvailable = roleManager.isRoleAvailable(RoleManager.ROLE_DIALER);
        val isDialerRoleHeld = roleManager.isRoleHeld(RoleManager.ROLE_DIALER);
        if(isDialerRoleAvailable && !isDialerRoleHeld) {
            launchRequestDialerRoleIntent(roleManager)
        }
    }

    private fun launchRequestDialerRoleIntent(roleManager: RoleManager) {
        val intent: Intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_DIALER);
        val resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    // for now nothing
                }
            };
        resultLauncher.launch(intent);
    }

    companion object {
        const val REQUEST_PERMISSION = 0;
    }
}