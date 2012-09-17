package de.ithinkinco.typrandroid;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class EditorFragment extends Fragment {
	
	public static final String ARG_SECTION_NUMBER = "section_number";
	
	public EditorFragment() {
		
	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
        Bundle args = getArguments();
        
        if(container == null)
        	return null;
        
        EditText textArea = (EditText) inflater.inflate(R.layout.editor, container, false);
        textArea.setText(Typr.document);
        return textArea;
    }
}
