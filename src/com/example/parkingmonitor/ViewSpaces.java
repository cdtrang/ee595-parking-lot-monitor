package com.example.parkingmonitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
 
import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.app.Activity;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupExpandListener;

public class ViewSpaces extends Activity { 

	// Progress Dialog
    private ProgressDialog pDialog;
 
    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();
 
    ArrayList<HashMap<String, String>> spacesList;
 
    // url to get all products list
    private static String url_all_spaces = "http://72.209.131.116:8080/ee595/get_all_spaces.php";
 
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_LOT = "lot";
    private static final String TAG_LOTID = "lotId";
    private static final String TAG_DESC = "desc";
    private static final String TAG_TOTAL_SPACE = "totalSpace";
    private static final String TAG_FREE_SPACE = "freeSpace";
     	    
    // products JSONArray
    JSONArray lots = null;
 
    // Expandable List View
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_lots);
	    
	    
        // Hashmap for ListView
        //spacesList = new ArrayList<HashMap<String, String>>();
 
        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        // Loading all parking lots in background
        new LoadAllSpaces().execute();        
        
/*        // Listview Group click listener
        expListView.setOnGroupClickListener(new OnGroupClickListener() {
 
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                int groupPosition, long id) {
            	ImageView showImg = (ImageView) findViewById(R.id.lblListHeader);
            	showImg.setVisibility(View.VISIBLE);
				Toast.makeText(getApplicationContext(),
				"Group Clicked " + listDataHeader.get(groupPosition),
				Toast.LENGTH_SHORT).show();
				return false;
            }
        });
 */
        
        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new OnGroupExpandListener() {
 
            @Override
            public void onGroupExpand(int groupPosition) {
              	//ImageView showImg = (ImageView) findViewById(R.id.lot_map);
            	//showImg.setVisibility(View.VISIBLE);	
            	//Toast.makeText(getApplicationContext(),
                        //listDataHeader.get(groupPosition) + " Expanded",
                        //Toast.LENGTH_SHORT).show();
            }
        });
        
    }
 
 
    /**
     * Background Async Task to Load all product by making HTTP Request
     * */
    class LoadAllSpaces extends AsyncTask<String, String, String> {
 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ViewSpaces.this);
            pDialog.setMessage("Loading spaces. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
 
        /**
         * getting all spaces from get_all_spaces.php url
         * */
        protected String doInBackground(String... args) {
        	//Initialization
        	listDataHeader = new ArrayList<String>();
            listDataChild = new HashMap<String, List<String>>();
        	
        	// Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(url_all_spaces, "GET", params);
 
            // Check log cat for JSON response
            Log.d("All Parking Lots: ", json.toString());
 
            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);
 
                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    lots = json.getJSONArray(TAG_LOT);
 
                    // looping through All Products
                    for (int i = 0; i < lots.length(); i++) {
                        JSONObject c = lots.getJSONObject(i);
 
                        // Storing each json item in variable
                        String id = c.getString(TAG_LOTID);
                        String desc = c.getString(TAG_DESC);
                        String totalSpace = c.getString(TAG_TOTAL_SPACE);
                        String freeSpace = c.getString(TAG_FREE_SPACE);
                                                
                        totalSpace = totalSpace.format("%03d", Integer.parseInt(totalSpace));
                        freeSpace = freeSpace.format("%03d", Integer.parseInt(freeSpace));
                        //System.out.println(freeSpace + "/" + totalSpace);
                        
                        listDataHeader.add(desc);
                        List<String> lotInfo = new ArrayList<String>();
                        
                        lotInfo.add(freeSpace + "/" + totalSpace);
                        //lotInfo.add(freeSpace);
                        listDataChild.put(desc, lotInfo);                      
                        
                    }
                } 
            } catch (JSONException e) {
                e.printStackTrace();
            }
 
            return null;
        }
 
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    // Updating parsed JSON data into ListView                     
                    listAdapter = new ExpandableListAdapter(ViewSpaces.this, listDataHeader, listDataChild);
                    
                    // setting list adapter
                    expListView.setAdapter(listAdapter);                                      
                }
            });            
        }     	

    }
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_refresh:
                finish();
                startActivity(getIntent());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main, menu);
	    return super.onCreateOptionsMenu(menu);
	}
}