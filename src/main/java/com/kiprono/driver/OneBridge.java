/*
*   @Author: Gabriel Kiprono
*  Simulate one bridge problem
*
* */

package com.kiprono.driver;


import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class OneBridge {

    public static void main(String[] args) {
        Bridge bridge = new Bridge();

        EastBound eastBound = new EastBound(bridge);
        WestBound westBound = new WestBound(bridge);

        eastBound.start();
        westBound.start();


        try {
            eastBound.join();
            westBound.join();
        } catch (InterruptedException ex){
            ex.printStackTrace();
        }
    }

}

// westbound thread traffic
class WestBound extends Thread{
    private Bridge bridge;
    private Queue<Vehicle> queue = new LinkedList<>();

    public WestBound(Bridge bridge) {
        this.bridge = bridge;
    }

    // alert the vehicles to cross
    public void signalNext(){
        synchronized (this){
            if(queue.isEmpty()){
                return;
            }


            Vehicle vehicle = queue.poll();
            vehicle.start();
        }
    }

    // westbound traffic
    @Override
    public void run() {

        try{
            while (true){
                // wait for sometime before making new traffic
                Thread.sleep(6000);

                Vehicle vehicle = new Vehicle(false, this, bridge);
                System.out.println(vehicle.toString() + " is WAITING to cross the bridge.");

                // if queue is empty, go ahead and cross, else wait
                synchronized (this){
                    if (queue.isEmpty()){
                        vehicle.start();
                    }else{
                        queue.offer(vehicle);
                    }
                }
            }
        }catch (InterruptedException ex){
            ex.printStackTrace();
        }
    }
}

// eastbound traffic threads class
class EastBound extends Thread{
    private Bridge bridge;
    private Queue<Vehicle> queue = new LinkedList<>();

    public EastBound(Bridge bridge) {
        this.bridge = bridge;
    }

    // alert the vehicles to cross
    public void signalNext(){
        synchronized (this){
            if(queue.isEmpty()){
                return;
            }

            Vehicle vehicle = queue.poll();
            vehicle.start();
        }
    }

    // Eastbound traffic
    @Override
    public void run() {

        try{
            while (true){
                // wait for sometime before making new traffic
                Thread.sleep(6000);

                Vehicle vehicle = new Vehicle(true, this, bridge);
                System.out.println(vehicle.toString() + " is WAITING to cross the bridge.");

                // if queue is empty, go ahead and cross, else wait
                synchronized (this){
                    if (queue.isEmpty()){
                        vehicle.start();
                    }else{
                        queue.offer(vehicle);
                    }
                }
            }
        }catch (InterruptedException ex){
            ex.printStackTrace();
        }
    }
}

/*
* Bridge simulator
*
* */

class Bridge {
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

/*
*  Vehicle thread class to simulate vehicles
*
* */
class Vehicle extends Thread{
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

