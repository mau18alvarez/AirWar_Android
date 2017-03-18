package com.example.mauri.controller;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import static com.example.mauri.controller.R.id.text;


public class Main_Menu extends AppCompatActivity {

    Button play;
    TextView text;

    //FUNCION PARA ESCONDER LOS BOTONES VIRTUALES DEL CELULAR
    private void hideVirtualButtons() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    //FUNCION PARA ESCONDER LOS BOTONES SIEMPRE INCLUSO SI SE SALE Y SE VUELVE A ENTRAR
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        //PARA QUITAR LOS BOTONES
        if (hasFocus) {
            // In KITKAT (4.4) and next releases, hide the virtual buttons
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                hideVirtualButtons();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        quitar_botones();
        setContentView(R.layout.activity_main__menu);

        play = (Button) findViewById(R.id.button_play);
        text = (TextView) findViewById(R.id.airwar_text);

         play.setOnClickListener(new View.OnClickListener(){

             public void onClick(View view) {
                 play.setVisibility (View.GONE); //Desaparecer el boton Play
                 text.setVisibility (View.GONE); //Desaparecer el texto

                 switch (view.getId()) {
                     case (R.id.button_play):

                        //PARA LOS BOTONES
                         RelativeLayout btn_layout = (RelativeLayout)findViewById(R.id.play_layout);
                         RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                         RelativeLayout.LayoutParams params_txt = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                         params.width = 800;
                         params.height = 800;
                         params_txt.width = 1600;
                         params_txt.height = 800;

                         TextView Butn_text = new TextView(getApplicationContext());
                         Butn_text.setText("Choose your favorite control type");
                         Butn_text.setTextSize(40);
                         Butn_text.setX(150);
                         Butn_text.setY(150);
                         btn_layout.addView(Butn_text,params_txt);

                         Button Joystick = new Button(getApplicationContext());
                         Joystick.setBackgroundResource(R.drawable.gyro);
                         Joystick.setX(400);
                         Joystick.setY(650);
                         btn_layout.addView(Joystick, params);

                         Joystick.setOnClickListener(new View.OnClickListener(){
                             public void onClick(View view){

                             }
                         });


                         Button Gyroscope = new Button(getApplicationContext());
                         Gyroscope.setBackgroundResource(R.drawable.acceler);
                         Joystick.setX(1600);
                         Gyroscope.setY(650);
                         btn_layout.addView(Gyroscope,params);

                         Gyroscope.setOnClickListener(new View.OnClickListener(){
                             public void onClick(View view){

                             }
                         });

                         break;
                 }
             }
         });

    }

    void quitar_botones(){
        //QUITAR TITULO SUPERIOR DE PANTALLA
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
    }

}

