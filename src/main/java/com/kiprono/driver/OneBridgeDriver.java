package com.kiprono.driver;

import com.kiprono.models.Bridge;
import com.kiprono.models.EastBound;
import com.kiprono.models.WestBound;

public class OneBridgeDriver {
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
