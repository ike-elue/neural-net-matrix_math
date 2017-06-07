/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.net.util;

import java.util.Arrays;

/**
 *
 * @author CSLAB313-1740
 */
public class TimeMatrix {
    
    protected final int rows, columns, time;
    protected final double[][][] matrix;
    private final TimeMatrix extraMatrix;
    
    public TimeMatrix(double[][][] matrix) {
        this.rows = matrix.length;
        this.columns = matrix[0].length;
        this.time = matrix[0][0].length;
        this.matrix = new double[rows][columns][time];
        set(matrix);
        extraMatrix = new TimeMatrix(this, false);
    }
    
    public TimeMatrix(double[][] matrix, int sequences) {
        this.rows = matrix.length;
        this.columns = matrix[0].length;
        this.time = sequences;
        this.matrix = new double[rows][columns][time];
        set(matrix);
        extraMatrix = new TimeMatrix(this, false);
    } 
    
    public TimeMatrix(double[] matrix, int sequences) {
        this.rows = 1;
        this.columns = matrix.length;
        this.time = sequences;
        this.matrix = new double[rows][columns][time];
        set(matrix);
        extraMatrix = new TimeMatrix(this, false);
    } 
    
    public TimeMatrix(int rows, int columns, int sequences) {
        this.rows = rows;
        this.columns = columns;
        this.time = sequences;
        matrix = new double[rows][columns][sequences];
        set(0);
        extraMatrix = new TimeMatrix(this, false);
    }
    
    public TimeMatrix(TimeMatrix m, boolean createExtra) {
        this.rows = m.rows;
        this.columns = m.columns;
        this.time = m.time;
        matrix = new double[rows][columns][time];
        set(m);
        if(createExtra)
            extraMatrix = new TimeMatrix(this, false);
        else
            extraMatrix = null;
    }
    
    public final void set(double m) {
        for(int row = 0; row < this.rows; row++)
            for(int col = 0; col < this.columns; col++)
                for(int t = 0; t < this.time; t++)
                    matrix[row][col][t] = m;
    }
    
    private void set(double[] matrix) {
        if(matrix.length != columns)
            return;
        for(int col = 0; col < this.columns; col++)
            for(int t = 0; t < this.time; t++)
                this.matrix[0][col][t] = matrix[col];
    }
    
    private void set(double[][] matrix) {
        if(matrix.length != rows || matrix[0].length != columns)
            return;
        for(int row = 0; row < this.rows; row++)
            for(int col = 0; col < this.columns; col++)
                for(int t = 0; t < this.time; t++)
                    this.matrix[row][col][t] = matrix[row][col];
    }
    
    private void set(double[][][] matrix) {
        if(matrix.length != rows || matrix[0].length != columns || matrix[0][0].length != time)
            return;
        for(int row = 0; row < this.rows; row++)
            for(int col = 0; col < this.columns; col++)
                System.arraycopy(matrix[row][col], 0, this.matrix[row][col], 0, this.time);
    }
    
    /**
     * 
     * @param matrix assumed to be single row
     * @param row row to set in main matrix
     */
    public final void setRow(TimeMatrix matrix, int row) {
        if(matrix.rows > rows || matrix.columns > columns || matrix.time > time)
            return;
        for(int r = 0; r < matrix.rows; r++)
            for(int c = 0; c < matrix.columns; c++)
                System.arraycopy(matrix.matrix[r][c], 0, this.matrix[row][c], 0, this.time);
    }
    
    public final void set(TimeMatrix matrix) {
        set(matrix.matrix);
    }
    
    public void spreadThroughTime(int row, int col) {
        for(int t = 0; t < time; t++)
            matrix[row][col][t] = matrix[row][col][0];
    }
    
    public int getRows() {
        return rows;
    }
    
    public int getColumns() {
        return columns;
    }
    
    public int getTime() {
        return time;
    }
    
    public double[][][] getMatrix() {
        return matrix;
    }
    
    public TimeMatrix transpose() {
        TimeMatrix m = new TimeMatrix(columns, rows, time);
        for (int x = 0; x < rows; x++)
            for (int y = 0; y < columns; y++)
                System.arraycopy(this.matrix[x][y], 0, m.matrix[y][x], 0, time);
        return m;
    }
    
