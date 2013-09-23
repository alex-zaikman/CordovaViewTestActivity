package t2k.asz.lib.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.cordova.api.LOG;

import t2k.asz.lib.model.util.CallBack;
import t2k.asz.lib.model.util.DlCallBack;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

public class JSI {

	Map<Double,CallBack> callbacks=new HashMap<Double,CallBack>();

	Set<CallBack> perfunc = new HashSet<CallBack>();

	WebView webview;
	String namespace;
	CallBack callonloadded;
	DlCallBack dlcallback=null;
	//=================API======================================================

	public JSI(WebView webview,String namespace){
		this.namespace=namespace;
		this.webview=webview;
	}

	public JSI(String namespace){
		this.namespace = namespace;
	}


	public JSI(String namespace, CallBack callonloadded){
		this.namespace = namespace;
		this.callonloadded=callonloadded;
	}


	public void setWebview(WebView webview){
		if(this.webview==null)
			this.webview= webview;
	}



	public void execJSGetReturnedStringVal( String command ,CallBack callback){

		Double d = putCallBack(callback);
		String jscommand="javascript:"+this.namespace+".jsReturnVal("+command+",'"+d+"');";
		webview.loadUrl(jscommand);
	}

	public void execJS(final String command){
		String jscommand="javascript:"+command;
		webview.loadUrl(jscommand);	
	}



	public void execJSFunction(final String jsFuncName , List<String> params , CallBack success, CallBack faliure){

		Double ds = putCallBack(success);
		Double df = putCallBack(faliure);

		String fsuccess = " function(msg){ window."+ this.namespace+".jsReturnVal( JSON.stringify(msg), '"+ ds + "' , '" +df+ "' );} ,";	

		String ffaliure = " function(msg){ window."+this.namespace+".jsReturnVal( JSON.stringify(msg), '"+ df + "' , '" +ds+ "' );}";	

		String jparams="";	

		if(params!=null && !params.isEmpty()){
			for(String p : params){
				jparams+=p+" , ";
			}
		}


		String jscommand="javascript:try{ "+jsFuncName+"(" +jparams + fsuccess + ffaliure+ ");   }catch(err){ window."+this.namespace+".jsReturnVal(  err.message , '"+ df + "' , '" +ds+ "' ); }" ;

		webview.loadUrl(jscommand);

	}

	public String[] getCallBackFuncs(CallBack success, CallBack faliure){

		Double ds = putCallBack(success);
		Double df = putCallBack(faliure);

		String fsuccess = " function(msg){ window."+ this.namespace+".jsReturnVal( msg, '"+ ds + "' , '" +df+ "' );} ,";	

		String ffaliure = " function(msg){ window."+this.namespace+".jsReturnVal( msg, '"+ df + "' , '" +ds+ "' );}";	

		String ret[] = {fsuccess, ffaliure};

		return ret;
	}

	//============================================================================
	@JavascriptInterface
	public void echo(String msg){
		LOG.d("asz",msg);
	}

	@JavascriptInterface
	public void loadded(String msg){
		if(callonloadded!=null) callonloadded.call(msg);
	}

	@JavascriptInterface
	public void dlCallBackApi(String msg){
		if(dlcallback!=null) dlcallback.call(msg);
	}


	@JavascriptInterface
	public void jsReturnVal(String msg, String d ,String rem){
		this.callbacks.remove(Double.parseDouble(rem));
		this.callbacks.get(Double.parseDouble(d)).call(msg);
		this.callbacks.remove(Double.parseDouble(d));
	}

	@JavascriptInterface
	public void jsReturnVal(String msg, String d){
		CallBack cb = this.callbacks.get(Double.parseDouble(d));
		cb.call(msg);
		this.callbacks.remove(Double.parseDouble(d));
	}

	private Double putCallBack(CallBack callback){

		if(callback ==null) return null;

		Double d = Math.random();
		while(this.callbacks.containsKey(d)){
			d = Math.random();
		}
		this.callbacks.put(d, callback);

		return d;
	}

	public void setDlCallBack(DlCallBack dlcallback) {
		this.dlcallback=dlcallback;
	}

}
