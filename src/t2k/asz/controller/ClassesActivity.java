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

	private List<Bitmap> mimages =new ArrayList<Bitmap>();


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_classes);
		final GridView myGallery = (GridView)findViewById(R.id.classgallery);
		final ImageAdapter adupt = new ImageAdapter(this,mimages);

		__init(new CallBack(){

			@Override
			public void call(String msg){

				myGallery.setAdapter(adupt);
				myGallery.invalidate();
			}

		});



	}

	private void __init(CallBack ca){

		

		DataModle.the().cdv.getCookie( new CallBack(){

			@Override
			public void call(String msg){
				
				
		///get classes ---getStudyClassesOnSuccess
		DataModle.the().cdv.getStudyClasses(new CallBack(){



			@Override
			public void call(String msg) {


				JSONArray jObj;
				try {
					jObj = new JSONArray(msg);

					List<?> jList =JsonHelper.toList(jObj);

					DataModle.the().ldata =jList; 
					//get class images
					Map<String,Object> aclass;
					int size = jList.size();
					int i=0;
					for(Object a : jList){


						aclass = (Map<String, Object>) a;

						String imageUrl = (String) aclass.get("imageURL");


						try {



							final URL url = new URL("http://cto.timetoknow.com/"+imageUrl);

							


									try {
										HttpURLConnection connection;
										connection = (HttpURLConnection) url.openConnection();
										connection.setRequestProperty("Cookie", msg);
										connection.setDoInput(true);
										connection.connect();	  

										int code =  connection.getResponseCode();
										LOG.d("asz",""+code);

										InputStream input = connection.getInputStream();

										Bitmap myBitmap = BitmapFactory.decodeStream(input);
										mimages.add(myBitmap);

										

									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} 
								

						} catch (Exception e) {
							e.printStackTrace();
						}

					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}, new CallBack(){});


			}

		}, new CallBack(){

			@Override
			public void call(String msg){
				LOG.d("asz",""+msg);
			}

		});   	
							
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
			return null;
		}

		public long getItemId(int position) {
			return 0;
		}

		// create a new ImageView for each item referenced by the Adapter
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView imageView;
			if (convertView == null) {  // if it's not recycled, initialize some attributes
				imageView = new ImageView(mContext);
				imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
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
