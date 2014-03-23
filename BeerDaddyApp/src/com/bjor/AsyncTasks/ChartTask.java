package com.bjor.AsyncTasks;

import org.achartengine.GraphicalView;

import android.app.Activity;
import android.os.AsyncTask;

public class ChartTask extends AsyncTask<Void, String, Void>{
	 
	
	public ChartTask(Activity activity, GraphicalView chart) {
		
	}
	
	
    // Generates dummy data in a non-ui thread
    @Override
    protected Void doInBackground(Void... params) {
        int i = 0;
        try{
            do{
                String [] values = new String[2];
                //Random r = new Random();
                //int visits = r.nextInt(10);

                values[0] = Integer.toString(i);
                //values[1] = Integer.toString(visits);

                publishProgress(values);
                Thread.sleep(1000);
                i++;
            }while(i<=10);
                }catch(Exception e){ }
            return null;
        }

        // Plotting generated data in the graph
        @Override
        protected void onProgressUpdate(String... values) {
            //visitsSeries.add(Integer.parseInt(values[0]), Integer.parseInt(values[1]));
            //mChart.repaint();
        }
    }
