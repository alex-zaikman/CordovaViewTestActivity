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
		final ImageAdapter adupt = new ImageAdapter(this,(List<Bitmap>) DataModle.the().ldata);

		

				myGallery.setAdapter(adupt);
				myGallery.invalidate();
			

		



	}




	public  class ImageAdapter extends BaseAdapter {
		private Context mContext;

		private List<Bitmap> mThumbIds=null;

		public ImageAdapter(Context c,List<Bitmap> mThumbId) {
			mContext = c;
			this.mThumbIds =mThumbId;
		}

		public int getCount() {     
			return mThumbIds.size();
		}

		public Object getItem(int position) {
			return mThumbIds.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView imageView;
			if (convertView == null) {  // if it's not recycled, initialize some attributes
				imageView = new ImageView(mContext);
				imageView.setLayoutParams(new GridView.LayoutParams(220, 220));
				imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
				imageView.setPadding(8, 8, 8, 8);
			} else {
				imageView = (ImageView) convertView;
			}

			imageView.setImageBitmap(mThumbIds.get(position));
			return imageView;
		}



	}





}
