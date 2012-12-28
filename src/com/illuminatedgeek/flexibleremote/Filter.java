package com.illuminatedgeek.flexibleremote;

class Filter {
	static final int AVERAGE_BUFFER = 10;
	float []m_arr = new float[AVERAGE_BUFFER];
	int m_idx = 0;

	public float append(float val) {
		m_arr[m_idx] = val;
		m_idx++;
		if (m_idx == AVERAGE_BUFFER)
			m_idx = 0;
		return avg();
	}
	public float avg() {
		float sum = 0;
		for (float x: m_arr)
			sum += x;
		return sum / AVERAGE_BUFFER;
	}

}