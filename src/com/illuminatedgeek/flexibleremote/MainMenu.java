package com.illuminatedgeek.flexibleremote;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainMenu extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	public void airmouseStart(View view){
		startActivity(new Intent(this, AirMouse.class));
	}
}
