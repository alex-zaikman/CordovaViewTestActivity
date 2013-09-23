package t2k.asz.lib.model.util;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class DlCallBack extends CallBack{

	@Override
	public void call(String jconfig){
		
		Map<String,Object> config=null;
		JSONObject json;
		try {
			json = new JSONObject(jconfig);
			config=JsonHelper.toMap(json); 
		} catch (JSONException e) {
			e.printStackTrace();
		}
		api(config);
	}
	
	public abstract void api(Map<String,Object> config);
	
	
}
