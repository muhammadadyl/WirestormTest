package com.example.adeelwirestorm.helpers;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.adeelwirestorm.models.Person;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.widget.Toast;

public class LoadFeedData extends AsyncTask<String, Integer, List<Person>> {

	private static boolean _connectionStatus;

	private static final String PERSON_URL = "http://s3-us-west-2.amazonaws.com/wirestorm/assets/response.json";

	private final ImageListAdapter mAdapter;
	private final boolean mRefresh;
	private final Context mContext;

	public LoadFeedData(ImageListAdapter adapter, boolean refresh, Context context) {
		this.mAdapter = adapter;
		this.mRefresh = refresh;
		this.mContext = context;
		_connectionStatus = GetConnectivityStatus(context);
	}

	@Override
	protected List<Person> doInBackground(String... url) {
		if (_connectionStatus) {
			JSONParser jsonParser = new JSONParser(mContext);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			String json = jsonParser.makeHttpRequest(PERSON_URL, "GET",
					params);
			if (json != null) {
				try {
						JSONArray jsonArray = new JSONArray(json);
						List<Person> persons = new ArrayList<Person>();
						if (jsonArray.length() > 0) {
							for (int i = 0; i < jsonArray.length(); i++) {
								JSONObject jsonItem = jsonArray
										.getJSONObject(i);
								Person person = new Person();
								person.setName(jsonItem.getString("name"));
								person.setPosition(jsonItem.getString("position"));
								person.setSmallPicUrl(jsonItem.getString("smallpic"));
								person.setLrgPicUrl(jsonItem.getString("lrgpic"));
								persons.add(person);
							}
						}
						return persons;
				} catch (JSONException e) {

				}
			}else{
				Toast.makeText(this.mContext, "Failed to fetch service. Please try some time later", Toast.LENGTH_LONG).show();
			} 
		}
		return new ArrayList<Person>();
	}

	@Override
	protected void onPostExecute(List<Person> entries) {
		if (entries.size() > 0) {
			mAdapter.upDateEntries(entries, mRefresh);
			new UpdateDb().execute();
		}
	}

	class UpdateDb extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... params) {
			new StoreImagesToStorage(mContext).cleanOldFiles(mAdapter.GetAllPersons());
			return null;
		}
	}

	private boolean GetConnectivityStatus(Context context) {
		ConnectivityManager conMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo i = conMgr.getActiveNetworkInfo();
		if (i == null)
			return false;
		if (!i.isConnected())
			return false;
		if (!i.isAvailable())
			return false;
		return true;
	}

}
