package t2k.asz.lib.model;

import org.apache.cordova.Config;
import org.apache.cordova.CordovaWebView;

import android.app.Activity;

public class CDVFactory {

	private static final Object lock = new Object();
	private static CDVFactory instance=null;
	public CordovaWebView cwv;
	public static MConfig config = new MConfig();


	public static CordovaWebView buildCordovaWebView(MConfig c){
		
		Config.init(c.activity);
		CordovaWebView ret = new CordovaWebView(c.activity);
		
		if(c.javascriptInterface!=null)
			ret.addJavascriptInterface(c.javascriptInterface, c.nameSpace);

		
		//must be last
		ret.loadUrl(c.url);
		
		return ret;
	}
	
	private CDVFactory(){	
		Config.init(config.activity);
		cwv = new CordovaWebView(config.activity);

		if(config.javascriptInterface!=null)
			cwv.addJavascriptInterface(config.javascriptInterface, config.nameSpace);

		
		//must be last
		cwv.loadUrl(config.url);
	}

	public static CDVFactory the(){

		if(instance==null){
			synchronized(lock){
				if(instance==null) 
					instance = new CDVFactory();
			}
		}
		return instance;
	}
	
	
	//=======static config class================== 
	public static class MConfig{
		public Activity activity=null;
		public String url=null;
		public String nameSpace="ASZDEFAULTJSNAMESPACE";
		public Object javascriptInterface=null;

	}

}
