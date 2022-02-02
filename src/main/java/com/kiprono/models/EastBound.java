/*
 *  @Author: Gabriel Kiprono
 *
 *
 *
 * */

package com.kiprono.models;

import java.util.LinkedList;
import java.util.Queue;

public class EastBound extends Thread{
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
