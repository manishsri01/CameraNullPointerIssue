package camera.nullpointer.androidhub4you.solution;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
/**
 * 
 * @author manishs
 *
 */
public class MainActivity extends Activity implements OnClickListener {

	private Button btnCamera;
	private ImageView imageView;
	private String _imageCapturedName = null;
	private File fileName = null;
	private final int TAKE_FRONT_PHOTO = 1;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		btnCamera = (Button) findViewById(R.id.button_camera);
		imageView = (ImageView) findViewById(R.id.imageView1);

		btnCamera.setOnClickListener(this);

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.button_camera:
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			_imageCapturedName = "Image_"
					+ String.valueOf(System.currentTimeMillis());
			fileName = Helper.createFileInSDCard(Helper.getTempFile()
					+ "=TestFolder/", _imageCapturedName + ".JPG");

			intent = new Intent("android.media.action.IMAGE_CAPTURE").putExtra(
					android.provider.MediaStore.EXTRA_OUTPUT,
					Uri.fromFile(new File(fileName.toString())));
			// ******** code for crop image
			intent.putExtra("crop", "true");
			intent.putExtra("aspectX", 0);
			intent.putExtra("aspectY", 0);
			intent.putExtra("outputX", 200);
			intent.putExtra("outputY", 150);
			//Read more: http://www.androidhub4you.com/2012/07/how-to-crop-image-from-camera-and.html#ixzz3C3ka12wl
			startActivityForResult(intent, TAKE_FRONT_PHOTO);
			break;
		
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case TAKE_FRONT_PHOTO:
				saveImage(TAKE_FRONT_PHOTO);
				break;
			
			}
		}
	}


	private void saveImage(int requestCode) {
		try {

			Bitmap bitmap = Helper.decodeFile(fileName);
			bitmap = Bitmap.createScaledBitmap(bitmap, 480, 320, true);
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes);

			File f = new File(fileName.toString());
			FileOutputStream fo = new FileOutputStream(f);
			fo.write(bytes.toByteArray());

			fo.flush();
			fo.close();

			// _bcardFront=fileName.toString();
			imageView.setImageBitmap(bitmap);

		} catch (Exception e) {
			e.printStackTrace();
		} catch (OutOfMemoryError o) {
			o.printStackTrace();

		}

	}

}