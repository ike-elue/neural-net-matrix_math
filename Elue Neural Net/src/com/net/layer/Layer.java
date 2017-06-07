/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.net.layer;

import com.net.network.Network;
import com.net.util.TimeMatrix;

/**
 *
 * @author CSLAB313-1740
 */
public class Layer {
    protected final TimeMatrix sums, prevBias, weights, error, delta;
    private final int activationType;
    public final int neurons;

    public Layer(int neurons, int backNeurons, int recurrentLength, int activationType, boolean isFirst) {
        this.neurons = neurons;
        this.sums = new TimeMatrix(1, neurons, recurrentLength);
        if(!isFirst)
            this.prevBias = new TimeMatrix(1, neurons, recurrentLength);
        else
            this.prevBias = null;
        this.error = new TimeMatrix(1, neurons, recurrentLength);
        this.delta = new TimeMatrix(1, neurons, recurrentLength);
        this.weights = new TimeMatrix(backNeurons, neurons, recurrentLength);
        
        if(!isFirst)
            prevBias.randomizeMatrix();
        weights.randomizeMatrix();
        
        this.activationType = activationType;
    }
    
    public Layer(String layerInfo, String weights, String bias) {
        String[] info = layerInfo.split(",");
        this.neurons = Integer.parseInt(info[0]);
        int backNeurons = Integer.parseInt(info[1]);
        activationType = Integer.parseInt(info[2]);
        int recurrentLength = Integer.parseInt(info[3]);
        
        this.sums = new TimeMatrix(1, neurons, recurrentLength);
        this.error = new TimeMatrix(1, neurons, recurrentLength);
        this.delta = new TimeMatrix(1, neurons, recurrentLength);
        
        String[] ws = weights.split(",");
        double[][][] w = new double[backNeurons][neurons][recurrentLength];
        int counter = 0;
        for(int row = 0; row  < w.length; row++) {
            for(int col = 0; col  < w[row].length; col++) {
                for(int t = 0; t  < w[row][col].length; t++) {
                    if(counter >= ws.length)
                        continue;
                    w[row][col][t] = Double.parseDouble(ws[counter]);
                    counter++;
                }   
            }
        }
        this.weights = new TimeMatrix(w);
        
        if(bias == null)
            this.prevBias = null;
        else {
            String[] bs = bias.split(",");
            double[][][] b = new double[backNeurons][neurons][recurrentLength];
            counter = 0;
            for(int row = 0; row  < b.length; row++) {
                for(int col = 0; col  < b[row].length; col++) {
                    for(int t = 0; t  < b[row][col].length; t++) {
                        if(counter >= bs.length)
                            continue;
                        b[row][col][t] = Double.parseDouble(bs[counter]);
                        counter++;
                    }   
                }
            }
            this.prevBias = new TimeMatrix(b);
        }
    }
    
    public TimeMatrix forward(TimeMatrix input, int timestep) {
        sums.set(input.dot(timestep, weights).add(timestep, prevBias, true));
        if(activationType == Network.SIGMOID)
            sums.sigmoid(timestep, true);
        else if(activationType == Network.HYPERTAN)
            sums.hyperbolicTangent(timestep, true);
        return sums;
    }
    
    public Layer backward(Layer layer, int timestep, double learningRate) {
        layer.weights.add(timestep, sums.transpose().dot(timestep, layer.delta), true);
        layer.prevBias.add(timestep, layer.delta, true);
        error.set(layer.delta.dot(timestep, layer.weights.transpose()));
        if(activationType == Network.SIGMOID)
            delta.set(error.mult(timestep, sums.sigmoidPrime(timestep, false), false).scale(timestep, learningRate, true));
        else if(activationType == Network.HYPERTAN)
            delta.set(error.mult(timestep, sums.hyperbolicTangentPrime(timestep, false), false).scale(timestep, learningRate, true));
        return this;
    }
    
    public TimeMatrix getWeights() {
        return weights;
    }
    
    public TimeMatrix getPreviousBiases() {
        return prevBias;
    }
    
    public TimeMatrix getSumsPrime(int timestep) {
        if(activationType == Network.SIGMOID)
            return sums.sigmoidPrime(timestep, false);
        else if(activationType == Network.HYPERTAN)
            return sums.hyperbolicTangentPrime(timestep, false);
        System.out.println("Error in prime function");
        return null;
    }
    
    public void setDelta(TimeMatrix matrix) {
        delta.set(matrix);
    }
    
    public void setSums(TimeMatrix matrix) {
        sums.set(matrix);
    }
    
    @Override
    public String toString() {
        return "\nWeights: " + weights + "\nPrevious Bias: " + prevBias;
    }
}
