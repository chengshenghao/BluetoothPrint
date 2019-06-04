package com.tcsl.bluetoothprint.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.text.TextUtils;

import com.tcsl.bluetoothprint.utils.LocalXml;

import java.util.Set;


/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/4/27 16:30
 */
public class BtHelper {

    public static boolean isBondPrinter(Context mContext, BluetoothAdapter bluetoothAdapter) {
        if (!BtUtil.isOpen(bluetoothAdapter)) {
            return false;
        }
        String defaultBluetoothDeviceAddress = LocalXml.getDefaultBluethoothDeviceAddress();
        if (TextUtils.isEmpty(defaultBluetoothDeviceAddress)) {
            return false;
        }
        Set<BluetoothDevice> deviceSet = bluetoothAdapter.getBondedDevices();
        if (deviceSet == null || deviceSet.isEmpty()) {
            return false;
        }
        for (BluetoothDevice bluetoothDevice : deviceSet) {
            if (bluetoothDevice.getAddress().equals(defaultBluetoothDeviceAddress)) {
                return true;
            }
        }
        return false;

    }

}
