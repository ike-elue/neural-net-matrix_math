/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.net.test;

import com.net.network.Network;
import com.net.util.IrisReader;
import com.net.util.TimeMatrix;
import java.io.File;
import java.util.Arrays;
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
        File file3 = new File("E:\\Elue Neural Net\\res\\offender.net");
        int[] layerStats = new int[]{2, 3, 1};
        int activationType = Network.SIGMOID;
        double learningRate = .1;
        int recurrentLength = 1; // Not a recurrent network is what this means
        double[][] prediction = new double[][]{{1, 1}, {0, 1}, {1, 1}, {0, 1}, {0, 0}, {1, 0}};

// Trains New Network
//        double[][] inputs = new double[][]{{0, 0}, {0, 1}, {1, 0}, {1, 1}};
//        double[][] outputs = new double[][]{{0}, {1}, {1}, {0}};
//        int iterations = 100000;
//        int everyNth = 10000;
//
//        
//        Network net = new Network(activationType, learningRate, layerStats, recurrentLength);
//        net.train(inputs, outputs, iterations, everyNth);
//        System.out.println(net.predict(prediction));
//        
//        Scanner scan = new Scanner(System.in);
//        System.out.println("Type 0 to save, Type anything to close");
//        if(scan.nextLine().equals("0"))
//            net.save(file);
//        else
//            System.exit(0);
        
// Runs Existing Network
//        Network net2 = new Network(activationType, learningRate, layerStats, recurrentLength);
//        net2.open(file);
//        System.out.println(net2.predict(prediction));

        int[] irisLayerStats = new int[] {4,8,3};
        double[][] predict = new double[3][4];
        IrisReader reader = new IrisReader(new File("res/iris dataset.txt"));
        predict[0] = reader.inputs[0];
        predict[1] = reader.inputs[80];
        predict[2] = reader.inputs[145];
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
        
        Network net2 = new Network(activationType, learningRate, irisLayerStats, recurrentLength);
        net2.open(file2);
        TimeMatrix matrix = new TimeMatrix(net2.predict(predict).round(), false);
        System.out.println(matrix);
        System.out.println(reader.create(matrix, 0));
        System.out.println(reader.create(matrix, 1));
        System.out.println(reader.create(matrix, 2));


// Needed Info for an Offender Net
//        int maxSize = 12; //23 is max, 5 is min
//        SpecialReader reader = new SpecialReader(new File("res/data.txt"), maxSize, 1, maxSize);
//        int[] offenderLayerStats = new int[] {maxSize, maxSize + (maxSize/2), 1};
//        double[][] predict = new double[1][maxSize];
//        predict[0] = reader.inputs[7];
//        predict[0] = reader.interpret("JONATHANELUE", false);
        
// Train New Offender Network
//        Network net = new Network(activationType, learningRate, offenderLayerStats, recurrentLength);
//        net.train(reader.inputs, reader.outputs, 100000, 10000);
//        System.out.println(Arrays.toString(reader.translate(predict[0])));
//        System.out.println(net.predict(predict));
//        if(!net.nanFound) {
//            Scanner scan = new Scanner(System.in);
//            System.out.println("Type 0 to save, Type anything to close");
//            if(scan.nextLine().equals("0"))
//                net.save(file3);
//            else
//                System.exit(0);
//        }
//        else
//            System.exit(0);

// Run Existing Offender
//        Network net2 = new Network(activationType, learningRate, offenderLayerStats, recurrentLength);
//        net2.open(file3);
//        System.out.println(Arrays.toString(reader.translate(predict[0])));
//        System.out.println(net2.predict(predict));
    }

}
