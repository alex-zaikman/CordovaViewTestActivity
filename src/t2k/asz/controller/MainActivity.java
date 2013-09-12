package t2k.asz.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.cordova.CordovaWebView;
import org.apache.cordova.api.CordovaInterface;
import org.apache.cordova.api.CordovaPlugin;
import org.apache.cordova.api.LOG;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import t2k.asz.lib.model.ApiMT;
import t2k.asz.lib.model.util.CallBack;
import t2k.asz.lib.model.util.JsonHelper;
import t2k.asz.modle.DataModle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends Activity implements CordovaInterface{



	
	 
	public final String NAME_SPASE = "ASZNSP";
	private List<Bitmap> mimages =new ArrayList<Bitmap>();
	
	private String TAG = "ASZ_CORDOVA_ACTIVITY";
	private String T2K = "ASZ_T2K_API";
	private final ExecutorService threadPool = Executors.newCachedThreadPool();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		setContentView(R.layout.activity_main);

		DataModle.the().cdv = new ApiMT("http://cto.timetoknow.com/lms/js/libs/t2k/t2ka.jsp", new CallBack(){

			@Override
			public void call(String msg){

				DataModle.the().cdv.callOnLoadded(new CallBack(){

					@Override
					public void call(String msg){

						Log.d(T2K,"cdv ready");
						Button btndo = (Button) findViewById(R.id.btnDo);
						btndo.setEnabled(true);
						btndo.invalidate();
					}

				}, new CallBack(){});
			}

		}, this);

		Button btndo = (Button) findViewById(R.id.btnDo);

		OnClickListener nd = new OnClickListener(){


			@Override
			public void onClick(View v) {

				
				EditText user = (EditText) findViewById(R.id.userName);
				EditText pass = (EditText) findViewById(R.id.password);
				
				DataModle.the().cdv.logInMF(user.getText().toString(), pass.getText().toString(), new CallBack(){

					@Override
					public void call(String msg){


						try {

							Log.d(T2K , "logged in");

							JSONObject jObj = new JSONObject(msg);
							Map<String, Object> jmap =JsonHelper.toMap(jObj);

							DataModle.the().mdata = jmap;
							
							{

								

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
																i++;
																
																if(i==size){
																	
																	DataModle.the().ldata = mimages;
																	
																	Intent intent = new Intent(getApplicationContext(), ClassesActivity.class);
																	
																    startActivity(intent);
																
																}				

															} catch (IOException e) {
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
							


						} catch (JSONException e) {
							e.printStackTrace();
						}


					}
				}, new CallBack(){

					@Override
					public void call(String msg){
						Log.d(TAG , "failed:  "+msg);

					}
				});

			}

		};


		btndo.setOnClickListener(nd);
		btndo.setEnabled(false);
	}

	//----------------------------------------------------------------------------------------------------
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override	
	protected void onDestroy() {
		super.onDestroy();
		if (DataModle.the().cdv!= null) {
			DataModle.the().cdv.getWebView().loadUrl("javascript:try{cordova.require('cordova/channel').onDestroy.fire();}catch(e){console.log('exception firing destroy event from native');};");
			DataModle.the().cdv.getWebView().loadUrl("about:blank");
			((CordovaWebView)DataModle.the().cdv.getWebView()).handleDestroy();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.d(TAG, "onPause");
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG, "onResume");
	}

	@Override
	public Activity getActivity() {
		return this;
	}

	@Override
	public ExecutorService getThreadPool() {
		return threadPool;
	}

	@Override
	public Object onMessage(String message, Object obj) {
		Log.d(TAG, "onMessage message: "+message);
		return null;
	}

	@Override
	public void setActivityResultCallback(CordovaPlugin cordovaPlugin) {
		Log.d(TAG, "setActivityResultCallback is unimplemented");
	}

	@Override
	public void startActivityForResult(CordovaPlugin cordovaPlugin,
			Intent intent, int resultCode) {
		Log.d(TAG, "startActivityForResult is unimplemented");
	}


}