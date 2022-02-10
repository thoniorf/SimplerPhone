package com.thoniorf.simplerphone

import android.os.Build
import android.telecom.*
import android.telecom.Connection.PROPERTY_SELF_MANAGED
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.N_MR1)
class CallConnectionService: ConnectionService() {
    override fun onCreateIncomingConnection(
        connectionManagerPhoneAccount: PhoneAccountHandle?,
        request: ConnectionRequest?
    ): Connection {
        return super.onCreateIncomingConnection(connectionManagerPhoneAccount, request)
    }

    override fun onCreateIncomingConnectionFailed(
        connectionManagerPhoneAccount: PhoneAccountHandle?,
        request: ConnectionRequest?
    ) {
        super.onCreateIncomingConnectionFailed(connectionManagerPhoneAccount, request)
    }

    override fun onCreateOutgoingConnectionFailed(
        connectionManagerPhoneAccount: PhoneAccountHandle?,
        request: ConnectionRequest?
    ) {
        super.onCreateOutgoingConnectionFailed(connectionManagerPhoneAccount, request)
    }


    override fun onCreateOutgoingConnection(
        connectionManagerPhoneAccount: PhoneAccountHandle?,
        request: ConnectionRequest?
    ): Connection {
        val connection:CallConnection =
            super.onCreateOutgoingConnection(connectionManagerPhoneAccount, request) as CallConnection;
        connection.connectionProperties = PROPERTY_SELF_MANAGED;
        connection.videoState = VideoProfile.STATE_AUDIO_ONLY;
        return connection
    }
}