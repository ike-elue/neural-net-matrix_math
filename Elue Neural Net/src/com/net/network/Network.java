/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.net.network;

import com.net.layer.Layer;
import com.net.util.TimeMatrix;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author CSLAB313-1740
 */
public class Network {
    public static final int SIGMOID = 0, HYPERTAN = 1;
    private final int activationType;
    
    private final List<Layer> layers;
    private final int[] layerNeurons;
    private final List<TimeMatrix> trainingDataIn;
    private final List<TimeMatrix> trainingDataOut;
 
    private TimeMatrix error, trueError;
    
    private boolean initialized;
    
    private final double learningRate;
    private final int recurrentLength;
    
    public boolean nanFound;
    
    public Network(int activationType, double learningRate, int[] layers, int recurrentLength) {
        this.activationType = activationType;
        this.recurrentLength = recurrentLength;
        this.learningRate = learningRate;
        this.layers = new ArrayList<>();
        trainingDataIn = new ArrayList<>();
        trainingDataOut = new ArrayList<>();
        initialized = false;
        layerNeurons = layers;
        error = null;
        trueError = null;
        nanFound = false;
    }
    
    private void addToInput(double[] input) {
        trainingDataIn.add(new TimeMatrix(input, recurrentLength));
    }
    
    private void addToOutput(double[] output) {
        trainingDataOut.add(new TimeMatrix(output, recurrentLength));
    }
    
    private boolean init(double[][] inputs, double[][] outputs) {
        if(!initialized)
            initSynapses();
        initialized = true;
        if(layers.size() < 2) {
            System.out.println("Less than 2 layers in network");
            initialized = false;
            layers.clear();
            return false;
        }
       
        for(int i = 0; i < inputs.length; i++) {
            addToInput(inputs[i]);
            addToOutput(outputs[i]);
        }
        System.out.println("Training Network Begin.....");
        return true;
    }
    
    private void initSynapses() {
        for(int i = 0; i < layerNeurons.length; i++) {
            if(i > 0)
                layers.add(new Layer(layerNeurons[i], layerNeurons[i - 1], recurrentLength, activationType, false));
            else
                layers.add(new Layer(layerNeurons[i], 1, recurrentLength, activationType, true));   
        }
    }
    
    public void train(double[][] inputs, double[][] outputs, int iterations, int everyNth) {
        if(!init(inputs, outputs))
            return;
        TimeMatrix determiner;
        for(int i = 0; i < iterations; i++) {
            determiner = update(i, everyNth);
            if(determiner.containsNAN()) {
                System.out.println("NaN found");
                nanFound = true;
                break;
            }
        }
        System.out.println("Training Network Finish.....");
        System.out.println("\nFinal Sturcture For Neural Network " + this);
    }
 
    private TimeMatrix update(int i, int everyNth) {
        for(int timestep = 0; timestep < recurrentLength; timestep++) {
            for(int j = 0; j < trainingDataIn.size(); j++) {
                error = new TimeMatrix(trainingDataOut.get(j).sub(timestep, forward(trainingDataIn.get(j), timestep), false), true);
                if(trueError == null)
                    trueError = new TimeMatrix(error, true);
                else
                    trueError.average(error, timestep);
                if(i % everyNth == 0 && j == trainingDataIn.size() - 1) 
                    System.out.println(i + "th iteration --> Error: " + trueError.absolute(timestep, false).average(timestep));
                error.mult(timestep, accessLayer(getFinalIndex()).getSumsPrime(timestep), true);
                accessLayer(getFinalIndex()).setDelta(error);
                backward(timestep);
            }
        }
        trueError = null;
        return error;
    }
    
    public TimeMatrix predict(double[][] inputs) {
        TimeMatrix output = new TimeMatrix(inputs.length, accessLayer(getFinalIndex()).neurons, recurrentLength);
        for (int row = 0; row < inputs.length; row++)
            output.setRow(forward(new TimeMatrix(inputs[row], recurrentLength), 0), row);
        return output;
    }
    
    private TimeMatrix forward(TimeMatrix matrix, int timestep) {
        TimeMatrix m = null;
        accessLayer(0).setSums(matrix);
        for(int i = 1; i < layers.size(); i++) {
            if(i == 1)
                m = accessLayer(i).forward(matrix, timestep);
            else
                m = accessLayer(i).forward(m, timestep); 
        }
        return m;
    }
    
    private void backward(int timestep) {
        Layer layer = accessLayer(getFinalIndex());
        for(int i = getFinalIndex() - 1; i > -1; i--)
            layer = accessLayer(i).backward(layer, timestep, learningRate);
    }
   
    private Layer accessLayer(int index) {
        return layers.get(index);
    }
    
    private int getFinalIndex() {
        return layers.size() - 1;
    }
    
    public void printTrainingData() {
        String str = "\nTraining Data For Neural Net";
        for(int i = 0; i < trainingDataIn.size(); i++)
            str += "\nInput " + i + ": " + trainingDataIn.get(i) + " Output " + i + ": " + trainingDataOut.get(i);
        System.out.println(str);
    }
    
    public final void open(File file) {
        String extension = "";
        int i = file.getPath().lastIndexOf('.');
        if (i > 0)
            extension = file.getPath().substring(i+1);
        if(!extension.equals("net"))
            return;
        
        Scanner scan;
        try {
            scan = new Scanner(file);
            
            String layerInfo, weights, bias = null;
            boolean canSearchBias = false;
            
            while(scan.hasNextLine()) {
                layerInfo = scan.nextLine(); // neurons, backNeurons, activationType, recurrentLength
                weights = scan.nextLine(); // List all
                if(canSearchBias)
                    bias = scan.nextLine(); // List all
                else
                    canSearchBias = true;
                layers.add(new Layer(layerInfo, weights, bias));
            }
        } catch(FileNotFoundException e) {
            layers.clear();
            e.printStackTrace();
        }
    }
    
    public final void save(File file) {
        if(layers.isEmpty()) {
            System.out.println("Empty Layers");
            return;
        }
        PrintWriter writer;
        try {
            if(!file.exists())
                file.createNewFile();
            writer = new PrintWriter(file, "UTF-8");
            for(Layer l : layers) {
                writer.println("" + l.neurons + "," + l.getWeights().getRows() + "," + activationType + "," + recurrentLength);
                writer.println(l.getWeights().printAll());
                if(l.getPreviousBiases() != null)
                    writer.println(l.getPreviousBiases().printAll());
            }
            writer.close();
        } catch (IOException e) {
           e.printStackTrace();
        }
    }
    
    @Override
    public String toString() {
        String str = "";
        for(int i = 1; i < layers.size(); i++) 
            str += layers.get(i) + "\n";
        return str;
    }
}
