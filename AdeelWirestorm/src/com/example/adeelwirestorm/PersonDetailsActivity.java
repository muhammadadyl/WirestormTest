package com.example.adeelwirestorm;

import com.example.adeelwirestorm.helpers.ImageDownloader;
import com.example.adeelwirestorm.helpers.ImageDownloader.ImageExtender;
import com.example.adeelwirestorm.helpers.StoreImagesToStorage;
import com.example.adeelwirestorm.models.Person;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.Menu;
import android.widget.ImageView;

public class PersonDetailsActivity extends Activity implements ImageExtender {

	private ImageDownloader mImageDownloader;
	private Person person;
	private StoreImagesToStorage storeImage;
	private ImageView imageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_person__details);

		Intent personIntent = getIntent();

		person = (Person) personIntent.getSerializableExtra("person");

		storeImage = new StoreImagesToStorage(this);

		mImageDownloader = new ImageDownloader(this);

		imageView = (ImageView) findViewById(R.id.lrgImage);
		
		String Url = person.getLrgPicUrl();
		String FileName = Url.substring(Url.lastIndexOf("/") + 1);
		if (storeImage.isFileExist(FileName)) {
			imageView.setImageBitmap(storeImage
					.getThumbnail(FileName));
		} else {
			mImageDownloader.download(Url, imageView);
		}

	}

	@Override
	public void saveImage(String url, Bitmap image) {
		new SaveImagetask().execute(url);
	}
	

	class SaveImagetask extends AsyncTask<String, Void, Boolean> {

		@Override
		protected Boolean doInBackground(String... url) {
			Bitmap image = mImageDownloader.getBitmapFromCache(url[0]);
			if (image != null) {
				if (url[0].contains("-sm.jpg")) {
					String filename = url[0]
							.substring(url[0].lastIndexOf('/') + 1);
					if (!storeImage.isFileExist(filename))
						return storeImage.SaveImage(
								url[0].substring(url[0].lastIndexOf('/') + 1),
								image);
					else
						return true;
				} else if (url[0].contains("-lrg.jpg")) {
					String filename = url[0]
							.substring(url[0].lastIndexOf('/') + 1);
					if (!storeImage.isFileExist(filename))
						return storeImage.SaveImage(
								url[0].substring(url[0].lastIndexOf('/') + 1),
								image);
					else
						return true;
				}

			}
			return false;
		}
	}



}
