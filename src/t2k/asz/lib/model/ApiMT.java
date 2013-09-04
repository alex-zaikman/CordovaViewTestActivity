package t2k.asz.lib.model;


import java.util.ArrayList;
import java.util.List;

import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CordovaWebViewClient;
import org.apache.cordova.IceCreamCordovaWebViewClient;
import org.apache.cordova.api.CordovaInterface;

import t2k.asz.lib.model.util.CallBack;
import android.app.Activity;
import android.util.Log;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;



public class ApiMT  {

	private JSI jsi=null;
	CordovaWebView webview=null;
	public final String NAME_SPASE = "ASZNSP";


	public ApiMT(String apiUrl, CallBack callbackOnLoadded, Activity activity){

		final CallBack call = callbackOnLoadded;
		jsi = new JSI(NAME_SPASE);
		
		CDVFactory.config.activity=activity;
		CDVFactory.config.url=apiUrl;
		CDVFactory.config.javascriptInterface = jsi;
		CDVFactory.config.nameSpace = NAME_SPASE;
		
		
		
		webview=CDVFactory.the().cwv;
		
		
		
		
		CordovaWebViewClient client =new IceCreamCordovaWebViewClient((CordovaInterface) CDVFactory.config.activity,webview){

			boolean first=true;
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				CookieSyncManager.getInstance().sync();
				Log.d("JSI" , "onPageFinished loadded:"+url);
				if( first ){
					first = false;
					call.call("AOK");
				}
			}
		};

		webview.setWebViewClient(client);
		
		
		jsi.setWebview(webview);
		
	}

	public WebView getWebView(){
		return this.webview;
	}
	
	//-------------------------------------API-----------------------------------------------------------------

	
	
	public void getImage(String url , CallBack success,CallBack faliure){
		List<String> params= new ArrayList<String>();
		params.add(comma(url));
		jsi.execJS("getImage", params, success, faliure);
	}
	
	
	public void callOnLoadded(CallBack success,CallBack faliure){
		jsi.execJS("callOnLoadded", null, success, faliure);
	}
	
	
	public void loadApi(CallBack success,CallBack faliure){
		List<String> params= new ArrayList<String>();
		params.add(comma("html5"));
		jsi.execJS("T2K.api.load", params, success, faliure);
	}

	public void initApi(CallBack success,CallBack faliure){
		jsi.execJS("T2K.server.initData", null, success, faliure);
	}

	public void logIn(String username, String password, CallBack success,CallBack faliure){	
		List<String> params= new ArrayList<String>();

		params.add(comma(username));
		params.add(comma(password));

		jsi.execJS("T2K.user.login", params, success, faliure);

	}

	public void logInMF(String username, String password, CallBack success,CallBack faliure){

		final String u= username;
		final String p= password;
		final CallBack s= success;
		final CallBack f= faliure;

		loadApi(  new CallBack(){
			public void call(String msg){
				initApi( new CallBack(){
					public void call(String msg){
						logIn( u,  p,  s, f);
					}
				}, f );
			}
		}  , f );
		
	}


	public void logOut(CallBack success,CallBack faliure){

		jsi.execJS("T2K.user.logout", null, success, faliure);
	}

	public void getStudyClasses(CallBack success,CallBack faliure){
		jsi.execJS( "T2K.user.getStudyClasses", null, success, faliure);
	}

	public void getCourse(String cid , CallBack success,CallBack faliure){

		List<String> params= new ArrayList<String>();

		params.add(cid);

		jsi.execJS( "T2K.content.getCourseByClass", params, success, faliure);
	}

	public void getLessonContent(String courseId, String lessonId , CallBack success,CallBack faliure){

		List<String> params= new ArrayList<String>();

		params.add(comma(courseId));
		params.add(comma(lessonId));

		jsi.execJS( "T2K.content.getLessonContent", params, success, faliure);
	}

	//======================================================================================================

	public void testjs(String functionName , List<String> params , CallBack success,CallBack faliure){
		jsi.execJS(functionName, params, success, faliure);
	}
	
	public static String comma(String me){
		return "'"+me+"'";
	}

	protected JSI getJSI(){
		return jsi;
	}

	//==========================================================================================================
	

	

}
