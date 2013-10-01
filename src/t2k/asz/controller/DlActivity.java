package t2k.asz.controller;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.cordova.CordovaWebView;
import org.apache.cordova.api.CordovaInterface;
import org.apache.cordova.api.CordovaPlugin;

import t2k.asz.lib.model.ApiDl;
import t2k.asz.lib.model.DlObjectCreatorImpl;
import t2k.asz.lib.model.FutureCache;
import t2k.asz.lib.model.util.CallBack;
import t2k.asz.modle.DataModle;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;

public class DlActivity extends Activity implements CordovaInterface{

	private final ExecutorService threadPool = Executors.newCachedThreadPool();


	static WebView webview ;
	static ApiDl apidl;
	DlObjectCreatorImpl creator;
	int currentPosition=0;
	FutureCache cache;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dl);


		//final String initData =  getIntent().getExtras().getString("initData");
		//final String playData = getIntent().getExtras().getString("playData");
		currentPosition = getIntent().getExtras().getInt("position");

		final String[][] params =(String[][]) DataModle.the().raw;  //{{initData,playData}};

		creator = new DlObjectCreatorImpl("http://cto.timetoknow.com/cms/player/dl/index2a.html",params,this);

		cache = new FutureCache(2, 2, currentPosition, creator);

		apidl = (ApiDl) cache.objectForIndex(currentPosition);

		webview = apidl.getWebView();

		LinearLayout ll = (LinearLayout) findViewById(R.id.dlLayout);

		webview.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		ll.addView(webview );



		final Button nextbtn = (Button)findViewById(R.id.next);
		OnClickListener ln= new OnClickListener(){

			@Override
			public void onClick(View arg0) {

				LinearLayout ll = (LinearLayout) findViewById(R.id.dlLayout);

				ApiDl tapidl = (ApiDl) cache.objectForIndex(++currentPosition);

				if(tapidl!=null){

					ll.removeAllViews();
					
					apidl = tapidl;

					webview = apidl.getWebView();

					webview.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

					ll.addView(webview);

					
				}
			}

		};
		nextbtn.setOnClickListener(ln);


		final Button prevbtn = (Button)findViewById(R.id.prev);
		OnClickListener lp= new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				
				LinearLayout ll = (LinearLayout) findViewById(R.id.dlLayout);

				ApiDl tapidl = (ApiDl) cache.objectForIndex(--currentPosition);

				if(tapidl!=null){
					
					ll.removeAllViews();
					
					apidl=tapidl;
					
					webview = apidl.getWebView();

					webview.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

					ll.addView(webview);
				}
			}

		};
		prevbtn.setOnClickListener(lp);


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dl, menu);
		return true;
	}
	@Override	
	protected void onDestroy() {
		super.onDestroy();
//		if (webview!= null) {
//			webview.loadUrl("javascript:try{cordova.require('cordova/channel').onDestroy.fire();}catch(e){console.log('exception firing destroy event from native');};");
//			webview.loadUrl("about:blank");
//			((CordovaWebView)webview).handleDestroy();
//		}
	}

	@Override
	protected void onPause() {
		super.onPause();

	}

	@Override
	protected void onResume() {
		super.onResume();

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

		return null;
	}

	@Override
	public void setActivityResultCallback(CordovaPlugin cordovaPlugin) {

	}

	@Override
	public void startActivityForResult(CordovaPlugin cordovaPlugin,
			Intent intent, int resultCode) {

	}



}
