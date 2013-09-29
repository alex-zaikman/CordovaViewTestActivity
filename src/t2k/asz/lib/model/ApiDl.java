package t2k.asz.lib.model;


import java.util.Map;

import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CordovaWebViewClient;
import org.apache.cordova.IceCreamCordovaWebViewClient;
import org.apache.cordova.api.CordovaInterface;
import org.json.JSONException;
import org.json.JSONObject;

import t2k.asz.lib.model.util.CallBack;
import t2k.asz.lib.model.util.DlCallBack;
import t2k.asz.lib.model.util.JsonHelper;
import t2k.asz.modle.DlCallBackImpl;
import android.app.Activity;
import android.webkit.WebView;


 
public class ApiDl  {

	private JSI jsi=null;
	CordovaWebView webview=null;
	public final String NAME_SPASE = "ASZNSPDL";
	DlCallBack dlcallback;

	public ApiDl(String apiUrl, CallBack callbackOnLoadded, Activity activity){

		final CallBack call = callbackOnLoadded;
		jsi = new JSI(NAME_SPASE);
		dlcallback = new DlCallBackImpl();
		jsi.setDlCallBack(dlcallback);
		CDVFactory.MConfig config = new CDVFactory.MConfig(); 
		config.activity=activity;
		config.url=apiUrl;
		config.javascriptInterface = jsi;
		config.nameSpace = NAME_SPASE;
		
		webview=CDVFactory.buildCordovaWebView(config);
		
		CordovaWebViewClient client =new IceCreamCordovaWebViewClient((CordovaInterface) config.activity,webview){

			boolean first=true;
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				
				if( first ){
					first = false;
					jsi.execJS(" window.dlapicall = function(res){  "+NAME_SPASE+".dlCallBackApi(JSON.stringify(res)); };");
					call.call("aok");
				}
			}
		};

		webview.setWebViewClient(client);
		
		jsi.setWebview(webview);
	}

	public WebView getWebView(){
		return this.webview;
	}
	
	
	public void callOnLoadded(CallBack success,CallBack faliure){
		jsi.execJSFunction("callOnLoadded", null, success, faliure);
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
