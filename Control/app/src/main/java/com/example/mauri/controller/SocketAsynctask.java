package com.example.mauri.controller;

import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
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
            out.println("sobon2");
            out.println("soboner2");
            out.println("soberron2");

            readyFlag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        while (readyFlag){
            try{
                String input = in.readLine();
                if(input == null){
                    continue;
                }else{
                    continue;
                    //devolver la vara
                }
            }catch (Exception e){
                readyFlag = false;
                e.printStackTrace();
            }
        }

        return null;
    }

    public void changeOrientation(String orientation){
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            out.println(orientation);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
