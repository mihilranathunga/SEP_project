package cse.sep.mihil.bridgemanagementhelper;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.os.Build;

public class DataActivity extends Activity {
	
	String partType;
	String projectName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_data);
		// Show the Up button in the action bar.
		setupActionBar();
		
		partType = getIntent().getStringExtra("TYPE");
		projectName = getIntent().getStringExtra("PROJECT_NAME");
		
		// change title of the activity
				setTitle(projectName);
				
		EditText type = (EditText)findViewById(R.id.type);
		type.setText(partType);
		
		EditText name = (EditText)findViewById(R.id.partname);
		name.setText(projectName);
		
		/*//restore data
		if(savedInstanceState != null){
			
		EditText weight = (EditText)findViewById(R.id.weight);
		EditText man = (EditText)findViewById(R.id.manuf);
			
		CharSequence wgt = savedInstanceState.getCharSequence("weight");
		CharSequence manf = savedInstanceState.getCharSequence("man");
		
		if(wgt != null){
			weight.setText(wgt);
		}
		if(manf != null){
			man.setText(manf);
		}
		}*/
		
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
		getMenuInflater().inflate(R.menu.data, menu);
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

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		
		// Store UI state to the savedInstanceState.
		  // This bundle will be passed to onCreate on next call.  
		
		EditText weight = (EditText)findViewById(R.id.weight);
		String wgt = weight.getText().toString();

		 EditText man = (EditText)findViewById(R.id.manuf);
		 String manf = man.getText().toString();


		 outState.putString("weight",wgt);
		 outState.putString("man", manf);
		 


	}
	
	/*

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
		
		EditText weight = (EditText)findViewById(R.id.weight);
		EditText man = (EditText)findViewById(R.id.manuf);
			
		CharSequence wgt = savedInstanceState.getCharSequence("weight");
		CharSequence manf = savedInstanceState.getCharSequence("man");	
			weight.setText(wgt);
			man.setText(manf);
	}
	*/
	public void saveState(View view){
		
		this.finish();
	}
	public void cancel(View view){
		
		finish();
		
	}


}
