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
public class NeyroArray {
//    Входные значения конструкторы
//    Количество слоев всего

    private int layers;
//    Количество нейронов во входном слое и скрытых слоях (без биаса)
    private int inputN;
//    Количество нейронов в выходном слое
    private int outputN;

//    Массив весов 
//    i-слой (выходной слой не учитывается)
//    j-нейрон (плюс 1 биас)
//    k-номер нейрона в следующем слое (куда обращен вес)
    private double[][][] weights;

//    Массив изменений весов в предыдущем цикле обучения
//    i-слой (выходной слой не учитывается)
//    j-нейрон (плюс 1 биас)
//    k-номер нейрона в следующем слое (куда обращен вес)
    private double[][][] deltaWeights;

//    Массив выходных значений нейронов
//    j-нейрон (плюс 1 биас)
    private double[][] outputs;

//    Массив сигм нейронов
//    i-слой (во ВХодной слой пишем 0 для удобства)
//    j-нейрон 
    private double[][] sigma;

//    Массив градиентов нейронов
//    i-слой (во ВЫХодной слой не учитывается)
//    j-нейрон  (плюс 1 биас)
//    k-номер нейрона в следующем слое (куда обращен вес)
    private double[][][] grads;
    
//    Скорость обучения
    private double speed = 0.7;
//    Момент
    private double moment = 0.3;
    
//    Нужен ли биас
    private int currBias;


    public NeyroArray(int layers, int inputN, int outputN, boolean withBias) {
        this.layers = layers;
        this.inputN = inputN;
        this.outputN = outputN;
        
        currBias = (withBias)? 1:0;// Прибавляем один биас в каждом слое, если не нужен биас, то не прибавляем
        
        

//  Массив весов         
//  Количество слоев      
        weights = new double[layers-1][][];
//  По всем слоям кроме выходного (у него отстутствуют веса)      
        for (int i = 0; i < (layers - 1); i++) {
//  Количество нейронов в слое плюс один биас            
            weights[i] = new double[inputN + currBias][];
//  Количество весов у нейрона равно количеству нейронов в следующем слое            
            if (i < (layers - 2)) {
                for (int j = 0; j < weights[i].length; j++) {
                    weights[i][j] = new double[inputN];
                }
            } else {
                for (int j = 0; j < weights[i].length; j++) {
                    weights[i][j] = new double[outputN];
                }
            }
        }

//  Массив изменений весов в предыдущем цикле обучения
//  Количество слоев      
        deltaWeights = new double[layers-1][][];
//  По всем слоям кроме выходного (у него отстутствуют веса)      
        for (int i = 0; i < (layers - 1); i++) {
//  Количество нейронов в слое плюс один биас            
            deltaWeights[i] = new double[inputN + currBias][];
//  Количество весов у нейрона равно количеству нейронов в следующем слое            
            if (i < (layers - 2)) {
                for (int j = 0; j < deltaWeights[i].length; j++) {
                    deltaWeights[i][j] = new double[inputN];
                }
            } else {
                for (int j = 0; j < deltaWeights[i].length; j++) {
                    deltaWeights[i][j] = new double[outputN];
                }
            }
        }

//  Массив выходных значений нейронов              
//  Количество слоев      
        outputs = new double[layers][];
//  По всем слоям     
        for (int i = 0; i < layers; i++) {

            if (i < (layers - 1)) {//Если это входной или внутренний слой - количество нейронов в слое плюс один биас 

                outputs[i] = new double[inputN + currBias];
            } else {// Если это выходной слой - количество нейронов в выходном слое
                outputs[i] = new double[outputN];
            }
// Расставляем значения всем нейронам 1 сразу, чтобы потом не заморачивться с биасами            
            for (int j = 0; j < outputs[i].length; j++) {
                outputs[i][j] = 1;
            }
        }

//  Массив сигм нейронов      
//  Количество слоев      
        sigma = new double[layers][];
//  По всем слоям     
        for (int i = 0; i < layers; i++) {

            if (i < (layers - 1)) {//Если это входной или внутренний слой - количество нейронов в слое  плюс один биас 

                sigma[i] = new double[inputN+currBias];
            } else {// Если это выходной слой - количество нейронов в выходном слое
                sigma[i] = new double[outputN];
            }
        }

//  Массив градиентов нейронов
        grads = new double[layers-1][][];
//  По всем слоям кроме выходного (у него отстутствуют веса)      
        for (int i = 0; i < (layers - 1); i++) {
//  Количество нейронов в слое плюс один биас            
            grads[i] = new double[inputN+currBias ][];
//  Количество весов у нейрона равно количеству нейронов в следующем слое            
            if (i < (layers - 2)) {
                for (int j = 0; j < grads[i].length; j++) {
                    grads[i][j] = new double[inputN];
                }
            } else {
                for (int j = 0; j < grads[i].length; j++) {
                    grads[i][j] = new double[outputN];
                }
            }
        }

    }

