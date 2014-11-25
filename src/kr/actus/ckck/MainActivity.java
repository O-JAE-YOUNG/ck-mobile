/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kr.actus.ckck;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import kr.actus.ckck.drawerlist.DrawerAdapter;
import kr.actus.ckck.drawerlist.DrawerItem;
import kr.actus.ckck.fragment.MainTab;
import kr.actus.ckck.fragment.MenuTab;
import kr.actus.ckck.fragment.StoreTab;
import kr.actus.ckck.setaddr.SetAddrActivity;
import kr.actus.ckck.util.ServerResponse;
import kr.actus.ckck.util.SetURL;
import kr.actus.ckck.util.SetUtil;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends FragmentActivity implements OnClickListener {
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	
	DrawerAdapter drawerAdapter;
	DrawerItem drawerItem;
	ArrayList<DrawerItem> drawerItemList = new ArrayList<DrawerItem>();
//	private LinearLayout mDrawerLinear;
	private ActionBarDrawerToggle mDrawerToggle;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private String[] mCategory;
	private Button addrOther;
	private final static String TAG = "MainActivity";

	public final static int MAINTAB = 0;
	public final static int STORETAB = 1;
	public final static int MENUTAB = 2;
	ServerResponse sResponese;
	SetUtil util;
	SetURL url;
	int mCurrentFragmentIndex;
	SharedPreferences pref;
	SharedPreferences.Editor editor;
	String uniqueKey;
	TextView addrBasic;
//	ServerResponse sr = new ServerResponse();
	AsyncHttpClient client = new AsyncHttpClient();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		getActionBar().setTitle(getTitle());

		pref = getSharedPreferences(url.pref, 0);
		editor = pref.edit();
		
		
		setDrawer();
	
		addrOther = (Button) findViewById(R.id.main_btn_addr_reg);
		addrOther.setOnClickListener(this);
		addrBasic = (TextView) findViewById(R.id.main_tv_addr_basic);
		
//		findViewById(R.id.drawer_myorder).setOnClickListener(this);
	
	
		// �׼ǹ� ������ Ȱ��ȭ
		getActionBar().setDisplayHomeAsUpEnabled(false);

		getActionBar().setHomeButtonEnabled(true);

		// �׼ǹ� �����ܰ� drawerToggle ���� ����
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				Log.v(TAG, getActionBar().getTitle() + "");
				invalidateOptionsMenu(); // onPrepareOptionsMenu()ȣ��
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			setTitle(R.string.app_name);
		}

		mCurrentFragmentIndex = MAINTAB;
		fragmentReplace(mCurrentFragmentIndex);

	
			setStatus();
