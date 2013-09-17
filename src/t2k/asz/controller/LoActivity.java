package t2k.asz.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

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
import android.os.StrictMode;
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
	String initData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

		StrictMode.setThreadPolicy(policy); 
		
		setContentView(R.layout.activity_lo);

		lessonId = getIntent().getExtras().getString("lessonId");
		courseId = getIntent().getExtras().getString("courseId");
		Ccid = getIntent().getExtras().getString("Ccid");
		rawData = getIntent().getExtras().getString("rawData");


		this.initData = this.prepInitData();



		Map<String,Object> rdata=null;
		JSONObject json;
		try {
			json = new JSONObject(this.rawData);
			rdata=JsonHelper.toMap(json); 
		} catch (JSONException e) {
			e.printStackTrace();
		}


		seqList = new ArrayList<Seq>();
		@SuppressWarnings("unchecked")
		List<Object> learningObjects = (List<Object>) ((Map<String, Object>)rdata.get("data")).get("learningObjects");

		this.los = new ArrayList<Lo>();
		try{
			this.prepLos(learningObjects);
		}catch(Exception e){
			e.printStackTrace();
		}
		final ListView lv = (ListView) findViewById(R.id.lolist);
		lv.setAdapter(new LoAdapter(this,R.id.txtRow , this.seqList));
	}

	private String prepInitData() {
		//media url
		StringBuilder mediaUrl=new StringBuilder("");

		mediaUrl.append("\"/cms/courses/");

		mediaUrl.append(this.Ccid);

		mediaUrl.append("\"");

		//init data
		StringBuilder initData= new StringBuilder("");

		initData.append("{ width: 1024, height: 600, scale: 1, basePaths: { player:");

		//this.playerPath
		initData.append("\"/cms/player/dl\"");


		initData.append(", media:");

		initData.append(mediaUrl);

		initData.append("}, complay:true, localeName:\"en_US\",   apiVersion: '1.0',  loId: 'inst_s', isLoggingEnabled: true, userInfo : { role: '");

		initData.append("student");

		initData.append("' }   }");

		return initData.toString() ;

	}

	private static String prepPlayData(String ref) {

		try {


			String jsonUrl = "http://cto.timetoknow.com"  + ref ;


			final URL url = new URL(jsonUrl);

			HttpURLConnection connection;
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestProperty("Cookie", DataModle.the().cookie);
			connection.setDoInput(true);
			connection.connect();	 
			Scanner s = new Scanner(connection.getInputStream());
			
			s.useDelimiter("\\Z");
			String response = s.next();
			
			return response;

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private void prepLos(List<Object> learningObjects) {
		this.los.clear();

		for(int i=0 ; i<learningObjects.size() ;i++){

			Lo lo = new Lo();

			lo.pedagogicalLoType =(String) ( (Map<String,Object>)learningObjects.get(i)).get("pedagogicalLoType");

			lo.seqs = new LinkedList<Seq>();

			List<Map<String,Object>> rawSeqs = (List<Map<String,Object>>) ( (Map<String,Object>)learningObjects.get(i)).get("sequences");

			if(rawSeqs!=null){

				for( Map<String,Object> rawSeq : rawSeqs){

					Seq seq =new Seq();


					seq.stitle = (String) ( (Map<String,Object>)rawSeq).get("title"); 
					seq.thumbnailHref=  (String) ( (Map<String,Object>)rawSeq).get("thumbnailHRef"); 
					seq.contentHref= (String) ( (Map<String,Object>)rawSeq).get("contentHRef"); 



					lo.seqs.add(seq);

					this.seqList.add(seq);
				}
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

	public  class LoAdapter extends BaseAdapter{

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

			
			final int index =position;

			TextView row = (TextView) LayoutInflater.from(context)
					.inflate(R.layout.table_row_simple, parent, false);

			final Seq seq = this.seqs.get(position);
			row.setText(seq.stitle); 

			final String contentHref = seq.contentHref;

			OnClickListener l = new OnClickListener(){

				@Override
				public void onClick(View v) {


					String playData = prepPlayData(contentHref);
					String initData = 	((LoActivity)context).initData;
					
					Intent intent = new Intent(getApplicationContext(), DlActivity.class);
					intent.putExtra("playData", playData);
					intent.putExtra("initData", initData);
				    startActivity(intent);


				}

			};
			row.setOnClickListener(l);


			return row;
		}
	}

}
