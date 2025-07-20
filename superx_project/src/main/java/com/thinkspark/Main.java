package com.thinkspark;


import com.thinkspark.view.CitizenDashboard;
import com.thinkspark.view.DisasterHelperUI;
import com.thinkspark.view.RequestHelpForm;

import javafx.application.Application;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        Application.launch(DisasterHelperUI.class,args);
    }
}