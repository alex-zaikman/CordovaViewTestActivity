package t2k.asz.controller;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.cordova.api.LOG;
import org.json.JSONException;
import org.json.JSONObject;

import t2k.asz.controller.OverviewActivity.OverviewAdapter;
import t2k.asz.lib.model.util.CallBack;
import t2k.asz.lib.model.util.JsonHelper;
import t2k.asz.modle.DataModle;
import t2k.asz.modle.OverviewBean;
import t2k.asz.modle.OverviewBean.Item;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class LoActivity extends Activity {

	String lessonId;
	String courseId;
	String Ccid;
	String rawData;
	List<Lo> los;
	ArrayList<Seq> seqList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lo);

		lessonId = getIntent().getExtras().getString("lessonId");
		courseId = getIntent().getExtras().getString("courseId");
		Ccid = getIntent().getExtras().getString("Ccid");
		rawData = getIntent().getExtras().getString("rawData");

		Map<String,Object> rdata=null;
		JSONObject json;
		try {
			json = new JSONObject(this.rawData);
			rdata=JsonHelper.toMap(json); 
		} catch (JSONException e) {
			e.printStackTrace();
		}


		seqList = new ArrayList<Seq>();
		final List<Object> learningObjects = (List<Object>) ((Map<String, Object>)rdata.get("data")).get("learningObjects");

		this.los = new ArrayList<Lo>();
		this.prepLos(learningObjects);

		final ListView lv = (ListView) findViewById(R.id.list);
		lv.setAdapter(new LoAdapter(this,R.id.txtRow , this.seqList));
	}

	private void prepLos(List<Object> learningObjects) {
		this.los.clear();

		for(int i=0 ; i<learningObjects.size() ;i++){

			Lo lo = new Lo();

			lo.pedagogicalLoType =(String) ( (Map<String,Object>)learningObjects.get(i)).get("pedagogicalLoType");

			lo.seqs = new LinkedList<Seq>();

			List<Map<String,Object>> rawSeqs = (List<Map<String,Object>>) ( (Map<String,Object>)learningObjects.get(i)).get("sequences");

			for( Map<String,Object> rawSeq : rawSeqs){

				Seq seq =new Seq();

				seq.stitle = (String) ( (Map<String,Object>)rawSeq).get("title"); 
				seq.thumbnailHref=  (String) ( (Map<String,Object>)rawSeq).get("thumbnailHref"); 
				seq.contentHref= (String) ( (Map<String,Object>)rawSeq).get("contentHref"); 

				lo.seqs.add(seq);

				this.seqList.add(seq);
			}

			this.los.add(lo);
		}





	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.lo, menu);
		return true;
	}



	public static class Lo{
		String pedagogicalLoType;
		List<Seq> seqs; 

	}

	public static class Seq{
		String stitle;
		String thumbnailHref;
		String contentHref;
	}

	public static class LoAdapter extends BaseAdapter{

		final Context context; 
		final int layoutResourceId;
		final ArrayList<Seq> seqs;
		
		public LoAdapter(Context context, int layoutResourceId ,ArrayList<Seq> seqs) {
			super();
			this.context=context;
			this.seqs=seqs;
			this.layoutResourceId= layoutResourceId;
		}

		@Override
		public int getCount() {
			return this.seqs.size();
		}

		@Override
		public Object getItem(int location) {
			return this.seqs.get(location);
		}

		@Override
		public long getItemId(int location) {
			return location;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			TextView row = (TextView) LayoutInflater.from(context)
					.inflate(R.layout.table_row_simple, parent, false);

			final Seq seq = this.seqs.get(position);
			row.setText(seq.stitle); 

			final String contentHref = seq.contentHref;
			
			OnClickListener l = new OnClickListener(){

				@Override
				public void onClick(View v) {
					

					//TODO
					
					
					
				}
				
			};
			row.setOnClickListener(l);


			return row;
		}
	}

}
