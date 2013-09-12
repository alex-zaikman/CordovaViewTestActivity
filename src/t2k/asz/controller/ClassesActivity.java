package t2k.asz.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.cordova.api.LOG;
import org.json.JSONArray;
import org.json.JSONException;

import t2k.asz.lib.model.util.CallBack;
import t2k.asz.lib.model.util.JsonHelper;
import t2k.asz.modle.DataModle;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;


public class ClassesActivity extends Activity {




	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_classes);
		final GridView myGallery = (GridView)findViewById(R.id.classgallery);
		final ImageAdapter adupt = new ImageAdapter(this,(List<Bitmap>) DataModle.the().rawList);
		
		myGallery.setAdapter(adupt);
		

	}




	public  class ImageAdapter extends BaseAdapter {
		private Context mContext;

		private List<Bitmap> mThumbIds=null;

		public ImageAdapter(Context c,List<Bitmap> mThumbId) {
			mContext = c;
			this.mThumbIds =mThumbId;
		}

		public int getCount() {  
			int ret =mThumbIds.size();
			return ret;
		}

		public Object getItem(int position) {
			return mThumbIds.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			
			ImageView view;
			
			LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			view = (ImageView) mInflater.inflate(R.layout.class_cell, parent, false);
			 
			view.setLayoutParams(new GridView.LayoutParams(220, 220));
			view.setScaleType(ImageView.ScaleType.CENTER_CROP);
			view.setPadding(8, 8, 8, 8);

			view.setImageBitmap(mThumbIds.get(position));
			return view;
		}



	}





}
