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

import java.util.Locale;

import kr.actus.ckck.setaddr.SetAddr;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private LinearLayout mDrawerLinear;
	private ActionBarDrawerToggle mDrawerToggle;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private String[] mCategory;

	private final String TAG = "MainActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		getActionBar().setTitle(getTitle());

		mDrawerTitle = getText(R.string.menu);

		mCategory = getResources().getStringArray(R.array.category_arr);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		mDrawerLinear = (LinearLayout) findViewById(R.id.drawer_linear);

		
		// drawer�� ���� drawer���� ��������
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		// drawer ����Ʈ�� ����� ����
		mDrawerList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.drawer_list_item, mCategory));
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// �׼ǹ� ������ Ȱ��ȭ
		getActionBar().setDisplayHomeAsUpEnabled(false);
		getActionBar().setHomeButtonEnabled(true);

		
		// �׼ǹ� �����ܰ� drawerToggle ���� ����
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, 
		R.drawable.ic_drawer, 
		R.string.drawer_open, 
		R.string.drawer_close 
		) {
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
			selectItem(0);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	//invalidateOptionsMenu()ȣ��
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		
		// drawer �޴��� ���������� �׼ǹ� ������ ����
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerLinear);
		menu.findItem(R.id.action_cart).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
         // drawer�޴��� �����ְų� �޴����� ������ Ŭ�������� �׼ǹپ����� ����
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        
        switch(item.getItemId()) {
        case R.id.action_cart: //��ٱ��� ���ý� ȭ�� ��ȯ
           
          Intent intent = new Intent(this,Cart.class);
           startActivity(intent);
            return true;
        case R.id.action_addUser: //ȸ�����Լ��ý� ȭ����ȯ
        	
        	Intent intentUser = new Intent(this,AddUser.class);
        	
        	startActivity(intentUser);
        
        	return true;
        
        default:
            return super.onOptionsItemSelected(item);
            
    
    }
	}
	// drawer�޴����� ����Ʈ���� ������ Ŭ��������
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);
		}
	}

	private void selectItem(int position) {
		// drawer�޴����� �������� Ŭ�������� fragment ȭ�� ����
		Fragment fragment = new MenuFragment();
		Bundle args = new Bundle();
		args.putInt(MenuFragment.MENU_NUM, position);
		fragment.setArguments(args);

		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.content_frame, fragment).commit();

		// update selected item and title, then close the drawer
		mDrawerList.setItemChecked(position, true);
		setTitle(mCategory[position]);
		mDrawerLayout.closeDrawer(mDrawerLinear);
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

	
	public static class MenuFragment extends Fragment implements
			OnClickListener {
		public static final String MENU_NUM = "menu_num";
		private ImageView imgAd;
		private Button addrOther;

		public MenuFragment() {
			//���� Ŭ������ ������
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			int i = getArguments().getInt(MENU_NUM);
			imgAd = (ImageView) rootView.findViewById(R.id.imgAd);
			addrOther = (Button) rootView.findViewById(R.id.addrOther);

			imgAd.setOnClickListener(this);
			addrOther.setOnClickListener(this);

			String category = getResources().getStringArray(
					R.array.category_arr)[i];

			int imageId = getResources().getIdentifier(
					category.toLowerCase(Locale.getDefault()), "drawable",
					getActivity().getPackageName());
			// ((ImageView)
			// rootView.findViewById(R.id.image)).setImageResource(imageId);
			getActivity().setTitle(category);
			return rootView;
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.imgAd:
				Toast.makeText(getActivity(), "imgAd click!",
						Toast.LENGTH_SHORT).show();
				break;
			case R.id.addrOther:
				Intent intent = new Intent(getActivity(), SetAddr.class);
				startActivity(intent);
				break;

			}

		}
	}
}