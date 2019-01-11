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
public class Layer {
    
    private int inputQuant;
    private int outputQuant;
    
    private double [] entries;
    
    private double [] answers;
    
    private double [] diff;
    private double [] currSigma;
    private double [] err;
    private double [][] grad;
    
    private double [][] weights;
    private double [][] deltaWeights;    
    private double [] outputs;

    public Layer(int inputQuant, int outputQuant) {
        this.inputQuant = inputQuant;
        this.outputQuant = outputQuant;
        
        this.entries = new double[inputQuant];
        this.diff = new double[outputQuant];
        this.err = new double[outputQuant];
        this.currSigma = new double[outputQuant];
        this.grad = new double[inputQuant][outputQuant];
        this.weights = new double[inputQuant][outputQuant];
        this.deltaWeights = new double[inputQuant][outputQuant];
        this.outputs = new double[outputQuant];
        
        createWeights();
    }

    public void setEntries(double[] entries) {
        this.entries = entries;
    }
    
    public void setAnswers(double[] answers) {
        this.answers = answers;
    }    
    
    private void createWeights(){
        
        for( int i=0; i<outputQuant; i++){
            
            for( int j=0; j<inputQuant; j++){
                
                weights[j][i] = 0.0;
            }
        }        
    }
    
    public void calcOuput(){
        
        for( int i=0; i<outputQuant; i++){
            
            outputs[i] = 0;
            
            for( int j=0; j<inputQuant-1; j++){
                
                outputs[i] += entries[j]*weights[j][i];
            }
            
            outputs[i] =1/(1+Math.exp(-1*(outputs[i])));
        }
    }

    public double[] getOutputs() {
        return outputs;
    }
    
    public void calcDiff(){
    
        for( int i=0; i<outputQuant; i++){
            
            diff[i] = (1-outputs[i])*outputs[i];
        }    
    }
    
    public void calcErr(){
    
        for( int i=0; i<outputQuant; i++){
            err[i] = answers[i] - outputs[i];
        }    
    }    
    
    public void calcOuterSigma(){
        
        for( int i=0; i<outputQuant; i++){
            
            currSigma[i] = diff[i]*err[i];
        }        
    }

    public double[] getCurrSigma() {
        return currSigma;
    }
    
    public void calcGrad(double[] nextSigma){
 
        for( int i=0; i<outputQuant; i++){
            
            for( int j=0; j<inputQuant; j++){
                
                grad[j][i] = entries[j]*nextSigma[i];
                deltaWeights[j][i] = 0.7*grad[j][i] + 0.3*deltaWeights[j][i];
                weights[j][i] = weights[j][i] + deltaWeights[j][i];
            }
        }
    }

    public double[] getDiff() {
        return diff;
    }

    public double[][] getGrad() {
        return grad;
    }
    
    
}
