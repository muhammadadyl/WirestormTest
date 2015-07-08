package com.example.adeelwirestorm;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class Person_Details extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_person__details);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.person__details, menu);
		return true;
	}

}
