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
import java.util.Locale;

import kr.actus.ckck.gridlist.GridAdapter;
import kr.actus.ckck.gridlist.GridItem;
import kr.actus.ckck.setaddr.SetAddr;
import kr.actus.ckck.viewpager.ViewPagerAdapter;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

public class MainActivity extends Activity implements OnClickListener{
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private LinearLayout mDrawerLinear;
	private ActionBarDrawerToggle mDrawerToggle;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private String[] mCategory;
	private Button addrOther;
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
		addrOther = (Button) findViewById(R.id.addrOther);
		addrOther.setOnClickListener(this);
		
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
//		private Button addrOther;
		private GridView gridList;
		private GridAdapter gAdapter;
		private GridItem gItem;
		private LinearLayout mPageMark;
		private ArrayList<GridItem> gListItem = new ArrayList<GridItem>();
		private ViewPager mPager;
		public int[] mRes = new int[]{R.drawable.menu_sample,R.drawable.menu_sample,R.drawable.menu_sample,R.drawable.menu_sample,R.drawable.menu_sample};
		private int prePosition;
		View cView;
		
		public MenuFragment() {
			//���� Ŭ������ ������
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			cView = inflater.inflate(R.layout.fragment_main, container,
					false);
			int i = getArguments().getInt(MENU_NUM);
//			imgAd = (ImageView) cView.findViewById(R.id.imgAd);
//			addrOther = (Button) cView.findViewById(R.id.addrOther);
			
			mPager = (ViewPager) cView.findViewById(R.id.content_pager);
			mPageMark=(LinearLayout) cView.findViewById(R.id.page_mark);
			setViewPager();
			setGrid();
			
//			imgAd.setOnClickListener(this);
//			addrOther.setOnClickListener(this);
			
			
			//drawer�޴� ����Ʈ�� �׽�Ʈ��
			String category = getResources().getStringArray(
					R.array.category_arr)[i];

			int imageId = getResources().getIdentifier(
					category.toLowerCase(Locale.getDefault()), "drawable",
					getActivity().getPackageName());
			
			getActivity().setTitle(category);
			return cView;
		}

	
		
		
		private void setViewPager() {	//����â ��������
			ViewPagerAdapter vpAdapter = new ViewPagerAdapter(getActivity(),mRes);
			mPager.setAdapter(vpAdapter);
			
			mPager.setOnPageChangeListener(new OnPageChangeListener() {
				
				@Override
				public void onPageSelected(int position) {	//������������ġ Ȯ�ο� ��Ŭ ����
					mPageMark.getChildAt(prePosition).setBackgroundResource(
							R.drawable.page_not);
					mPageMark.getChildAt(position).setBackgroundResource(
							R.drawable.board_circle);
					prePosition = position;
					
				}
				
				@Override
				public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onPageScrollStateChanged(int arg0) {
					// TODO Auto-generated method stub
					
				}
			});
			initPageMark();
			
		}

		private void initPageMark() {	//���� ������ ��ġ Ȯ�ο� ��Ŭ
			for (int i = 0; i < mRes.length; i++) {
				ImageView iv = new ImageView(getActivity().getApplicationContext());
				
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT);
				params.gravity = Gravity.CENTER;
			
				iv.setLayoutParams(params);
				iv.setPadding(10, 10, 10, 10);
				
				if (i == 0)
					iv.setBackgroundResource(R.drawable.board_circle);
				else
					iv.setBackgroundResource(R.drawable.page_not);

				mPageMark.addView(iv);
			}
			prePosition = 0;
			
		}

		private void setGrid() {// �׸���� ������ �׽�Ʈ
			for(int k=0;k<10;k++){
				gItem = new GridItem(R.drawable.menu_sample, "��ȣ��", "Ÿ��", "�ּ��ֹ��ݾ�", "��޿���");
				gListItem.add(gItem);
				}
				gridList = (GridView)cView.findViewById(R.id.gridView1);
				gAdapter = new GridAdapter(getActivity(), R.layout.gridview_item, gListItem);
				gridList.setAdapter(gAdapter);
				
			
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
//			case R.id.imgAd:	//�����׸� Ŭ����
//				Toast.makeText(getActivity(), "imgAd click!",
//						Toast.LENGTH_SHORT).show();
//				break;
			

			}

		}
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		
		case R.id.addrOther:
			
			Intent intent = new Intent(this, SetAddr.class);
			startActivity(intent);
			break;
		}
		
	}
	
}