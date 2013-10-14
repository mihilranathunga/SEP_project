package cse.sep.mihil.bridgemanagementhelper;

import java.io.File;

import android.app.*;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.*;

public class AnnotationActivity extends Activity {

	private String photoPath;
	private String projectName;
	//private String partType;
	public Bitmap bitmap;
	
	private static final int EDIT_PHOTO = 35;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_annotation);
		
		// get image path and project name from intent
		
		photoPath = getIntent().getStringExtra("IMAGE_PATH");
		projectName = getIntent().getStringExtra("PROJECT_NAME");
		
		// change title of the activity
		setTitle(projectName);
		
		handleBigCameraPhoto();
		
		Spinner spinner = (Spinner) findViewById(R.id.selection_spinner);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		        R.array.selection_array, R.layout.spinner_layout);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);
		
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		        Object item = parent.getItemAtPosition(pos);
		        data(item.toString());
		        
		        
		    }
		    public void onNothingSelected(AdapterView<?> parent) {
		    }
		});
		
		
 
		
	}
	
	private void handleBigCameraPhoto() {

		if (photoPath != null) {
			setPic();
			galleryAddPic();
			//photoPath = null;
		}

	}
	private void galleryAddPic() {
	    Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
		File f = new File(photoPath);
	    Uri contentUri = Uri.fromFile(f);
	    mediaScanIntent.setData(contentUri);
	    this.sendBroadcast(mediaScanIntent);
}
	

	private void setPic() {

		/* There isn't enough memory to open up more than a couple camera photos */
		/* So pre-scale the target bitmap into which the file is decoded */

		/* Get the size of the ImageView */
		ImageView iview = (ImageView)findViewById(R.id.taken_image);
		int targetW = iview.getWidth();
		int targetH = iview.getHeight();

		/* Get the size of the image */
		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		bmOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(photoPath, bmOptions);
		int photoW = bmOptions.outWidth;
		int photoH = bmOptions.outHeight;
		
		/* Figure out which way needs to be reduced less */
		int scaleFactor = 1;
		if ((targetW > 0) || (targetH > 0)) {
			scaleFactor = Math.min(photoW/targetW, photoH/targetH);	
		}

		/* Set bitmap options to scale the image decode target */
		bmOptions.inJustDecodeBounds = false;
		bmOptions.inSampleSize = scaleFactor;
		bmOptions.inPurgeable = true;

		/* Decode the JPEG file into a Bitmap */
		bitmap = BitmapFactory.decodeFile(photoPath, bmOptions);
		
		/* Associate the Bitmap to the ImageView */
		iview.setImageBitmap(bitmap);
		iview.setVisibility(View.VISIBLE);

	}
	public void annotate(View view){
		
		// get bitmap in image view
		ImageView iview = (ImageView)findViewById(R.id.taken_image);
		BitmapDrawable drawable = (BitmapDrawable)iview.getDrawable();
		Bitmap bitmap = drawable.getBitmap();
		
		// pass bitmap and projectname to image edit intent
		Intent intent = new Intent(this, ImageAnnotateActivity.class);
		intent.putExtra("IMAGE_NAME", projectName);
		intent.putExtra("IMAGE", bitmap);
		startActivityForResult(intent,EDIT_PHOTO);
		
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		switch (requestCode) {
		
		// get's result from annotation
		
		case EDIT_PHOTO: {
			if (resultCode == RESULT_OK & data != null) {
				bitmap = (Bitmap)data.getParcelableExtra("EDITED_IMAGE");
				/* Associate the Bitmap to the ImageView */
				ImageView iview = (ImageView)findViewById(R.id.taken_image);
				iview.setImageBitmap(bitmap);
				iview.setVisibility(View.VISIBLE);
			}
		}
		break;
		
		default:
			break;
		
	}
	}
	
	public void resetImage(View view){
		
		handleBigCameraPhoto();
	}
	public void openData(View view){
		
		Spinner spinner = (Spinner) findViewById(R.id.selection_spinner);
		String partType = spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();
		
		data(partType);
		
	}
	public void data(String partType){
		
		// work if spinner item was selected
		
		if(!partType.equals("Select type")){
		
		Intent intent = new Intent(this,DataActivity.class);
		intent.putExtra("PROJECT_NAME", projectName);
		intent.putExtra("TYPE", partType);
		startActivity(intent);
		}
		
	}
	public void saveData(View view){
		
	}

}
