package com.bjor.fragments;

import com.bjor.beerdaddy.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * 
 * @author Arnar Orri Eyj&#243lfsson
 * This is an empty fragment, put here for future use
 */
public class CurrentFragment extends Fragment {
	
	/**
	 * Empty constructor required for fragment subclasses
	 */
	public CurrentFragment() {
        // Empty constructor required for fragment subclasses
    }

    /* (non-Javadoc)
     * @see android.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_current, container, false);
        
        return rootView;
    }

}
