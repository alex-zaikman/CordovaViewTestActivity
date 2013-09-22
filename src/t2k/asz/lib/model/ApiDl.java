package t2k.asz.lib.model;


import java.util.Map;

import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CordovaWebViewClient;
import org.apache.cordova.IceCreamCordovaWebViewClient;
import org.apache.cordova.api.CordovaInterface;
import org.json.JSONException;
import org.json.JSONObject;

import t2k.asz.lib.model.util.CallBack;
import t2k.asz.lib.model.util.JsonHelper;
import android.app.Activity;
import android.util.Log;
//import android.webkit.CookieSyncManager;
import android.webkit.WebView;


 
public class ApiDl  {

	private JSI jsi=null;
	CordovaWebView webview=null;
	public final String NAME_SPASE = "ASZNSPDL";


	public ApiDl(String apiUrl, CallBack callbackOnLoadded, Activity activity){

		final CallBack call = callbackOnLoadded;
		jsi = new JSI(NAME_SPASE,call);
		CDVFactory.MConfig config = new CDVFactory.MConfig(); 
		config.activity=activity;
		config.url=apiUrl;
		config.javascriptInterface = jsi;
		config.nameSpace = NAME_SPASE;
		
		webview=CDVFactory.buildCordovaWebView(config);
		
		CordovaWebViewClient client =new IceCreamCordovaWebViewClient((CordovaInterface) config.activity,webview){

		//	boolean first=true;
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				
//				Log.d("JSI" , "onPageFinished loadded:"+url);
//				if( first ){
//					first = false;
//					
//					
//				}
			}
		};

		webview.setWebViewClient(client);
		
		jsi.setWebview(webview);
	}

	public WebView getWebView(){
		return this.webview;
	}
	
	public void isLoadded(CallBack call){
		jsi.execJSGetReturnedStringVal("isLoadded()", call);
	}
	
	//-------------------------------------API-----------------------------------------------------------------

	
	

	public void initPlayer(String initData , CallBack success,CallBack faliure){
		String command = createCommandForAction("init",initData,success, faliure);
		jsi.execJS(command);

	}

	public void playSequence(String seqData , CallBack success,CallBack faliure){

	
		Map<String,Object> rdata=null;
		JSONObject json;
		try {
			json = new JSONObject(seqData);
			rdata=JsonHelper.toMap(json); 
		} catch (JSONException e) {
			e.printStackTrace();
		}

		String key = (String) rdata.keySet().toArray()[0];
		
	
	    
	   StringBuilder data = new StringBuilder("");
	    
	    data.append("{ 'id':'");
	    data.append(key);
	    data.append("' , 'data': ");
	    data.append(seqData);
	    data.append("}");
	    
	    
		String command = createCommandForAction("playSequence",data.toString(),success, faliure);

		jsi.execJS(command);
	}
	

	//======================================================================================================
	private String createCommandForAction(String action,String data,CallBack success, CallBack faliure){
		   
		
		String[] funcs = jsi.getCallBackFuncs( success,  faliure);
		
		
	    StringBuilder command = new StringBuilder("");
	    
	    command.append("window.dlhost.player.api({ 'action':'");
	    command.append(action);
	    command.append("', 'data':");
	    command.append(data);
	    command.append(", 'success':  "
	    		+ funcs[0]
	    		+ "    'error':  "
	    		+ funcs[1]
	    		+ "   } );");
	 
	    return command.toString();
	}
	
	
	

	public static String comma(String me){
		return "'"+me+"'";
	}

	protected JSI getJSI(){
		return jsi;
	}

	//==========================================================================================================
	

	

}
