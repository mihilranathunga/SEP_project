package cse.sep.mihil.bridgemanagementhelper;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;

public class ImageEditActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_edit);
		// Show the Up button in the action bar.
		setupActionBar();
		
		// get bitmap and project name
		
		Bitmap bitmap = (Bitmap)getIntent().getParcelableExtra("IMAGE");
		String projectName = getIntent().getStringExtra("PROJECT_NAME");
		
		/* Associate the Bitmap to the ImageView */
		
		ImageView iview = (ImageView)findViewById(R.id.edit_image);
		
		iview.setImageBitmap(bitmap);
		iview.setVisibility(View.VISIBLE);
		
		Bitmap bmp =drawTextToBitmap(this,bitmap,"Hello Android");
		
		iview = (ImageView)findViewById(R.id.edit_image);
		
		// create spinner
		
		Spinner sizeSpinner = (Spinner) findViewById(R.id.size_spinner);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		        R.array.size_array, R.layout.spinner_layout);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
		// Apply the adapter to the spinner
		sizeSpinner.setAdapter(adapter);
		
		// create spinner
		Spinner colorSpinner = (Spinner) findViewById(R.id.color_spinner);
		// Create an ArrayAdapter using the string array and a default spinner layout
		adapter = ArrayAdapter.createFromResource(this,
		        R.array.color_array, R.layout.spinner_layout);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
		// Apply the adapter to the spinner
		colorSpinner.setAdapter(adapter);
		
		// create spinner
				Spinner allignSpinner = (Spinner) findViewById(R.id.allign_spinner);
				// Create an ArrayAdapter using the string array and a default spinner layout
				 adapter = ArrayAdapter.createFromResource(this,
				        R.array.allignment_array, R.layout.spinner_layout);
				// Specify the layout to use when the list of choices appears
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
				// Apply the adapter to the spinner
				allignSpinner.setAdapter(adapter);
				
			
		/* Associate the annonated Bitmap to the ImageView */
		
		iview.setImageBitmap(bmp);
		iview.setVisibility(View.VISIBLE);
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.image_edit, menu);
		return true;
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
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	 public Bitmap drawTextToBitmap(Context mContext, Bitmap bmp,  String mText) {
		    try {
		    	Resources resources = mContext.getResources();
		    	 float scale = resources.getDisplayMetrics().density;

		            android.graphics.Bitmap.Config bitmapConfig =  bmp.getConfig();
		            
		            // set default bitmap config if none
		            if(bitmapConfig == null) {
		              bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
		            }
		            
		            // resource bitmaps are imutable,
		            // so we need to convert it to mutable one
		            Bitmap bitmap = bmp.copy(bitmapConfig, true);

		            Canvas canvas = new Canvas(bitmap);
		            
		            // new antialised Paint
		            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		            // text color - #3D3D3D
		            paint.setColor(Color.rgb(110,110, 110));
		            // text size in pixels
		            paint.setTextSize((int) (12 * scale));
		            // text shadow
		            paint.setShadowLayer(1f, 0f, 1f, Color.DKGRAY);

		            // draw text to the Canvas center
		            Rect bounds = new Rect();
		            paint.getTextBounds(mText, 0, mText.length(), bounds);
		            int x = (bitmap.getWidth() - bounds.width())/6;
		            int y = (bitmap.getHeight() + bounds.height())/5;

		          canvas.drawText(mText, x * scale, y * scale, paint);

		            return bitmap;
		    } catch (Exception e) {
		        // TODO: handle exception



		        return null;
		    }

		  }
		public void sendOk(View view) {

			Intent resultIntent = new Intent();
			// get bitmap in image view
			ImageView iview = (ImageView)findViewById(R.id.taken_image);
			BitmapDrawable drawable = (BitmapDrawable)iview.getDrawable();
			Bitmap bitmap = drawable.getBitmap();
			
			resultIntent.putExtra("EDITED_IMAGE", bitmap);
			setResult(Activity.RESULT_OK, resultIntent);
			finish();
		}

		// cancel button click
		
		public void sendCancel(View view) {

			this.finish();
		}

}
