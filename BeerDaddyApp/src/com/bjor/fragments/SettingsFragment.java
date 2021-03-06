package com.bjor.fragments;

import com.bjor.beerdaddy.R;
import com.bjor.models.Global;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Application settings - this class controls the user settings for the application
 * 
 * @author Arnar Orri Eyj&#243lfsson
 *
 */
public class SettingsFragment extends Fragment {

	/** User set variables  */
	private String age, gender, weight, emergency;
	/** Gender input field */
	private Spinner genderInput;
	/** Input fields for variables */
	private EditText ageInput, weightInput, emergencyInput;
	/** Navigation buttons - save or discard information */
	private Button buttonSave, buttonCancel;
	/** An instance of SharedPreferences */
	private SharedPreferences sharedPref;

	
	/**
	 * Empty constructor required for fragment subclasses
	 */
	public SettingsFragment() {
        // Empty constructor required for fragment subclasses
    }

    /* (non-Javadoc)
     * @see android.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
                
        sharedPref = this.getActivity().getSharedPreferences(Global.PREFS_NAME, 0);
		ageInput = (EditText) rootView.findViewById(R.id.ageInput);
		genderInput = (Spinner) rootView.findViewById(R.id.genderInput);
		
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(),
		        R.array.gender_array, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		genderInput.setAdapter(adapter);
		
		weightInput = (EditText) rootView.findViewById(R.id.weightInput);
		emergencyInput = (EditText) rootView.findViewById(R.id.emergencyInput);
		
		buttonSave = (Button) rootView.findViewById(R.id.buttonSave);
		buttonCancel = (Button) rootView.findViewById(R.id.buttonCancel);
        
        buttonSave.setOnClickListener(new OnClickListener()
        {
    		@Override
    		public void onClick(View v) {
    			age = ageInput.getText().toString();
    			gender = genderInput.getSelectedItem().toString();
    			weight = weightInput.getText().toString();
    			emergency = emergencyInput.getText().toString();
    			
    			
    			SharedPreferences.Editor editor = sharedPref.edit();
    			if(!weight.equals("") && !weight.startsWith(".") && !weight.endsWith(".")) {
    				editor.putString("age", age);
    		        editor.putString("gender", gender);
    		        editor.putString("weight", weight);
    		        editor.putString("emergency", emergency);
    		        editor.commit();
    		        
    		        Toast.makeText(getActivity(), "Changes saved!", Toast.LENGTH_SHORT).show();
    			} else {
    				Toast.makeText(getActivity(), "Make sure all input fields are valid!", Toast.LENGTH_SHORT).show();
    			}
    		}
        });
        buttonCancel.setOnClickListener(new OnClickListener()
        {
    		@Override
    		public void onClick(View v) {
    			
    		}
        });
        

        ageInput.setText(sharedPref.getString("age", ""));
        int pos;
        if(sharedPref.getString("gender", "Male").equals("Male")) {
        	pos = 0;
        } else {
        	pos = 1;
        }
        genderInput.setSelection(pos);
        weightInput.setText(sharedPref.getString("weight", ""));
        emergencyInput.setText(sharedPref.getString("emergency", ""));
        
        return rootView;
    }
	
    
}

