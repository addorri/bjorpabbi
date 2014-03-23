package com.bjor.fragments;

import com.bjor.beerdaddy.R;
import com.bjor.models.Gender;
import com.bjor.models.Global;
import com.bjor.models.Widmark;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * This fragment calculates users drink size for drinking
 * games
 * 
 * @author G&#237sli Laxdal Sturlaugsson, Arnar P&#225ll Birgisson
 *
 */
public class DrinkGameFragment extends Fragment {

	/** User specific variables are stored in shared preferences */
	private SharedPreferences sharedpref;
	/** Gender of the user */
	private Gender gender;
	/** Weight of the user */
	private float weight;
	/** Number of drinks */
	private int nod;
	/** Text views */
	private TextView myTextView1, myTextView2, myTextView3;
	/** String resources */
	private Resources res;

	/**
	 * Empty constructor required for fragment subclasses
	 */
	public DrinkGameFragment() {
		//Empty constructor
    }

    /* (non-Javadoc)
     * @see android.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_drink_game, container, false);
        
        sharedpref = getActivity().getSharedPreferences(Global.PREFS_NAME, 0);
		myTextView1 = (TextView) rootView.findViewById(R.id.textView1);
		myTextView2 = (TextView) rootView.findViewById(R.id.textView2);
		myTextView3 = (TextView) rootView.findViewById(R.id.textView3);
		res = getResources();
        
        return rootView;
    }
	
	/* (non-Javadoc)
	 * @see android.app.Fragment#onResume()
	 */
	@Override
	public void onResume() {	
		super.onResume();
		
		gender = new Gender(sharedpref.getString("gender", "male"));
		weight = Float.parseFloat(sharedpref.getString("weight", "80"));
    
		double avgMan = Widmark.BAC(new Gender("male"), 1, 80);
		double user = Widmark.BAC(gender, 1, weight);
		int ratio = (int) (100 * avgMan / user);
		
		myTextView1.setText(String.format(res.getString(R.string.alcTol), ratio));
		
		nod = getNod(ratio);
				
		if( ratio > 110 && ratio < 190) {
			myTextView2.setText(res.getQuantityString(R.plurals.addDrinks, nod, nod));
		}
		else if(ratio <= 90 && ratio > 50) {
			myTextView2.setText(res.getQuantityString(R.plurals.skipDrinks, nod, nod));
		}
		else if(ratio <= 50) {
			myTextView2.setText(res.getString(R.string.halfDrinks));
		}
		else if( ratio >= 190 && ratio < 280) {
			myTextView2.setText(res.getString(R.string.doubleDrinks));
		}
		else if( ratio >= 280) {
			myTextView2.setText(res.getString(R.string.trebleDrinks));
		}
		else {
			myTextView2.setText(res.getString(R.string.normalDrinks));
		}
	
		myTextView3.setText(res.getString(R.string.avgManMsg));
	}
	
	/**
	 * Private method to decide number of drinks
	 * @param ratio
	 * @return number of drinks
	 */
	private int getNod(int ratio) {
		if( (ratio < 90 && ratio > 70) || (ratio > 110 && ratio < 130) ) 
			return 1;
		else if( (ratio <= 70 && ratio > 50) || (ratio >= 130 && ratio < 150) ) 
			return 2;
		else if( (ratio <= 50 && ratio > 0) || (ratio >= 150 && ratio < 170) ) 
			return 3;
		else
			return 4;
	}
	
}
