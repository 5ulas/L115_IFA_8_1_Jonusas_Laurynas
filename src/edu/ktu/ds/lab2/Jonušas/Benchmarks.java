/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ktu.ds.lab2.Jonušas;

import edu.ktu.ds.lab2.gui.ValidationException;
import edu.ktu.ds.lab2.utils.*;

import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;
import edu.ktu.ds.lab2.demo.Car;
import edu.ktu.ds.lab2.demo.Timekeeper;
import edu.ktu.ds.lab2.gui.ValidationException;
import edu.ktu.ds.lab2.utils.AvlSet;
import edu.ktu.ds.lab2.utils.BstSet;
import edu.ktu.ds.lab2.utils.BstSetIterative;
import edu.ktu.ds.lab2.utils.Ks;
import edu.ktu.ds.lab2.utils.SortedSet;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.TreeSet;

/**
 *
 * @author llaur
 */
public class Benchmarks {
    public static final String FINISH_COMMAND = "                       ";
    private static final ResourceBundle MESSAGES = ResourceBundle.getBundle("edu.ktu.ds.lab2.gui.messages");

    private static final String[] BENCHMARK_NAMES = {"addBstRec", "addBstIte", "addAvlRec", "removeBst"};
    private static final int[] COUNTS2 = {500000,100000,250000,500000,800000,1200000,1500000, 1750000};
    private static final int[] COUNTS = {300};
    //{100000, 20000, 40000,80000,120000,160000,200000,250000,350000};
    private final Timekeeper timeKeeper;
    private final String[] errors;

    private final SortedSet<Person> cSeries = new BstSet<>(Person.byHeight);
    private final SortedSet<Person> cSeries2 = new BstSetIterative<>(Person.byHeight);
    private final SortedSet<Person> cSeries3 = new AvlSet<>(Person.byHeight);
    
    private static final TreeSet<Integer> tree = new TreeSet<>(); 
    private static final HashSet<Integer> hash = new HashSet<>(); 
    private static final TreeSet<Integer> tree2 = new TreeSet<>(); 
    private static final HashSet<Integer> hash2 = new HashSet<>(); 

    public static void main(String[] args) {
        executeTest();
    }
    /**
     * For console benchmark
     */
    public Benchmarks() {
        timeKeeper = new Timekeeper(COUNTS);
        errors = new String[]{
                MESSAGES.getString("badSetSize"),
                MESSAGES.getString("badInitialData"),
                MESSAGES.getString("badSetSizes"),
                MESSAGES.getString("badShuffleCoef")
        };
    }
    
    public Benchmarks(BlockingQueue<String> resultsLogger, Semaphore semaphore) {
        semaphore.release();
        timeKeeper = new Timekeeper(COUNTS, resultsLogger, semaphore);
        errors = new String[]{
                MESSAGES.getString("badSetSize"),
                MESSAGES.getString("badInitialData"),
                MESSAGES.getString("badSetSizes"),
                MESSAGES.getString("badShuffleCoef")
        };
    }
   public static void executeTest() {
        // suvienodiname skaičių formatus pagal LT lokalę (10-ainis kablelis)
        Locale.setDefault(new Locale("LT"));
        Ks.out("Greitaveikos tyrimas:\n");
        new Benchmarks().startBenchmark();
    }

    public void startBenchmark() {
        try {
            benchmark();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
        }
    }
    private void treeSetAndHashSetBenchmarks() throws InterruptedException{
        Random rnd = new Random();
        int[] indexes = new int[3000];
           for (int j = 0; j < indexes.length; j++) {
                indexes[j] = rnd.nextInt(COUNTS2[2]);
                int x = rnd.nextInt(COUNTS2[2]);
                tree2.add(x);
                hash2.add(x);
            }
            for (int i = 0; i < COUNTS2.length; i++) {
            for (int j = 0; j < COUNTS2[i]; j++) {
                tree.add(j);
            }
            long timeBefore = System.nanoTime();
            for (int j = 0; j < indexes.length; j++) {
                tree.contains(indexes[j]);
                //tree.containsAll(tree2);
            }
            long elapsed = System.nanoTime() - timeBefore;
            System.out.println("elapsed tree: " + elapsed);
            
            for (int j = 0; j < COUNTS2[i]; j++) {
                hash.add(j);
            }
            long timeBefore2 = System.nanoTime();
            for (int j = 0; j < indexes.length; j++) {
                hash.contains(indexes[j]);
                //hash2.containsAll(hash2);
            }
            long elapsed2 = System.nanoTime() - timeBefore2;
            System.out.println("elapsed hash: " + elapsed2);
        }
    }
    private void benchmark() throws InterruptedException {
        try {
            for (int k : COUNTS) {
                Person[] people = new PeopleGenerator().generateShuffle(k, 1.0);
                cSeries.clear();
                cSeries2.clear();
                cSeries3.clear();
                timeKeeper.startAfterPause();

                timeKeeper.start();
                Arrays.stream(people).forEach(cSeries::add);
                timeKeeper.finish(BENCHMARK_NAMES[0]);

                Arrays.stream(people).forEach(cSeries2::add);
                timeKeeper.finish(BENCHMARK_NAMES[1]);

                Arrays.stream(people).forEach(cSeries3::add);
                timeKeeper.finish(BENCHMARK_NAMES[2]);

                Arrays.stream(people).forEach(cSeries::remove);

                timeKeeper.finish(BENCHMARK_NAMES[3]);
                
                timeKeeper.seriesFinish();
            }
            timeKeeper.logResult(FINISH_COMMAND);
            treeSetAndHashSetBenchmarks();
        } catch (ValidationException e) {
            if (e.getCode() >= 0 && e.getCode() <= 3) {
                timeKeeper.logResult(errors[e.getCode()] + ": " + e.getMessage());
            } else if (e.getCode() == 4) {
                timeKeeper.logResult(MESSAGES.getString("allSetIsPrinted"));
            } else {
                timeKeeper.logResult(e.getMessage());
            }
        }
    }
}
        

