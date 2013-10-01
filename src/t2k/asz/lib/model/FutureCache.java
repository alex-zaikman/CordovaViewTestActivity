package t2k.asz.lib.model;

import java.util.ArrayList;
import java.util.List;

import android.util.SparseIntArray;

public class FutureCache {

	private int pastSize=2;
	private int futureSize=2;

	private SparseIntArray cacheIndex;
	private ArrayList<Object> cache;
	private int casheSize=0;
	private CacheObjectCreator creator;

	public FutureCache(int pastSize, int futureSize, int startIndex, CacheObjectCreator creator){

		this.pastSize=pastSize;
		this.futureSize=futureSize;

		this.casheSize = pastSize+futureSize+1;
		this.creator=creator;

		cacheIndex = new SparseIntArray();
		cache = new ArrayList<Object>();

		repopulateAround(startIndex , null);
	}

	protected void repopulateAround(int absoluteIndex , Object o){

		if(o!=null &&  cacheIndex.get(absoluteIndex, -1)==-1 ){
			cache.add(o);
			cacheIndex.append(absoluteIndex, cache.size()-1);
		}else if(o==null){
			cache.add(  creator.createObjectForIndex(absoluteIndex)   );
			cacheIndex.append(absoluteIndex, cache.size()-1);
		}

//		if(cacheIndex.get(absoluteIndex+1, -1)==-1 ){
//			cache.add(  creator.createObjectForIndex(absoluteIndex+1)   );
//			cacheIndex.append(absoluteIndex, cache.size()-1);
//		}
		
		/*
		int startIndex =  Math.max(0, absoluteIndex - pastSize);
		int endIndex =  absoluteIndex + futureSize;

		Object[] tmpcache = new Object[this.casheSize];		

		//recalc objects
		int rj=0;
		for(int abi = startIndex ; abi<=endIndex ; abi++){


			if(abi == absoluteIndex && o!=null){
				tmpcache[rj]=o;
			}
			else{
				int ri=-1;
				if((ri = cacheIndex.get(abi, -1)) == -1){
					tmpcache[rj]=creator.createObjectForIndex(abi);
				}else{
					tmpcache[rj]=cache[ri];
				}

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
		 */
	}

	protected Object getForIndex(int absoluteIndex, boolean repopulate){

		Object ret=null;

		int ri=-1;
		if((ri=cacheIndex.get(absoluteIndex, -1)) != -1){
			ret = cache.get(ri);
		}else{		
			if(creator.canCreateObjectFor(absoluteIndex)){
				ret = creator.createObjectForIndex(absoluteIndex);
			}
		}

		if(repopulate) 
			repopulateAround(absoluteIndex, ret);

		return ret;

	}

	//---------------------------------------------public API---------------------------------------------------------------

	public Object objectForIndex(int absoluteIndex){
		Object ret = null;

		if(creator.canCreateObjectFor(absoluteIndex)){
			ret = getForIndex(absoluteIndex,true);
		}

		return ret;
	}

	public Object objectForIndex(List<Integer> absoluteIndexs){
		return null;
		//		List<Object> ret = new ArrayList<Object>();
		//
		//		int count = absoluteIndexs.size();
		//
		//		for(int i=0; i<count ; i++){
		//			Object o =getForIndex(absoluteIndexs.get(i),false);
		//
		//			if(o==null){
		//				return null;
		//			}
		//			ret.add(o);
		//		}
		//
		//
		//		int pivot  =  (int) Math.floor(absoluteIndexs.size()/2) ;
		//		int absoluteIndexToPivot = absoluteIndexs.get(pivot);
		//		repopulateAround(absoluteIndexToPivot);
		//
		//
		//		return ret;
	}

	//-----------------------------------CacheObjectCreator interface-------------------------------------------------------

	public static interface CacheObjectCreator{

		public Object createObjectForIndex(int absoluteIndexs);

		public boolean canCreateObjectFor(int absoluteIndexs);

	}

}
