package com.tcsl.bluetoothprint;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tcsl.bluetoothprint.bluetooth.BlueToothDialog;
import com.tcsl.bluetoothprint.print.OnPrinterListener;
import com.tcsl.bluetoothprint.print.QueuePrintManager;
import com.tcsl.bluetoothprint.utils.LocalXml;
import com.tcsl.bluetoothprint.utils.Tip;

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

    /**
     * 蓝牙连接对话框
     *
     * @param view
     */
    public void showBlueToothDialog(View view) {
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

    /**
     * 测试打印
     *
     * @param view
     */
    public void print(View view) {
        if (QueuePrintManager.getInstance().isPrinting()) {
//                showConfirmDialog("打印机正在工作，请稍后", null);
        } else {
            testPrint();
        }
    }

    private void testPrint() {
        QueuePrintManager.getInstance().testPrint(new PrintSettingModel().getTestPrintBean(null, "服务商子商户"
                ,"","", "http://alpha.wuuxiang.com/i5xwxplus/q/crm7/center/c.html",
                "听到叫号请到迎宾台，过号请重新取号", true)
                , new OnPrinterListener() {
                    @Override
                    public void onDeviceNotBound() {
                        Tip.showShort("未绑定可用打印机");
                    }

                    @Override
                    public void onConnectFailed() {
                        Tip.showShort("打印机连接失败");
                    }

                    @Override
                    public void onPrintError(String msg) {
                        Tip.showShort(msg);
                    }
                });
    }

}
