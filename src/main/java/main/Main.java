package main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import simple.Layer;
import simple.LoadData;
import simple.NetEntity;
import simple.NeyroArray;
import simple.SigmaArr;

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
        {0, 0, 1},
        {1, 0, 1},
        {0, 1, 1},
        {1, 1, 1}
    };

    static double tableOfAnswers[] = {0, 0, 0, 1};

    public static void main(String[] args) {

//        SigmaArr sa = new SigmaArr();
//        double[] sigmaarr = sa.sigmaarr;
//        System.out.println("Sigma "+sigmaarr[(int)(0.2367*10000)+10000]);
//        if(true)return;   
        
        train("neuronet.out", 0.0025);
//        answer("neuronet.out",new double[]{1,0,1,0});
      
    if(true)return;          
        NeyroArray    na = new NeyroArray(new int[]{2,2,1},false,0.7,0.3);
//            NeyroArray    na = new NeyroArray(new int[]{2,2,2},true,0.7,0.3);
//NeyroArray    na = new NeyroArray(new int[]{2,1},true,0.7,0.3);
//         double[][][] weights = {
//            {{0.5,1,1},{0.5,1,1},{0.5,1,1}},
//            {{0.5,1},{0.5,1},{0.5,1}} 
//        };     
//         double[][][] weights = {
//            {{0.5,1},{0.5,1}},
//            {{0.5},{0.5}} 
//        };  
//        double[][][] weights = {
//            {{0.45,0.78,1},{-0.12,0.13,1},{1,1,1}},
//            {{1.5},{-2.3},{1}}
//        };
//        double[][][] weights = {
//            {{0.45,0.78},{-0.12,0.13}},
//            {{1.5},{-2.3}}
//        };
//        na.setWeights(weights);        
//        na.calcIteration(new double[] {1,0});
//        for (int i=0; i<1000; i++){
//          na.oneIteration(new double[][]{{0,0},{0,1},{1,0},{1,1}}, new double[][]{{1},{0},{0},{1}});
//        }
        double qErr = 1;
        int i = 0;
        while (qErr > 0.005) {
//            qErr = na.oneIteration(new double[][]{{0,0},{0,1},{1,1},{1,0}}, new double[][]{{0,1},{0,0},{1,1},{0,0}});
//            qErr = na.oneIteration(new double[][]{{0,0},{0,1},{1,0},{1,1}}, new double[][]{{1},{0},{0},{1}});
//              qErr = na.oneIteration(new double[][]{{0,0},{0,1},{1,0},{1,1}}, new double[][]{{0},{0},{0},{1}});            
//                qErr = na.oneIteration(ld.getInputData(), ld.getOutputData());
            System.out.println("iteration= " + i++);
        }

//            weights = new double[][][] {
//            {{0.958682,7.877922},
//            {0.958012,7.854763}},
//            {{33.790297},{-27.052415}}
//        };
//            weights = new double[][][] {
//            {
//                {6.087521,5.096861,-2.112090},
//                {-3.708194,4.746403,5.686168},
//                {1.107325,-0.623098,-0.343721}
//            },
//           
//            {{5.764597},{-8.808167},{5.763135}}
//        };
//            
//        na.setWeights(weights);     
//            
//        na.calcIteration(new double[]{1, 0});

        na.getWeights();
        na.getOutputs();

//        layers.add(new Layer(3, 1));
//        layers.add(new Layer(1, 1));
//     
//        train();
        System.exit(0);
    }

    static void train(String fileName,double qErrMax) {

//        NetEntity ne = new NetEntity(new int[]{4,4, 1}, true, 0.7, 0.3, null);
        NetEntity ne = new NetEntity(new int[]{4,4,2}, true, 0.7, 0.3, null);

        writeNetSerial(ne,fileName);

        ne = readNetSerial(fileName);

        LoadData ld = new LoadData("neurodata.txt", null);
        ld.createDataArr();

        NeyroArray na = new NeyroArray(ne.netDim, ne.withBias, ne.speed, ne.moment);

        double qErr = 1;
        int i = 0;
        while (qErr > qErrMax) {
            qErr = na.oneIteration(ld.getInputData(), ld.getOutputData());
            System.out.println("iteration= " + i++);
        }

        ne.weights = na.getWeights();
        writeNetSerial(ne,fileName);
        na.getOutputs();
    }

    public static void answer(String fileName, double[] askin) {

        NetEntity ne = readNetSerial(fileName);
        NeyroArray na = new NeyroArray(ne.netDim, ne.withBias, ne.speed, ne.moment);
        na.setWeights(ne.weights);
        na.calcIteration(askin);
        na.getOutputs();

    }

    public static void writeNetSerial(NetEntity ne, String fileName) {

        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(ne);
            oos.flush();
            oos.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static NetEntity readNetSerial(String fileName) {

        NetEntity ne = null;

        try {
            FileInputStream fis = new FileInputStream(fileName);
            ObjectInputStream oin = new ObjectInputStream(fis);
            ne = (NetEntity) oin.readObject();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        return ne;
    }
}
