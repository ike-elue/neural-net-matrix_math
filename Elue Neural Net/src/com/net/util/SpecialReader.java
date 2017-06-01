/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.net.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Jonathan Elue
 */
public class SpecialReader {
    
    public double[][] inputs, outputs;
    private final int maxSize;
    private final double normalizer;
    
    /**
     * 
     * @param file sex offender list
     * @param inputNeurons should be 80 (8 bits, 10 letters max)
     * @param outputNeurons should be 1
     * @param maxSize
     */
    public SpecialReader(File file, int inputNeurons, int outputNeurons, int maxSize) {
        this.maxSize = maxSize;
        this.normalizer = 100;
        Scanner scan;
        try {
            scan = new Scanner(file);
            String[] info;
            List<String> names = new ArrayList<>();
            List<Integer> yes_no = new ArrayList<>();
            int counter = 0, counter2 = 0, yesses = 0, nos = 0;
            while(scan.hasNext()) {
                counter++;
                info = scan.nextLine().split(",");
                if(info.length != 3|| info[1].length() + info[0].length() > maxSize) {
                    counter2++;
                    continue;
                }
                names.add(info[1] + info[0]);
                if(info[2].toLowerCase().equals("y")) {
                    yesses++;
                    yes_no.add(1);
                }
                else {
                    nos++;
                    yes_no.add(0);
                }
            }
            System.out.println("Items: " + counter);
            System.out.println("True Items: " + (counter - counter2));
            System.out.println("Yes Items: " + yesses + "\nNo Items: " + nos);
            inputs = new double[names.size()][inputNeurons];
            outputs = new double[yes_no.size()][outputNeurons];
            
            for(int i = 0; i < inputs.length; i++) {
                inputs[i] = interpret(names.get(i), false);
            }
            
            for(int i = 0; i < outputs.length; i++) {
                outputs[i][0] = yes_no.get(i);
            }
        } catch(FileNotFoundException exc) {
            exc.printStackTrace();
            inputs = null;
            outputs = null;
        }
    }
    
    public double[] interpret(String s, boolean useComplicated) {
        double[] d;
        if(useComplicated) {
            d = new double[maxSize * 8];
            String str = s.toLowerCase();
            String intString;
            ArrayList<String> intStrings = new ArrayList<>();
            for (char c : str.toCharArray()) {
                intString = String.format("%08d", Integer.parseInt(Integer.toBinaryString((int) c)));
                intStrings.add(intString);
            }

            int counter = 0;
            for(int i = 0; i < d.length; i += 8) {
                for(int j = 0; j < 8; j++) {
                    d[i + j] = Integer.parseInt("" + intStrings.get(counter).charAt(j));
                }
                counter++;
                if(counter >= intStrings.size())
                    break;
            }
        }
        else {
            d = new double[maxSize];
           for(int i = 0; i < s.length(); i ++)
               d[i] = ((int)s.charAt(i))/normalizer;
        }
        return d;
    }
    
    public char[] translate(double[] array) {
        char[] chars = new char[array.length];
        for(int i = 0; i < chars.length; i++)
            chars[i] = (char)(array[i]*normalizer);
        return chars;
    }
}
