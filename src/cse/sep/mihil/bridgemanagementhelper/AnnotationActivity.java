package cse.sep.mihil.bridgemanagementhelper;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Bundle;
import android.widget.ImageView;

public class AnnotationActivity extends Activity {
	
	public static final String PUBLIC_STATIC_PROJECT_IDENTIFIER = "projectname";
	public static final String PUBLIC_STATIC_IMAGEPATH_IDENTIFIER = "image";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_annotation);
		
		String image = (String)getIntent().getExtras().get(PUBLIC_STATIC_IMAGEPATH_IDENTIFIER);
		String title = (String) getIntent().getStringExtra(PUBLIC_STATIC_PROJECT_IDENTIFIER);
/*		BitmapFactory.Options options = new BitmapFactory.Options(); 

		 Bitmap imageFile = BitmapFactory.decodeFile(image, options);
		 
		 ImageView editableImage = (ImageView) findViewById(R.id.taken_image);

		 editableImage.setImageBitmap(imageFile);*/
		
		
	}
	

}
