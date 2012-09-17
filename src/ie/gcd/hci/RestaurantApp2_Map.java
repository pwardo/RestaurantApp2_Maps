/*
 * Assignment 3, Restaurant App Version 2 with Maps Added
 * Module: HCI and GUI Programming
 * 
 * Student Name: Patrick Ward
 * Student Number: 2761238
 * 
 * Hold down to delete a record
 * Press Menu to get options
 */

package ie.gcd.hci;

import java.util.List;

import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class RestaurantApp2_Map extends MapActivity{
    
	private Long mRowId;
	private RestaurantApp2_DBAdapter myDbHelper;

	private MapView mapView;
	private MapController mc;
	private Drawable drawable;
		
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.record_map);
		setTitle(R.string.map_title);
		
		myDbHelper = new RestaurantApp2_DBAdapter(this);
		myDbHelper.open();

		mRowId = (savedInstanceState == null) ? null
				: (Long) savedInstanceState
						.getSerializable(RestaurantApp2_DBAdapter.KEY_ROWID);
		if (mRowId == null) {
			Bundle extras = getIntent().getExtras();
			mRowId = extras != null ? extras.getLong(RestaurantApp2_DBAdapter.KEY_ROWID) : null;
		}
        
		// you first obtain a reference to the map using the findViewById()
		// method
		mapView = (MapView) findViewById(R.id.mapview);
		// setTitle(R.string.map_title);

		mapView.setBuiltInZoomControls(true);
		
		/*
		 * After a reference to the map is obtained, you get the controller for
		 * the map and assign it to a MapController object (mc) Focuses map on
		 * position at start
		 */
		
		mc = mapView.getController();
		
		// Get info of restaurant and display on map
		List<Overlay> mapOverlays = mapView.getOverlays();
		drawable = this.getResources().getDrawable(R.drawable.androidmarker);
		
		RestaurantApp2_MapOverlay itemizedoverlay = new	RestaurantApp2_MapOverlay(drawable,this);
			
//		Add Location of Griffith College
		String message = new String("You are here.");
		String coordinates[] = {"53.331354", "-6.278000"};
		
		double lat = Double.parseDouble(coordinates[0]);
		double lng = Double.parseDouble(coordinates[1]);

		GeoPoint point1 = new GeoPoint(
			(int) (lat * 1E6), 
			(int) (lng * 1E6));
		
		OverlayItem overlayitem1 = new OverlayItem(point1, message, "");
		itemizedoverlay.addOverlay(overlayitem1);
		
		
//		Add location of current restaurant
		Cursor record = myDbHelper.fetchRecord(mRowId);
		startManagingCursor(record);
		
		String name = new String(
				(record.getString(record.getColumnIndex(RestaurantApp2_DBAdapter.KEY_NAME)))
		);
		String address = new String(
				(record.getString(record.getColumnIndex(RestaurantApp2_DBAdapter.KEY_ADDRESS)))
		);
		GeoPoint point2 = new GeoPoint(
				(int)(record.getDouble(record.getColumnIndex(RestaurantApp2_DBAdapter.KEY_LATITUDE))*1E6),
				(int)(record.getDouble(record.getColumnIndex(RestaurantApp2_DBAdapter.KEY_LONGITUDE))*1E6)
		);
		OverlayItem overlayitem2 = new OverlayItem(point2, name, address);
		itemizedoverlay.addOverlay(overlayitem2);
		
//
//		GeoPoint centrePoint = new GeoPoint(
//				(int) ((point1.getLatitudeE6()) - (point2.getLatitudeE6())),
//				(int) ((point1.getLongitudeE6()) - (point2.getLongitudeE6()))
//		);
		
			
		mapView.setSatellite(true);
		mapView.invalidate();
		mc.setZoom(17);
		mc.animateTo(point1);
//		mc.zoomToSpan(point.getLatitudeE6(), point2.getLatitudeE6());
		
		mapOverlays.add(itemizedoverlay);
	}
	
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
}