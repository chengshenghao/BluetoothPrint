package com.tcsl.bluetoothprint.bluetooth;

import android.content.Intent;

/**
 * Created by yefeng on 7/1/15.
 * github:yefengfreedom
 */
public interface BtInterface {
    /**
     * start discovery bt device
     *
     */
    void btStartDiscovery(Intent intent);

    /**
     * finish discovery bt device
     *
     */
    void btFinishDiscovery(Intent intent);

    /**
     * bluetooth status changed
     *
     */
    void btStatusChanged(Intent intent);

    /**
     * found bt device
     *
     */
    void btFoundDevice(Intent intent);

    /**
     * device bond status change
     *
     */
    void btBondStatusChange(Intent intent);

    /**
     * pairing bluetooth request
     *
     */
    void btPairingRequest(Intent intent);
}
