package models;

import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.phong.elevatorsimulator.MainActivity;

import java.util.ArrayList;
import java.util.LinkedList;

public class Elevator {

	private static final int TO_TOP = 1;
	private static final int TO_BOTTOM = -1;
	public boolean isBusy = false;
    TextView[][] mTextviewArray; // only use for GUI
    int elevatorIndex;
	private int identifier;
	public int getIdentifier() {
		return identifier;
	}
	public void setIdentifier(int identifier) {
		this.identifier = identifier;
	}

	private int currentFloor;
    int mTopFloor;
	private LinkedList<Passenger> passengers;
    Context mContext;

	public Elevator(Context context,int top_floor) {
        mContext = context;
		this.currentFloor = 0;
		mTopFloor = top_floor;
		this.passengers = new LinkedList<Passenger>();



	}

    public void setGUI(TextView[][] textviews){
        mTextviewArray = textviews;
    }

    public void setIndex(int input){
        elevatorIndex = input;
    }

    public void takeRequest(final Passenger passenger){
        passengers.add(passenger);
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                ((MainActivity)mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        goToFloor(passengers.getLast().getCurrentFloor(), true);
                    }
                });

            }
        });
        th.run();

    }
	public boolean takePassenger(Passenger passenger) {
			passengers.add(passenger);
			passenger.setElevator(this);
			return true;
	}

    public void goToFloor(final int floorIndex, final boolean isMore){

            mTextviewArray[currentFloor][elevatorIndex].setBackgroundColor(Color.RED);
            CountDownTimer timer = new CountDownTimer((Math.max(floorIndex - currentFloor,currentFloor-floorIndex ) +2) * 1000,1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    if(!isMore) {
                        setTextviewStatusBusy(mTextviewArray[currentFloor][elevatorIndex]);

                    } else {
                        setTextviewStatusFree(mTextviewArray[currentFloor][elevatorIndex]);
                    }


                        if(currentFloor > 0) {
                            hiddenTextview(mTextviewArray[currentFloor - 1][elevatorIndex]);
                        }

                        if(currentFloor < mTopFloor -1) {
                            hiddenTextview(mTextviewArray[currentFloor + 1][elevatorIndex]);
                        }

                    if(currentFloor != floorIndex) {
                        if(currentFloor <= floorIndex) {
                            currentFloor++;
                        } else {
                            currentFloor--;
                        }
                    }
                }

                @Override
                public void onFinish() {
                    if(currentFloor >0) {
                        hiddenTextview(mTextviewArray[currentFloor - 1][elevatorIndex]);
                    }
                    setTextviewStatusBusy(mTextviewArray[currentFloor][elevatorIndex]);
                    if(isMore) {
                        goToFloor(passengers.getLast().getWantedFloor(), false);
                    } else {
                        isBusy = false;
                        passengers.remove();
                        setTextviewStatusFree(mTextviewArray[currentFloor][elevatorIndex]);
                    }
                }
            };
            timer.start();

    }
    public void setTextviewStatusFree(TextView input){
        input.setBackgroundColor(Color.YELLOW);
        input.setTextColor(Color.BLACK);
        if(passengers.size() == 0) {
            input.setText("| E" + elevatorIndex + "(free) |");
        }else {
            input.setText("| E" + elevatorIndex + "(busy)|");
        }
    }
    public void setTextviewStatusBusy(TextView input){
        input.setBackgroundColor(Color.RED);
        input.setTextColor(Color.BLACK);
        input.setText("| E" + elevatorIndex + "(busy) |");
    }
    public void hiddenTextview(TextView input){
        input.setBackgroundColor(Color.TRANSPARENT);
        input.setTextColor(Color.TRANSPARENT);
        input.setText("| E" + elevatorIndex + "(free) |");
    }


	public void releasePassenger(Passenger passenger) {
		if (passengers.contains(passenger)) {
			passengers.remove(passenger);
			passenger.setElevator(null);

		}
	}
	


}