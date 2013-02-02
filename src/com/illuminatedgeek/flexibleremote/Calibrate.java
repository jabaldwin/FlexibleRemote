package com.illuminatedgeek.flexibleremote;

import java.net.SocketException;
import java.net.UnknownHostException;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;

public class Calibrate extends Activity implements SensorEventListener {

	private SensorManager mSensorManager;
	private Sensor mAccel;
	private Sensor mMagnet;

	private PacketCannon pc;
	private DiviningRod dr;
	final int SENSOR_ACCURACY = SensorManager.SENSOR_DELAY_FASTEST;
	
	// default ip
	public static String SERVERIP = "192.168.0.189";

	// designate a port
	public static final int SERVERPORT = 7777;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calibrate);
		
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE); 
		mAccel = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mMagnet = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		
		try {
			pc = new PacketCannon(SERVERIP, SERVERPORT);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		dr = new DiviningRod();
		
		pc.sendPacket("a:calibrate");
	}
	
	protected void onResume(){
		super.onResume();
		mSensorManager.registerListener(this, mAccel, SENSOR_ACCURACY);
		mSensorManager.registerListener(this, mMagnet, SENSOR_ACCURACY);
	}
	
	protected void onPause() {
		super.onPause();
		mSensorManager.unregisterListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_calibrate, menu);
		return true;
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// To satisfy SensorEventListener
		
	}

	@Override
	public void onSensorChanged(SensorEvent arg0) {
		dr.sensorChanged(arg0);
		pc.sendPacket("yaw:"+dr.m_lastYaw, "pitch:"+dr.m_lastPitch, "roll:"+dr.m_lastRoll);
	}

}
