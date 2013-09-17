package t2k.asz.controller;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CordovaWebViewClient;
import org.apache.cordova.IceCreamCordovaWebViewClient;
import org.apache.cordova.api.CordovaInterface;
import org.apache.cordova.api.CordovaPlugin;

import t2k.asz.lib.model.ApiMT;
import t2k.asz.lib.model.CDVFactory;
import t2k.asz.lib.model.JSI;
import t2k.asz.modle.DataModle;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class DlActivity extends Activity implements CordovaInterface{
	
	private final ExecutorService threadPool = Executors.newCachedThreadPool();
	
	public String initData="";
	public String playData="";
	
	WebView webview ;
	JSI jsi;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dl);
		
		
		initData =  getIntent().getExtras().getString("initData");
		playData = getIntent().getExtras().getString("playData");
		
		
		CDVFactory.MConfig config = new CDVFactory.MConfig();
		
		jsi = new JSI("aszdlcdv");
		
		config.activity=this;
		config.url="http://cto.timetoknow.com/cms/player/dl/index2.html";
		config.javascriptInterface = jsi;
		config.nameSpace = "aszdlcdv";
		
		webview =  CDVFactory.buildCordovaWebView(config);
		
		 CordovaWebViewClient client =new IceCreamCordovaWebViewClient( this ,(CordovaWebView) webview){

				boolean first=true;
				@Override
				public void onPageFinished(WebView view, String url) {
					super.onPageFinished(view, url);
					
				
					if( first ){
						
				        StringBuilder js=new StringBuilder("");
				        
				        js.append("window.dlhost.initAndPlay(");
				        
				        js.append(initData);
				        
				        js.append("        ,     ");
				        
				        js.append(playData );
				        
				        js.append(");");
				        
				        
				        StringBuilder javaScript =new StringBuilder("");
				        
				        javaScript.append("var loadded = 'YES';   var ondlload =function(){   setTimeout(function(){  ");
				        javaScript.append(js);
				        javaScript.append("   },0);    };  ");
				        
				        view.loadUrl("javascript: "+javaScript );
					}
				}
			};

			webview.setWebViewClient(client);
			
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
		if (DataModle.the().cdv!= null) {
			DataModle.the().cdv.getWebView().loadUrl("javascript:try{cordova.require('cordova/channel').onDestroy.fire();}catch(e){console.log('exception firing destroy event from native');};");
			DataModle.the().cdv.getWebView().loadUrl("about:blank");
			((CordovaWebView)DataModle.the().cdv.getWebView()).handleDestroy();
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
