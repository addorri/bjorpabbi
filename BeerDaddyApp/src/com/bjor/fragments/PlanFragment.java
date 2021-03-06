package com.bjor.fragments;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.LineChart;
import org.achartengine.chart.PointStyle;
import org.achartengine.chart.ScatterChart;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import antistatic.spinnerwheel.AbstractWheel;
import antistatic.spinnerwheel.OnWheelChangedListener;
import antistatic.spinnerwheel.OnWheelScrollListener;
import antistatic.spinnerwheel.adapters.ArrayWheelAdapter;

import com.bjor.beerdaddy.EmergencyButtonActivity;
import com.bjor.beerdaddy.R;
import com.bjor.db.MySQLiteHelper;
import com.bjor.models.Global;
import com.bjor.models.IPlan;
import com.bjor.models.Level;
import com.bjor.models.LevelPlan;
import com.bjor.models.LinearPlan;
/**
 * Fragment that shows user current plan.
 * 
 * @author Arnar P&#225ll Birgisson
 *
 */
public class PlanFragment extends Fragment {

	/** Renderer that renders the main graph */
	private XYMultipleSeriesRenderer renderer;
	/** Renderer that renders BAC level graph */
	private XYSeriesRenderer bacSeriesRenderer;
	/** Renderer that renders the dots that denote drinks */
	private XYSeriesRenderer drinkSeriesRenderer;
	/** Renderer for yellowLine */
	private XYSeriesRenderer yellowSeriesRenderer;
	/** Renderer for greeLine */
	private XYSeriesRenderer greenSeriesRenderer;
	/** Renderer for redLine */
	private XYSeriesRenderer redSeriesRenderer;
	/** Data serie for yellow line on graph */
    private XYSeries yellowLineSeries;
    /** Data serie for green line on graph */
    private XYSeries greenLineSeries;
    /** Data serie for red line on graph */
    private XYSeries redLineSeries;

	/** The chart view that displays the data. */
	private GraphicalView chartView;
	/** The main dataset that includes all the series that go into a chart. */
	private XYMultipleSeriesDataset dataSet;
	/** The DrinkingPlan object */
	private IPlan drinkingPlan;
	/** The layout that the graph is painted on */
	private LinearLayout graphLayout;
	/** Button that adds drink to current plan and updates graph */
	private Button btnAddDrink;
	/** Button that saves the current plan in a database */
	private Button btnSavePlan;
	/** Button that sends message to emergency number */
	private Button btnEmergency;
	/** An instance of a database */
	SQLiteDatabase database;
	/** Context */
	final Context context = this.getActivity();
	/** Wheels for defining drink */
	private AbstractWheel drinkTypeWheel, drinkVolumeWheel, alcByVolWheel;
	/** Variable for if wheels are scrolling */
    private boolean drinkTypeWheelScrolling = false;
    
    
	/**
	 * Empty constructor required for fragment subclasses
	 */
	public PlanFragment() {
		//Empty constructor
    }

    /* (non-Javadoc)
     * @see android.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_plan, container, false);
        
        initVariables();
		setUpGraph();
		createInitDataSet();
		
		graphLayout = (LinearLayout) rootView.findViewById(R.id.chart);
		btnAddDrink = (Button) rootView.findViewById(R.id.addDrink);
		btnAddDrink.setOnClickListener(addDrinkOnClickListener);
		btnSavePlan = (Button) rootView.findViewById(R.id.savePlan);
		btnSavePlan.setOnClickListener(savePlanOnClickListener);
		
		btnEmergency = (Button) rootView.findViewById(R.id.emergency);
		btnEmergency.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
		btnEmergency.setOnClickListener(EmergencyOnClickListener);
		
        
        return rootView;
    }

	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onResume(android.os.Bundle)
	 */
	@Override
	public void onResume() {
		super.onResume();
		
		if (Global.createNewPlan()) {
			initVariables();
			setUpGraph();
			createInitDataSet();
		}
		
	    chartView = ChartFactory.getCombinedXYChartView(this.getActivity(), dataSet, renderer, 
	    		new String[] { LineChart.TYPE, ScatterChart.TYPE, LineChart.TYPE, LineChart.TYPE, LineChart.TYPE });
	    graphLayout.removeAllViews();
	    graphLayout.addView(chartView, new LayoutParams(LayoutParams.MATCH_PARENT,
	    		LayoutParams.MATCH_PARENT));
	}
	
	/**
	 * 	Method that initializes all series and renderer variables
	 */
	private void initVariables() {
		
		renderer = new XYMultipleSeriesRenderer();
		bacSeriesRenderer = new XYSeriesRenderer();
		drinkSeriesRenderer = new XYSeriesRenderer();
		yellowSeriesRenderer = new XYSeriesRenderer();
		greenSeriesRenderer = new XYSeriesRenderer();
		redSeriesRenderer = new XYSeriesRenderer();
	    yellowLineSeries = new XYSeries("Low");
	    greenLineSeries = new XYSeries("Mid");
	    redLineSeries = new XYSeries("High");	
	}

