/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simple;

/**
 *
 * @author mike
 */
public class SigmaArr {
    
    public double[] sigmaarr = new double[20001];

    public SigmaArr() {
        
        double x = -1.0;
        
        for (int i = 0 ; i < 20001; i++){
            
            sigmaarr[i] = 1 / (1 + Math.exp(-1 * (x)));
            
            x += 0.0001;
        }
    }
    
    
    
}
