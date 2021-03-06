/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simple;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author mike
 */
public class NeyroArrayTest {
    
    public NeyroArrayTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @Before
    public void setUp() {
    }

    @org.junit.Test
    public void testInitialWeghts() {
        
        NeyroArray na = new NeyroArray(new int[]{2,1},true,0.7,0.3);
        double[][][] weights = na.getWeights();
        assertEquals(1,weights.length);
        
        assertEquals(3,weights[0].length); 
        
        assertEquals(1,weights[0][0].length);
        assertEquals(1,weights[0][1].length);
        assertEquals(1,weights[0][2].length);
        

        na = new NeyroArray(new int[]{2,2,2},true,0.7,0.3);
        weights = na.getWeights();
        assertEquals(2,weights.length);
        
        assertEquals(3,weights[0].length); 
        
        assertEquals(2,weights[0][0].length);
        assertEquals(2,weights[0][1].length);
        assertEquals(2,weights[0][2].length); 
        
        
        assertEquals(3,weights[1].length); 
        
        assertEquals(2,weights[1][0].length);
        assertEquals(2,weights[1][1].length);
        assertEquals(2,weights[1][2].length);
        
        na = new NeyroArray(new int[]{2,2,1},false,0.7,0.3);
        weights = na.getWeights();
        
        assertEquals(2,weights.length);
        
        assertEquals(2,weights[0].length); 
        
        assertEquals(2,weights[0][0].length);
        assertEquals(2,weights[0][1].length); 
        
        
        
        assertEquals(2,weights[1].length); 
        
        assertEquals(1,weights[1][0].length);
        assertEquals(1,weights[1][1].length);
        
        
        
        
    }
    
    @org.junit.Test
    public void testInitialDeltaWeghts() {
        
        NeyroArray na = new NeyroArray(new int[]{2,1},true,0.7,0.3);
        double[][][] weights = na.getDeltaWeights();
        assertEquals(1,weights.length);
        
        assertEquals(3,weights[0].length); 
        
        assertEquals(1,weights[0][0].length);
        assertEquals(1,weights[0][1].length);
        assertEquals(1,weights[0][2].length);
        

        na = new NeyroArray(new int[]{2,2,2},true,0.7,0.3);
        weights = na.getWeights();
        assertEquals(2,weights.length);
        
        assertEquals(3,weights[0].length); 
        
        assertEquals(2,weights[0][0].length);
        assertEquals(2,weights[0][1].length);
        assertEquals(2,weights[0][2].length); 
        
        
        assertEquals(3,weights[1].length); 
        
        assertEquals(2,weights[1][0].length);
        assertEquals(2,weights[1][1].length);
        assertEquals(2,weights[1][2].length);
        
        
        na = new NeyroArray(new int[]{2,2,2},false,0.7,0.3);
        weights = na.getWeights();
        assertEquals(2,weights.length);
        
        assertEquals(2,weights[0].length); 
        
        assertEquals(2,weights[0][0].length);
        assertEquals(2,weights[0][1].length);
        
        
        assertEquals(2,weights[1].length); 
        
        assertEquals(2,weights[1][0].length);
        assertEquals(2,weights[1][1].length);
    }
    
    @org.junit.Test
    public void testSetWeights(){
        
        NeyroArray na = new NeyroArray(new int[]{2,2,1},false,0.7,0.3);
        
        double[][][] weights = {
            {{0.45,0.78},{-0.12,0.13}},
            {{1.5,-2.3}}
        };
        
        na.setWeights(weights);
        
        weights = na.getWeights();
        
        assertEquals(0.45,weights[0][0][0],0.0); 
        assertEquals(0.13,weights[0][1][1],0.0); 
        assertEquals(-2.3,weights[1][0][1],0.0); 
        
    }
    
    
    @org.junit.Test
    public void testInitialOutputs() {
        
        NeyroArray na = new NeyroArray(new int[]{2,2,1},false,0.7,0.3);
        
        double[][][] weights = {
            {{0.45,0.78},{-0.12,0.13}},
             {{1.5},{-2.3}}
        };
        
        na.setWeights(weights);
        
        na.calcIteration(new double[] {0,0});
        
        weights = na.getWeights();
        
        assertEquals(0.4869720274831185,weights[0][0][0],0.1); 
        assertEquals(0.7286114498906873,weights[0][0][1],0.1); 
        assertEquals(-0.12,weights[0][1][0],0.1); 
        assertEquals(0.13,weights[0][1][1],0.1);  
        
        assertEquals(1.5633038058047803,weights[1][0][0],0.1); 
        assertEquals(-2.2289168491578506,weights[1][1][0],0.1);  
        
        double answer = na.learnCalcDesending( new double[] {0}); 
        assertEquals(0.161,answer,0.1);
        
        
    
        na = new NeyroArray(new int[]{2,1},true,0.7,0.3);
        
        double[][] outputs = na.getOutputs();
        assertEquals(2,outputs.length); 
        assertEquals(3,outputs[0].length); 
        assertEquals(1,outputs[1].length); 
//  Не забываем указывать так же вес биаса если он (биас) есть      
        weights = new double[][][] {
            {{1.0},{1.0},{1.0}}
        };
        
        na.setWeights(weights);
        
        na.calcIteration(new double[] {1,1});
        
        weights = na.getWeights();   
        assertEquals(1,weights.length); 
        assertEquals(3,weights[0].length);  
        
        assertEquals(1.0,weights[0][0][0],0.1); 
        assertEquals(1.0,weights[0][1][0],0.1); 
        assertEquals(1.0,weights[0][2][0],0.1); 
        
        answer = na.learnCalcDesending( new double[] {1}); 
        assertEquals(0.0022492134,answer,0.1);        
    }     
}
