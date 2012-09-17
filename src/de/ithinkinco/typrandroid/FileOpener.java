package de.ithinkinco.typrandroid;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.EditText;

public class FileOpener implements DialogInterface.OnClickListener {
	
	private static final String TAG = "FileOpener";
	
	private String[] fileList;
	private String chosenFile;
	private File path;
	private Activity activity;
	
	public FileOpener(String[] mFileList, String mChosenFile, File mPath, Activity mActivity) {
		fileList = mFileList;
		chosenFile = mChosenFile;
		path = mPath;
		activity = mActivity;
	}

	public void onClick(DialogInterface dialog, int which) {
		chosenFile = path.toString() + "/" + fileList[which];
		if(fileList[which].equals(ToolbarActions.CURRENT_FOLDER)) {
			Log.d(TAG, path.toString());
		} else if(fileList[which].equals(ToolbarActions.UP_FOLDER)) {

			Log.d(TAG, chosenFile);
			List<String> temp = new ArrayList<String>(Arrays.asList(chosenFile.split("/")));
			for(int i = 0; i < 3; i++) {
				temp.remove(temp.size() -1);
			}
			
			chosenFile = "";
			for(String item : temp) {
				chosenFile += item + "/";
			}
			ToolbarActions.path = new File(chosenFile);
			activity.removeDialog(Typr.DIALOG_OPEN_FILE);
			activity.showDialog(Typr.DIALOG_OPEN_FILE);
		} else if(!fileList[which].contains(ToolbarActions.FTYPE)) {

			Log.d(TAG, chosenFile);
			ToolbarActions.path = new File(chosenFile);
			activity.removeDialog(Typr.DIALOG_OPEN_FILE);
			activity.showDialog(Typr.DIALOG_OPEN_FILE);
		} else {
			Log.d(TAG, chosenFile);
			ToolbarActions.chosenFile = chosenFile;
			File file = new File(chosenFile);
			StringBuilder text = new StringBuilder();
			try {
				BufferedReader br = new BufferedReader(new FileReader(file));
				String line;
				while((line = br.readLine()) != null) {
					text.append(line);
					text.append("\n");
				}
				EditText textArea = (EditText) activity.findViewById(R.id.editText);
				textArea.setText(text);
				List<String> temp = new ArrayList<String>(Arrays.asList(chosenFile.split("/")));
				String fileName = temp.get(temp.size() -1);
				activity.setTitle(fileName);
			} catch(IOException ex) {
				Log.e(TAG, ex.toString());
			}
		}
	}
}
