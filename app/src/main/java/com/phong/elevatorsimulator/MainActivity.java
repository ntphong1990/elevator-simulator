package com.phong.elevatorsimulator;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import factories.SimulatorFactory;
import models.Elevator;
import models.Passenger;


public class MainActivity extends ActionBarActivity {

    TextView[][] building;
    SimulatorFactory mSimulatorFactory;
    int mMaxFloor = 0;
    TextView mTextViewStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn1 = (Button) findViewById(R.id.btn_create_simulator);
        final EditText editTextFloor = (EditText) findViewById(R.id.input_floor);
        final EditText editTextElevator = (EditText) findViewById(R.id.input_elevetor);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String floor = editTextFloor.getText().toString();
                String elevator = editTextElevator.getText().toString();
                if (!floor.equalsIgnoreCase("") &&
                        !elevator.equalsIgnoreCase("")) {
                    int floorNumber = Integer.parseInt(floor);
                    int elevatorNumber = Integer.parseInt(elevator);

                    createSimulator(floorNumber, elevatorNumber);
                }
            }
        });
    }
// setup for GUI  and simulator
    public void createSimulator(int floor, int elevator){
        mMaxFloor = floor;
        mSimulatorFactory = new SimulatorFactory(this);
        mSimulatorFactory.getBuilding(floor,elevator);

        building = new TextView[floor][elevator];
        ScrollView sv = new ScrollView(this);

        TableLayout ll=new TableLayout(this);

        HorizontalScrollView hsv = new HorizontalScrollView(this);

        for(int i=0;i<floor;i++) {
            TableRow tbrow=new TableRow(this);
            tbrow.setBackgroundColor(Color.GRAY);
            final TextView tv=new TextView(this);
            tv.setText("Floor " + i + " ");
            tv.setTag(i);
            tv.setTextSize(20);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Input the target floor");

                    final EditText input = new EditText(MainActivity.this);

                    input.setInputType(InputType.TYPE_CLASS_NUMBER);
                    builder.setView(input);


                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            String text = input.getText().toString();
                            int target = -1;
                            try {
                                target = Integer.parseInt(text);
                            } catch (Exception e) {
                                Toast toast = Toast.makeText(MainActivity.this, "Invalid number ", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                            if (target != -1 && target < mMaxFloor) {
                                Passenger p = new Passenger((int) tv.getTag(), target);
                                mSimulatorFactory.addNewRequest(p);
                            } else {
                                Toast toast = Toast.makeText(MainActivity.this, "Invalid number of floor", Toast.LENGTH_SHORT);
                                toast.show();
                            }

                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();


                }
            });
            tbrow.addView(tv);
            for(int j=0;j<elevator;j++) {
                TextView tv1=new TextView(this);
                tv1.setTextSize(20);
                String s1 = Integer.toString(i);
                String s2 = Integer.toString(j);
                String s3 = s1+s2;
                int id = Integer.parseInt(s3);
                tv1.setId(id);
                tv1.setTextColor(Color.TRANSPARENT);
                if(i == 0) {
                    tv1.setBackgroundColor(Color.YELLOW);
                    tv1.setTextColor(Color.BLACK);
                }

                tv1.setText("| E"+j + "(free) |");
                building[i][j] = tv1;
                tbrow.addView(tv1);
            }
            ll.addView(tbrow);
        }

        mTextViewStatus = new TextView(this);
        mTextViewStatus.setText("Total requests : 0 , Taking/Finished : 0 , Remaining : 0");
        ll.addView(mTextViewStatus);


        TextView txtGuildline = new TextView(this);
        txtGuildline.setText("Please click at Floor to request a evelator.");
        ll.addView(txtGuildline);

        Button btmReset = new Button(this);
        btmReset.setText("Reset");
        btmReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
                MainActivity.this.finish();
            }
        });
        ll.addView(btmReset);
        hsv.addView(ll);
        sv.addView(hsv);

        setContentView(sv);
        mSimulatorFactory.setTextViewArray(building);
        mSimulatorFactory.Run();
    }

    public void updateStatus(final int total, final int handling, final int waiting){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTextViewStatus.setText("Total requests : "+total+" ,  Taking/Finished : "+handling+" , Remaining : "+waiting);
            }
        });

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