    public double calcIteration(double[] quests, double[] answers) {

//    Ошибка
        double iterErr = 0.0;

//  По всем слоям вперед    
        for (int i = 0; i < layers; i++) {// Входной слой

            if (i == 0) {
                for (int j = 0; j < (inputN+currBias); j++) {
                    
                        outputs[i][j] = quests[j];

                }

            } else if (i < (layers - 1)) {// Спрятанные слои

                for (int j = 0; j < (inputN+currBias); j++) {// По всем нейронам включая биас

                    outputs[i][j] = 0;

//                    if (j < (inputN+currBias)) {// Если это не биас

                        for (int j1 = 0; j1 < inputN; j1++) {

                            outputs[i][j] += outputs[i - 1][j1] * weights[i - 1][j1][j];
                        }

                        outputs[i][j] = 1 / (1 + Math.exp(-1 * (outputs[i][j])));

//                    } else {// Если это биас
//                        outputs[i][j] = 1;
//                    }
                }

            } else {// Выходной слой

                for (int j = 0; j < outputN; j++) {

                    outputs[i][j] = 0;

                    for (int j1 = 0; j1 < outputN; j1++) {

                        outputs[i][j] += outputs[i - 1][j1] * weights[i - 1][j1][j];
                    }

                    outputs[i][j] = 1 / (1 + Math.exp(-1 * (outputs[i][j])));

                    iterErr += answers[j] - outputs[i][j];

                    sigma[i][j] = (1 - outputs[i][j]) * outputs[i][j] * (answers[j] - outputs[i][j]);

                }
            }

        }

//  По всем слоям обратно   - метод градиентного спуска  
        for (int i = (layers - 2); i >= 0; i--) {

            for (int j = 0; j <(inputN+currBias); j++) {// По всем нейронам рассчитываемого слоя включая биас
                
                int nextNeuroQant = (inputN+currBias);// Количество нейронов в следующем слое
                
                // Если следующий слой выходной тогда количество нейронов в следующем слое такое
                if (i == (layers - 2)) {
                   nextNeuroQant =  outputN;
                   
                }
                
                if (i <= (layers - 2) && i > 0){// Если следующий слой не выходной и текущий слой не входной - тогда рассчитаем сигмы для него
                    
                    
                    double sigmaAweight = 0;
                    
                    // По всем нейронам следующего слоя (без биаса)
                    for (int j1 = 0; j1 < nextNeuroQant; j1++) {

                        System.out.println("i= "+i+" j= "+j+" j1= "+j1);
                        sigmaAweight += weights[i][j][j1]*sigma[i+1][j1];
                    }
                    
                    sigma[i][j] = (1 - outputs[i][j]) * outputs[i][j] * sigmaAweight;
                }
                // По всем нейронам следующего слоя (без биаса)
                for (int j1 = 0; j1 < nextNeuroQant; j1++) {

                    grads[i][j][j1] = outputs[i][j]*sigma[i+1][j1];
                    deltaWeights[i][j][j1] = speed*grads[i][j][j1] + moment*deltaWeights[i][j][j1];
                }
                
            }

        }

        for (int i=0; i<(layers-1); i++){
            for(int j=0; j<weights[i].length; j++){
                for(int k=0; k<weights[i][j].length; k++){
                    weights[i][j][k] += deltaWeights[i][j][k];
                }
            }
        }
        
        
        System.out.println("iterErr " + iterErr);

        return iterErr;
    }

    public double[][][] getWeights() {
        return weights;
    }

    public double[][][] getDeltaWeights() {
        return deltaWeights;
    }

    public double[][] getOutputs() {
        return outputs;
    }

    public void setWeights(double[][][] weights) {
        this.weights = weights;
        System.out.println("");
    }

    
    
}
