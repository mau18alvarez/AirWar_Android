package com.example.mauri.controller;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import static java.lang.Math.abs;

public class ControllerActivy extends AppCompatActivity implements SensorEventListener {

    int controllerMode = 0;
    Button button_shoot;
    private SensorManager mSensorManager;
    private Sensor mSensorAcc;
    RelativeLayout layout_joystick;
    JoyStickClass js;

    private SocketAsynctask client;

    /**
     * Funcion para esconder los botones virtuales del movil
     */
    private void hideVirtualButtons() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            // In KITKAT (4.4) and next releases, hide the virtual buttons
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                hideVirtualButtons();
            }
        }
    }

    /**
     * On CREATE....Activity
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        quitar_botones();

        client = new SocketAsynctask();
        client.port = 9898;
        client.serverAddress = "192.168.1.10";
        client.execute();


        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            this.controllerMode = bundle.getInt("controllerMode");
        }
        /**
         * ACTIVITY DEL JOYSTICK
         */
        if(this.controllerMode == 1) {

            setContentView(R.layout.activity_joy_stick_activity);
            button_shoot = (Button) findViewById(R.id.button_shoot2);

            //BOTON PARA DISPARAR
            button_shoot.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    client.changeOrientation("SHOOT");
                }
            });

            layout_joystick = (RelativeLayout)findViewById(R.id.layout_joystick);

            js = new JoyStickClass(getApplicationContext()
                    , layout_joystick, R.drawable.llitlecircle);

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

                        int direction = js.get8Direction();
                        if (direction == JoyStickClass.STICK_UP) {
                            client.changeOrientation("UP");
                        } else if (direction == JoyStickClass.STICK_UPRIGHT) {
                            client.changeOrientation("UP");
                            client.changeOrientation("RIHT");
                        } else if (direction == JoyStickClass.STICK_RIGHT) {
                            client.changeOrientation("RIGHT");
                        } else if (direction == JoyStickClass.STICK_DOWNRIGHT) {
                            client.changeOrientation("DOWN");
                            client.changeOrientation("RIHT");
                        } else if (direction == JoyStickClass.STICK_DOWN) {
                            client.changeOrientation("DOWN");
                        } else if (direction == JoyStickClass.STICK_DOWNLEFT) {
                            client.changeOrientation("DOWN");
                            client.changeOrientation("LEFT");
                        } else if (direction == JoyStickClass.STICK_LEFT) {
                            client.changeOrientation("LEFT");
                        } else if (direction == JoyStickClass.STICK_UPLEFT) {
                            client.changeOrientation("UP");
                            client.changeOrientation("LEFT");
                        } else if (direction == JoyStickClass.STICK_NONE) {
                            client.changeOrientation("CENTER");
                        }
                    } else if (arg1.getAction() == MotionEvent.ACTION_UP) {

                    }
                    return true;
                }
            });
            /**
             * ACTIVITY DEL GIROSCOPIO
             */

        }else if(this.controllerMode == 0) {
            setContentView(R.layout.activity_gyroscope_);
            button_shoot = (Button) findViewById(R.id.button_shoot);

            //BOTON PARA DISPARAR
            button_shoot.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    client.changeOrientation("SHOOT");
                }
            });


            // Get the sensors to use
            mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            mSensorAcc = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }


    }
    @Override
    protected void onResume() {
        super.onResume();
        if(this.controllerMode == 0) {
            mSensorManager.registerListener(this, mSensorAcc, SensorManager.SENSOR_DELAY_NORMAL);
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
        if (this.controllerMode == 0) {

            if (event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE) {

                if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                    Log.d("ACELEROMETRO", "SIN SUFICIENTE EXACTITUD: ");

                }
                return;
            }

            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                //client.changeOrientation("x = " + Float.toString(event.values[0]));
                //client.changeOrientation("y = " + Float.toString(event.values[1]));
                //client.changeOrientation("z = " + Float.toString(event.values[2]));
                float currentX = event.values[0];
                float currentY = event.values[1];

                    if (currentY > 0.5) {
                        client.changeOrientation("RIHT");
                    }
                    if (currentY < -0.5) {
                        client.changeOrientation("LEFT");
                    }
                    if (currentX < -1) {
                        client.changeOrientation("UP");
                    }
                    if (currentX > 6) {
                        client.changeOrientation("DOWN");
                }
            }
        }
    }




    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    void quitar_botones(){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
    }

}
