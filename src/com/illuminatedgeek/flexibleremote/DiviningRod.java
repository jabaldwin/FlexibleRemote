package com.illuminatedgeek.flexibleremote;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;

public class DiviningRod {
	
	/* fix random noise by averaging tilt values */
	private final static int AVERAGE_BUFFER = 30;
	float m_lastPitch = 0.f;
	float m_lastYaw = 0.f;

	float []m_prevRoll = new float[AVERAGE_BUFFER];
	float m_lastRoll = 0.f;
	
	private float[] m_lastMagFields;
	private float[ ]m_lastAccels;
	private float[] m_rotationMatrix = new float[16];
	private float[] m_orientation = new float[4];
	Filter [] m_filters = { new Filter(), new Filter(), new Filter() };
	
	private void computeOrientation() {
		if (SensorManager.getRotationMatrix(m_rotationMatrix, null,
				m_lastAccels, m_lastMagFields)) {
			SensorManager.getOrientation(m_rotationMatrix, m_orientation);

			/* [0] : yaw, rotation around z axis
			 * [1] : pitch, rotation around x axis
			 * [2] : roll, rotation around y axis */
			float yaw = m_orientation[0];
			float pitch = m_orientation[1];
			float roll = m_orientation[2];

			/* append returns an average of the last 10 values */
			m_lastYaw = m_filters[0].append(yaw);
			m_lastPitch = m_filters[1].append(pitch);
			m_lastRoll = m_filters[2].append(roll);
		}
	}
	
	public void sensorChanged(SensorEvent event){
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			accel(event);
		}
		if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
			mag(event);
		}
	}
	
	private void accel(SensorEvent event) {
		if (m_lastAccels == null) {
			m_lastAccels = new float[3];
		}

		System.arraycopy(event.values, 0, m_lastAccels, 0, 3);

		/*if (m_lastMagFields != null) {
            computeOrientation();
        }*/
	}

	private void mag(SensorEvent event) {
		if (m_lastMagFields == null) {
			m_lastMagFields = new float[3];
		}

		System.arraycopy(event.values, 0, m_lastMagFields, 0, 3);

		if (m_lastAccels != null) {
			computeOrientation();
		}
	}
}
