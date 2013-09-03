package t2k.asz.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import t2k.asz.controller.R;
import t2k.asz.modle.DataModle;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.webkit.CookieManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import t2k.asz.lib.model.util.*;


public class ClassesActivity extends Activity {


	 
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_classes);
	        
	        
	        final LinearLayout myGallery = (LinearLayout)findViewById(R.id.classgallery);
	        ///get classes ---getStudyClassesOnSuccess
	        DataModle.the().cdv.getStudyClasses(new CallBack(){
	        	
	        
				@Override
	        	public void call(String msg){
	        		

					JSONArray jObj;
					try {
						jObj = new JSONArray(msg);
				
					List<?> jList =JsonHelper.toList(jObj);
	        		
	        		DataModle.the().ldata =jList; 
	        		   //get class images
	        		Map<String,Object> aclass;
	    	       // for(Object a : jList)
	        		{
	        			Object	a = jList.get(0);
	    	        	aclass = (Map<String, Object>) a;
	    	        	
	    	        	String imageUrl = (String) aclass.get("imageURL");
	    	        	
	    	        	
	    	         	 DataModle.the().cdv.getImage(imageUrl, new CallBack(){
	    	        		 
	    	        		 @Override
	    	        		 public void call(String msg){
	    	        			 
	    	        			 
	    	        			msg = msg+"";
	    	        			
								//Bitmap image = BitmapFactory.decodeStream(new ByteArrayInputStream(msg.getBytes()));
	    	        			
	    	        			byte[] decodedString = Base64.decode(msg, Base64.DEFAULT);
	    	        			Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
	    	        		//	 myGallery.addView(insertPhoto(image));
	    	        			 
	    	        			 
	    	        		 }
	    	        		 
	    	        		 
	    	        	 },  new CallBack(){});
	    	        	
	    	        	
	    	        	//Bitmap image = getBitmapFromURL(imageUrl) ;
	    	    
	    	         //	String className = (String) aclass.get("name");
	    	         	
	    	         //	String id = (String) aclass.get("id");
	    	         	
	    	         	//insert and attache events
	    	         //	 myGallery.addView(insertPhoto(image)); //TODO do insert here
	    	         	
	    	         //	 myGallery.invalidate();
	    	         	
	    	        }
	    	        
	    	        
	    	        
	    	        
	    	       
	    	       
	    	        
	    	        
	    	        //TODO
//	    	        String ExternalStorageDirectoryPath = Environment
//	    	          .getExternalStorageDirectory()
//	    	          .getAbsolutePath();
//	    	        
//	    	        String targetPath = ExternalStorageDirectoryPath + "/test/";
//	    	        
//	    	        Toast.makeText(getApplicationContext(), targetPath, Toast.LENGTH_LONG).show();
//	    	        File targetDirector = new File(targetPath);
//	    	                 
//	    	        File[] files = targetDirector.listFiles();
//	    	        for (File file : files){
//	    	         myGallery.addView(insertPhoto(file.getAbsolutePath())); //TODO do insert here
//	    	        }   
//	    	        
	        		
	        		
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	        		
	        	}
	        	
	        }, new CallBack(){});
	        
	     
	        
	        
	    }

	 //   public static Bitmap getBitmapFromURL(String src) {
	    //    try {
	        	
	        	
	        	
	        	
//	        	 DataModle.the().cdv.getImage(src, new CallBack(){
//	        		 
//	        		 @Override
//	        		 public void call(String msg){
//	        			 
//	        			 msg=msg;
//	        			 int i=8;
//	        			 i++;
//	        			 
//	        		 }
//	        		 
//	        		 
//	        	 },  new CallBack(){});
	        	
//	            URL url = new URL(src);
//	            
//	            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//	            connection.setDoInput(true);
//	           
//                String cookie = CookieManager.getInstance().getCookie("http://cto.timetoknow.com");
//	            connection.addRequestProperty("Cookie", cookie);
//	      
//	            connection.connect();
//	            int httpResponseCode =connection.getResponseCode();   
//	            
//	            
//	            InputStream input = connection.getInputStream();
//	            Bitmap myBitmap = BitmapFactory.decodeStream(input);
//	            return myBitmap;
	  //          return null;
	            
//	        } catch (IOException e) {
//	            e.printStackTrace();
//	            return null;
//	        }
	//    }
	    
	    
	    
	    View insertPhoto(Bitmap bm){
	     
	     LinearLayout layout = new LinearLayout(getApplicationContext());
	     layout.setLayoutParams(new LayoutParams(250, 250));
	     layout.setGravity(Gravity.CENTER);
	     
	     ImageView imageView = new ImageView(getApplicationContext());
	     imageView.setLayoutParams(new LayoutParams(220, 220));
	     imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
	     imageView.setImageBitmap(bm);
	     
	     layout.addView(imageView);
	     return layout;
	    }
	    

	}
