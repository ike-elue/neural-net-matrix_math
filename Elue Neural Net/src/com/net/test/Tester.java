/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.net.test;

import com.net.network.Network;
import com.net.util.GlassReader;
import com.net.util.TimeMatrix;
import java.io.File;
import java.util.Scanner;

/**
 *
 * @author CSLAB313-1740
 */
public class Tester {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
// Needed Info for a Network/Existing Network
        File file = new File("E:\\Elue Neural Net\\res\\xornetwork.net");
        File file2 = new File("E:\\Elue Neural Net\\res\\iris.net");
        File file3 = new File("E:\\Elue Neural Net\\res\\glass.net");
        int activationType = Network.SIGMOID;
        double learningRate = .1;
        int recurrentLength = 1; // Not a recurrent network is what this means

// Trains XOR Network
//        int[] xorLayerStats = new int[]{2, 3, 1};
//        double[][] prediction = new double[][]{{1, 1}, {0, 1}, {1, 1}, {0, 1}, {0, 0}, {1, 0}};
//        double[][] inputs = new double[][]{{0, 0}, {0, 1}, {1, 0}, {1, 1}};
//        double[][] outputs = new double[][]{{0}, {1}, {1}, {0}};
//        int iterations = 100000;
//        int everyNth = 10000;
//
//        
//        Network net = new Network(activationType, learningRate, xorLayerStats, recurrentLength);
//        net.train(inputs, outputs, iterations, everyNth);
//        System.out.println(net.predict(prediction));
//        
//        Scanner scan = new Scanner(System.in);
//        System.out.println("Type 0 to save, Type anything to close");
//        if(scan.nextLine().equals("0"))
//            net.save(file);
//        else
//            System.exit(0);
        
// Runs Existing XOR Network
//        Network net2 = new Network(activationType, learningRate, xorLayerStats, recurrentLength);
//        net2.open(file);
//        System.out.println(net2.predict(prediction));

// Iris Stats
//        int[] irisLayerStats = new int[] {4,8,3};
//        double[][] predict = new double[3][4];
//        IrisReader reader = new IrisReader(new File("res/iris dataset.txt"));
//        predict[0] = reader.inputs[0];
//        predict[1] = reader.inputs[80];
//        predict[2] = reader.inputs[145];

// Train Iris Network
//        Network net = new Network(activationType, learningRate, irisLayerStats, recurrentLength);
//        net.train(reader.inputs, reader.outputs, 1000000, 100000);
//        TimeMatrix matrix = new TimeMatrix(net.predict(predict).round(), false);
//        System.out.println(matrix);
//        System.out.println(reader.create(matrix, 0));
//        System.out.println(reader.create(matrix, 1));
//        System.out.println(reader.create(matrix, 2));
//        if(!net.nanFound) {
//            Scanner scan = new Scanner(System.in);
//            System.out.println("Type 0 to save, Type anything to close");
//            if(scan.nextLine().equals("0"))
//                net.save(file2);
//            else
//                System.exit(0);
//        }
//        else
//            System.exit(0);

// Run Existing Iris Network
//        Network net2 = new Network(activationType, learningRate, irisLayerStats, recurrentLength);
//        net2.open(file2);
//        TimeMatrix matrix = new TimeMatrix(net2.predict(predict).round(), false);
//        System.out.println(matrix);
//        System.out.println(reader.create(matrix, 0));
//        System.out.println(reader.create(matrix, 1));
//        System.out.println(reader.create(matrix, 2));

 //Glass Stats
        int[] glassLayerStats = new int[] {9,18,7};
        double[][] predict = new double[3][9];
        GlassReader reader = new GlassReader(new File("res/glass data.txt"));
        predict[0] = reader.inputs[0];
        predict[1] = reader.inputs[43];
        predict[2] = reader.inputs[100];

 //Train Glass Network (Has a tendency to classify as headlamps)
        Network net = new Network(activationType, learningRate, glassLayerStats, recurrentLength);
        net.train(reader.inputs, reader.outputs, 1000, 100);
        TimeMatrix matrix = new TimeMatrix(net.predict(predict).round(), false);
        System.out.println(matrix);
        System.out.println(reader.create(matrix, 0));
        System.out.println(reader.create(matrix, 1));
        System.out.println(reader.create(matrix, 2));
        if(!net.nanFound) {
            Scanner scan = new Scanner(System.in);
            System.out.println("Type 0 to save, Type anything to close");
            if(scan.nextLine().equals("0"))
                net.save(file3);
            else
                System.exit(0);
        }
        else
            System.exit(0);

// Run Existing Glass Network
//        Network net2 = new Network(activationType, learningRate, glassLayerStats, recurrentLength);
//        net2.open(file3);
//        TimeMatrix matrix = new TimeMatrix(net2.predict(predict).round(), false);
//        System.out.println(matrix);
//        System.out.println(reader.create(matrix, 0));
//        System.out.println(reader.create(matrix, 1));
//        System.out.println(reader.create(matrix, 2));
    }

}
