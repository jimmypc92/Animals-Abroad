package com.animalsabroad.animalsabroad;

/**
 * Created by Jimmy on 5/16/2015.
 */
public class AccentDictionary {

    public static String[] accents = {
        "Egyptian",
        "Spanish"
    };

    public static String getRandomAccent(){
        return accents[ (int)(Math.random() * accents.length) ];
    }
}
