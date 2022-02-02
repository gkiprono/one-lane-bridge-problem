/*
 *  @Author: Gabriel Kiprono
 *  Bridge simulator: one vehicle at a time
 *
 *
 * */


package com.kiprono.models;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class Bridge {

    private Semaphore semaphore;
    private int numEastBoundCrossed;
    private int numWestBoundCrossed;
    private Random random;

    public Bridge() {
        this.semaphore = new Semaphore(1, true);
        this.random = new Random();
        this.numEastBoundCrossed = 0;
        this.numWestBoundCrossed = 0;
    }

    // one vehicle enters the bridge
    public void enter(Vehicle vehicle){
        try{
            semaphore.acquire();

            // vehicles take 5 seconds to cross
            System.out.println(vehicle.toString() + " is now Crossing the bridge");
            Thread.sleep(6000);
            System.out.println(vehicle.toString() + " is Done crossing the bridge");

            // increment counters
            if(vehicle.isEastBound()) numEastBoundCrossed++;
            else numWestBoundCrossed++;

            // release the lock
            semaphore.release();
        }catch (InterruptedException ex){
            ex.printStackTrace();
        }
    }

}