//			setActionBar();
		
	}
	private void setDrawer() {
		mDrawerTitle = getText(R.string.menu);
		 mCategory = getResources().getStringArray(R.array.category_arr);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerLayout.setOnClickListener(this);
		 mDrawerList = (ListView) findViewById(R.id.left_drawer);
//		mDrawerLinear = (LinearLayout) findViewById(R.id.drawer_linear);
		
		// drawer�� ���� drawer���� ��������
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		// drawer ����Ʈ�� ����� ����
		View header = getLayoutInflater().inflate(R.layout.drawer_list_item_header, null,false);
		View footer = getLayoutInflater().inflate(R.layout.drawer_list_item_footer, null,false);
		drawerItem = new DrawerItem(1,"��ü");
		drawerItemList.add(drawerItem);
		
		mDrawerList.addHeaderView(header);
		mDrawerList.addFooterView(footer);
		drawerAdapter = new DrawerAdapter(this,this,R.layout.drawer_list_item,drawerItemList);  
		mDrawerList.setAdapter(drawerAdapter);

		// mDrawerList.setAdapter(new )
//		 mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
		
	}
	//�׼ǹ� ������ �������� ���ؼ� ȸ������ ���ϰ� ó���ؾ���             14/11/23
	private void setStatus() {
		String uniKey = Secure.getString(this.getContentResolver(),
				Secure.ANDROID_ID);
		RequestParams param = new RequestParams();
		param.put("uniqueKey", uniKey);
//		JSONArray result = sr.postJSONArray(SetURL.MEMINFO, param);
		
		//			editor.putString("regKey", result.getString(0));
		
		client.post(SetURL.MEMINFO, param, new JsonHttpResponseHandler(){

			@Override
			public void onFailure(Throwable e, JSONArray errorResponse) {
				Log.v(TAG,"array failure : "+errorResponse);
				super.onFailure(e, errorResponse);
			}

			@Override
			public void onSuccess(JSONArray response) {
				try {
					Log.v(TAG,"array success : "+response.getJSONObject(0));
					JSONObject con = response.getJSONObject(0);
//					editor.clear();
					editor.putString("memPoint", con.getString("memPoint"));
					editor.putString("regDate", con.getString("regDate"));
					editor.putString("address1", con.getString("address1"));
					editor.putString("address2", con.getString("address2"));
					editor.putString("memName", con.getString("memName"));
					editor.putString("uniqueKey", con.getString("uniqueKey"));
					editor.putString("mobile", con.getString("mobile"));
					
					editor.commit();
					
					addrBasic.setText(con.getString("address1"));
					pref.getString("uniqueKey", null);
					Log.v(TAG,"pref : "+pref.getString("uniqueKey", null));
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				
				super.onSuccess(response);
			}
			
		});	
				
		
	}

	Bundle savebundle = new Bundle();

	public void fragmentReplace(int fragmentIndex) {

		Fragment newFragment = null;
		newFragment = getFragment(fragmentIndex);
		// �÷��׸�Ʈ ��ü
		final FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		// Bundle savebundle = new Bundle();
		// savebundle.putString("title", "bundle value");

		newFragment.setArguments(savebundle);
		Log.v(TAG, "savebundle :" + savebundle);
		transaction.replace(R.id.content_frame, newFragment);
		Log.v(TAG, "newFragment :" + newFragment);
		transaction.commit();

	}

	private Fragment getFragment(int idx) {
		Fragment newFragment = null;
		switch (idx) {
		case MAINTAB:
			newFragment = new MainTab(this);

			break;
		case STORETAB:
			newFragment = new StoreTab(this);
			break;
		case MENUTAB:
			newFragment = new MenuTab(this);
			break;
		default:

			break;
		}

		return newFragment;

	}

//	int ACTION_MENU;
//	//�׼ǹ� ������ ���� �۾���.....
//	public void setActionBar() {
//		if (pref.getString("uniqueKey", null) != null) {
//			
//			ACTION_MENU = R.menu.main;
//
//		} else {
//
//			ACTION_MENU = R.menu.main_adduser;
//
//		}
//
//	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);

		return super.onCreateOptionsMenu(menu);
	}

	// invalidateOptionsMenu()ȣ��
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {

		// drawer �޴��� ���������� �׼ǹ� ������ ����
//		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerLinear);
		// menu.findItem(R.id.action_cart).setVisible(!drawerOpen);

		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// drawer�޴��� �����ְų� �޴����� ������ Ŭ�������� �׼ǹپ����� ����
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		switch (item.getItemId()) {
		case R.id.action_cart: // ��ٱ��� ���ý� ȭ�� ��ȯ

			Intent intent = new Intent(this, CartActivity.class);
			startActivity(intent);
			return true;
		case R.id.action_addUser: // ȸ�����Լ��ý� ȭ����ȯ

			Intent intentUser = new Intent(this, AddUserActivity.class);

			startActivity(intentUser);

			return true;

		default:
			return super.onOptionsItemSelected(item);

		}
	}

	// drawer�޴����� ����Ʈ���� ������ Ŭ��������
//	private class DrawerItemClickListener implements
//			ListView.OnItemClickListener {
//		@Override
//		public void onItemClick(AdapterView<?> parent, View view, int position,
//				long id) {
//			selectItem(position);
//		}
//	}

	private void selectItem(String title) {
		// drawer�޴����� �������� Ŭ�������� fragment ȭ�� ����
		Fragment fragment = getFragment(MENUTAB);
		Bundle args = new Bundle();
		args.putString(MainTab.MENU_NUM, title);
		fragment.setArguments(args);

		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.content_frame, fragment).commit();

		// update selected item and title, then close the drawer
//		mDrawerList.setItemChecked(title, true);
		setTitle(title);
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// ��ۻ��� ����ȭ
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// ���� �� ��ۿ� ����
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.main_btn_addr_reg:

			Intent intent = new Intent(this, SetAddrActivity.class);
			startActivity(intent);
			break;
		case R.id.drawer_myorder:
			Log.v(TAG,"myorder");
			break;
		}

	}

	public void receive(Bundle bundle, int index) {
		if (savebundle != null) {
			savebundle = null;
		}
		savebundle = bundle;
		fragmentReplace(index);
	}
	public void receive(String title){
		
		selectItem(title);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

}