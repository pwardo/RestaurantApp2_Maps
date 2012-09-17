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
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.text.method.NumberKeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;

public class RestaurantApp2_EditRecord extends Activity {

	private EditText nameText;
	private EditText addressText;
	private EditText phoneText;
	// private EditText noteText;

	private RatingBar ratingbarValue;

	private TextView latitudeText;
	private TextView longitudeText;

	private Long mRowId;
	private RestaurantApp2_DBAdapter myDbHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		myDbHelper = new RestaurantApp2_DBAdapter(this);
		myDbHelper.open();

		setContentView(R.layout.record_edit);
		setTitle(R.string.edit_record);

		nameText = (EditText) findViewById(R.id.name);
		addressText = (EditText) findViewById(R.id.address);

		phoneText = (EditText) findViewById(R.id.phone);
		phoneText.setKeyListener(new NumberKeyListener() {
			@Override
			protected char[] getAcceptedChars() {
				// restrict input to these characters
				char[] numberChars = { '0', '1', '2', '3', '4', '5', '6', '7',
						'8', '9', '.' };
				return numberChars;
			}

			@Override
			public int getInputType() {
				// TODO Auto-generated method stub
				return 0;
			}
		});
		
		// noteText = (EditText) findViewById(R.id.note);

		ratingbarValue = (RatingBar) findViewById(R.id.ratingbar);
		ratingbarValue.setOnRatingBarChangeListener(new	OnRatingBarChangeListener()	{
			@Override
			public void onRatingChanged(RatingBar ratingbar, float rating,
					boolean fromUser)
			{
				ratingbar.getRating();
			}
		});

		// ------------------------------------------------------------------------------------
		latitudeText = (EditText) findViewById(R.id.latitude);
		latitudeText.setKeyListener(new NumberKeyListener() {
			@Override
			protected char[] getAcceptedChars() {
				// restrict input to these characters
				char[] numberChars = { '0', '1', '2', '3', '4', '5', '6', '7',
						'8', '9', '.' };
				return numberChars;
			}

			@Override
			public int getInputType() {
				// TODO Auto-generated method stub
				return 0;
			}
			});

		longitudeText = (EditText) findViewById(R.id.longitude);
		longitudeText.setKeyListener(new NumberKeyListener() {
			@Override
			protected char[] getAcceptedChars() {
				// restrict input to these characters
				char[] numberChars = { '0', '1', '2', '3', '4', '5', '6', '7','8', '9', '.', '-' };
					return numberChars;
				}
	
				@Override
				public int getInputType() {
					// TODO Auto-generated method stub
					return 0;
				}
			});

		// ------------------------------------------------------------------------------------
		Button confirmButton = (Button) findViewById(R.id.confirm);

		// Alert Dialog for input errors
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("").setCancelable(false)
				.setNegativeButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});

		mRowId = (savedInstanceState == null) ? null
				: (Long) savedInstanceState
						.getSerializable(RestaurantApp2_DBAdapter.KEY_ROWID);
		if (mRowId == null) {
			Bundle extras = getIntent().getExtras();
			mRowId = extras != null ? extras
					.getLong(RestaurantApp2_DBAdapter.KEY_ROWID) : null;
		}

		populateFields();

		confirmButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (nameText.length() > 0) {
					setResult(RESULT_OK);
					finish();
				} else {
					AlertDialog alertDialog = builder.create();
					alertDialog.setTitle("ERROR!");
					alertDialog.setMessage("Restaurant must have a name");
					alertDialog.show();
					setResult(RESULT_CANCELED);
				}
			}

		});
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
			latitudeText
					.setText(record.getString(record
							.getColumnIndexOrThrow(RestaurantApp2_DBAdapter.KEY_LATITUDE)));
			longitudeText
					.setText(record.getString(record
							.getColumnIndexOrThrow(RestaurantApp2_DBAdapter.KEY_LONGITUDE)));
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		saveState();
		outState.putSerializable(RestaurantApp2_DBAdapter.KEY_ROWID, mRowId);
	}

	@Override
	protected void onPause() {
		super.onPause();
		saveState();
	}

	@Override
	protected void onResume() {
		super.onResume();
		populateFields();
	}

	private void saveState() {
		String name = nameText.getText().toString();
		String address = addressText.getText().toString();
		String phone = phoneText.getText().toString();
		String note = "Void"; // Removed to make room on EditRecord View
		float rating = ratingbarValue.getRating();

		// ----------------------------------------------------------------------------------
		String latitude = latitudeText.getText().toString();
		String longitude = longitudeText.getText().toString();
		// ----------------------------------------------------------------------------------

		if (mRowId == null) {
			long id = myDbHelper.createRecord(name, address, phone, note,
					rating, latitude, longitude);
			if (id > 0) {
				mRowId = id;
			}
		} else {
			myDbHelper.updateRecord(mRowId, name, address, phone, note, rating,
					latitude, longitude);
		}
	}
}