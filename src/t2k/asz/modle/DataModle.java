package t2k.asz.modle;

import java.util.List;
import java.util.Map;

import t2k.asz.lib.model.ApiMT;


public class DataModle {
	
	private static final Object lock = new Object();
	private static DataModle instance=null;
	
	public Map<String,Object> mdata;
	public List<?> ldata;
	public Object raw;
	
	private DataModle(){
	
	}
	
	public static DataModle the(){

		if(instance==null){
			synchronized(lock){
				if(instance==null) 
					instance = new DataModle();
			}
		}
		return instance;
	}
	
	public  ApiMT cdv=null;
}
