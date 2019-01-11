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
public class SimpleNeuro2 {
   
    static double enters[] = new double[3]; // создаём входы
    static double out; // храним выход сети
    static double[] weights = {0,0,0}; // весовые коэффициенты
    
    static double[] deltaweights = {0.0,0.0,0.0};
    static double[] grad = new double[3];
    
    static double tableOfLearn[][] = {
        {0,0,1,0},
        {1,0,1,0},
        {0,1,1,0},
        {1,1,1,1}
    };
    
        public static void summator(){
        out = 0; //обнуляем выход
        for ( int i = 0; i < enters.length; i++ ){
            out+=enters[i]*weights[i]; // вход * вес, суммируем.
            
        }    

        out = 1/(1+Math.exp(-1*(out)));
        
//        if ( out > 0.1 ) out=1; else out=0; // функция активации
//        System.out.print( " out1 = "+out);
        
    }    
        
        
        public static void train(){
        double gError = 0; // создаём счётчик ошибок
        int it = 0; // количество итераций
        do {
            gError = 0; // обнуляем счётчик
            it++; // увеличиваем на 1 итерации
            for ( int i = 0; i < tableOfLearn.length; i++ ){
                enters = java.util.Arrays.copyOf(tableOfLearn[i], 
                    tableOfLearn[i].length - 1); // копируем в входы обучающие входы
                summator(); // суммируем
                double error = tableOfLearn[i][3] - out; // получаем ошибку
                gError+=Math.pow(error,2.0); // суммируем ошибку в модуле
//                gError+=Math.abs(error); 
                double sigma0 = (1-out)*out * error;
                
                System.out.println("I= "+i + " output = "+out+" sigma0 = "+sigma0+" error= "+error);
                for ( int j = 0; j < enters.length; j++ ){
                    
                    grad[j] = sigma0*enters[j];
                    deltaweights[j] = 0.7*grad[j]+0.3*deltaweights[j];
                    System.out.println("J="+j+" grad[j] = "+grad[j]+" deltaweights[j] = "+deltaweights[j]+" enters[j]= "+enters[j]);
                    weights[j]+=deltaweights[j];                   
                    
//                    weights[j]+=0.1*error*enters[j]; // старый вес + скорость * ошибку * i-ый вход
                }  
                
                 
            }
            
            System.out.println("gError=  "+gError+" it = "+it+" w0 = "+weights[0]+" w1 = "+weights[1]+" w2 = "+weights[2]);
        } while(gError/4> 0.001); // пока gError не равно 0, выполняем код
    }
        
    public static void main(String[] args) {
        
         train();
        for ( int p = 0; p < tableOfLearn.length; p++ ){
            enters = java.util.Arrays.copyOf(tableOfLearn[p], 
                tableOfLearn[p].length - 1);

   			summator();

   			System.out.println(out);
            }       
    }
     
}
