package com.example.andre.bluetooth;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import java.util.UUID;


public class Bluetooth extends AppCompatActivity {

    public int revCount = 0;
    private TextView txtCount;
    static Handler mHandler;
    private static BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private StringBuilder sb = new StringBuilder();
    private static BluetoothDevice mDevice;
    private static UUID myUUID;
    protected static BluetoothSocket mmSocket;



    @Override
    @TargetApi(Build.VERSION_CODES.M)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        Log.d("MainActivity", "HelloWorld");
        txtCount = (TextView) findViewById(R.id.txtCount);
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);

        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);
        }
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        for (BluetoothDevice device: pairedDevices) {
            mDevice = device;
        }
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                mDevice = device;
            }
        }
        myUUID = UUID.fromString("19b10000-e8f2-537e-4f6c-d104768a1214");
        try {
            mmSocket = mDevice.createRfcommSocketToServiceRecord(myUUID);
        } catch (IOException e) {
            Log.e("MainActivity","socketCreationError");
        }

        ConnectThread mConnectThread = new ConnectThread(mDevice);
        mConnectThread.start();
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch(msg.what) {
                    case 1:
                        byte[] readBuf = (byte[]) msg.obj;
                        String strIncom = new String(readBuf, 0, msg.arg1);                 // create string from bytes array
                        sb.append(strIncom);                                                // append string
                        int endOfLineIndex = sb.indexOf("\r\n");                            // determine the end-of-line
                        if (endOfLineIndex > 0) {                                            // if end-of-line,
                            String sbprint = sb.substring(0, endOfLineIndex);               // extract string
                            sb.delete(0, sb.length());                                      // and clear
                            txtCount.setText(sbprint);            // update TextView
                        }
                        Log.d("MainActivity", "...String:"+ sb.toString() +  "Byte:" + msg.arg1 + "...");
                        break;
                }
            }
        };
        ConnectedThread mConnectedThread = new ConnectedThread(mmSocket);
        mConnectedThread.start();
    }

    protected static class ConnectThread extends Thread {
        private final BluetoothDevice mmDevice;
        public ConnectThread(BluetoothDevice device) {
            mmDevice = device;
        }

        public void run() {
            mBluetoothAdapter.cancelDiscovery();
            try {
                Log.i("MainActivity", "Is connetion up? " + mmSocket.isConnected());
                mmSocket.connect();
                Log.i("MainActivity", "Is connetion up? " + mmSocket.isConnected());
            } catch (IOException connectException) {
                try {
                    Log.e("MainActivity", "Socket connect error", connectException);
                    mmSocket.close();
                } catch (IOException closeException) { }
                return;
            }
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }

    protected class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            try {
                Log.e("MainActivity", "Yolo");
                tmpIn = socket.getInputStream();
                Log.e("MainActivity", "BroOh");
            } catch (IOException e) { Log.e("MainActivity", "oh shit: " + e.getStackTrace()); }
                mmInStream = tmpIn;
                Log.e("MainActivity", mmInStream.toString());
        }

        public void run() {
            byte[] buffer = new byte[1024];
            int begin = 0;
            int bytes = 0;
            boolean notAdded = true;
            //while (true) {
                try {
                    if(mmSocket.isConnected()){
                        Log.i("MainActivity", "Socket is connected");
                    }
                    else{
                        mmSocket.connect();
                    }
                    Log.d("MainActivity", Integer.toString(bytes) + 6);
                    bytes = mmInStream.read(buffer, bytes, buffer.length - bytes);
                 //   Log.d("MainActivity", Integer.toString(bytes));
                   if (Integer.valueOf(bytes) == 1 && notAdded) {
                       revCount += 1;
                       txtCount.setText(Integer.toString(revCount));
                       notAdded = false;
                    } else {
                        notAdded = true;
                    }
                } catch (IOException e) {
                    Log.e("MainActivity", "poop ", e);
                }
            //}
        }
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }

    public void countUp(View view) {
        revCount += 1;
        txtCount.setText(Integer.toString(revCount));
        Log.e("MainActivity", "Yoloaf");
    }

}
