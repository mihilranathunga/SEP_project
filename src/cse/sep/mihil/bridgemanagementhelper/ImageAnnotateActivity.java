package cse.sep.mihil.bridgemanagementhelper;

import cse.sep.mihil.bridgemanagementhelper.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.support.v4.app.NavUtils;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class ImageAnnotateActivity extends Activity {
	/**
	 * Whether or not the system UI should be auto-hidden after
	 * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
	 */
	private static final boolean AUTO_HIDE = true;

	/**
	 * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
	 * user interaction before hiding the system UI.
	 */
	private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

	/**
	 * If set, will toggle the system UI visibility upon interaction. Otherwise,
	 * will show the system UI visibility upon interaction.
	 */
	private static final boolean TOGGLE_ON_CLICK = true;

	/**
	 * The flags to pass to {@link SystemUiHider#getInstance}.
	 */
	private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

	/**
	 * The instance of the {@link SystemUiHider} for this activity.
	 */
	private SystemUiHider mSystemUiHider;

	private static final int EDIT_PHOTO = 35;

	public Bitmap bitmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_image_annotate);
		setupActionBar();

		final View controlsView = findViewById(R.id.fullscreen_content_controls);
		final View contentView = (View) findViewById(R.id.edit_image);

		// Set up an instance of SystemUiHider to control the system UI for
		// this activity.
		mSystemUiHider = SystemUiHider.getInstance(this, contentView,
				HIDER_FLAGS);
		mSystemUiHider.setup();
		mSystemUiHider
				.setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
					// Cached values.
					int mControlsHeight;
					int mShortAnimTime;

					@Override
					@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
					public void onVisibilityChange(boolean visible) {
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
							// If the ViewPropertyAnimator API is available
							// (Honeycomb MR2 and later), use it to animate the
							// in-layout UI controls at the bottom of the
							// screen.
							if (mControlsHeight == 0) {
								mControlsHeight = controlsView.getHeight();
							}
							if (mShortAnimTime == 0) {
								mShortAnimTime = getResources().getInteger(
										android.R.integer.config_shortAnimTime);
							}
							controlsView
									.animate()
									.translationY(visible ? 0 : mControlsHeight)
									.setDuration(mShortAnimTime);
						} else {
							// If the ViewPropertyAnimator APIs aren't
							// available, simply show or hide the in-layout UI
							// controls.
							controlsView.setVisibility(visible ? View.VISIBLE
									: View.GONE);
						}

						if (visible && AUTO_HIDE) {
							// Schedule a hide().
							delayedHide(AUTO_HIDE_DELAY_MILLIS);
						}
					}
				});

		// Set up the user interaction to manually show or hide the system UI.
		contentView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (TOGGLE_ON_CLICK) {
					mSystemUiHider.toggle();
				} else {
					mSystemUiHider.show();
				}
			}
		});

		// Upon interacting with UI controls, delay any scheduled hide()
		// operations to prevent the jarring behavior of controls going away
		// while interacting with the UI.
		findViewById(R.id.color_spinner).setOnTouchListener(
				mDelayHideTouchListener);
		findViewById(R.id.size_spinner).setOnTouchListener(
				mDelayHideTouchListener);
		findViewById(R.id.allign_spinner).setOnTouchListener(
				mDelayHideTouchListener);

		/* MY CODE */

		// get bitmap and project name

		bitmap = (Bitmap) getIntent().getParcelableExtra("IMAGE");
		String projectName = getIntent().getStringExtra("PROJECT_NAME");

		// Associate the Bitmap to the ImageView

		ImageView iview = (ImageView) findViewById(R.id.edit_image);

		iview.setImageBitmap(bitmap);
		iview.setVisibility(View.VISIBLE);

		// create spinner

		Spinner sizeSpinner = (Spinner) findViewById(R.id.size_spinner);
		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.size_array, R.layout.spinner_layout);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
		// Apply the adapter to the spinner
		sizeSpinner.setAdapter(adapter);

		// create spinner
		Spinner colorSpinner = (Spinner) findViewById(R.id.color_spinner);
		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		adapter = ArrayAdapter.createFromResource(this, R.array.color_array,
				R.layout.spinner_layout);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
		// Apply the adapter to the spinner
		colorSpinner.setAdapter(adapter);

		// create spinner
		Spinner allignSpinner = (Spinner) findViewById(R.id.allign_spinner);
		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		adapter = ArrayAdapter.createFromResource(this,
				R.array.allignment_array, R.layout.spinner_layout);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
		// Apply the adapter to the spinner
		allignSpinner.setAdapter(adapter);

	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		// Trigger the initial hide() shortly after the activity has been
		// created, to briefly hint to the user that UI controls
		// are available.
		delayedHide(100);
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			// Show the Up button in the action bar.
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			// TODO: If Settings has multiple levels, Up should navigate up
			// that hierarchy.
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Touch listener to use for in-layout UI controls to delay hiding the
	 * system UI. This is to prevent the jarring behavior of controls going away
	 * while interacting with activity UI.
	 */
	View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {
			if (AUTO_HIDE) {
				delayedHide(AUTO_HIDE_DELAY_MILLIS);
			}
			return false;
		}
	};

	Handler mHideHandler = new Handler();
	Runnable mHideRunnable = new Runnable() {
		@Override
		public void run() {
			mSystemUiHider.hide();
		}
	};

	/**
	 * Schedules a call to hide() in [delay] milliseconds, canceling any
	 * previously scheduled calls.
	 */
	private void delayedHide(int delayMillis) {
		mHideHandler.removeCallbacks(mHideRunnable);
		mHideHandler.postDelayed(mHideRunnable, delayMillis);
	}

	public Bitmap drawTextToBitmap(Context mContext, Bitmap bmp, String mText) {
		try {
			Resources resources = mContext.getResources();
			//float scale = resources.getDisplayMetrics().density;

			Spinner spin1 = (Spinner) findViewById(R.id.size_spinner);
			Spinner spin2 = (Spinner) findViewById(R.id.color_spinner);
			Spinner spin3 = (Spinner) findViewById(R.id.allign_spinner);

			int textSize = Integer.parseInt(spin1.getItemAtPosition(
					spin1.getSelectedItemPosition()).toString());
			String textColor = spin2.getItemAtPosition(
					spin2.getSelectedItemPosition()).toString();
			String textAllign = spin3.getItemAtPosition(
					spin3.getSelectedItemPosition()).toString();

			android.graphics.Bitmap.Config bitmapConfig = bmp.getConfig();

			// set default bitmap config if none
			if (bitmapConfig == null) {
				bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
			}

			// resource bitmaps are imutable,
			// so we need to convert it to mutable one
			Bitmap bitmap = bmp.copy(bitmapConfig, true);

			Canvas canvas = new Canvas(bitmap);

			// new antialised Paint
			Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

			// get spinner size value
			int size;

			switch (textSize) {

			case 1:
				size = 1;
				break;
			case 2:
				size = 2;
				break;
			case 3:
				size = 3;
				break;
			case 4:
				size = 4;
				break;
			case 6:
				size = 5;
				break;

			default:
				size = 1;
				break;
			}

			// text size in pixels
			paint.setTextSize((int) (size*12));

			// get spinner color and set
			
			int color;

			if (textColor.equals("Black")) {

				color = Color.BLACK;

			} else if (textColor.equals("White")) {

				color = Color.WHITE;

			} else if (textColor.equals("Gray")) {
				color = Color.GRAY;

			} else if (textColor.equals("Yellow")) {
				color = Color.YELLOW;

			} else if (textColor.equals("Red")) {
				color = Color.RED;

			} else if (textColor.equals("Blue")) {
				color = Color.BLUE;

			} else {

				color = Color.rgb(110, 110, 110);
			}
			
			// set paint
			
			paint.setColor(color);

			// text shadow
			paint.setShadowLayer(1f, 0f, 1f, Color.DKGRAY);
			
			// draw text to the Canvas center
						Rect textBounds = new Rect();
						paint.getTextBounds(mText, 0, mText.length(), textBounds);
						int x = 0,y = 0;
			

			// allign text
						
						paint.setTextAlign(Align.LEFT);

			if (textAllign.equals("Upper Left")) {

				 x =  0 - textBounds.left;
				 y =  0 - textBounds.top;
				
	

			} else if (textAllign.equals("Upper Right")) {
				
				 x =  bitmap.getWidth()  - textBounds.right;
				 y =  0 - textBounds.top;


			} else if (textAllign.equals("Down Left")) {
				
				x =  0  - textBounds.left;
				y =  bitmap.getHeight() - textBounds.bottom;


			} else if (textAllign.equals("Down Right")) {
				
				x =  bitmap.getWidth()  - textBounds.right;
				y =  bitmap.getHeight() - textBounds.bottom;


			} else if (textAllign.equals("Center")) {
				
				x = (bitmap.getWidth() - textBounds.width())/2;
	            y = (bitmap.getHeight() +textBounds.height())/2;


			} else {

				x = (bitmap.getWidth() - textBounds.width())/6;
	            y = (bitmap.getHeight() + textBounds.height())/5;
			}

			canvas.drawText(mText, x , y , paint);

			return bitmap;
		} catch (Exception e) {
			// TODO: handle exception

			return null;
		}

	}

	public void sendConfirm(View view) {

		EditText text1 = (EditText) findViewById(R.id.annotate_text);

		Bitmap bmp = drawTextToBitmap(this, bitmap, text1.getText().toString());

		ImageView iview = (ImageView) findViewById(R.id.edit_image);
		// Associate the annonated Bitmap to the ImageView

		iview.setImageBitmap(bmp);
		iview.setVisibility(View.VISIBLE);

		Intent resultIntent = new Intent();
		// get bitmap in image view
		iview = (ImageView) findViewById(R.id.edit_image);
		BitmapDrawable drawable = (BitmapDrawable) iview.getDrawable();
		Bitmap bitmap = drawable.getBitmap();

		resultIntent.putExtra("EDITED_IMAGE", bitmap);
		setResult(Activity.RESULT_OK, resultIntent);
		finish();
	}

	// cancel button click

	public void sendNotConfirm(View view) {

		this.finish();
	}
}
