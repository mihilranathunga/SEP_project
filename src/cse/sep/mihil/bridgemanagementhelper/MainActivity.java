package cse.sep.mihil.bridgemanagementhelper;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

public class MainActivity extends Activity {

	private static final int ACTION_TAKE_PHOTO_B = 1;

	/*
	 * private static final int ACTION_TAKE_PHOTO_S = 2; private static final
	 * String BITMAP_STORAGE_KEY = "viewbitmap"; private static final String
	 * IMAGEVIEW_VISIBILITY_STORAGE_KEY = "imageviewvisibility";
	 */

	private ImageView mImageView;
	private Bitmap mImageBitmap;

	private String mCurrentPhotoPath;

	private static final String JPEG_FILE_PREFIX = "IMG_";
	private static final String JPEG_FILE_SUFFIX = ".jpg";
	private static final int CAPTURE_PROJECT_NAME_REQUEST_CODE = 100;
	private static final int ANNOTATE_ACTIVITY_REQUEST_CODE = 0;

	private static final int RESULT_LOAD_IMAGE = 20;

	private AlbumStorageDirFactory mAlbumStorageDirFactory = null;

	/* Photo album for this application */
	private String getAlbumName() {
		String album_name = getString(R.string.album_name);
		return album_name;
	}

	private File getAlbumDir() {
		File storageDir = null;

		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {

			storageDir = mAlbumStorageDirFactory
					.getAlbumStorageDir(getAlbumName());

			if (storageDir != null) {
				if (!storageDir.mkdirs()) {
					if (!storageDir.exists()) {
						Log.d("CameraSample", "failed to create directory");
						return null;
					}
				}
			}

		} else {
			this.finish();
			Log.v(getString(R.string.app_name),
					"External storage is not mounted READ/WRITE.");
		}

		return storageDir;
	}

