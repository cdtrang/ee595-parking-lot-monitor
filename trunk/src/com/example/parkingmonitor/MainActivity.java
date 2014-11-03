package com.example.parkingmonitor;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;


public class MainActivity extends ActionBarActivity {
	private Button btnViewSpaces;
 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	    
	    ActionBar actionBar = getSupportActionBar();
	    actionBar.show();
		
        // Buttons
        btnViewSpaces = (Button) findViewById(R.id.btnViewSpaces);
 
        
        // view products click event
        btnViewSpaces.setOnClickListener(new View.OnClickListener() {
 
            @Override
            public void onClick(View view) {
                // Launching ViewSpaces Activity
                Intent i = new Intent(getApplicationContext(), ViewSpaces.class);
                startActivity(i);
            }
        });		
	}

 
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		/*getMenuInflater().inflate(R.menu.main, menu);
		return true;*/
		
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
 
        return super.onCreateOptionsMenu(menu);
	} 
}