    public void randomizeMatrix() {
        for(int i = 0; i < rows; i++)
            for(int j = 0; j < columns; j++) {
                matrix[i][j][0] = Math.random();
                spreadThroughTime(i, j);
            }
    }
    
    public void empty() {
        for(int i = 0; i < rows; i++)
            for(int j = 0; j < columns; j++)
                for(int k = 0; k < time; k++)
                    matrix[i][j][k] = 0;
    }
    
    public boolean containsNAN() {
        for(int i = 0; i < rows; i++)
            for(int j = 0; j < columns; j++)
                for(int k = 0; k < time; k++)
                    if(Double.isNaN(matrix[i][j][k]))
                        return true;
        return false;
    }
    
    public TimeMatrix round() {
        for(int i = 0; i < rows; i++)
            for(int j = 0; j < columns; j++)
                for(int k = 0; k < time; k++)
                    matrix[i][j][k] = (int)(matrix[i][j][k] + .5);
        return this;
    }
    
    public double average(int timestep) {
        double average = 0;
        
        for(int i = 0; i < rows; i++)
            for(int j = 0; j < columns; j++)
                average += matrix[i][j][timestep];
    
        average /= rows;
        
        return average;
    }
    
    public void average(TimeMatrix matrix, int timestep) {
        for(int i = 0; i < rows; i++)
                for(int j = 0; j < columns; j++) {
                    this.matrix[i][j][timestep] += matrix.matrix[i][j][timestep];
                    this.matrix[i][j][timestep] /= 2;
                }
    }
    
    public double averageTime(int row) {
        double average = 0;
        
        for(int i = 0; i < columns; i++)
            for(int j = 0; j < time; j++)
                average += matrix[row][i][j];
        
        average /= time;
        
        return average;
    }
    
    public TimeMatrix absolute(int timestep, boolean save) {
        if(save) {
            for(int i = 0; i < rows; i++)
                for(int j = 0; j < columns; j++)
                    if(matrix[i][j][timestep] < 0)
                        matrix[i][j][timestep] *= -1;
            return this;
        }
        else {
            if(extraMatrix==null)
                return null;
            extraMatrix.set(this);
            return extraMatrix.absolute(timestep, true);
        }
    }
    
    public TimeMatrix dot(int timestep, TimeMatrix matrix) {
        if(matrix == null)
            return null;
        
        if(columns != matrix.rows) {
            System.out.println("Invalid Dimensions");
            System.out.println("This matrix: \n" + getDimensions());
            System.out.println("\nOther matrix: \n" + getDimensions());
            return null;
        }
        
        TimeMatrix m = new TimeMatrix(rows, matrix.getColumns(), 1); // Might be subject to change to actual time var
        double sum;
        
        for(int i = 0; i < m.getRows(); i++)
            for(int j = 0; j < m.getColumns(); j++) {
                sum = 0;
                for(int k = 0; k < matrix.getRows(); k++)
                    sum += this.matrix[i][k][timestep] * matrix.matrix[k][j][timestep];
                m.matrix[i][j][timestep] = sum;
            }
        return m;
    }
    
    public TimeMatrix add(int timestep, TimeMatrix matrix, boolean save) {
        if(matrix == null)
            return this;
        if(save) {
            for(int i = 0; i < rows; i++)
                for(int j = 0; j < columns; j++)
                    this.matrix[i][j][timestep] += matrix.matrix[i][j][timestep];
            return this;
        } 
        else {
            if(extraMatrix==null) 
                return null;
            extraMatrix.set(this);
            return extraMatrix.add(timestep, matrix, true);
        }
    }
    
    public TimeMatrix sub(int timestep, TimeMatrix matrix, boolean save) {
        if(matrix == null)
            return this;
        if(save) {
            for(int i = 0; i < rows; i++)
                for(int j = 0; j < columns; j++)
                    this.matrix[i][j][timestep] -= matrix.matrix[i][j][timestep];
            return this;
        } 
        else {
            if(extraMatrix==null)
                return null;
            extraMatrix.set(this);
            return extraMatrix.sub(timestep, matrix, true);
        }
    }
    
    public TimeMatrix scale(int timestep, double scalar, boolean save) {
        if(save) {
            for(int i = 0; i < rows; i++)
                for(int j = 0; j < columns; j++)
                    this.matrix[i][j][timestep] *= scalar;
            return this;
        } 
        else {
            if(extraMatrix==null)
                return null;
            extraMatrix.set(this);
            return extraMatrix.scale(timestep, scalar, true);
        }
    }
    
