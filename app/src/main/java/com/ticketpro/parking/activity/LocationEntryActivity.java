package com.ticketpro.parking.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ticketpro.exception.TPException;
import com.ticketpro.model.Address;
import com.ticketpro.model.Direction;
import com.ticketpro.model.StreetPrefix;
import com.ticketpro.model.StreetSuffix;
import com.ticketpro.parking.R;
import com.ticketpro.parking.bl.LocationEntryBLProcessor;
import com.ticketpro.util.TPConstant;
import com.ticketpro.util.TPUtility;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

/**
 * Home screen (Ref. Mockups).
 * 
 * @author : TicketPRO
 * @version : 1.0
 * @since : 1.0
 *
 */

public class LocationEntryActivity extends BaseActivityImpl {

	private EditText blockInputText;
	private int position = 0;
	private ListView suffixListview;
	private ListView prefixListview;
	private ListView directionListview;
	private TextView locationTextView;
	private String previousSuffix;
	private String pickedLocation = "";
	private boolean fromPlusSuffix = false;
	private Button l2btn;

	private ArrayList<StreetSuffix> suffixes;
	private ArrayList<StreetPrefix> prefixes;
	private ArrayList<Direction> directions;
	private Address activeAddress;
	private boolean isLocationEntry = false;

	private CheckBox skipEditChk;
	private CheckBox keyboardPopupChk;
	private CheckBox clearLocationChk;

	private SharedPreferences mPreferences;
	private boolean togglebutton = true;
	private String currentSuffix;
	private boolean secondLocation = false;
	
