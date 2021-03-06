package com.example.mauri.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by mauri on 3/27/2017.
 */

public class SocketAsynctask extends AsyncTask<Void,Integer,Void> {

    public static int port;
    public static String serverAddress;
    private boolean readyFlag = true;
    private BufferedReader in;
    private static PrintWriter out;


    @Override
    protected Void doInBackground(Void... voids) {
        try {

            Socket socket = new Socket(serverAddress, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);


            readyFlag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        while (readyFlag) {
            try {
                String input = in.readLine();
                if (input != null) {
                    String_Brain(input);
                    Log.d("MESSAGE",input);
                } else {
                    continue;
                }
            } catch (Exception e) {
                readyFlag = false;
                e.printStackTrace();
            }
        }

        return null;
    }

    public String changeOrientation(String orientation) {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Log.d("output",orientation);
            out.println(orientation);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orientation;
    }

    void String_Brain(String Data){
        if (Data.equals("ADD_Power0")){
            Shared_Data.getInstance().power1_quantity +=1;
            Log.d("power1",String.valueOf(Shared_Data.getInstance().power1_quantity));
        }
        if (Data.equals("ADD_Power1")){
            Shared_Data.getInstance().power2_quantity +=1;
        }
        if (Data.equals("ADD_Power2")){
            Shared_Data.getInstance().power3_quantity +=1;
        }
    }

}

