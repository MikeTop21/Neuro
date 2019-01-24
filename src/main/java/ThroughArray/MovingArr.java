/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ThroughArray;

import Supersampling.SuperSamp;
import static main.Main.readNetSerial;
import simple.NetEntity;
import simple.NeyroArray;

/**
 *
 * @author mike
 */
public class MovingArr {

    private double[][] mainArray;
    private double[][] findArray;
    private int lenXFind;
    private int lenYFind;
    
    private NetEntity ne ;
    private NeyroArray na;
    private static Supersampling.SuperSamp susa;

    public static void main(String[] args) {

        MovingArr ma = new MovingArr(new double[][]{{0, 1, 1}, 
                                                    {1, 0, 0}, 
                                                    {1, 0, 0}},
                new double[2][2]);
        
//        susa = new SuperSamp(new double[][]{{0.2, 0.7, 0.3}, 
//                                            {0.3, 0.9, 0.8}, 
//                                            {0.7, 0.6, 0.7}}, 
//                new double[2][2]);
//        
//  MovingArr ma = new MovingArr(susa.getNewArray(),
//                new double[2][1]);       

        ma.moving();
    }

    public MovingArr(double[][] mainArray, double[][] findArray) {
        this.mainArray = mainArray;
        this.findArray = findArray;
        this.lenXFind = findArray[0].length;
        this.lenYFind = findArray.length;
        
        ne = readNetSerial("neuronet.out");
        na = new NeyroArray(ne.netDim, ne.withBias, ne.speed, ne.moment);        
        na.setWeights(ne.weights);
    }

    public void moving() {

        for (int i = 0; i <= (mainArray.length - lenYFind); i++) {

            for (int j = 0; j <= (mainArray[i].length - lenXFind); j++) {
                System.out.println(" \ti= " + i + " j= " + j + " val= " + mainArray[i][j]);

                double[] asking = new double[lenYFind*lenXFind];
                int count = 0;
                
                for (int k = 0; k < lenYFind; k++) {

                    for (int l = 0; l < lenXFind; l++) {

                        findArray[k][l] = mainArray[i+k][j+l];
                        asking[count++] = mainArray[i+k][j+l];
                        

                    }
                }
                
                answer(asking);
                double[][] outputs = na.getOutputs();
                        if(outputs[outputs.length-1][0]>0.85){
                            System.out.println("\n\tFROM_1 i="+i+" j= "+j);
                        }     
                        
                        if(outputs[outputs.length-1][1]>0.85){
                            System.out.println("\n\tFROM_2 i="+i+" j= "+j);
                        }                 
                 System.out.println("\n------------------------- " );
            }
        }
    }
    
    public  void answer(double[] askin) {

        na.calcIteration(askin);
//        na.getOutputs();
    }    

    public void setMainArray(double[][] mainArray) {
        this.mainArray = mainArray;
    }
    
    
}

