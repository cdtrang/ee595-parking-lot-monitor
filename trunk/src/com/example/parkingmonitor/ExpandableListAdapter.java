package com.example.parkingmonitor;
 
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
 
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
 
    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;
 
    //temp vars for lot's info
    float freeSpace;
    float totalSpace;
    float ratio;
    
    public ExpandableListAdapter(Context context, List<String> listDataHeader,
            HashMap<String, List<String>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }
 
	@Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }
 
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
 
    @Override
    public View getChildView(int groupPosition, final int childPosition,
            boolean isLastChild, View convertView, ViewGroup parent) {
 
        final String childText = (String) getChild(groupPosition, childPosition);
 
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }
 
        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);
 
        //txtListChild.setText(childText);        
                
        if (freeSpace >= 25) {
        	txtListChild.setText((int)(freeSpace) + " available spots in this lot.");
        	
        } else if (freeSpace >= 1){
        	txtListChild.setText("Hurry! Only " + (int)freeSpace + " spots left.");
        } else {
        	txtListChild.setText("Full.");
        }
        
        //Set lot's map according to the location
        ImageView imageView = (ImageView) convertView.findViewById(R.id.lot_map);
        switch (groupPosition) {
        	case 0:
        		imageView.setImageResource(R.drawable.twentyfirst_bluff);
        		break;
        	case 1:
        		imageView.setImageResource(R.drawable.seventeenth_fairmount);
        		break;        		
        	case 2:
        		imageView.setImageResource(R.drawable.ablah_library);
        		break;
        	case 3:
        		imageView.setImageResource(R.drawable.duerksen_center);
        		break;    
        }
        

        
        return convertView;
    }
 
    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }
 
    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }
 
    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }
 
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
 
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
            View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }
 
        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);
        
        //Getting the layout to change the background later
        LinearLayout li = (LinearLayout) convertView.findViewById(R.id.group_layout);

        //temp String[] for reading values
        String[] temp = _listDataHeader.toArray(new String[_listDataHeader.size()]);        
        String[] temp2 = _listDataChild.get( temp[groupPosition] ).toArray((new String[ _listDataChild.get( temp[groupPosition] ).size() ]));
        
        freeSpace = Float.parseFloat(temp2[0].substring(0,3));
        totalSpace = Float.parseFloat(temp2[0].substring(4));
        ratio = freeSpace / totalSpace;

        //Compare ratio to change the color accordingly 
        if ( ratio >= .60) {
        	li.setBackgroundColor(Color.parseColor("#7cbb00"));
        } else if ( ratio >= .25) {
        	li.setBackgroundColor(Color.parseColor("#ffa500"));
        } else {
        	li.setBackgroundColor(Color.parseColor("#ff1919"));
        }        
        
        return convertView;
    }
 
    @Override
    public boolean hasStableIds() {
        return false;
    }
 
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}