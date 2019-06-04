package com.tcsl.bluetoothprint;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.tcsl.bluetoothprint.bluetooth.BlueToothDialog;
import com.tcsl.bluetoothprint.utils.LocalXml;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private BlueToothDialog blueToothDialog;
    @BindView(R.id.tv_blue_address)
    public TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    public void showBlueToothDialog(View view){
        if (blueToothDialog == null) {
            blueToothDialog = new BlueToothDialog();
            blueToothDialog.setOnCloseListener(new BlueToothDialog.OnCloseListener() {
                @Override
                public void onClose() {
                    textView.setText(LocalXml.getDefaultBluethoothDeviceAddress());
                }
            });
        }
        blueToothDialog.show(getSupportFragmentManager(), "BlueToothDialog");
    }
}