	/**
	 * Entry point of the Activity
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.location_entry);
			setLogger(LocationEntryActivity.class.getName());
			setActiveScreen(this);

			mPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);

			blockInputText = (EditText) findViewById(R.id.location_entry_bloc_edit_text);
			suffixListview = (ListView) findViewById(R.id.location_entry_suffix);
			prefixListview = (ListView) findViewById(R.id.location_entry_prefix);
			directionListview = (ListView) findViewById(R.id.location_entry_directions);
			locationTextView = (TextView) findViewById(R.id.location_entry_location_textview);
			skipEditChk = (CheckBox) findViewById(R.id.skipEdit_chk);
			keyboardPopupChk = (CheckBox) findViewById(R.id.keyboard_chk);
			clearLocationChk = (CheckBox) findViewById(R.id.clearSticky_chk);
			l2btn = (Button) findViewById(R.id.l2_btn);
			// l2btn.setText("+");

			l2btn.setVisibility(View.GONE);
			activeAddress = TPUtility.createEmptyAddress();
			Intent data = getIntent();
			if (data.hasExtra("Location")) {
				activeAddress.setLocation(data.getStringExtra("Location"));
				activeAddress.setStreetNumber(data.hasExtra("StreetNumber") ? data.getStringExtra("StreetNumber") : "");
				activeAddress.setStreetPrefix(data.hasExtra("StreetPrefix") ? data.getStringExtra("StreetPrefix") : "");
				activeAddress.setStreetSuffix(data.hasExtra("StreetSuffix") ? data.getStringExtra("StreetSuffix") : "");
				activeAddress.setDirection(data.hasExtra("Direction") ? data.getStringExtra("Direction") : "");

				locationTextView.setText(TPUtility.getFullAddress(activeAddress));
			}

			if (TPApp.getUserSettings() != null) {
				skipEditChk.setChecked(TPApp.getUserSettings().isSkipLocationEntry());
				keyboardPopupChk.setChecked(TPApp.getUserSettings().isLocationKeyboard());

				secondLocation = TPApp.getUserSettings().isSecondLocation();

				if (secondLocation) {
					l2btn.setVisibility(View.VISIBLE);
					try {
						String streetSuffix = activeAddress.getStreetSuffix();
						if (streetSuffix != null && streetSuffix.contains("/")) {
							togglebutton = false;
							l2btn.setText("-");
						} else {
							togglebutton = true;
							l2btn.setText("+");
						}
					}catch (Exception e) {
						e.printStackTrace();
					}
				}

				skipEditChk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						TPApp.getUserSettings().setSkipLocationEntry(isChecked);
						TPApp.updateUserSettings();
					}
				});

				keyboardPopupChk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						TPApp.getUserSettings().setLocationKeyboard(isChecked);
						TPApp.updateUserSettings();
					}
				});
			}

			try {
				boolean isClearLocation = false;
				isClearLocation = mPreferences.getBoolean(TPConstant.PREFS_KEY_CLEAR_LOCATION, isClearLocation);
				clearLocationChk.setChecked(isClearLocation);
			} catch (Exception e) {
				log.error(TPUtility.getPrintStackTrace(e));
			}

			// Clear location flag after printing ticket/adding chalk
			clearLocationChk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					SharedPreferences.Editor editor = mPreferences.edit();
					editor.putBoolean(TPConstant.PREFS_KEY_CLEAR_LOCATION, isChecked);
					editor.apply();
				}
			});

			bindDataAtLoadingTime();

			// create the grid item mapping
			String[] from = new String[] { "locatione_entry_item" };
			int[] to = new int[] { R.id.locatione_entry_item };

			// prepare the list of all records
			List<HashMap<String, String>> fillMaps = new ArrayList<HashMap<String, String>>();
			HashMap<String, String> clearMapSuffix = new HashMap<String, String>();
			clearMapSuffix.put("locatione_entry_item", "");
			fillMaps.add(clearMapSuffix);

			for (int i = 0; i < suffixes.size(); i++) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("locatione_entry_item", suffixes.get(i).getTitle());
				fillMaps.add(map);
			}

			// fill in the grid_item layout
			SimpleAdapter adapter = new SimpleAdapter(LocationEntryActivity.this, fillMaps,
					R.layout.location_entry_item, from, to);
			suffixListview.setAdapter(adapter);
			suffixListview.setEnabled(true);
			suffixListview.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
					activeAddress.setStreetSuffix("");
					suffixListview.setEnabled(true);
					String suffix = getValue(arg0.getItemAtPosition(pos).toString());
					activeAddress.setStreetSuffix(suffix);
					isLocationEntry = false;

					updateLocation();
				}
			});

			// prepare the list of all records
			fillMaps = new ArrayList<HashMap<String, String>>();
			HashMap<String, String> clearMapPrefix = new HashMap<String, String>();
			clearMapPrefix.put("locatione_entry_item", "");
			fillMaps.add(clearMapPrefix);

			for (int i = 0; i < prefixes.size(); i++) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("locatione_entry_item", prefixes.get(i).getTitle());
				fillMaps.add(map);
			}

			// fill in the grid_item layout
			adapter = new SimpleAdapter(LocationEntryActivity.this, fillMaps, R.layout.location_entry_item, from, to);
			prefixListview.setAdapter(adapter);
			prefixListview.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
					String prefix = getValue(arg0.getItemAtPosition(pos).toString());
					activeAddress.setStreetPrefix(prefix);
					isLocationEntry = false;
					updateLocation();
				}
			});

			// prepare the list of all records
			fillMaps = new ArrayList<HashMap<String, String>>();
			HashMap<String, String> clearMapDirection = new HashMap<String, String>();
			clearMapDirection.put("locatione_entry_item", "");
			fillMaps.add(clearMapDirection);

			for (int i = 0; i < directions.size(); i++) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("locatione_entry_item", directions.get(i).getCode());
				fillMaps.add(map);
			}

			// fill in the grid_item layout
			adapter = new SimpleAdapter(LocationEntryActivity.this, fillMaps, R.layout.location_entry_item, from, to);
			directionListview.setAdapter(adapter);
			directionListview.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
					String direction = getValue(arg0.getItemAtPosition(pos).toString());
					activeAddress.setDirection(direction);
					isLocationEntry = false;
					updateLocation();
				}
			});

			updateLocation();
			getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

			locationTextView.setOnLongClickListener(new View.OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					isLocationEntry = true;
					locationAction(v);
					return true;
				}
			});

			locationTextView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					String hasValue = locationTextView.getText().toString();
					hasValue = hasValue.replaceAll(" ", "%20");
					if (!hasValue.matches(""))
						hideKeyboard(v);
					else {
						showKeyboard(v);
					}
				}
			});
			
			locationTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					String hasValue = locationTextView.getText().toString();
					hasValue = hasValue.replaceAll(" ", "%20");
					if (!hasValue.matches(""))
						hideKeyboard(v);
					else {
						showKeyboard(v);
					}
				}
			});
			
			locationTextView.addTextChangedListener(new TextWatcher() {
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {

				}

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {

				}

				@Override
				public void afterTextChanged(Editable s) {

					if (isLocationEntry) {
						try {
							String addressString = locationTextView.getText().toString();

							int index = 0;
							if (activeAddress.getStreetNumber() != null) {
								index = addressString.indexOf(activeAddress.getStreetNumber());
								if (index == -1) {
									activeAddress.setStreetNumber("");
								} else {
									addressString = addressString.replaceAll(activeAddress.getStreetNumber(), "");
								}
							}

							if (activeAddress.getDirection() != null) {
								index = addressString.indexOf(activeAddress.getDirection());
								if (index == -1) {
									activeAddress.setDirection("");
								} else {
									addressString = addressString.replaceAll(activeAddress.getDirection(), "");
								}
							}

							if (activeAddress.getStreetPrefix() != null) {
								index = addressString.indexOf(activeAddress.getStreetPrefix());
								if (index == -1) {
									activeAddress.setStreetPrefix("");
								} else {
									addressString = addressString.replaceAll(activeAddress.getStreetPrefix(), "");
								}
							}

							if (activeAddress.getStreetSuffix() != null) {
								index = addressString.indexOf(activeAddress.getStreetSuffix());
								if (index == -1) {
									activeAddress.setStreetSuffix("");
								} else {
									addressString = addressString.replaceAll(activeAddress.getStreetSuffix(), "");
								}
							}

							activeAddress.setLocation(addressString);

						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					isLocationEntry = true;
				}
			});

			blockInputText.addTextChangedListener(new TextWatcher() {
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {

				}

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {
					/*
					 * String hasValue = locationTextView.getText().toString();
					 * hasValue = hasValue.replaceAll(" ", "%20");
					 * if(!hasValue.matches("")) hideKeyboard(blockInputText);
					 * else {}
					 */
				}

				@Override
				public void afterTextChanged(Editable s) {
					/*
					 * String hasValue = locationTextView.getText().toString();
					 * hasValue = hasValue.replaceAll(" ", "%20");
					 * if(!hasValue.matches("")) hideKeyboard(blockInputText);
					 * else {}
					 */
					activeAddress.setStreetNumber(s.toString());
					isLocationEntry = false;
					updateLocation();
				}
			});

		} catch (Exception e) {
			log.error(TPUtility.getPrintStackTrace(e));
		}

		l2btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				isLocationEntry = false;
				if (togglebutton) {

					suffixListview.setEnabled(false);
					fromPlusSuffix = true;
					locationAction(v);
					togglebutton = false;
					l2btn.setText("-");
				} else {
					suffixListview.setEnabled(true);
					togglebutton = true;
					l2btn.setText("+");
					if (activeAddress != null)
						previousSuffix = activeAddress.getStreetSuffix();

					if (previousSuffix != null && previousSuffix.contains("/")) {
						currentSuffix = previousSuffix.toString();
						if (currentSuffix != null && !currentSuffix.matches(""))
							currentSuffix = previousSuffix.substring(0, currentSuffix.indexOf("/"));
						activeAddress.setStreetSuffix(currentSuffix);
						currentSuffix = activeAddress.getStreetSuffix();
						locationTextView.setText(TPUtility.getFullAddress(activeAddress));
					}

				}
			}
		});
	}

	public void bindDataAtLoadingTime() {
		try {
			LocationEntryBLProcessor blProcessor = new LocationEntryBLProcessor();
			this.directions = blProcessor.getDirections();
			this.suffixes = blProcessor.getSuffixes();
			this.prefixes = blProcessor.getPrefixes();

		} catch (TPException e) {
			log.error(TPUtility.getPrintStackTrace(e));
		}
	}

	@Override
	public void onClick(View v) {

	}

	public void press0(View v) {
		keyPress("0");
	}

	public void press1(View v) {
		keyPress("1");
	}

	public void press2(View v) {
		keyPress("2");
	}

	public void press3(View v) {
		keyPress("3");
	}

	public void press4(View v) {
		keyPress("4");
	}

	public void press5(View v) {
		keyPress("5");
	}

	public void press6(View v) {
		keyPress("6");
	}

	public void press7(View v) {
		keyPress("7");
	}

	public void press8(View v) {
		keyPress("8");
	}

	public void press9(View v) {
		keyPress("9");
	}

	public void pressBack(View v) {
		blockInputText.setFocusableInTouchMode(true);
		blockInputText.requestFocus();
		if (position > 0)
			position--;

		blockInputText.setSelection(position);
	}

	public void pressC(View v) {
		blockInputText.setText("");
		activeAddress.setStreetNumber("");
		position = 0;

		isLocationEntry = false;
		updateLocation();
	}

	public void pressClear(View v) {
		suffixListview.setEnabled(true);
		isLocationEntry = false;
		blockInputText.setText("");
		locationTextView.setText("");
		position = 0;

		activeAddress.setLocation("");
		activeAddress.setStreetNumber("");
		activeAddress.setStreetPrefix("");
		activeAddress.setStreetSuffix("");
		activeAddress.setDirection("");

		togglebutton = true;
		l2btn.setText("+");

	}

	private void keyPress(String ch) {
		try {

			blockInputText
					.setText(new StringBuffer(blockInputText.getText().toString()).insert(position, ch).toString());
			position = blockInputText.getText().toString().length();
			blockInputText.setFocusableInTouchMode(true);
			blockInputText.requestFocus();
			blockInputText.setSelection(position);
			activeAddress.setStreetNumber(blockInputText.getText().toString());

			isLocationEntry = false;
			updateLocation();

		} catch (Exception e) {
			log.error(TPUtility.getPrintStackTrace(e));
		}
	}

	private String getValue(String s) {
		return s.substring(22, s.length() - 1);
	}

	public void cancelAction(View v) {
		finish();
	}

	private void updateLocation() {
		if (pickedLocation != null && fromPlusSuffix) {

			// String addressString =
			// TPUtility.getFullAddress(activeAddress).toString();
			previousSuffix = activeAddress.getStreetSuffix();
			if (previousSuffix != null && previousSuffix.contains("/")) {
				if (!previousSuffix.matches(""))
					previousSuffix = previousSuffix.substring(0, previousSuffix.indexOf("/"));
				if (previousSuffix.contains("/"))
					activeAddress.setStreetSuffix(previousSuffix + pickedLocation);
				else
					activeAddress.setStreetSuffix(previousSuffix + "/" + pickedLocation);

			} else {
				if (previousSuffix == null)
					activeAddress.setStreetSuffix("/" + pickedLocation);

				else {
					if (previousSuffix.matches(""))
						activeAddress.setStreetSuffix("/" + pickedLocation);

					else
						activeAddress.setStreetSuffix(previousSuffix + "/" + pickedLocation);

				}

			}
			fromPlusSuffix = false;
			previousSuffix = activeAddress.getStreetSuffix();

			locationTextView.setText(TPUtility.getFullAddress(activeAddress));

		} else if (pickedLocation != null && isLocationEntry) {
			activeAddress.setLocation(pickedLocation);
			activeAddress.setStreetNumber("");
			activeAddress.setStreetPrefix("");
			activeAddress.setStreetSuffix("");
			activeAddress.setDirection("");
			blockInputText.setText("");
			togglebutton = true;
			suffixListview.setEnabled(true);
			l2btn.setText("+");

			locationTextView.setText(TPUtility.getFullAddress(activeAddress));

			isLocationEntry = false;
		} else
			locationTextView.setText(TPUtility.getFullAddress(activeAddress));

		position = blockInputText.getText().toString().length();
		// isLocationEntry = false;

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (data.hasExtra("Location")) {
				pickedLocation = data.getStringExtra("Location");

				updateLocation();
			}
		}
	}

	public void locationAction(View view) {

		Intent i = new Intent();
		i.setClass(this, SearchLookupActivity.class);
		i.putExtra("LIST_TYPE", TPConstant.SELECT_LOCATION_LIST);
		startActivityForResult(i, 0);
	}

	public void acceptAction(View v) {

		Intent data = new Intent();
		data.putExtra("FULL_LOCATION", TPUtility.getFullAddress(activeAddress).trim());
		data.putExtra("IS_LOCATION_FORM", true);
		data.putExtra("Location", activeAddress.getLocation());
		data.putExtra("StreetNumber", activeAddress.getStreetNumber());
		data.putExtra("StreetPrefix", activeAddress.getStreetPrefix());
		data.putExtra("StreetSuffix", activeAddress.getStreetSuffix());
		data.putExtra("Direction", activeAddress.getDirection());

		if (getParent() == null) {
			setResult(Activity.RESULT_OK, data);
		} else {
			getParent().setResult(Activity.RESULT_OK, data);
		}

		finish();
	}

	@Override
	public void handleVoiceInput(String text) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleVoiceMode(boolean voiceMode) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleNetworkStatus(boolean connected, boolean isFastConnection) {
		// TODO Auto-generated method stub

	}

	private void hideKeyboard(View view) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		locationTextView.setInputType(InputType.TYPE_NULL);
	}

	private void showKeyboard(View view) {
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
		locationTextView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
	}
}
