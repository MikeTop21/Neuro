/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simple;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mike
 */
public class LoadData {
    
    private String dataFileStr;
    private String netFileStr;
    private File dataFile;
    private File netFile;
    
    private double[][] inputData;
    private double[][] outputData;

    public LoadData(String dataFileStr, String netFileStr) {
        
        this.dataFileStr = dataFileStr;
        this.netFileStr = netFileStr;
    }
    
    
    
    public void createDataArr (){
        
        dataFile = new File(dataFileStr);
        
        List<double[]> arrInputList = new ArrayList();
        List<double[]> arrOutputList = new ArrayList();
        
        boolean goInput = false;
        boolean goOutput = false;
        
        try {
            BufferedReader br = new BufferedReader(new FileReader(dataFile));
            
                String line;
                while ((line = br.readLine()) != null) {
                    
                    
                    if(goInput){
                        
                        if(line.length()==0 || line.equals("OUTPUT")){
                            
                           goInput = false;
                           
                        }else{
                            
                            String[] starr = line.split(";");
                            double[] darr = new double[starr.length];
                            for(int i=0; i<starr.length; i++){
                                darr[i] = Double.parseDouble(starr[i]);
                            }
                            arrInputList.add(darr);
                            System.out.println(line);
                            
                        }
                        
                    }
                    
                    if(goOutput){
                        
                        if(line.length()==0 ){
                            
                           goOutput = false;
                           
                        }else{
                            
                            String[] starr = line.split(";");
                            double[] darr = new double[starr.length];
                            for(int i=0; i<starr.length; i++){
                                darr[i] = Double.parseDouble(starr[i]);
                            }
                            arrOutputList.add(darr);
                            
                            System.out.println(line);
                            
                        }
                        
                    }     
                    

                    if(line.equals("INPUT")){
                        goInput = true;
                    }
                     
                    if(line.equals("OUTPUT")){
                        goOutput = true;
                    }                    
                }     
                
            inputData = new double [arrInputList.size()][];   
            
            for(int i=0; i<arrInputList.size(); i++){
                inputData[i] = arrInputList.get(i);
            }
 
    
            outputData = new double [arrInputList.size()][];   
            
            for(int i=0; i<arrOutputList.size(); i++){
                outputData[i] = arrOutputList.get(i);
            }            
            System.err.println("");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(LoadData.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(LoadData.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    public double[][] getInputData() {
        return inputData;
    }

    public double[][] getOutputData() {
        return outputData;
    }
    
    
}