	private File createImageFile() throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
		File albumF = getAlbumDir();
		File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX,
				albumF);
		return imageF;
	}

	private File setUpPhotoFile() throws IOException {

		File f = createImageFile();
		mCurrentPhotoPath = f.getAbsolutePath();

		return f;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mImageView = (ImageView) findViewById(R.id.taken_image);

		Button picBtn = (Button) findViewById(R.id.new_project_button);
		setBtnListenerOrDisable(picBtn, mTakePicOnClickListener,
				MediaStore.ACTION_IMAGE_CAPTURE);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
			mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
		} else {
			mAlbumStorageDirFactory = new BaseAlbumDirFactory();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	// get image capture intent
	private void dispatchTakePictureIntent(int actionCode) {

		// create Intent to take a picture and return control to the calling
		// application
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		switch (actionCode) {
		case ACTION_TAKE_PHOTO_B:
			File f = null;

			try {
				f = setUpPhotoFile(); // create a file to save the image
				mCurrentPhotoPath = f.getAbsolutePath();
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(f));
			} catch (IOException e) {
				e.printStackTrace();
				f = null;
				mCurrentPhotoPath = null;
			}
			break;

		default:
			break;
		} // switch

		// start the image capture Intent

		startActivityForResult(takePictureIntent, actionCode);

	}

	/*
	 * private void handleSmallCameraPhoto(Intent intent) { Bundle extras =
	 * intent.getExtras(); mImageBitmap = (Bitmap) extras.get("data");
	 * mImageView.setImageBitmap(mImageBitmap);
	 * mImageView.setVisibility(View.VISIBLE);
	 * 
	 * }
	 */

	Button.OnClickListener mTakePicOnClickListener = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			dispatchTakePictureIntent(ACTION_TAKE_PHOTO_B);
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (requestCode) {
		case ACTION_TAKE_PHOTO_B: {
			if (resultCode == RESULT_OK) {
				// handleBigCameraPhoto();

				// Image captured and saved to fileUri specified in the Intent
				Toast.makeText(this, "Image saved to:\n" + mCurrentPhotoPath,
						Toast.LENGTH_LONG).show();
				// start activity to get project name
				Intent intent = new Intent(this, EnterNameActivity.class);
				startActivityForResult(intent,CAPTURE_PROJECT_NAME_REQUEST_CODE);
			}
		}
			break;
		case RESULT_LOAD_IMAGE: {
			if (resultCode == RESULT_OK & data != null) {
				Uri selectedImage = data.getData();
				String[] filePathColumn = { MediaStore.Images.Media.DATA };

				Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
				cursor.moveToFirst();

				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				String picturePath = cursor.getString(columnIndex);
				cursor.close();

				// String picturePath contains the path of selected Image
				// it is stored in mCurrentPhotopath Variable to attach it to another activity
				
				mCurrentPhotoPath = picturePath;
				
				// start activity to get project name
				Intent intent = new Intent(this, EnterNameActivity.class);
				startActivityForResult(intent,CAPTURE_PROJECT_NAME_REQUEST_CODE);
				
			}
			break;
		} // ACTION_TAKE_PHOTO_B

		/*
		 * case ACTION_TAKE_PHOTO_S: { if (resultCode == RESULT_OK) {
		 * handleSmallCameraPhoto(data); } break; } // ACTION_TAKE_PHOTO_S
		 */
		case CAPTURE_PROJECT_NAME_REQUEST_CODE: {
			if (resultCode == RESULT_OK) {
				// Project name entered
				String newText = data
						.getStringExtra(EnterNameActivity.PUBLIC_STATIC_STRING_IDENTIFIER);

				Intent annotate = new Intent(this, AnnotationActivity.class);
				annotate.putExtra("PROJECT_NAME", newText);
				annotate.putExtra("IMAGE_PATH", mCurrentPhotoPath);

				startActivityForResult(annotate, ANNOTATE_ACTIVITY_REQUEST_CODE);

				// reset photopath variable
				mCurrentPhotoPath = null;
			}
		}

		}

	}

	/*
	 * @Override protected void onSaveInstanceState(Bundle outState) {
	 * outState.putParcelable(BITMAP_STORAGE_KEY, mImageBitmap);
	 * outState.putParcelable(VIDEO_STORAGE_KEY, mVideoUri);
	 * outState.putBoolean(IMAGEVIEW_VISIBILITY_STORAGE_KEY, (mImageBitmap !=
	 * null) ); outState.putBoolean(VIDEOVIEW_VISIBILITY_STORAGE_KEY, (mVideoUri
	 * != null) ); super.onSaveInstanceState(outState); }
	 * 
	 * @Override protected void onRestoreInstanceState(Bundle
	 * savedInstanceState) { super.onRestoreInstanceState(savedInstanceState);
	 * mImageBitmap = savedInstanceState.getParcelable(BITMAP_STORAGE_KEY);
	 * mVideoUri = savedInstanceState.getParcelable(VIDEO_STORAGE_KEY);
	 * mImageView.setImageBitmap(mImageBitmap); mImageView.setVisibility(
	 * savedInstanceState.getBoolean(IMAGEVIEW_VISIBILITY_STORAGE_KEY) ?
	 * ImageView.VISIBLE : ImageView.INVISIBLE );
	 * mVideoView.setVideoURI(mVideoUri); mVideoView.setVisibility(
	 * savedInstanceState.getBoolean(VIDEOVIEW_VISIBILITY_STORAGE_KEY) ?
	 * ImageView.VISIBLE : ImageView.INVISIBLE ); }
	 */

	/**
	 * Indicates whether the specified action can be used as an intent. This
	 * method queries the package manager for installed packages that can
	 * respond to an intent with the specified action. If no suitable package is
	 * found, this method returns false.
	 * http://android-developers.blogspot.com/2009/01/can-i-use-this-intent.html
	 * 
	 * @param context
	 *            The application's environment.
	 * @param action
	 *            The Intent action to check for availability.
	 * 
	 * @return True if an Intent with the specified action can be sent and
	 *         responded to, false otherwise.
	 */
	public static boolean isIntentAvailable(Context context, String action) {
		final PackageManager packageManager = context.getPackageManager();
		final Intent intent = new Intent(action);
		List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
				PackageManager.MATCH_DEFAULT_ONLY);
		return list.size() > 0;
	}

	public void sendMessage(View view) {

		Intent i = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

		startActivityForResult(i, RESULT_LOAD_IMAGE);

	}

	private void setBtnListenerOrDisable(Button btn,
			Button.OnClickListener onClickListener, String intentName) {
		if (isIntentAvailable(this, intentName)) {
			btn.setOnClickListener(onClickListener);
		} else {
			btn.setText(getText(R.string.cannot).toString() + " "
					+ btn.getText());
			btn.setClickable(false);
		}
	}

}
