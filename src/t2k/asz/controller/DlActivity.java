package t2k.asz.controller;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.cordova.CordovaWebView;
import org.apache.cordova.api.CordovaInterface;
import org.apache.cordova.api.CordovaPlugin;
import org.apache.cordova.api.LOG;

import t2k.asz.lib.model.ApiDl;
import t2k.asz.lib.model.util.CallBack;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.widget.LinearLayout;

public class DlActivity extends Activity implements CordovaInterface{

	private final ExecutorService threadPool = Executors.newCachedThreadPool();

	//public String initData="";
	//public String playData="";

	WebView webview ;
	ApiDl apidl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dl);


		final String initData =  getIntent().getExtras().getString("initData");
		final String playData = getIntent().getExtras().getString("playData");

		final String[] params={initData,playData};


		this.apidl = new ApiDl( "http://cto.timetoknow.com/cms/player/dl/index2a.html", new CallBack(params){



			@Override
			public void call(String msg){


				apidl.callOnLoadded(new CallBack(params){@Override
					public void call(String msg){

					apidl.initPlayer(this.params[0], new CallBack(this.params){

						@Override
						public void call(String msg){
							
							apidl.playSequence(this.params[1], new CallBack(){}, new CallBack(){});
							
						}}, new CallBack(){});

				}}, new CallBack(){});

			}
		} , this);


		this.webview = this.apidl.getWebView();


		final LinearLayout ll = (LinearLayout) findViewById(R.id.dlLayout);

		webview.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		ll.addView(webview );



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
		if (this.webview!= null) {
			this.webview.loadUrl("javascript:try{cordova.require('cordova/channel').onDestroy.fire();}catch(e){console.log('exception firing destroy event from native');};");
			this.webview.loadUrl("about:blank");
			((CordovaWebView)this.webview).handleDestroy();
		}
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
