package com.example.mauri.controller;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ControllerActivy extends AppCompatActivity implements SensorEventListener {

    int controllerMode = 0;

    /**
     * Constants for sensors
     */
    private static final float SHAKE_THRESHOLD = 1.1f;
    private static final int SHAKE_WAIT_TIME_MS = 250;
    private static final float ROTATION_THRESHOLD = 2.0f;
    private static final int ROTATION_WAIT_TIME_MS = 100;

    /**
     * Sensors
     */
    private SensorManager mSensorManager;
    private Sensor mSensorAcc;
    private Sensor mSensorGyr;
    private long mShakeTime = 0;
    private long mRotationTime = 0;

    RelativeLayout layout_joystick;
    JoyStickClass js;
    TextView textView1, textView2, textView3, textView4, textView5;

    private SocketAsynctask client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        client = new SocketAsynctask();
        client.port = 9898;
        client.serverAddress = "192.168.0.18";
        client.execute();


        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            this.controllerMode = bundle.getInt("controllerMode");
        }

        if(this.controllerMode == 1) {

            setContentView(R.layout.activity_joy_stick_activity);


            textView1 = (TextView)findViewById(R.id.textView1);
            textView2 = (TextView)findViewById(R.id.textView2);
            textView3 = (TextView)findViewById(R.id.textView3);
            textView4 = (TextView)findViewById(R.id.textView4);
            textView5 = (TextView)findViewById(R.id.textView5);


            layout_joystick = (RelativeLayout)findViewById(R.id.layout_joystick);

            js = new JoyStickClass(getApplicationContext()
                    , layout_joystick, R.drawable.fondo);


            js.setStickSize(200, 200);
            js.setLayoutSize(800, 800);
            js.setLayoutAlpha(150);
            js.setStickAlpha(100);
            js.setOffset(90);
            js.setMinimumDistance(50);

            layout_joystick.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View arg0, MotionEvent arg1) {
                    js.drawStick(arg1);
                    if (arg1.getAction() == MotionEvent.ACTION_DOWN
                            || arg1.getAction() == MotionEvent.ACTION_MOVE) {
                        textView1.setText("X : " + String.valueOf(js.getX()));
                        textView2.setText("Y : " + String.valueOf(js.getY()));
                        textView3.setText("Angle : " + String.valueOf(js.getAngle()));
                        textView4.setText("Distance : " + String.valueOf(js.getDistance()));

                        int direction = js.get8Direction();
                        if (direction == JoyStickClass.STICK_UP) {
                            textView5.setText("Direction : Up");
                            client.changeOrientation("UP");
                        } else if (direction == JoyStickClass.STICK_UPRIGHT) {
                            textView5.setText("Direction : Up Right");
                        } else if (direction == JoyStickClass.STICK_RIGHT) {
                            textView5.setText("Direction : Right");
                            client.changeOrientation("RIGHT");
                        } else if (direction == JoyStickClass.STICK_DOWNRIGHT) {
                            textView5.setText("Direction : Down Right");
                        } else if (direction == JoyStickClass.STICK_DOWN) {
                            textView5.setText("Direction : Down");
                            client.changeOrientation("DOWN");
                        } else if (direction == JoyStickClass.STICK_DOWNLEFT) {
                            textView5.setText("Direction : Down Left");
                        } else if (direction == JoyStickClass.STICK_LEFT) {
                            textView5.setText("Direction : Left");
                            client.changeOrientation("LEFT");
                        } else if (direction == JoyStickClass.STICK_UPLEFT) {
                            textView5.setText("Direction : Up Left");
                        } else if (direction == JoyStickClass.STICK_NONE) {
                            textView5.setText("Direction : Center");
                            client.changeOrientation("CENTER");
                        }
                    } else if (arg1.getAction() == MotionEvent.ACTION_UP) {

                        textView1.setText("X :");
                        textView2.setText("Y :");
                        textView3.setText("Angle :");
                        textView4.setText("Distance :");
                        textView5.setText("Direction :");
                    }
                    return true;
                }
            });
        }else if(this.controllerMode == 0){
            setContentView(R.layout.activity_gyroscope_);

            textView1 = (TextView)findViewById(R.id.gyro_x);
            textView2 = (TextView)findViewById(R.id.gyro_y);
            textView3 = (TextView)findViewById(R.id.gyro_z);

            // Get the sensors to use
            mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            mSensorAcc = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            mSensorGyr = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        }



    }
    @Override
    protected void onResume() {
        super.onResume();
        if(this.controllerMode == 0) {
            mSensorManager.registerListener(this, mSensorAcc, SensorManager.SENSOR_DELAY_NORMAL);
            mSensorManager.registerListener(this, mSensorGyr, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(this.controllerMode == 0) {
            mSensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(this.controllerMode == 0) {

            if (event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE) {

                if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                    textView1.setText(R.string.act_main_no_acuracy);
                    textView2.setText(R.string.act_main_no_acuracy);
                    textView3.setText(R.string.act_main_no_acuracy);

                }
                return;
            }

            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                textView1.setText("x = " + Float.toString(event.values[0]));
                textView2.setText("y = " + Float.toString(event.values[1]));
                textView3.setText("z = " + Float.toString(event.values[2]));
                detectShake(event);
            } else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
                detectRotation(event);
            }
        }

    }

    // References:
    //  - http://jasonmcreynolds.com/?p=388
    //  - http://code.tutsplus.com/tutorials/using-the-accelerometer-on-android--mobile-22125

    /**
     * Detect a shake based on the ACCELEROMETER sensor
     *
     * @param event
     */
    private void detectShake(SensorEvent event) {
        if(this.controllerMode == 0) {
            long now = System.currentTimeMillis();

            if ((now - mShakeTime) > SHAKE_WAIT_TIME_MS) {
                mShakeTime = now;

                float gX = event.values[0] / SensorManager.GRAVITY_EARTH;
                float gY = event.values[1] / SensorManager.GRAVITY_EARTH;
                float gZ = event.values[2] / SensorManager.GRAVITY_EARTH;

                // gForce will be close to 1 when there is no movement
                double gForce = Math.sqrt(gX * gX + gY * gY + gZ * gZ);

                // Change background color if gForce exceeds threshold;
                // otherwise, reset the color
                //if (gForce > SHAKE_THRESHOLD) {
                //soundAcc.start();
            }
        }
    }

    /**
     * Detect a rotation in on the GYROSCOPE sensor
     *
     * @param event
     */
    private void detectRotation(SensorEvent event) {
        if(this.controllerMode == 0) {
            long now = System.currentTimeMillis();

            if ((now - mRotationTime) > ROTATION_WAIT_TIME_MS) {
                mRotationTime = now;

                // Change background color if rate of rotation around any
                // axis and in any direction exceeds threshold;
                // otherwise, reset the color
                if (Math.abs(event.values[0]) > ROTATION_THRESHOLD ||
                        Math.abs(event.values[1]) > ROTATION_THRESHOLD ||
                        Math.abs(event.values[2]) > ROTATION_THRESHOLD) {
                    // soundGyro.start();
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}
