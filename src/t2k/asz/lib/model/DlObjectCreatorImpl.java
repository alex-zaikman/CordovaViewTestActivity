package t2k.asz.lib.model;

import android.app.Activity;
import t2k.asz.lib.model.FutureCache.CacheObjectCreator;
import t2k.asz.lib.model.util.CallBack;

public class DlObjectCreatorImpl implements CacheObjectCreator {

	private ApiDl apidl=null;
	final String domain;
	private Activity activity;
	private String[][] ipdata;

	public DlObjectCreatorImpl( String domain , String[][] paramArrayes , Activity activity){
		this.ipdata=paramArrayes;
		this.domain = domain;
		this.activity=activity;
	}

	@Override
	public Object createForIndex(int absoluteIndexs) {

		if (ipdata.length < absoluteIndexs+1)
			return null;
		
		apidl = null;
		apidl = new ApiDl( domain , new CallBack(this.ipdata[absoluteIndexs]){

			@Override
			public void call(String msg){

				apidl.callOnLoadded(new CallBack(this.params){@Override
					public void call(String msg){

					apidl.initPlayer(this.params[0], new CallBack(this.params){

						@Override
						public void call(String msg){

							apidl.playSequence(this.params[1], new CallBack(){}, new CallBack(){});

						}}, new CallBack(){});

				}}, new CallBack(){});

			}
		}, this.activity);


		return apidl;
	}

	@Override
	public boolean canCreateFor(int absoluteIndexs){
		return (absoluteIndexs>-1 && absoluteIndexs < this.ipdata.length );
	}
}
