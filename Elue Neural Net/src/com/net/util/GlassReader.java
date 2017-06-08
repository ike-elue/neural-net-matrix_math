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
public class GlassReader {
    public double[][] inputs, outputs;
    public final String[] classifications = {"building_windows_float_processed", 
        "building_windows_non_float_processed",
        "vehicle_windows_float_processed",
        "vehicle_windows_non_float_processed", 
        "containers",
        "tableware",
        "headlamps"};
    /**
     * 
     * @param file glass data
     */
    public GlassReader(File file) {
        Scanner scan;
        try {
            scan = new Scanner(file);
            String[] info;
            List<Double> glassValues = new ArrayList<>();
            List<int[]> glassType = new ArrayList<>();
            while(scan.hasNext()) {
                info = scan.nextLine().split(",");
                if(info.length != 11)
                    continue;
                for(int i = 1; i < info.length - 1; i++)
                    glassValues.add(Double.parseDouble(info[i]));
                
                glassType.add(create(info[10]));
            }
            inputs = new double[glassValues.size()/9][9];
            outputs = new double[glassType.size()][7];
            for(int i = 0; i < glassValues.size(); i+=inputs[0].length)
                for(int j = 0; j < inputs[0].length; j++)
                    inputs[i/inputs[0].length][j] = glassValues.get(i + j);
            
            for(int i = 0; i < outputs.length; i++) {
                for(int j = 0; j < outputs[i].length; j++)
                    outputs[i][j] = glassType.get(i)[j];
            }
        } catch(FileNotFoundException exc) {
            exc.printStackTrace();
            inputs = null;
            outputs = null;
        }
    }
    
    public final int[] create(String classification) {
        int pointer = Integer.parseInt(classification);
        int[] array = new int[7];
        array[pointer - 1] = 1;
        return array;
    }
    
    public final String create(TimeMatrix value, int row) {
        for(int i = 0; i < 7; i++) {
            if(value.get(row,i,0) == 1)
                return classifications[i];
        }
        return "NULL";
    }
}