	/**
	 * Listener for Add Drink button
	 */
	protected OnClickListener addDrinkOnClickListener = new OnClickListener()
	{
		@Override
		public void onClick(View v) {            
			// get prompts.xml view
			LayoutInflater li = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View promptsView = li.inflate(R.layout.dialog_drink_picker, null); 

			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					getActivity());

			// set prompts.xml to alertdialog builder
			alertDialogBuilder.setView(promptsView);
			
			drinkTypeWheel = (AbstractWheel) promptsView.findViewById(R.id.drinkType);
	        drinkTypeWheel.setVisibleItems(3);
	        ArrayWheelAdapter<String> drinkTypeAdapter = new ArrayWheelAdapter<String>(getActivity(), Global.drinkTypes);
	        drinkTypeAdapter.setTextSize(18);
	        drinkTypeWheel.setViewAdapter(drinkTypeAdapter);
	        
	        drinkVolumeWheel = (AbstractWheel) promptsView.findViewById(R.id.drinkVolume);
	        drinkVolumeWheel.setVisibleItems(3);
	        
	        alcByVolWheel = (AbstractWheel) promptsView.findViewById(R.id.alcByVolume);
	        alcByVolWheel.setVisibleItems(3);
	        ArrayWheelAdapter<String> alcByVolAdapter = new ArrayWheelAdapter<String>(getActivity(), Global.alcByVol);
	        alcByVolAdapter.setTextSize(18);
	        alcByVolWheel.setViewAdapter(alcByVolAdapter);
	        
	        drinkTypeWheel.addChangingListener(new OnWheelChangedListener() {
	            @Override
				public void onChanged(AbstractWheel wheel, int oldValue, int newValue) {
	                if (!drinkTypeWheelScrolling) {
	                    updateVolumeWheel(drinkVolumeWheel, Global.volumes, newValue);
	                	updateAlcByVolWheel(alcByVolWheel, drinkTypeWheel.getCurrentItem());
	                }
	            }
	        });
			
	        drinkTypeWheel.addScrollingListener( new OnWheelScrollListener() {
	            @Override
				public void onScrollingStarted(AbstractWheel wheel) {
	            	drinkTypeWheelScrolling = true;
	            }
	            @Override
				public void onScrollingFinished(AbstractWheel wheel) {
	            	drinkTypeWheelScrolling = false;
	            	updateVolumeWheel(drinkVolumeWheel, Global.volumes, drinkTypeWheel.getCurrentItem());
	            	updateAlcByVolWheel(alcByVolWheel, drinkTypeWheel.getCurrentItem());
	            }
	        });
	        
	        drinkVolumeWheel.addChangingListener(new OnWheelChangedListener() {
	            @Override
				public void onChanged(AbstractWheel wheel, int oldValue, int newValue) {
	                if (!drinkTypeWheelScrolling) {
	                	Global.activeVolumes[Global.activeDrinkType] = newValue;
	                }
	            }
	        });
	        
	        alcByVolWheel.addChangingListener(new OnWheelChangedListener() {
	        	@Override
				public void onChanged(AbstractWheel wheel, int oldValue, int newValue) {
	        		if (!drinkTypeWheelScrolling) {
	        			Global.activeAlcByVol[Global.activeDrinkType] = newValue;
	        		}
	        	}
	        });
	        
	        drinkTypeWheel.setCurrentItem(1);

			// set dialog message
			alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("OK",
				  new DialogInterface.OnClickListener() {
				    @Override
					public void onClick(DialogInterface dialog,int id) {
						Date now = new Date();
						int timeStepsSinceInit = (int) ((int) TimeUnit.MILLISECONDS.toMinutes(
								now.getTime() - Global.getPlanInitTime().getTime()) / Global.GRAPH_RES);						
						dataSet = drinkingPlan.addDrink(timeStepsSinceInit, Global.getSelectedDrink());
						onResume();
						}
				  })
				.setNegativeButton("Cancel",
				  new DialogInterface.OnClickListener() {
				    @Override
					public void onClick(DialogInterface dialog,int id) {
					dialog.cancel();
				    }
				  });

			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();

			// show it
			alertDialog.show();                

		}
	};
	
	/**
	 * Listener for Save Plan button
	 */
	protected OnClickListener savePlanOnClickListener = new OnClickListener()
	{
		@Override
		public void onClick(View v) {
			MySQLiteHelper dbHelper = new MySQLiteHelper(getActivity());
			SQLiteDatabase database = dbHelper.getWritableDatabase();
						
			database.execSQL("insert into history (time, buzz, nod, duration, type) values (" + "\"" + (new Date(System.currentTimeMillis()).toString()) + "\"" + ", " +
			Global.getBuzz() + ", " + Global.getNod() + ", " + Global.getDuration() + ", " + Global.getType() + ");");
			
			dbHelper.close();
			
			Log.d("savePLAN", "PLAN SAVED!");
			Log.d("savePLAN", "\"" + (new Date(System.currentTimeMillis()).toString()) + "\"");
			
			Toast.makeText(getActivity(), "Plan saved in history!", Toast.LENGTH_LONG).show();
		}
	};
	
	
	/**
	 * Listener for the emergency button
	 */
	protected OnClickListener EmergencyOnClickListener = new OnClickListener()
	{
		
		@Override
		public void onClick(View v) {
			 Intent myIntent = new Intent( v.getContext(), EmergencyButtonActivity.class);
		     startActivity(myIntent);
			Toast.makeText(getActivity(), "Emergency Messege sent!", Toast.LENGTH_LONG).show();
	    	
		}
	};
	
	/**
	 * 	A method that set's up the look of the graph
	 */
	private void setUpGraph()
	{
		// Customize bacSeries
		bacSeriesRenderer.setColor(Color.GREEN);
		bacSeriesRenderer.setLineWidth(2);
		
		// Customize drinkSeries
		drinkSeriesRenderer.setColor(Color.RED);
		drinkSeriesRenderer.setPointStyle(PointStyle.CIRCLE);
		drinkSeriesRenderer.setFillPoints(true);
		drinkSeriesRenderer.setLineWidth(0.0f);
		
		// Customize yellowLineSeries
		yellowSeriesRenderer.setColor(Color.YELLOW);
		yellowSeriesRenderer.setPointStrokeWidth(0);
		
		// Customize greenLineSeries
		greenSeriesRenderer.setColor(Color.GREEN);
		greenSeriesRenderer.setPointStrokeWidth(0);
		
		// Customize redLineSeries
		redSeriesRenderer.setColor(Color.RED);
		redSeriesRenderer.setPointStrokeWidth(0);
		
	    // set some properties on the main renderer
	    renderer.setApplyBackgroundColor(false);
	    renderer.setMarginsColor(Color.WHITE);
	    renderer.setAxisTitleTextSize(16);
	    renderer.setChartTitleTextSize(20);
	    renderer.setLabelsTextSize(15);
	    renderer.setLegendTextSize(15);
	    renderer.setMargins(new int[] { 5, 20, 15, 5 });
	    renderer.setZoomButtonsVisible(false);
	    renderer.setPointSize(5);
	    renderer.setXTitle("Time");
	    renderer.setYTitle("BAC");
	    renderer.setYLabelsPadding(-20.0f);	    
	    
	    renderer.addSeriesRenderer(bacSeriesRenderer);
	    renderer.addSeriesRenderer(drinkSeriesRenderer);	
	    renderer.addSeriesRenderer(yellowSeriesRenderer);
	    renderer.addSeriesRenderer(greenSeriesRenderer);
	    renderer.addSeriesRenderer(redSeriesRenderer);
	    
	    double yellowLine = new Level(1).lowerBAC;
	    double greenLine = new Level(2).lowerBAC;
	    double redLine = new Level(3).lowerBAC;
	    double endPoint = Global.getDuration() / Global.MIN_PER_HOUR;
	    
	    yellowLineSeries.add(0, yellowLine);
	    greenLineSeries.add(0, greenLine);
	    redLineSeries.add(0, redLine);
	    yellowLineSeries.add(endPoint, yellowLine);
	    greenLineSeries.add(endPoint, greenLine);
	    redLineSeries.add(endPoint, redLine);
	}

	/**
	 * 	A method that creates the initial dataset to graph
	 */
	private void createInitDataSet() {
		
		switch ( Global.getType() ) {
		case 2: // Buzz
			drinkingPlan = new LevelPlan(Global.getDuration(), new Level(Global.getBuzz()),
					Global.getSelectedDrink(), getActivity());
			break;
		default:
			drinkingPlan = new LinearPlan(Global.getDuration(), Global.getNod(), 
					Global.getSelectedDrink(), getActivity());
		}
		
	    dataSet = drinkingPlan.getDataSet();
	    
	    dataSet.addSeries(yellowLineSeries);
	    dataSet.addSeries(greenLineSeries);
	    dataSet.addSeries(redLineSeries);
	    
	    Global.newPlanCreated();
	}
    /**
     * Updates the drinkVolumeWheel spinnerwheel
     */
    private void updateVolumeWheel(AbstractWheel drinkVolumeWheel, String volumes[][], int index) {
    	boolean animated = true;
    	Global.activeDrinkType = index;
        ArrayWheelAdapter<String> adapter =
            new ArrayWheelAdapter<String>(this.getActivity(), volumes[index]);
        adapter.setTextSize(18);
        drinkVolumeWheel.setViewAdapter(adapter);
        drinkVolumeWheel.setCurrentItem(Global.activeVolumes[index], animated);
    }
    /**
     * Updates the alcByVolWheel spinnerWheel
     * @param alcByVolWheel
     * @param drinkTypeWheelIndex
     */
    private void updateAlcByVolWheel(AbstractWheel alcByVolWheel, int drinkTypeWheelIndex) {
    	boolean animated = true;
    	alcByVolWheel.setCurrentItem(Global.activeAlcByVol[drinkTypeWheelIndex], animated);
    }
	
}
