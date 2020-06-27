

/*
Before making changes to FILE, READ THIS :
* ALL THE CONTENTS IN THIS FILE ARE INITIALIZER OF VARIABLES USED IN THE APPLICATION
* i.e CONFIGURATION OF APPLICATION
*/

package com.example.wheeltracker;

public class InitConfig {

    public static final String HOST = "192.168.1.5"; //HOST for connecting with local server DB
    public static final String PATH = "http://"+HOST+":1234/wheeltrackerapp/Android/";
    public static final int DEFAULT_LENGTH = 4;  //length of input should be min. 5 only after that login button will be enabled
    public static final int TIME = 1000;     // milliseconds
}
