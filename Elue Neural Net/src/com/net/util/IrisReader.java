/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.net.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author CSLAB313-1740
 */
public class IrisReader {
    public double[][] inputs, outputs;
    
    /**
     * 
     * @param file sex offender list
     */
    public IrisReader(File file) {
        Scanner scan;
        try {
            scan = new Scanner(file);
            String[] info;
            List<Double> flowerValues = new ArrayList<>();
            List<int[]> flowerType = new ArrayList<>();
            while(scan.hasNext()) {
                info = scan.nextLine().split(",");
                if(info.length != 5)
                    continue;
                flowerValues.add(Double.parseDouble(info[0]));
                flowerValues.add(Double.parseDouble(info[1]));
                flowerValues.add(Double.parseDouble(info[2]));
                flowerValues.add(Double.parseDouble(info[3]));
                flowerType.add(create(info[4]));
            }
            inputs = new double[flowerValues.size()/4][4];
            outputs = new double[flowerType.size()][3];
            for(int i = 0; i < flowerValues.size(); i+=4) {
                inputs[i/4][0] = flowerValues.get(i);
                inputs[i/4][1] = flowerValues.get(i+1);
                inputs[i/4][2] = flowerValues.get(i+2);
                inputs[i/4][3] = flowerValues.get(i+3);
            }
            
            for(int i = 0; i < outputs.length; i++) {
                for(int j = 0; j < outputs[i].length; j++)
                    outputs[i][j] = flowerType.get(i)[j];
            }
        } catch(FileNotFoundException exc) {
            exc.printStackTrace();
            inputs = null;
            outputs = null;
        }
    }
    
    public final int[] create(String species) {
        switch(species.toLowerCase()) {
            case "iris-setosa":
                return new int[] {1,0,0};
            case "iris-versicolor":
                return new int[] {0,1,0};
            case "iris-virginica":
                return new int[] {0,0,1};
            default: 
                return null;
        }
    }
    
    public final String create(TimeMatrix value, int row) {
        if(value.get(row,0,0) == 1)
            return "iris-setosa";
        else if(value.get(row,1,0) == 1)
            return "iris-versicolor";
        else if(value.get(row,2,0) == 1)
            return "iris-virginica";
        else
            return "";
    }
}