    public TimeMatrix mult(int timestep, TimeMatrix matrix, boolean save) {
        if(matrix == null)
            return this;
        if(save) {
            for(int i = 0; i < rows; i++)
                for(int j = 0; j < columns; j++)
                    this.matrix[i][j][timestep] *= matrix.matrix[i][j][timestep];
            return this;
        } 
        else {
            if(extraMatrix==null)
                return null;
            extraMatrix.set(this);
            return extraMatrix.mult(timestep, matrix, true);
        }
    }
    
    public TimeMatrix hyperbolicTangent(int timestep, boolean save) {
        if(save) {
            for(int i = 0; i < rows; i++)
                for(int j = 0; j < columns; j++)
                    this.matrix[i][j][timestep] = hyperbolicTangent(this.matrix[i][j][timestep]);
            return this;
        } 
        else {
            if(extraMatrix==null)
                return null;
            extraMatrix.set(this);
            return extraMatrix.hyperbolicTangent(timestep, true);
        }
    }
    
    public TimeMatrix hyperbolicTangentPrime(int timestep, boolean save) {
        if(save) {
            for(int i = 0; i < rows; i++)
                for(int j = 0; j < columns; j++)
                    this.matrix[i][j][timestep] = hyperbolicTangentPrime(this.matrix[i][j][timestep]);
            return this;
        } 
        else {
            if(extraMatrix==null)
                return null;
            extraMatrix.set(this);
            return extraMatrix.hyperbolicTangentPrime(timestep, true);
        }
    }
    
    public TimeMatrix sigmoid(int timestep, boolean save) {
        if(save) {
            for(int i = 0; i < rows; i++)
                for(int j = 0; j < columns; j++) 
                    this.matrix[i][j][timestep] = sigmoid(this.matrix[i][j][timestep]);
            return this;
        } 
        else {
            if(extraMatrix==null)
                return null;
            extraMatrix.set(this);
            return extraMatrix.sigmoid(timestep, true);
        }
    }
    
    public TimeMatrix sigmoidPrime(int timestep, boolean save) {
       if(save) {
            for(int i = 0; i < rows; i++)
                for(int j = 0; j < columns; j++)
                    this.matrix[i][j][timestep] = sigmoidPrime(this.matrix[i][j][timestep]);
            return this;
        } 
        else {
            if(extraMatrix==null)
                return null;
            extraMatrix.set(this);
            return extraMatrix.sigmoidPrime(timestep, true);
        }
    }
    
    public TimeMatrix getRow(int index, int timestep) {
        if(index >= matrix.length) {
            System.out.println("Too many rows");
            return null;
        }
        double[][] m = new double[1][matrix[0].length]; 
        
        for(int col = 0; col < columns; col++)
            m[0][col] = matrix[index][col][timestep];
                
        System.arraycopy(matrix[index], 0, m[0], 0, matrix[index].length);
        
        return new TimeMatrix(m, time);
    }
    
     public static double hyperbolicTangent(double weightedSum) {
        double e = Math.exp(weightedSum);
        double eNeg = Math.exp(-weightedSum);
        return (e - eNeg) / (e + eNeg);
    }
    
    public static double hyperbolicTangentPrime(double sum) {
        return 1 - (sum * sum);
    }
    
    /**
    * @param weightedSum The activation from the neuron.
    * @return The activation applied to the threshold method.
    */
    public static double sigmoid(double weightedSum) {
        return 1.0 / (1 + Math.exp(-1.0 * weightedSum));
    }
    
    public static double sigmoidPrime(double sum) {
        return sum * (1 - sum);
    }
    
    public final double get(int row, int col, int timestep) {
        return matrix[row][col][timestep];
    }
    
    public String getDimensions() {
        return "Rows: " + rows + "\nColumns: " + columns + "\nSequences: " + time;
    }
    
    public String printAll() {
        String str = "";
        for(int row = 0; row < this.rows; row++)
            for(int col = 0; col < this.columns; col++)
                for(int t = 0; t < this.time; t++)
                    str += "," + matrix[row][col][t];
        return str.substring(1);
    }
    
    @Override
    public String toString() {
        return Arrays.deepToString(matrix);
    }

}
