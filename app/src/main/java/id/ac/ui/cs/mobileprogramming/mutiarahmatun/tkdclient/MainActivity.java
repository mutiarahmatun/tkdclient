package id.ac.ui.cs.mobileprogramming.mutiarahmatun.tkdclient;

import id.ac.ui.cs.mobileprogramming.mutiarahmatun.tkd.IFirstRankService;

import android.os.Bundle;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;

import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

    protected static final String TAG = "FirstRankClient";
    private IFirstRankService firstRankService = null;
    private Button bindBtn;
    private Button callBtn;
    private Button unbindBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindBtn = (Button)findViewById(R.id.bindBtn);
        bindBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                String packageName = "id.ac.ui.cs.mobileprogramming" +
                        ".mutiarahmatun.tkd";
                intent.setClassName(packageName, packageName+".FirstRankService");
                bindService(intent, serConn, Context.BIND_AUTO_CREATE);
                bindBtn.setEnabled(false);
                callBtn.setEnabled(true);
                unbindBtn.setEnabled(true);
            }});
        callBtn = (Button)findViewById(R.id.callBtn);
        callBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view) {
                callService();
            }});
        callBtn.setEnabled(false);

        unbindBtn = (Button)findViewById(R.id.unbindBtn);
        unbindBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view) {
                unbindService(serConn);
                bindBtn.setEnabled(true);
                callBtn.setEnabled(false);
                unbindBtn.setEnabled(false);
            }});
        unbindBtn.setEnabled(false);
    }

    private void callService() {
        try {
            String val = firstRankService.getFirstRank();
            Toast.makeText(MainActivity.this, "First rank is " + val,
                    Toast.LENGTH_SHORT).show();
        } catch (RemoteException ee) {
            Log.e("MainActivity", ee.getMessage(), ee);
        }
    }

    private ServiceConnection serConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service)
        {
            Log.v(TAG, "onServiceConnected() called");
            firstRankService = IFirstRankService.Stub.asInterface(service);
            callService();
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.v(TAG, "onServiceDisconnected() called");
            firstRankService = null;
        }
    };
}