package de.ithinkinco.typrandroid;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.EditText;

import com.petebevin.markdown.MarkdownProcessor;

public class PreviewFragment extends Fragment {
	
	public static final String ARG_SECTION_NUMBER = "section_number";
	
	public String mimeType = "text/html";
	
	WebView markdownPreview;
	EditText textArea;

	public PreviewFragment () {
		
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
        Bundle args = getArguments();
        
        if(container == null)
        	return null;
        
        markdownPreview = (WebView) inflater.inflate(R.layout.preview, container, false);
        textArea = (EditText) getActivity().findViewById(R.id.editText);
        
        MarkdownProcessor mdProcessor = new MarkdownProcessor();
        String html = mdProcessor.markdown(textArea.getText().toString());
        markdownPreview.loadData(html, mimeType, null);
        return markdownPreview;
    }
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);	
	}
}
