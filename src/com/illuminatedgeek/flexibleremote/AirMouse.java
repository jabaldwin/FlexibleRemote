package com.illuminatedgeek.flexibleremote;

import java.net.SocketException;
import java.net.UnknownHostException;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ToggleButton;

public class AirMouse extends Activity implements SensorEventListener {

	private SensorManager mSensorManager;
	private Sensor mAccel;
	private Sensor mMagnet;
	private boolean send = false;
	private Button clickButton;

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
		setContentView(R.layout.activity_mouse);

		// Get an instance of the SensorManager
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE); 
		mAccel = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mMagnet = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		
		clickButton = (Button) findViewById(R.id.ButtonClick);
		clickButton.setOnTouchListener(new OnTouchListener(){
			public boolean onTouch(View v, MotionEvent event){
				int action = event.getAction() & MotionEvent.ACTION_MASK;
				if(action == MotionEvent.ACTION_DOWN){
					pc.sendPacket("a:leftDown");
				} else if (action == MotionEvent.ACTION_UP){
					pc.sendPacket("a:leftUp");
				}
				return true;
			}
		});
		
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

	public void clickTouch(View view, MotionEvent event) {
		int action = event.getAction() & MotionEvent.ACTION_MASK;
		if(action == MotionEvent.ACTION_DOWN){
			pc.sendPacket("a:leftDown");
		} else if (action == MotionEvent.ACTION_UP){
			pc.sendPacket("a:leftUp");
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
	}
}
