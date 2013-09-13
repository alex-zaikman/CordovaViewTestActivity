package t2k.asz.controller;

import org.apache.cordova.api.LOG;

import t2k.asz.modle.DataModle;
import t2k.asz.modle.OverviewBean;
import t2k.asz.modle.OverviewBean.Item;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class OverviewActivity extends Activity {

	OverviewBean ob=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_overview);

		int classId = (Integer) getIntent().getExtras().get("classId");
		ob = new OverviewBean(classId, DataModle.the().mdata);
		final ListView lv = (ListView) findViewById(R.id.list);

		lv.setAdapter(new OverviewAdapter(this,R.id.txtRow ,ob));


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//	getMenuInflater().inflate(R.menu.overview, menu);
		return true;
	}











	public static class OverviewAdapter extends BaseAdapter{

		Context context; 
		public final OverviewBean ob;
		int layoutResourceId;

		public OverviewAdapter(Context context, int layoutResourceId, OverviewBean ob) {
			super();
			this.context=context;
			this.ob=ob;
			this.layoutResourceId= layoutResourceId;
		}

		@Override
		public int getCount() {
			int size = this.ob.listExpanddedItemsByDFS().size();
			return size;
		}

		@Override
		public Object getItem(int location) {
			return this.ob.listExpanddedItemsByDFS().get(location);
		}

		@Override
		public long getItemId(int location) {
			return location;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			
			
			TextView row = (TextView) LayoutInflater.from(context)
					.inflate(R.layout.table_row_simple, parent, false);

			final Item item = (Item) getItem(position);
			row.setText(item.getTitle());

			

			if(!item.isLeaf()){
				row.setBackgroundColor(Color.BLUE);
			}else{

				row.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
					
						v.setBackgroundColor(Color.CYAN);
						String selectedNodeCid = item.getCid();
						String courseId = ob.getCourseId();
						String dataCid = ob.getCid();
						
						LOG.d("asz","selectedNodeCid: "+ selectedNodeCid +" courseId: "+courseId +" dataCid: "+dataCid   );

					}

				});

			}


			return row;
		}
	}

}
