/*
*  @Author: Gabriel Kiprono
*
*
*
* */



package com.kiprono.models;

import java.util.Random;

public class Vehicle extends Thread{
    private static int count = 1;
    private int number; // car id
    private boolean eastBound; // true? heads east, else westbound
    private boolean done; // finished crossing
    private Bridge bridge;
    private Thread mainThread; // instance of thread,


    // constructor
    public Vehicle( boolean eastBound, Thread thread, Bridge bridge) {
        this.mainThread = thread;
        this.eastBound = eastBound;
        this.bridge = bridge;
        this.number = count; // id will be count,
        count++; // increment count

        this.done = false;
    }

    @Override
    public void run() {
        // randomly assign the direction
        Random random = new Random();

        try{
            // temporary wait
            Thread.sleep(6000);

            // try to cross the bridge
            this.bridge.enter(this);

            // add this to parent queue
            if(eastBound){
                ((EastBound)this.mainThread).signalNext();
            }else {
                ((WestBound)this.mainThread).signalNext();
            }
        }catch (InterruptedException ex){
            ex.printStackTrace();
        }
    }

    public int getNumber() {
        return number;
    }

    public boolean isEastBound() {
        return eastBound;
    }

    public boolean isDone() {
        return done;
    }

    // vehicle heading info
    @Override
    public String toString() {
        String info = "";
        if(this.eastBound) info += "Eastbound ";
        else info += "Westbound ";

        return info += "vehicle: " + this.number;
    }
}
