package com.example.parkingmonitor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity {
	private Button btnViewSpaces;
 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);	    
		
        //Directly start the ViewSpaces activity
		Intent i = new Intent(getApplicationContext(), ViewSpaces.class);
        startActivity(i);
		
		
		
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
	public boolean onPrepareOptionsMenu(Menu menu) {
	    super.onPrepareOptionsMenu(menu);

	    MenuItem item  = menu.findItem(R.id.action_refresh);
	    item.setVisible(false);
	    return true;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main, menu);
	    
		return super.onCreateOptionsMenu(menu);
	} 
}