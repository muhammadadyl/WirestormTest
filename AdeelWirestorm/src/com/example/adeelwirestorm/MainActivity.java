package com.example.adeelwirestorm;

import com.example.adeelwirestorm.helpers.ImageListAdapter;
import com.example.adeelwirestorm.helpers.LoadFeedData;
import com.example.adeelwirestorm.models.Person;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;

public class MainActivity extends ListActivity {
	
	private ImageListAdapter adapter;
	private LoadFeedData loadFeedData;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        adapter = new ImageListAdapter(this);
		setListAdapter(adapter);
		loadList(true);
    }
    
    @Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Person person = (Person) adapter.getItem(position);
		Log.i("Name", person.getName());
		Intent personOnItemIntent = new Intent(MainActivity.this,
				PersonDetailsActivity.class);
		personOnItemIntent.putExtra("person", person);
		personOnItemIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(personOnItemIntent);

	}

	public void loadList(boolean refresh) {
		loadFeedData = new LoadFeedData(adapter, refresh, MainActivity.this);
		loadFeedData.execute();
	}

    
}
