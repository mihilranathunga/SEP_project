package cse.sep.mihil.bridgemanagementhelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class EnterNameActivity extends Activity {

	static final String PUBLIC_STATIC_STRING_IDENTIFIER = "projectname";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_entername);
	}
	
	// ok button click

	public void sendOk(View view) {

		Intent resultIntent = new Intent();
		EditText pname = (EditText) findViewById(R.id.enter_project_name);
		resultIntent.putExtra(PUBLIC_STATIC_STRING_IDENTIFIER, pname.getText().toString());
		setResult(Activity.RESULT_OK, resultIntent);
		finish();
	}

	// cancel button click
	
	public void sendCancel(View view) {

		this.finish();
	}

}
