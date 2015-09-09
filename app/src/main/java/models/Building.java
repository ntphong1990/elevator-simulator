package models;

import android.content.Context;

import java.util.ArrayList;
import java.util.LinkedList;

public class Building {


	private ArrayList<Elevator> elevators = null;

	private LinkedList<Passenger> passengers = null;


	private int floorCount;

	public Building(int floor_count,int elevator_count, Context context) {
        ArrayList<Elevator> es = new ArrayList<Elevator>();
        for(int i = 0; i < elevator_count;i++){
            es.add(new Elevator(context,floor_count));
        }
		constructor(floor_count,es , new LinkedList<Passenger>());
	}

	private void constructor(int floor_count, ArrayList<Elevator> elevators_list, LinkedList<Passenger> passengers_list) {
		this.floorCount = floor_count;
		this.elevators = elevators_list;
		this.passengers = passengers_list;
	}

    public void addPassenger(Passenger p){
        passengers.add(p);
    }

	public Passenger getLongestWatingRequest(){
		if(passengers.size() > 0){
			Passenger result = passengers.getLast();
			return result;
		} else {
			return null;
		}
	}

    public void requestAssigned(Passenger p){
        passengers.remove(p);
    }

	public Elevator getAvailbleElevator(){
		if(elevators.size() > 0){
			for (int i = 0; i < elevators.size();i++){
				if(!elevators.get(i).isBusy){
                    elevators.get(i).isBusy = true;
                    elevators.get(i).setIndex(i);
					return elevators.get(i);
				}
			}
			return null;
		} else {
			return null;
		}
	}
    public void freeElevator(int index){
        elevators.get(index).isBusy = false;
    }












}