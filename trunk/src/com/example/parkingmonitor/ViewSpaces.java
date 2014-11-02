package com.example.parkingmonitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
 
import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
 
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class ViewSpaces extends ListActivity {
 
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
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_lots);
	    
        // Hashmap for ListView
        spacesList = new ArrayList<HashMap<String, String>>();
 
        // Loading all spaces in background
        new LoadAllSpaces().execute();
        
        // Get ListView
        ListView listView = getListView();
        
        //Show a map when a parking lot is clicked on
        listView.setOnItemClickListener(new OnItemClickListener() {
        	 
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                // getting values from selected ListItem
                String lotId = ((TextView) view.findViewById(R.id.lotId)).getText()
                        .toString();
                
                
                ImageView newImg = (ImageView) findViewById(R.id.lot_map);
                
                if (lotId.equalsIgnoreCase("1")) {
                	newImg.setImageResource(R.drawable.twentyfirst_bluff);	
                }
                if (lotId.equalsIgnoreCase("2")) {
                	newImg.setImageResource(R.drawable.seventeenth_fairmount);	
                }
                if (lotId.equalsIgnoreCase("3")) {
                	newImg.setImageResource(R.drawable.ablah_library);	
                }
                
                newImg.setVisibility(View.VISIBLE);

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
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(url_all_spaces, "GET", params);
 
            // Check your log cat for JSON response
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
                        
                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();
 
                        // adding each child node to HashMap key => value
                        map.put(TAG_LOTID, id);
                        map.put(TAG_DESC, desc);
                        map.put(TAG_TOTAL_SPACE, totalSpace);
                        map.put(TAG_FREE_SPACE, freeSpace);
 
                        // adding HashList to ArrayList
                        spacesList.add(map);
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
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    ListAdapter adapter = new SimpleAdapter(
                            ViewSpaces.this,
                            spacesList,
                            R.layout.list_item, 
                            new String[] {TAG_LOTID, TAG_DESC, TAG_TOTAL_SPACE, TAG_FREE_SPACE},
                            new int[] { R.id.lotId, R.id.desc, R.id.total, R.id.free });
                    // updating listview
                    setListAdapter(adapter);
                    
                    
                }
            });
 
        }
 
    	
    	public boolean onCreateOptionsMenu(Menu menu) {
    		// Inflate the menu; this adds items to the action bar if it is present.
    		/*getMenuInflater().inflate(R.menu.main, menu);
    		return true;*/
    		
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.activity_view_lots, menu);
     
            return true;
    	}
    }
}