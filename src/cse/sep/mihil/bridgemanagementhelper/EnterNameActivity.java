package cse.sep.mihil.bridgemanagementhelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class EnterNameActivity extends Activity {

	public static final String PUBLIC_STATIC_PROJECT_IDENTIFIER = "projectname";
	public static final String PUBLIC_STATIC_IMAGEPATH_IDENTIFIER = "image";
			

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_entername);
	}

	public void sendOk(View view) {
		
		// result is send back to the recieving activity

		Intent resultIntent = new Intent();
		EditText pname = (EditText) findViewById(R.id.enter_project_name);
		resultIntent.putExtra(PUBLIC_STATIC_PROJECT_IDENTIFIER, pname.getText()
				.toString());
		setResult(Activity.RESULT_OK, resultIntent);
		finish();
	}

	public void sendCancel(View view) {

		this.finish();
	}

}
