package com.bjor.fragments;

import com.bjor.beerdaddy.R;
import com.bjor.models.Global;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * This class creates a selection of different plans for the user to pick.
 * 
 * @author Arnar Orri Eyj&#243lfsson
 *
 */
public class PickPlanFragment extends Fragment {

	/** Clickable image which functions like a button.
	 * Clicking the picture sends you to the appropriate activity screen. */
	private ImageView btnBuzz, btnNOD, btnTime;
	
	/**
	 * Empty constructor required for fragment subclasses
	 */
	public PickPlanFragment() {
		//Empty constructor
    }

    /* (non-Javadoc)
     * @see android.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pick_plan, container, false);
        
        btnBuzz = (ImageView) rootView.findViewById(R.id.buttonbuzz);
		btnNOD = (ImageView) rootView.findViewById(R.id.buttonnod);
		btnTime = (ImageView) rootView.findViewById(R.id.buttontime);
		
		btnBuzz.setOnClickListener(buzzOnClickListener);
		btnNOD.setOnClickListener(nodOnClickListener);
		btnTime.setOnClickListener(timeOnClickListener);
        
        return rootView;
    }
	
	

    /**
	 * Listener for the Buzz-image
	 */
	protected OnClickListener buzzOnClickListener = new OnClickListener()
    {
		@Override
		public void onClick(View v) {
			Global.setType(1);
			navigateFragments();
		}
    };
    
    /**
     * Listener for the Number of drinks-image
     */
    protected OnClickListener nodOnClickListener = new OnClickListener()
    {
		@Override
		public void onClick(View v) {
			Global.setType(2);
			navigateFragments();
		}
    };
    
    /**
     * Listener for the Duration-image
     */
    protected OnClickListener timeOnClickListener = new OnClickListener()
    {
		@Override
		public void onClick(View v) {
			Global.setType(3);
			navigateFragments();
		}
    };
    
    /**
	 * Fragment navigator, replaces current fragment with a new
	 * instance of PlanInputFragment
	 */
    private void navigateFragments() {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, new PlanInputFragment()).commit();
    	
    }
	
}
