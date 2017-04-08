package com.example.mauri.controller;

import android.util.Log;

/**
 * Created by mauri on 4/6/2017.
 */

public class Shared_Data {

    static Shared_Data instance = null;
    public int power1_quantity = 0;
    public int power2_quantity = 0;
    public int power3_quantity = 0;
    public boolean endgame = false;


    private void Shared_Data (){
    }

    static Shared_Data getInstance(){
        if (instance == null){
            instance = new Shared_Data();
        }
        return instance;
    }

}
