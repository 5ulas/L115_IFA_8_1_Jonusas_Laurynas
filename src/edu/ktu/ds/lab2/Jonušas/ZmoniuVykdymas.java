/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ktu.ds.lab2.Jonušas;

import edu.ktu.ds.lab2.Jonušas.ManoTestas;
import edu.ktu.ds.lab2.gui.MainWindow;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Locale;
/**
 *
 * @author llaur
 */
public class ZmoniuVykdymas extends Application {
    public static void main(String[] args){
        ZmoniuVykdymas.launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        Locale.setDefault(Locale.US); // Suvienodiname skaičių formatus 
        ManoTestas.executeTest();
        MainWindow.createAndShowGui(primaryStage);
    }
}
