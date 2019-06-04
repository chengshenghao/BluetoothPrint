package com.tcsl.bluetoothprint.bluetooth;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tcsl.bluetoothprint.R;
import com.tcsl.bluetoothprint.utils.LocalXml;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 描述: 蓝牙连接对话框
 * <p/>作者：景阳
 * <p/>创建时间: 2017/4/27 14:42
 */
public class BlueToothDialog extends BlueToothDialogFragment implements AdapterView.OnItemClickListener {

    private static final int OPEN_BLUETOOTH_REQUEST = 100;

    @BindView(R.id.iv_status)
    ImageView ivStatus;
    @BindView(R.id.tv_searching)
    TextView tvSearching;
    @BindView(R.id.tv_research)
    TextView tvResearch;
    @BindView(R.id.lv_devices)
    ListView lvDevices;

    private BtDeviceAdapter deviceAdapter;
    private BluetoothAdapter bluetoothAdapter;

    private OnCloseListener onCloseListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initDialog();
        View view = inflater.inflate(R.layout.dialog_bluetooth, container, false);
        ButterKnife.bind(this, view);
        lvDevices.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    private void initData() {
        if (null == deviceAdapter) {
            deviceAdapter = new BtDeviceAdapter(getActivity().getApplicationContext(), null);
        }
        lvDevices.setAdapter(deviceAdapter);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUi();
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
        searchDeviceOrOpenBluetooth();
    }

    private void updateUi() {
    }

    private void init() {
        if (null != bluetoothAdapter) {
            Set<BluetoothDevice> deviceSet = bluetoothAdapter.getBondedDevices();
            if (null != deviceSet) {
                deviceAdapter.addDevices(new ArrayList<BluetoothDevice>(deviceSet));
            }
        }
        if (!BtUtil.isOpen(bluetoothAdapter)) {
            tvSearching.setText("未连接蓝牙打印机");
            tvResearch.setText("系统蓝牙已关闭,点击开启");
            ivStatus.setImageResource(R.drawable.ic_bluetooth_off);
        } else {
            if (!BtHelper.isBondPrinter(getActivity(), bluetoothAdapter)) {
                tvSearching.setText("正在绑定打印机...");
                tvResearch.setText("");
                //未绑定蓝牙打印机器
//                tvSearching.setText("未连接蓝牙打印机");
//                tvResearch.setText("点击后搜索蓝牙打印机");
                ivStatus.setImageResource(R.drawable.ic_bluetooth_off);
            } else {
                //已绑定蓝牙设备
                tvSearching.setText(getPrinterName() + "已连接");
                String blueAddress = LocalXml.getDefaultBluethoothDeviceAddress();
                if (TextUtils.isEmpty(blueAddress)) {
                    blueAddress = "点击后搜索蓝牙打印机";
                }
                tvResearch.setText(blueAddress);
                ivStatus.setImageResource(R.drawable.ic_bluetooth_device_connected);
                deviceAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * search device, if bluetooth is closed, open it
     */
    private void searchDeviceOrOpenBluetooth() {
        if (!BtUtil.isOpen(bluetoothAdapter)) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, OPEN_BLUETOOTH_REQUEST);
        } else {
            BtUtil.searchDevices(bluetoothAdapter);
        }
    }

    private String getPrinterName() {
        String dName = LocalXml.getDefaultBluetoothDeviceName(getActivity());
        if (TextUtils.isEmpty(dName)) {
            dName = "未知设备";
        }
        return dName;
    }

    @Override
    public void onStop() {
        super.onStop();
        BtUtil.cancelDiscovery(bluetoothAdapter);
    }

    /**
     * 初始化Dialog属性
     */
    protected void initDialog() {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().getDecorView().setPadding(0, 0, 0, 0);
        getDialog().getWindow().setWindowAnimations(R.style.anim_dialog_right_right);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().setGravity(Gravity.CENTER);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog_MinWidth);
    }

    @OnClick({R.id.group_search, R.id.tv_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.group_search:
                searchDeviceOrOpenBluetooth();
                break;
            case R.id.tv_close:
                if (onCloseListener != null) {
                    onCloseListener.onClose();
                }
                dismiss();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OPEN_BLUETOOTH_REQUEST && resultCode == Activity.RESULT_OK) {
            init();
        } else if (requestCode == OPEN_BLUETOOTH_REQUEST && resultCode == Activity.RESULT_CANCELED) {
            Log.e("BlueToothDialog", "您已拒绝使用蓝牙");
            dismissAllowingStateLoss();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null == deviceAdapter) {
            return;
        }
        final BluetoothDevice bluetoothDevice = deviceAdapter.getItem(position);
        if (null == bluetoothDevice) {
            return;
        }
        new AlertDialog.Builder(getActivity())
                .setTitle("绑定" + getPrinterName(bluetoothDevice.getName()) + "?")
                .setMessage("点击确认绑定蓝牙设备")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            BtUtil.cancelDiscovery(bluetoothAdapter);
                            LocalXml.setDefaultBluetoothDeviceAddress(getActivity().getApplicationContext(), bluetoothDevice.getAddress());
                            LocalXml.setDefaultBluetoothDeviceName(getActivity().getApplicationContext(), bluetoothDevice.getName());
                            if (null != deviceAdapter) {
                                deviceAdapter.setConnectedDeviceAddress(bluetoothDevice.getAddress());
                            }
                            if (bluetoothDevice.getBondState() == BluetoothDevice.BOND_BONDED) {
                                init();
//                                goPrinterSetting();
                            } else {
                                Method createBondMethod = BluetoothDevice.class.getMethod("createBond");
                                createBondMethod.invoke(bluetoothDevice);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            LocalXml.setDefaultBluetoothDeviceAddress(getActivity().getApplicationContext(), "");
                            LocalXml.setDefaultBluetoothDeviceName(getActivity().getApplicationContext(), "");
                            Log.e("BlueToothDialog", "蓝牙绑定失败,请重试");
                        }
                    }
                })
                .create()
                .show();
    }

    private String getPrinterName(String dName) {
        if (TextUtils.isEmpty(dName)) {
            dName = "未知设备";
        }
        return dName;
    }

    @Override
    public void btStartDiscovery(Intent intent) {
        tvSearching.setText("正在搜索蓝牙设备…");
        tvResearch.setText("");
    }

    @Override
    public void btFinishDiscovery(Intent intent) {
        tvSearching.setText("搜索完成");
        tvResearch.setText("点击重新搜索");
    }

    @Override
    public void btStatusChanged(Intent intent) {
        init();
    }

    @Override
    public void btFoundDevice(Intent intent) {
        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        if (null != deviceAdapter && device != null) {
            deviceAdapter.addDevices(device);
        }
    }

    @Override
    public void btBondStatusChange(Intent intent) {
        init();
        if (BtHelper.isBondPrinter(getActivity().getApplicationContext(), bluetoothAdapter)) {
//            goPrinterSetting();
        }
    }

    @Override
    public void btPairingRequest(Intent intent) {
        Log.e("BlueToothDialog", "正在绑定打印机");
    }

    public void setOnCloseListener(OnCloseListener onCloseListener) {
        this.onCloseListener = onCloseListener;
    }

    public interface OnCloseListener {
        void onClose();
    }
}
