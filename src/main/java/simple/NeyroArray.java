/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simple;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Random;

/**
 *
 * @author mike
 */
public class NeyroArray {
//    Входные значения конструкторы
//    Количество слоев всего

    private int layers;

//    Массив слоев с количеством элементов в них
    private int[] ls;

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

    private boolean withBias;
    
    private double[] sigmaarr ;

    public NeyroArray(int[] ls, boolean withBias,double speed,double moment) {
        this.layers = ls.length;
        this.speed = speed;
        this.moment = moment;
        this.withBias = withBias;
        this.ls = new int[ls.length];// Сколько слоев в сети
// Если необходим биас - тогда добавляем его  в каждый слой кроме выходного        

        for (int i = 0; i < ls.length; i++) {
            if (withBias && i < (ls.length - 1)) {// Если с биасом и слой не выходной добавляем биас
                this.ls[i] = ls[i]+1;
            } else {
                this.ls[i] = ls[i];
            }
        }

//  Массив весов         
//  Количество слоев      
        weights = new double[layers - 1][][];
//  По всем слоям кроме выходного (у него отстутствуют веса)      
        for (int i = 0; i < (layers - 1); i++) {
//  Количество нейронов в слое плюс один биас            
            weights[i] = new double[this.ls[i]][];
//  Количество весов у нейрона равно количеству нейронов в следующем слое            

            for (int j = 0; j < weights[i].length; j++) {
// Если присутствуют биасы и следующий слой не выходной - тогда количество весов в нейроне будет меньше
// числа нейронов в следующем слое на 1 т.к. к биасу весы от нашего нейрона не не идут,
// но в выходном слое биаса не бывает
                if (withBias && i < (layers - 2)) {
                    weights[i][j] = new double[this.ls[i + 1] - 1];
                } else {
                    weights[i][j] = new double[this.ls[i + 1]];
                }
            }

        }
        
        activateWeights();

//  Массив изменений весов в предыдущем цикле обучения
//  Количество слоев      
        deltaWeights = new double[layers - 1][][];
//  По всем слоям кроме выходного (у него отстутствуют веса)      
        for (int i = 0; i < (layers - 1); i++) {
//  Количество нейронов в слое плюс один биас            
            deltaWeights[i] = new double[this.ls[i]][];
//  Количество весов у нейрона равно количеству нейронов в следующем слое            

            for (int j = 0; j < deltaWeights[i].length; j++) {
                deltaWeights[i][j] = new double[this.ls[i + 1]];
            }
        }

//  Массив выходных значений нейронов              
//  Количество слоев      
        outputs = new double[layers][];
//  По всем слоям     
        for (int i = 0; i < layers; i++) {

// Количество нейронов в слое плюс один биас 
            outputs[i] = new double[this.ls[i]];
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
// Количество нейронов в слое плюс один биас 
            sigma[i] = new double[this.ls[i]];
        }

//  Массив градиентов нейронов
        grads = new double[layers - 1][][];
//  По всем слоям кроме выходного (у него отстутствуют веса)      
        for (int i = 0; i < (layers - 1); i++) {
//  Количество нейронов в слое плюс один биас            
            grads[i] = new double[this.ls[i]][];
//  Количество весов у нейрона равно количеству нейронов в следующем слое            

            for (int j = 0; j < grads[i].length; j++) {
                grads[i][j] = new double[this.ls[i + 1]];
            }
        }
// Загрузка массива заранее рассчитанных значений логистической функции, чтобы не рассчитывать в коде
//        SigmaArr sa = new SigmaArr();
//        sigmaarr = sa.sigmaarr;        
        
    }
//  Рассчет значений

