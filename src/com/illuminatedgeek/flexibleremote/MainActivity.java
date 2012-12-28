package com.illuminatedgeek.flexibleremote;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ToggleButton;

public class MainActivity extends Activity implements SensorEventListener {

	private SensorManager mSensorManager;
	private Sensor mAccel;
	private Sensor mMagnet;
	public Handler handler = new Handler();
	public DatagramSocket ds;
	public DatagramPacket dp;
	private boolean send = false;
	
	private PacketCannon pc;
	private DiviningRod dr;
	
	final int SENSOR_ACCURACY = SensorManager.SENSOR_DELAY_GAME;

	// default ip
	public static String SERVERIP = "192.168.0.197";

	// designate a port
	public static final int SERVERPORT = 7777;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Get an instance of the SensorManager
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
	}
	
	public void toggleSend(View view) {
		send = ((ToggleButton) view).isChecked();
	}
	
	@Override
	public final void onAccuracyChanged(Sensor sensor, int accuracy) {
		// Placeholder to satisfy SensorEventListener
	}

	@Override
	public final void onSensorChanged(SensorEvent event) {
		dr.sensorChanged(event);
		if(send){
			pc.sendPacket("x:"+dr.m_lastYaw, "y:"+dr.m_lastPitch);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		mSensorManager.registerListener(this, mAccel, SENSOR_ACCURACY);
		mSensorManager.registerListener(this, mMagnet, SENSOR_ACCURACY);
	}

	@Override
	protected void onPause() {
		super.onPause();
		mSensorManager.unregisterListener(this);
	}
	
	public void sendClick(View view) {
		pc.sendPacket("a:leftClick");
	}

	@Override
	protected void onStop() {
		super.onStop();
	}
}
