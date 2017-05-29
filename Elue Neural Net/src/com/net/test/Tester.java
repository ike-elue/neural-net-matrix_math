/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.net.test;

import com.net.network.Network;
import com.net.util.SpecialReader;
import java.io.File;

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
        File file = new File("D:\\Elue Neural Net\\res\\xornetwork.net");
        int[] layerStats = new int[]{2, 3, 1};
        int activationType = Network.SIGMOID;
        double learningRate = .7;
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
//        if(scan.nextInt() == 0)
//            net.save(file);
//        else
//            System.exit(0);
        
// Runs Existing Network
        Network net2 = new Network(activationType, learningRate, layerStats, recurrentLength);
        net2.open(file);
        System.out.println(net2.predict(prediction));
        
// Offender Data (Don't Work)
//        SpecialReader reader = new SpecialReader(new File("res/data.txt"), 10, 1);
//        Network net = new Network(activationType, learningRate, new int[] {10, 12, 1}, recurrentLength);
//        net.train(reader.inputs, reader.outputs, 100, 10);
//        double[][] predict = new double[1][80];
//        predict[0] = reader.inputs[0];
//        System.out.println(net.predict(predict));

    }

}
