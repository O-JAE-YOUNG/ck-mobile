package kr.actus.ckck.fragment;

import java.util.ArrayList;
import java.util.Locale;

import kr.actus.ckck.R;
import kr.actus.ckck.gridlist.GridAdapter;
import kr.actus.ckck.gridlist.GridItem;
import kr.actus.ckck.setaddr.SetAddr;
import kr.actus.ckck.viewpager.ViewPagerAdapter;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MainFragment extends Fragment implements OnClickListener {

	public static final String MENU_NUM = "menu_num";
	private ImageView imgAd;
	private Button addrOther;
	private GridView gridList;
	private GridAdapter gAdapter;
	private GridItem gItem;
	private LinearLayout mPageMark;
	private ArrayList<GridItem> gListItem = new ArrayList<GridItem>();
	private ViewPager mPager;
	public int[] mRes = new int[]{R.drawable.menu_sample,R.drawable.menu_sample,R.drawable.menu_sample,R.drawable.menu_sample,R.drawable.menu_sample};
	private int prePosition;
	View cView;
	
	public MainFragment() {
		//���� Ŭ������ ������
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		cView = inflater.inflate(R.layout.fragment_main, container,
				false);
		int i = getArguments().getInt(MENU_NUM);
//		imgAd = (ImageView) cView.findViewById(R.id.imgAd);
		addrOther = (Button) cView.findViewById(R.id.addrOther);
		
		mPager = (ViewPager) cView.findViewById(R.id.content_pager);
		mPageMark=(LinearLayout) cView.findViewById(R.id.page_mark);
		setViewPager();
		setGrid();
		
//		imgAd.setOnClickListener(this);
		addrOther.setOnClickListener(this);
		
		
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
			iv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT));
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0,
					0);

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
//		case R.id.imgAd:	//�����׸� Ŭ����
//			Toast.makeText(getActivity(), "imgAd click!",
//					Toast.LENGTH_SHORT).show();
//			break;
		case R.id.addrOther:	//�ٸ������ Ŭ����
			Intent intent = new Intent(getActivity(), SetAddr.class);
			startActivity(intent);
			break;

		}

	}
}
	
	
	
	
	
	
	
	
	
	
	
	
