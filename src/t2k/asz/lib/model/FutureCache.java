package t2k.asz.lib.model;

import java.util.ArrayList;
import java.util.List;

import android.util.SparseIntArray;

public class FutureCache {

	private int pastSize=2;
	private int futureSize=2;
	
	private SparseIntArray cacheIndex;
	private Object[] cache;

	private CacheObjectCreator creator;

	public FutureCache(int pastSize, int futureSize, int startIndex, CacheObjectCreator creator){

		this.pastSize=pastSize;
		this.futureSize=futureSize;
		

		this.creator=creator;

		cacheIndex = new SparseIntArray();
		cache = new Object[pastSize+futureSize+1];

		repopulateAround(startIndex);
	}

	protected void repopulateAround(int absoluteIndex){

		int startIndex = Math.max(0, absoluteIndex - pastSize);
		int endIndex = Math.min(pastSize+futureSize, absoluteIndex + futureSize);

		Object[] tmpcache = new Object[pastSize+futureSize+1];		

		//recalc objects
		int rj=0;
		for(int abi = startIndex ; abi<=endIndex ; abi++){

			int ri=-1;
			if((ri = cacheIndex.get(abi, -1)) == -1){
				tmpcache[rj]=creator.createForIndex(abi);
			}else{
				tmpcache[rj]=cache[ri];
			}
			rj++;
		}
		//recalc and set indexes
		cacheIndex.clear();
		rj =0;
		for(int abi = startIndex ; abi<=endIndex ; abi++){
			cacheIndex.put(abi, rj);
			rj++;
		}

		//set cache
		cache = tmpcache;

	}

	protected Object getForIndex(int absoluteIndex, boolean repopulate){

		Object ret=null;

		int ri=-1;
		if((ri=cacheIndex.get(absoluteIndex, -1)) != -1){
			ret = cache[ri];
		}else{		
			ret = creator.createForIndex(absoluteIndex);
		}

		if(repopulate) 
			repopulateAround(absoluteIndex);

		return ret;

	}

	//---------------------------------------------public API---------------------------------------------------------------

	public Object getForIndex(int absoluteIndex){
		Object ret = null;

		if(creator.canCreateFor(absoluteIndex)){
			ret = getForIndex(absoluteIndex,true);
		}

		return ret;
	}

	public Object getForIndex(List<Integer> absoluteIndexs){

		List<Object> ret = new ArrayList<Object>();

		int count = absoluteIndexs.size();

		for(int i=0; i<count ; i++){
			Object o =getForIndex(absoluteIndexs.get(i),false);

			if(o==null){
				return null;
			}
			ret.add(o);
		}

	
		int pivot  =  (int) Math.floor(absoluteIndexs.size()/2) ;
		int absoluteIndexToPivot = absoluteIndexs.get(pivot);
		repopulateAround(absoluteIndexToPivot);
		

		return ret;
	}

	//-----------------------------------CacheObjectCreator interface-------------------------------------------------------

	public static interface CacheObjectCreator{

		public Object createForIndex(int absoluteIndexs);

		public boolean canCreateFor(int absoluteIndexs);

	}

}
