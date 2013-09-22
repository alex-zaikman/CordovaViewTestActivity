package t2k.asz.lib.model.util;

public abstract class CallBack {

	public String[] params;
	
	public CallBack(){}
	
	public CallBack(String[] params){
		
		this.params=params;
	}
	
	public void call(String msg){
		
	}

}
