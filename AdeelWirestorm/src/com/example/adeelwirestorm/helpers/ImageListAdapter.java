package com.example.adeelwirestorm.helpers;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.example.adeelwirestorm.R;
import com.example.adeelwirestorm.helpers.ImageDownloader.ImageExtender;
import com.example.adeelwirestorm.models.Person;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ImageListAdapter extends BaseAdapter implements ImageExtender {

	private Context mContext;

	private LayoutInflater mLayoutInflater;

	public List<Person> persons = new ArrayList<Person>();

	private final ImageDownloader mImageDownloader;

	private StoreImagesToStorage storeImage;

	private Hashtable<String, Integer> urlId;

	// private OnFinishDeleteEvent OnFinishDeleteEventObj;

	public ImageListAdapter(Context context) {
		mContext = context;
		mLayoutInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mImageDownloader = new ImageDownloader(this);
		storeImage = new StoreImagesToStorage(context);
		urlId = new Hashtable<String, Integer>();
	}

	@Override
	public int getCount() {
		return persons.size();
	}

	@Override
	public Object getItem(int position) {
		return persons.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		RelativeLayout itemView;
		final Person item = persons.get(position);
		if (convertView == null) {
			itemView = (RelativeLayout) mLayoutInflater.inflate(
					R.layout.list_item, parent,
					false);
		} else {
			itemView = (RelativeLayout) convertView;
		}

		ImageView imageView = (ImageView) itemView.findViewById(R.id.listImage);
		TextView NameText = (TextView) itemView.findViewById(R.id.listName);
		TextView positionText = (TextView) itemView
				.findViewById(R.id.listPosition);
		String imageUrl = item.getSmallPicUrl();
		String fileName = imageUrl.substring(imageUrl.lastIndexOf('/') + 1);

		if (storeImage.isFileExist(fileName))
			imageView.setImageBitmap(storeImage.getThumbnail(fileName));
		else if (!urlId.containsKey(imageUrl)) {
			mImageDownloader.download(imageUrl, imageView);
			urlId.put(imageUrl, position);
		} else {
			mImageDownloader.download(imageUrl, imageView);
		}

		String title = item.getName();
		NameText.setText(title);
		String description = item.getPosition();
		if (description.trim().length() == 0) {
			description = "Sorry, no description for this image.";
		}
		positionText.setText(description);

		return itemView;
	}

	public void upDateEntries(List<Person> persons, boolean refresh) {
		if (refresh)
			this.persons = persons;
		else if (persons.size() > 0)
			this.persons.addAll(persons);
		notifyDataSetChanged();
	}

	public void updateCartEntries(List<Person> persons) {
		this.persons = persons;
		notifyDataSetChanged();
	}

	public List<Person> GetAllPersons() {
		return this.persons;
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
