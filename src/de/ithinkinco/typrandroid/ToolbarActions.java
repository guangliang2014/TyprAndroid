package de.ithinkinco.typrandroid;

import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.text.Editable;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.Toast;

import de.ithinkinco.typrandroid.DirectoryPicker;

public class ToolbarActions extends Activity {

	private static final String TAG = "ToolbarActions";
	
	private static final int DIALOG_WORD_COUNT = 1004;
	
	private String[] fileList;
	public static String chosenFile;
	private static final String SDPATH = Environment.getExternalStorageDirectory().getPath().toString();
	public static File path = new File(Environment.getExternalStorageDirectory().getPath());
	public static final String FTYPE = ".md";
	public static final String UP_FOLDER = "../ (Up a folder)";
	public static final String CURRENT_FOLDER = "./ (Current folder)";
	
	EditText textArea;
	
	Dialog dialog = null;
	AlertDialog.Builder builder = null;
	
	public ToolbarActions() {
	}
	
	public Dialog newFile(final Activity activity) {
		builder = new AlertDialog.Builder(activity);
		builder.setMessage("Are you sure you want to open a new file without saving the current one?")
			.setCancelable(true)
			.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					Intent intent = activity.getIntent();
					activity.finish();
					Typr.isNew = true;
					activity.startActivity(intent);
				}
	       })
	       .setNegativeButton("No", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	                dialog.cancel();
	           }
	       });
		return dialog = builder.create();
	}
	
	public Dialog openFile(final Activity activity) {
		builder = new Builder(activity);
		builder.setTitle("Choose your file");
		loadFileList();
		builder.setItems(fileList, new FileOpener(fileList, chosenFile, path, activity));
		return dialog = builder.create();
	}
	
	public boolean saveFile(Activity activity) {
		Log.d(TAG, path.toString());
		String file = path.toString();
		if(file == null || 
				file.equals(SDPATH) || 
				file.equals(CURRENT_FOLDER) || 
				file.equals(UP_FOLDER) ||
				Typr.isNew == true) {
			saveAs(activity);
			return true;
		} else {
			return writeFile(activity);
		}
	}
	
    public void saveAs(Activity activity) {
		activity.showDialog(Typr.DIALOG_SAVEAS_FILE);
		activity.showDialog(Typr.DIALOG_DIRECTORY_PICKER);
    }
	
	public Dialog saveAsFile(final Activity activity) {
		builder = new AlertDialog.Builder(activity);
		builder.setTitle("Name of file");
		final EditText fileName = new EditText(activity);
		builder.setView(fileName);
		builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				activity.setTitle(fileName.getText().toString());
				chosenFile = path.toString() + "/" + fileName.getText().toString();
				writeFile(activity);
				return;
			}
		});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				return;
			}
		});
		return dialog = builder.create();
	}
	
	public Dialog pickDirectory(Activity activity) {
		builder = new Builder(activity);
		builder.setTitle("Pick save folder");
		loadFileList();
		builder.setItems(fileList, new DirectoryPicker(fileList, chosenFile, path, activity));
		return dialog = builder.create();
	}
    
    public void showTools(final Activity activity) {
    	PopupMenu toolsMenu = new PopupMenu(activity, activity.findViewById(R.id.menu_tools));
    	toolsMenu.inflate(R.menu.toolsmenu);
		toolsMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			public boolean onMenuItemClick(MenuItem item) {
				switch(item.getItemId()) {
				case R.id.word_count:
					showWordCount(activity);
				}
				return false;
			}
		});
		toolsMenu.show();
    }
    
    public void showWordCount(Activity activity) {
    	String wordCount = Integer.toString(wordCount(activity));
    	Toast toast = null;
    	toast = Toast.makeText(activity, "Word Count: " + wordCount, Toast.LENGTH_SHORT);
		// Place it just below the actionbar
		toast.setGravity(Gravity.TOP, Gravity.CENTER_VERTICAL, 60);
		toast.show();
    }
    
    private void loadFileList() {
    	try {
    		path.mkdir();
    	} catch(SecurityException ex) {
    		Log.e(TAG, "unable to write on the sd card " + ex.toString());
    	}
    	if(path.exists()) {
    		FilenameFilter filter = new FilenameFilter() {
    			public boolean accept(File dir, String filename) {
    				File selected = new File(dir, filename);
    				if(selected.isHidden() || !selected.canRead())
    					return false;
    				else {
    					return filename.contains(FTYPE) || selected.isDirectory();
    				}
    			}
    		};
    		List<String> newFileList = new ArrayList<String>(Arrays.asList(path.list(filter)));
    		if(!path.toString().equals(SDPATH)) {
    			newFileList.add(UP_FOLDER);
    		}
    		newFileList.add(CURRENT_FOLDER);
    		fileList = new String[newFileList.size()];
    		newFileList.toArray(fileList);
    		java.util.Arrays.sort(fileList);
    	} else {
    		fileList = new String[0];
    	}
    }
    
    public int wordCount(Activity activity) {
		int wordCount = 0;
		int index = 0;
		boolean prevWhiteSpace = true;

		EditText textArea = (EditText) activity.findViewById(R.id.editText);
		String document = textArea.getText().toString();

		while(index < document.length()) {
			char c = document.charAt(index++);
			boolean currWhiteSpace = Character.isWhitespace(c);
			if(prevWhiteSpace && !currWhiteSpace) {
				wordCount++;
			}
			prevWhiteSpace = currWhiteSpace;
		}
		return wordCount;
	}
    
    private boolean writeFile(Activity activity) {
    	Log.d(TAG, chosenFile);
    	Toast toast = null;
		File file = new File(chosenFile);
		textArea = (EditText) activity.findViewById(R.id.editText);
		Editable document = textArea.getText();
		try	{
			FileWriter writer = new FileWriter(file);
			writer.append(document);
			writer.flush();
			writer.close();
			toast = Toast.makeText(activity, "File saved.", Toast.LENGTH_SHORT);
			// Place it just below the actionbar
			toast.setGravity(Gravity.TOP, Gravity.CENTER_VERTICAL, 60);
			toast.show();
			return true;
		} catch(IOException ex) {
			Log.e(TAG, ex.toString());
			return false;
		}
    }
}
