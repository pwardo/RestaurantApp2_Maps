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

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

public class RestaurantApp2_DetailView extends Activity {

	private TextView nameText;
	private TextView addressText;
	private TextView phoneText;
//	private TextView noteText;

	private RatingBar ratingbarValue;

//	private TextView latitudeText;
//	private TextView longitudeText;

	private Long mRowId;
	private RestaurantApp2_DBAdapter myDbHelper;
	
	private static final int ACTIVITY_EDIT = 1;
	private static final int UPDATE_ID = Menu.FIRST;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		myDbHelper = new RestaurantApp2_DBAdapter(this);
		myDbHelper.open();

		setContentView(R.layout.record_detail);
		setTitle(R.string.detail_record);

		nameText = (TextView) findViewById(R.id.name);
		addressText = (TextView) findViewById(R.id.address);
		phoneText = (TextView) findViewById(R.id.phone);
//		noteText = (TextView) findViewById(R.id.note);

		ratingbarValue = (RatingBar) findViewById(R.id.ratingbar);

//		latitudeText = (TextView) findViewById(R.id.latitude);
//		longitudeText = (TextView) findViewById(R.id.longitude);

		mRowId = (savedInstanceState == null) ? null
				: (Long) savedInstanceState
						.getSerializable(RestaurantApp2_DBAdapter.KEY_ROWID);
		if (mRowId == null) {
			Bundle extras = getIntent().getExtras();
			mRowId = extras != null ? extras
					.getLong(RestaurantApp2_DBAdapter.KEY_ROWID) : null;
		}

		populateFields();
	}

	public void showMap(View v) {
		long id = mRowId;
		Intent intent = new Intent(this, RestaurantApp2_Map.class);
		intent.putExtra(RestaurantApp2_DBAdapter.KEY_ROWID, id);
		startActivity(intent);
	}

	private void populateFields() {
		if (mRowId != null) {
			Cursor record = myDbHelper.fetchRecord(mRowId);
			startManagingCursor(record);
			nameText.setText(record.getString(record
					.getColumnIndexOrThrow(RestaurantApp2_DBAdapter.KEY_NAME)));
			addressText.setText(record.getString(record
					.getColumnIndexOrThrow(RestaurantApp2_DBAdapter.KEY_ADDRESS)));
			phoneText.setText(record.getString(record
					.getColumnIndexOrThrow(RestaurantApp2_DBAdapter.KEY_PHONE)));
//			noteText.setText(record.getString(record
//					.getColumnIndexOrThrow(RestaurantApp2_DBAdapter.KEY_NOTE)));
			ratingbarValue.setRating(record.getFloat(record
					 .getColumnIndexOrThrow(RestaurantApp2_DBAdapter.KEY_RATING)));

			// ---------------------------------------------------------------------------------
//			latitudeText.setText(record.getString(record
//							.getColumnIndexOrThrow(RestaurantApp2_DBAdapter.KEY_LATITUDE)));
//			longitudeText.setText(record.getString(record
//							.getColumnIndexOrThrow(RestaurantApp2_DBAdapter.KEY_LONGITUDE)));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, UPDATE_ID, 0, R.string.menu_update);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case UPDATE_ID:
			updateRecord(mRowId);
			return true;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	private void updateRecord(long id) {
		Intent i = new Intent(this, RestaurantApp2_EditRecord.class);
		i.putExtra(RestaurantApp2_DBAdapter.KEY_ROWID, id);
		startActivityForResult(i, ACTIVITY_EDIT);
	}

//	@Override
//	protected void onSaveInstanceState(Bundle outState) {
//		super.onSaveInstanceState(outState);
//		saveState();
//		outState.putSerializable(RestaurantApp2_DBAdapter.KEY_ROWID, mRowId);
//	}
//
//	@Override
//	protected void onPause() {
//		super.onPause();
//		saveState();
//	}

	@Override
	protected void onResume() {
		super.onResume();
		populateFields();
	}

//	private void saveState() {
//		String name = nameText.getText().toString();
//		String address = addressText.getText().toString();
//		String phone = phoneText.getText().toString();
//		String note = "Void"; // Removed to make room on EditRecord View
//		float rating = ratingbarValue.getRating();

		// ----------------------------------------------------------------------------------
//		String latitude = latitudeText.getText().toString();
//		String longitude = longitudeText.getText().toString();
		// ----------------------------------------------------------------------------------

//		if (mRowId == null) {
//			long id = myDbHelper.createRecord(name, address, phone, note,
//					rating, latitude, longitude);
//			if (id > 0) {
//				mRowId = id;
//			}
//		} else {
//			myDbHelper.updateRecord(mRowId, name, address, phone, note, rating,
//					latitude, longitude);
//		}
//	}
}