package com.android.battlenoleproject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;
import android.os.Message;

public class PlayHost extends Activity {

    private boolean CONTINUE_READ_WRITE = true;
    private static final int MESSAGE_READ = 9999;

    public static final int STATE_NONE = 0;       // we're doing nothing
    public static final int STATE_LISTEN = 1;     // now listening for incoming connections
    public static final int STATE_CONNECTING = 2; // now initiating an outgoing connection
    public static final int STATE_CONNECTED = 3;  // now connected to a remote device



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        registerReceiver(discoveryResult, new IntentFilter(BluetoothDevice.ACTION_FOUND));

        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if(adapter != null && adapter.isDiscovering()){
            adapter.cancelDiscovery();
        }
        adapter.startDiscovery();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try{unregisterReceiver(discoveryResult);}catch(Exception e){e.printStackTrace();}
        if(socket != null){
            try{
                is.close();
                os.close();
                socket.close();
                CONTINUE_READ_WRITE = false;
            }catch(Exception e){}
        }
    }

    private static final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if  (msg.what == MESSAGE_READ) {
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);

            }

            else if (msg.what == MESSAGE_READ) {



            }
        }
    };



    private BluetoothSocket socket;
    private OutputStreamWriter os;
    private InputStream is;
    private BluetoothDevice remoteDevice;
    private BroadcastReceiver discoveryResult = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            android.util.Log.e("TrackingFlow", "WWWTTTFFF");
            unregisterReceiver(this);
            remoteDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            new Thread(reader).start();

        }
    };

    private Runnable reader = new Runnable() {

        @Override
        public void run() {
            try {
                android.util.Log.e("TrackingFlow", "Found: " + remoteDevice.getName());
                UUID uuid = UUID.fromString("4e5d48e0-75df-11e3-981f-0800200c9a66");
                socket = remoteDevice.createRfcommSocketToServiceRecord(uuid);
                socket.connect();
                android.util.Log.e("TrackingFlow", "Connected...");
                os = new OutputStreamWriter(socket.getOutputStream());
                is = socket.getInputStream();
                android.util.Log.e("TrackingFlow", "WWWTTTFFF34243");
                new Thread(writer).start();
                android.util.Log.e("TrackingFlow", "WWWTTTFFF3wwgftggggwww4243: " + CONTINUE_READ_WRITE);
                int bufferSize = 1024;
                int bytesRead = -1;
                byte[] buffer = new byte[bufferSize];
                //Keep reading the messages while connection is open...
                while(CONTINUE_READ_WRITE){
                    android.util.Log.e("TrackingFlow", "WWWTTTFFF3wwwww4243");
                    final StringBuilder sb = new StringBuilder();
                    bytesRead = is.read(buffer);
                    if (bytesRead != -1) {
                        String result = "";
                        while ((bytesRead == bufferSize) && (buffer[bufferSize-1] != 0)){
                            result = result + new String(buffer, 0, bytesRead - 1);
                            bytesRead = is.read(buffer);
                        }
                        result = result + new String(buffer, 0, bytesRead - 1);
                        sb.append(result);
                    }
                    mHandler.obtainMessage(MESSAGE_READ, bytesRead, -1, buffer).sendToTarget();


                    android.util.Log.e("TrackingFlow", "Read: " + sb.toString());

                }
            } catch (IOException e) {e.printStackTrace();}
        }



    };


    private Runnable writer = new Runnable() {

        @Override
        public void run() {

            int index = 0;
            while (CONTINUE_READ_WRITE) {
                try {
                    os.write("Message From Client" + (index++) + "\n");
                    os.flush();
                    Thread.sleep(2000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };





}
