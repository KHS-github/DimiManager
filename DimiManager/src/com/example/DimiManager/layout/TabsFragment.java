package com.example.DimiManager.layout;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import com.example.DimiManager.R;

public class TabsFragment extends Fragment implements OnTabChangeListener {

	private static final String TAG = "FragmentTabs";
	public static final String TAB_NOTICES = "Notices";
	public static final String TAB_TODOS = "Todos";

	private View mRoot;
	private TabHost mTabHost;
	private int mCurrentTab;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRoot = inflater.inflate(R.layout.tabs_fragment, null);
		mTabHost = (TabHost) mRoot.findViewById(android.R.id.tabhost);
		setupTabs();
		return mRoot;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setRetainInstance(true);

		mTabHost.setOnTabChangedListener(this);
		mTabHost.setCurrentTab(mCurrentTab);
		// manually start loading stuff in the first tab
		updateTab(TAB_NOTICES, R.id.tab_1);
	}

	private void setupTabs() {
		mTabHost.setup(); // important!
		mTabHost.addTab(newTab(TAB_NOTICES, R.string.tab_notices, R.id.tab_1));
		mTabHost.addTab(newTab(TAB_TODOS, R.string.tab_todos, R.id.tab_2));
	}

	private TabSpec newTab(String tag, int labelId, int tabContentId) {
        View indicator=null;
		Log.d(TAG, "buildTab(): tag=" + tag);

        if(labelId == R.string.tab_notices) {
            indicator = LayoutInflater.from(getActivity()).inflate(
                    R.layout.noticetab,
                    (ViewGroup) mRoot.findViewById(android.R.id.tabs), false);
        }
        else if(labelId == R.string.tab_todos){
            indicator = LayoutInflater.from(getActivity()).inflate(
                    R.layout.todotab,
                    (ViewGroup) mRoot.findViewById(android.R.id.tabs), false);
        }
        indicator.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));

		TabSpec tabSpec = mTabHost.newTabSpec(tag);
		tabSpec.setIndicator(indicator);
		tabSpec.setContent(tabContentId);
		return tabSpec;
	}

	@Override
	public void onTabChanged(String tabId) {
		Log.d(TAG, "onTabChanged(): tabId=" + tabId);
		if (TAB_NOTICES.equals(tabId)) {
			updateTab(tabId, R.id.tab_1);
			mCurrentTab = 0;
			return;
		}
		if (TAB_TODOS.equals(tabId)) {
			updateTab(tabId, R.id.tab_2);
			mCurrentTab = 1;
			return;
		}
	}

	private void updateTab(String tabId, int placeholder) {
		FragmentManager fm = getFragmentManager();
		if (fm.findFragmentByTag(tabId) == null) {
			fm.beginTransaction()
					.replace(placeholder, new MyListFragment(tabId), tabId)
					.commit();
		}
	}

}
