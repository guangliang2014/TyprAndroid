package de.ithinkinco.typrandroid;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;

public class Typr extends Activity {

    private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
    final String TAG = "Typr";

	private final ToolbarActions actions = new ToolbarActions();
    
    public static final int DIALOG_NEW_FILE = 1000;
	public static final int DIALOG_OPEN_FILE = 1001;
	public static final int DIALOG_SAVE_FILE = 1002;
	public static final int DIALOG_SAVEAS_FILE = 1003;
	
	//
	public static final int DIALOG_DIRECTORY_PICKER = 2000;
	
	public static boolean isNew = false;
    
	EditText textArea;
	
	// Used for orientation changes
	public static CharSequence document = "";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_typr);
        Log.d(TAG, Boolean.toString(isNew));
        if(savedInstanceState != null && isNew == false) {
        	document = savedInstanceState.getCharSequence("text");
        	setTitle(savedInstanceState.getCharSequence("title"));
        } else {
        	document = "";
        	setTitle("Typr");
        	
        }
        
        // Keeps the keyboard from popping up the instant the app opens.
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
        try {
        	Tab editorTab = actionBar.newTab();
            editorTab.setText("Editor");
            editorTab.setTabListener(new EditorListener());
            actionBar.addTab(editorTab, true);

            Tab previewTab = actionBar.newTab();
            previewTab.setText("Preview");
            previewTab.setTabListener(new PreviewListener());
            actionBar.addTab(previewTab);
        } catch(IllegalStateException ex) {
        	Log.e(TAG, ex.toString());
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
            getActionBar().setSelectedNavigationItem(
                    savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_SELECTED_NAVIGATION_ITEM,
        		getActionBar().getSelectedNavigationIndex());
        textArea = (EditText) findViewById(R.id.editText);
        outState.putCharSequence("text", textArea.getText());
        outState.putCharSequence("title", this.getTitle());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_typr, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()) {
    	
    	case R.id.menu_new:
    		showDialog(DIALOG_NEW_FILE);
    		return true;
    		
    	case R.id.menu_open:
    		showDialog(DIALOG_OPEN_FILE);
    		return true;
    		
    	case R.id.menu_save:
    		actions.saveFile(this);
    		return true;
    		
    	case R.id.menu_saveas:
    		actions.saveAs(this);
    		return true;
    	
    	case R.id.menu_tools:
        		actions.showTools(this);
    		return true;
    	}
    	return true;
    }
    
    @Override
    protected Dialog onCreateDialog(int id) {
    	Dialog dialog = null;
    	switch(id) {
    	
    	case DIALOG_NEW_FILE:
    		dialog = actions.newFile(this);
    		break;
    		
    	case DIALOG_OPEN_FILE:
    		dialog = actions.openFile(this);
    		break;
    		
    	case DIALOG_SAVEAS_FILE:
    		dialog = actions.saveAsFile(this);
    		break;
    	
    	case DIALOG_DIRECTORY_PICKER:
    		dialog = actions.pickDirectory(this);
    	}
    	return dialog;
    }
}
