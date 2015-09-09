package factories;

import android.content.Context;
import android.widget.TextView;

import com.phong.elevatorsimulator.MainActivity;

import java.util.Timer;
import java.util.TimerTask;

import models.*;

public class SimulatorFactory {

	TextView[][] mTextviewArray;
    Building mBuilding;
    Context mContext;
    int mNumberRequests = 0;
    int mNumberHandling = 0;
    int mNumberWaiting = 0;

    public SimulatorFactory(Context context){
        mContext = context;
    }
	public void getBuilding(int floor_count, int elevator_count) {
        mBuilding = new Building(floor_count,elevator_count,mContext);

	}

	public void setTextViewArray(TextView[][] building){
		mTextviewArray = building;
	}

    public void Run(){
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                mainFunction();
            }
        }, 1000, 1000);

    }
    public void addNewRequest(Passenger p){
        mNumberRequests++;
        mNumberWaiting++;
        mBuilding.addPassenger(p);
    }



    public void mainFunction(){
        ((MainActivity) mContext).updateStatus(mNumberRequests,mNumberHandling,mNumberWaiting);
        Passenger p = mBuilding.getLongestWatingRequest();
        if(p != null) {
            Elevator e = mBuilding.getAvailbleElevator();
            if (e != null) {
                e.setGUI(mTextviewArray);
                e.takeRequest(p);
                mBuilding.requestAssigned(p);
                mNumberWaiting--;
                mNumberHandling++;
            }
        }
    }

}