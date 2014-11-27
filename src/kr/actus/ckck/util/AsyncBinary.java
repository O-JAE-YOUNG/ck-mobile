package kr.actus.ckck.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class AsyncBinary {
	private static final String TAG = "MainActivity";
	AsyncHttpClient client = new AsyncHttpClient();
	JSONObject object = null;
//	JSONArray resultArray=null;
	JSONArray array=null;
	

	public JSONArray postJSONArray(String getUrl, RequestParams param) {
		
		client.post(getUrl, param, new JsonHttpResponseHandler() {
			
		
		

			@Override
			public void onSuccess(int statusCode, JSONArray response) {
				Log.v(TAG,"JSONArray response : "+response);
				array = response;
				super.onSuccess(statusCode, response);
			}

			@Override
			public void onFailure(Throwable e, JSONArray errorResponse) {
				Log.v(TAG,"JSONArray errorResponse : "+errorResponse);
				array = errorResponse;
				super.onFailure(e, errorResponse);
			}
			

		});
		
		return array;

	}
public JSONObject postJSONObject(String getUrl, RequestParams param) {
		
		client.post(getUrl, param, new JsonHttpResponseHandler() {
			
			@Override
			public void onSuccess(JSONObject response) {
				object = response;
				Log.v(TAG,"success Response : "+response);
				super.onSuccess(response);
			}

			@Override
			public void onFailure(Throwable e, JSONObject errorResponse) {
				object = errorResponse;
				Log.v(TAG,"postRequest failure : "+e+"\nerrorResponse : "+errorResponse);
				super.onFailure(e, errorResponse);
			}

		

			

		});
		
		return object;

	}

	public void binaryClient(final String imgUrl, final String saveFile) {
		
		String[] allow = new String[] { "image/png","image/jpeg" };
		client.get(SetURL.URL+imgUrl,new BinaryHttpResponseHandler() {
					// ���̳ʸ����ٿ���ÿ� ���̳ʸ����� �⺻ ������.
					@Override
					public void onSuccess(byte[] fileData) {
						// TODO Auto-generated method stub
						
						FileOutputStream out = null;
						
					
						try {

							out = new FileOutputStream(saveFile);

							out.write(fileData);
							out.close();

						} catch (FileNotFoundException e1) {
							Log.v(TAG, "FileNotFoundException :" + e1);
							e1.printStackTrace();
						} catch (IOException e) {
							Log.v(TAG, "IOException :" + e);
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							byte[] binaryData, Throwable error) {
						Log.v(TAG, "BinaryDate error :" + error);
						super.onFailure(statusCode, headers, binaryData, error);
					}

				
				});
		
	}



}
