package main;


import java.util.ArrayList;
import java.util.List;
import simple.Layer;
import simple.NeyroArray;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author mike
 */
public class Main {
    
    private static final List<Layer> layers = new ArrayList<>();
    
    static double tableOfLearn[][] = {
    {0,0,1},
    {1,0,1},
    {0,1,1},
    {1,1,1}
    };
    
    static double tableOfAnswers[] = {0,0,0,1};
    
    public static void main(String[] args) {
        
        NeyroArray    na = new NeyroArray(3,2,1,false);
        
        double[][][] weights = {
            {{0.45,0.78},{-0.12,0.13}},
            {{1.5},{-2.3}}
        };
        
        na.setWeights(weights);        
        
        na.calcIteration(new double[] {1,0}, new double[] {1});
        
//        layers.add(new Layer(3, 1));
//        layers.add(new Layer(1, 1));
//     
//        train();
        
        System.exit(0);
    }

    
    static void train(){
        
        for(int i=0; i<tableOfLearn.length; i++){
            
            for(int l=0; l<layers.size(); l++){
                
                if(l == 0){
                    
                    layers.get(l).setEntries(tableOfLearn[i]);
                    
                    
                    layers.get(l).calcOuput();
                    
                    layers.get(l).calcGrad(layers.get(l+1).getCurrSigma());
                    
                    System.out.print("l= "+l);
                    printDubleArray(layers.get(l).getGrad());
                    
                }else if(l == layers.size()-1){
                    
                    layers.get(l).setEntries(layers.get(l-1).getOutputs());
                    
                    layers.get(l).setAnswers(tableOfAnswers);
                    
                    layers.get(l).calcOuput();
                    
                    layers.get(l).calcDiff();
                    
                    layers.get(l).calcErr();
                    
                    layers.get(l).calcOuterSigma();
                    
//                    System.out.print("l= "+l);
//                    printArray(layers.get(l).getCurrSigma());
                    
                    
                }else{
                    
                }               
            }
        }
    }
    
    private static void printArray(double[] arr){
        
        for( int i=0; i<arr.length; i++){
            
            System.out.println(" i= "+i+" arr= "+arr[i]);
        }
        
    }
    
    private static void printDubleArray(double[][] arr){
        
        for( int i=0; i<arr.length; i++){
            
            System.out.print(" i= "+i);
            
            for( int j=0; j<arr[i].length; j++){
             System.out.print(" j= "+j+" arr= "+arr[i][j]+" w "+tableOfLearn[j][i]);
            }
             
            System.out.println(); 
        }
        
    }    
}
