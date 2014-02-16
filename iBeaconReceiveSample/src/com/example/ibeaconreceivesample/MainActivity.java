package com.example.ibeaconreceivesample;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	private BluetoothAdapter mBluetoothAdapter;
	/** BLE 機器検索のタイムアウト(ミリ秒) */
	private static final long SCAN_PERIOD = 10000;
	private static final String TAG = "MainActivity";
	Handler mHandler = new Handler(); // (1)
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final BluetoothManager bluetoothManager =
		        (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
		mBluetoothAdapter = bluetoothManager.getAdapter();
		
		Button btn = (Button)findViewById(R.id.button1);
		btn.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			    mHandler.postDelayed(new Runnable() {
			        @Override
			        public void run() {
			            // タイムアウト
			        	Log.d(TAG, "タイムアウト");
			            mBluetoothAdapter.stopLeScan(mLeScanCallback);
			        }
			    }, SCAN_PERIOD);
			 
			    // スキャン開始
			    mBluetoothAdapter.startLeScan(mLeScanCallback);
			}
		} );
		
		
	}

	private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
	    @Override
	    public void onLeScan(final BluetoothDevice device, int rssi,byte[] scanRecord) {
	         Log.d(TAG, "receive!!!");
	         getScanData(scanRecord);
	         Log.d(TAG, "device name:"+device.getName() );
	         Log.d(TAG, "device address:"+device.getAddress() );
	    }
	    
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private void getScanData( byte[] scanRecord ){
		if(scanRecord.length > 30)
		{
		    if((scanRecord[5] == (byte)0x4c) && (scanRecord[6] == (byte)0x00) &&
		       (scanRecord[7] == (byte)0x02) && (scanRecord[8] == (byte)0x15))
		    {
		            String uuid = Integer.toHexString(scanRecord[9] & 0xff) 
		            + Integer.toHexString(scanRecord[10] & 0xff)
		            + Integer.toHexString(scanRecord[11] & 0xff)
		            + Integer.toHexString(scanRecord[12] & 0xff)
		            + "-"
		            + Integer.toHexString(scanRecord[13] & 0xff)
		            + Integer.toHexString(scanRecord[14] & 0xff)
		            + "-"
		            + Integer.toHexString(scanRecord[15] & 0xff)
		            + Integer.toHexString(scanRecord[16] & 0xff)
		            + "-"
		            + Integer.toHexString(scanRecord[17] & 0xff)
		            + Integer.toHexString(scanRecord[18] & 0xff)
		            + "-"
		            + Integer.toHexString(scanRecord[19] & 0xff)
		            + Integer.toHexString(scanRecord[20] & 0xff)
		            + Integer.toHexString(scanRecord[21] & 0xff)
		            + Integer.toHexString(scanRecord[22] & 0xff)
		            + Integer.toHexString(scanRecord[23] & 0xff)
		            + Integer.toHexString(scanRecord[24] & 0xff);

		            String major = Integer.toHexString(scanRecord[25] & 0xff) + Integer.toHexString(scanRecord[26] & 0xff);
		            String minor = Integer.toHexString(scanRecord[27] & 0xff) + Integer.toHexString(scanRecord[28] & 0xff);
		            
		            Log.d(TAG, "UUID:"+uuid );
		            Log.d(TAG, "major:"+major );
		            Log.d(TAG, "minor:"+minor );
		        }
		}
	}

}
