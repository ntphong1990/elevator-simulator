package models;




public class Passenger {

	protected long beginTime;
	protected Elevator elevator;
	protected int currentFloor;
	protected int wantedFloor;
	int personCount = 1; //assume that only has 1 person, this is not rea

	public Passenger(int current_floor, int  wanted_floor) {
		currentFloor = current_floor;
		wantedFloor = wanted_floor;

	}



	public Elevator getElevator() {
		return elevator;
	}

	public void setElevator(Elevator elevator) {
		this.elevator = elevator;
	}


	public boolean isArrived() {
		return (wantedFloor == currentFloor) && !isInTheElevator();
	}

	public boolean isInTheElevator() {
		return elevator != null;
	}

	public int getCurrentFloor() {
		return currentFloor;
	}
	public void setCurrentFloor(int floor) {
		currentFloor = floor;
	}

	public long getTime() { 
		return 1000;
	}

	public int getWantedFloor() {
		return wantedFloor;
	}







}