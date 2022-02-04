package com.thoniorf.simplerphone

import android.Manifest.permission.CALL_PHONE
import android.app.role.RoleManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
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


    }

    override fun onStart() {
        super.onStart()
        requestRole();
        binding.callBtn.setOnClickListener { makeCall() }
        phoneNumber = intent.data?.schemeSpecificPart ?: "";
        if(phoneNumber.isNotEmpty()) {
            binding.phoneNumber.text = phoneNumber
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
            val uri ="tel:${binding.phoneNumber.text}".toUri();
            startActivity(Intent(Intent.ACTION_DIAL, uri));

        } else {
            requestPermissions(arrayOf(CALL_PHONE),REQUEST_PERMISSION);
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