package de.ithinkinco.typrandroid;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class EditorListener extends FragmentActivity implements ActionBar.TabListener {

	Fragment fragment;
	
	public EditorListener() {
		
	}
	
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		Bundle args = new Bundle();
		//args.putInt(EditorFragment.ARG_SECTION_NUMBER, tab.getPosition() + 1);
		//fragment.setArguments(args);
		if(fragment == null) {
			fragment = new EditorFragment();
			ft.add(R.id.container, fragment);
		} else {
			ft.attach(fragment);
		}
	}

	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

}