    public void calcIteration(double[] quests) {

//  По всем слоям вперед    
        for (int i = 0; i < layers; i++) {
// Входной слой
            if (i == 0) {
//                for (int j = 0; j < (ls[i]); j++) {
                for (int j = 0; j < quests.length; j++) {

                    outputs[i][j] = quests[j];

                }

            } else {// Спрятанные слои и выходной слой

                for (int j = 0; j < (ls[i]); j++) {// По всем нейронам включая биас
// Если это биас, то на выходе просто единица
                    if (withBias && i < (layers - 1) && j == (ls[i] - 1)) {

                        outputs[i][j] = 1;

                    } else {

                        outputs[i][j] = 0;

                        for (int j1 = 0; j1 < ls[i - 1]; j1++) {
//                            System.out.print("i= " + i + " j= " + j + " j1= " + j1);
//                            System.out.print(" iput= " + outputs[i - 1][j1]);
//                            System.out.println(" weights= " + weights[i - 1][j1][j]);
                            outputs[i][j] += outputs[i - 1][j1] * weights[i - 1][j1][j];
                        }

                        outputs[i][j] = 1 / (1 + Math.exp(-1 * (outputs[i][j])));
// ----------------------- Если мы используем заранее сосчитанные значения функции сигма из класса LoadData, тогда раскомментировать,
// а предыдущую строку расомментировать
//System.out.println("outputs[i][j]= " + outputs[i][j]);
//                        int x = (int)( outputs[i][j]*10000)+10000;
//                        if(x<0 ) {
//                            outputs[i][j] = 0;
//                        }else if( x>20000){
//                            outputs[i][j] = 1;
//                        }else{
//                         outputs[i][j] =sigmaarr[x];
//                        }
//---------------------------------------------------------------------------------------
                    }

                    System.out.println("outputs[i][j]= " + outputs[i][j]);

                }

            }

        }
    }

// Обучение методом градиентного спуска    
    public double learnCalcDesending(double[] answers) {

//    Ошибка
        double iterErr = 0.0;

//  Получим ошибку за итерацию и сигму для выходного слоя        
        for (int j = 0; j < ls[layers - 1]; j++) {
//  Квадратичная ошибка          
            iterErr += Math.pow((answers[j] - outputs[layers - 1][j]), 2);
// Сигма для нейронов выходного слоя
// производня от логистической функции где аргумент выходное значения умножить на ошибку значения
            sigma[layers - 1][j] = (1 - outputs[layers - 1][j]) * outputs[layers - 1][j] * (answers[j] - outputs[layers - 1][j]);
        }

//  По всем слоям обратно   - метод градиентного спуска  
        for (int i = (layers - 2); i >= 0; i--) {

            for (int j = 0; j < (ls[i]); j++) {// По всем нейронам рассчитываемого слоя включая биас

                if (i <= (layers - 2) && i > 0) {// Если следующий слой не выходной и текущий слой не входной - тогда рассчитаем сигмы для него

                    double sigmaAweight = 0;

                    // По всем нейронам следующего (верхнего) слоя (без биаса)
//  перемножаем все веса нейрона для нейронов верхнего слоя на сигму нейрона верхнего слоя                  
                    for (int j1 = 0; j1 < ls[i + 1]; j1++) {

                        sigmaAweight += weights[i][j][j1] * sigma[i + 1][j1];
                    }
// Умножаем производную от логистической функции с аргументом равным выходному значению  на 
// сумму весов для нейрона следующего(верхнего) слоя умноженную на сигму следующего(верхнего) слоя
                    sigma[i][j] = (1 - outputs[i][j]) * outputs[i][j] * sigmaAweight;
                }
                // По всем нейронам  слоя (без биаса)
                for (int j1 = 0; j1 < ls[i + 1]; j1++) {
                    
//  Получаем градиент для текущего нейрона умножением выхода нейрона на сигму нейрона следующего слоя для которого предназначен этот вес                  
                    grads[i][j][j1] = outputs[i][j] * sigma[i + 1][j1];
//  Получим значения изменений весов для весов текущего слоя 
// умножаем скорость на градиент плюс умножение предыдущего значения изменения веса на момент
                    deltaWeights[i][j][j1] = speed * grads[i][j][j1] + moment * deltaWeights[i][j][j1];

                }

            }

        }

//  К весам нейронов добавляем рассчитанное изменение весов      
        for (int i = 0; i < (layers - 1); i++) {
            for (int j = 0; j < weights[i].length; j++) {
                for (int k = 0; k < weights[i][j].length; k++) {
                    weights[i][j][k] += deltaWeights[i][j][k];
                }
            }
        }

        System.out.println("iterErr " + (iterErr / answers.length));

        return iterErr / answers.length;
    }

    public double oneIteration(double[][] example, double[][] answer) {

        double qErr = 0;

        for (int i = 0; i < example.length; i++) {

            calcIteration(example[i]);

            qErr += learnCalcDesending(answer[i]);

        }

        qErr = qErr / example.length;

        System.out.println("qErr= " + qErr);

        return qErr;
    }

    public double[][][] getWeights() {
        for (int i = 0; i < (layers - 1); i++) {
            for (int j = 0; j < weights[i].length; j++) {
                for (int k = 0; k < weights[i][j].length; k++) {
                    System.out.print("i= " + i + " j= " + j + " k= " + k + " weight= ");
                    System.out.printf("%f", weights[i][j][k]);
                    System.out.println("");
                }
            }
        }

        return weights;
    }

    public double[][][] getDeltaWeights() {
        return deltaWeights;
    }

    public double[][] getOutputs() {

        for (int i = 0; i < layers; i++) {
            System.out.println("\n layer= " + i);
            for (int j = 0; j < outputs[i].length; j++) {
                System.out.print(" outputs " + j + " = ");
                System.out.printf("%f", outputs[i][j]);
            }
        }

        return outputs;
    }

    public void setWeights(double[][][] weights) {
        this.weights = weights;
        System.out.println("");
    }

    public void activateWeights(){
        for (int i = 0; i < (layers - 1); i++) {
            for (int j = 0; j < weights[i].length; j++) {
                for (int k = 0; k < weights[i][j].length; k++) {
                    weights[i][j][k] = Math.random()-0.5;
//                    System.out.print("i= " + i + " j= " + j + " k= " + k + " weight= ");
//                    System.out.printf("%f", weights[i][j][k]);
//                    System.out.println("");
                }
            }
        }        
    }
}
