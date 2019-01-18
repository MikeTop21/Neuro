/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simple;

import java.io.Serializable;

/**
 *
 * @author mike
 */
public class NetEntity implements Serializable{
    
    
    public int[] netDim;
    public boolean withBias;
    public double speed;
    public double moment;
    public double[][][] weights;

    public NetEntity(int[] netDim, boolean withBias, double speed, double moment, double[][][] weights) {
        this.netDim = netDim;
        this.withBias = withBias;
        this.speed = speed;
        this.moment = moment;
        this.weights = weights;
    }

    
}
