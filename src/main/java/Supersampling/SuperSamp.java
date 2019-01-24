/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Supersampling;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mike
 */

//Суперсемплинг для уменьшения изображения попиксельно
//Его суть в разбиении исходного изображения по сетке пикселей конечного и складывании всех исходных пикселей, 
//приходящихся на каждый пиксель конечного в соответствии с площадью, попавшей под конечный пиксель (вес).        
public class SuperSamp {

    double[][] oldArray;// Массив ИЗ которого происходит преобразование (старый)
    double[][] newArray;// Массив В который происходит преобразование (новый)
    double xRate;// Соотношение преобразования по вертикали
    double yRate;// Соотношение преобразования по горизонтали
    int xOldArr = 0;// Номер колонки (для ячейки) из старого массива для которой совершаем преобразование
    int yOldArr = 0;// Номер строки (для ячейки) из старого массива для которой совершаем преобразование
    
    List <double[]> resYCells = new ArrayList();// Массив номеров по горизонтали ячеек (0 элемент массива) и соответсвующего ей веса и из старого массива для одной ячейки нового массива
    List <double[]> resXCells = new ArrayList();// Массив номеров по вертикали ячеек (0 элемент массива) и соответсвующего ей веса и из старого массива для одной ячейки нового массива
   

    public static void main(String[] args) {

//        SuperSamp susa = new SuperSamp(new double[][]{{0,1,2},{3,4,5},{6,7,8}},
//                new double[2][2]);
//
//SuperSamp susa = new SuperSamp(new double[][]{{1,1,0},{1,1,0},{1,1,0}},
//                new double[2][2]);
        


SuperSamp susa = new SuperSamp(new double[][]{{0.2, 0.7, 0.3}, 
                                              {0.3, 0.9, 0.8}, 
                                              {0.7, 0.6, 0.7}},
                new double[2][2]);
    }

    public SuperSamp(double[][] oldArray, double[][] newArray) {
        
        this.oldArray = oldArray;
        this.newArray = newArray;

//        Вычисляем соотношения по вертикали и горизонтали
        xRate = (double)oldArray[0].length / (double)newArray[0].length;
        
        yRate = (double)oldArray.length / (double)newArray.length;
        
        sampling();
    }

    
    public void sampling(){
         
        double ceilVal = 0;// Суммарное значение всех ячеек старого массива, которые соответствуют ячейке в новом, перемноженных на попавшую площадь
        double totalKoeff = 0;// Суммарное значение всех попавших площадей старого массива, которые соответствуют ячейке в новом
        double currXRate = 0;// Сколько осталось распределить из отношения по горизонтали (для одной новой ячейки)
        double currYRate = 0;// Сколько осталось распределить из отношения по вертикали (для одной новой ячейки)
        double cyr = 0;// Текущий "вес" площади старой ячейки по вертикали
        double cxr = 0;// Текущий "вес" площади старой ячейки по горизонтали
        
        // По строкам нового массива
        for( int i=0; i<newArray.length; i++){
            
            resYCells.clear();// Очищаем список старых ячеек для новой ячейки по вертикали
            
//      Если от предыдущей новой ячеки остался кусок соотношения, то его припишем к предыдущей ячейке (по вертикали)      
            if(currYRate != 0){
                cyr = 1+currYRate;// Т.к. оставшийся кусок приходит отрицательным, то вычисляем его такм образом.
                System.out.println(" cyr1= "+cyr+" yOldArr= "+yOldArr);
                resYCells.add(new double[] {yOldArr-1, cyr});// В список старых ячеек по вертикали добавляем предыдущую ячейку и ее попавшая площадь
            }
            
            currYRate = yRate+currYRate;// Присваиваем распределяемому соотношению по вертикали общего значения и уменьшаем его на значение оставшегося от предыдущей ячейки площади

//      Пока есть распределяемое соотношение по вертикали
            while (currYRate >0){
//      Если распределяемое соотношение больше или равно 1, тогда попавшая площадь старой ячейки будет 1                
                cyr = (currYRate>=1)?1:currYRate;
                System.out.println(" cyr= "+cyr+" yOldArr= "+yOldArr);                
                resYCells.add(new double[] {yOldArr, cyr});// Номер строки старого массива и его попавшая площадь
                currYRate--;// Убираем 1 из распределяемого соотношения
                yOldArr++;// Следующий ряд старого массива
            }
            
            xOldArr = 0;// Обнуляем значение колонки строго массива
            // По колонкам нового массива
            for( int j=0; j<newArray[i].length; j++){
                
                resXCells.clear();// Очищаем список старых ячеек для новой ячейки по горизонтали
//      Если от предыдущей новой ячеки остался кусок соотношения, то его припишем к предыдущей ячейке (по горизонтали)                     
                if(currXRate != 0){
                    cxr = 1+currXRate;// Т.к. оставшийся кусок приходит отрицательным, то вычисляем его такм образом.
                    System.out.print(" cxr1= "+cxr+" xOldArr= "+xOldArr);
                    resXCells.add(new double[]{xOldArr-1,cxr});// В список старых ячеек по горизонтали добавляем предыдущую ячейку и ее попавшая площадь 
                }
                
                currXRate = xRate+currXRate;// Присваиваем распределяемому соотношению по горизонтали общего значения и уменьшаем его на значение оставшегося от предыдущей ячейки площади
                
//      Пока есть распределяемое соотношение по горизонтали                
                while (currXRate >0){
//      Если распределяемое соотношение больше или равно 1, тогда попавшая площадь старой ячейки будет 1  
                    cxr = (currXRate>=1)?1:currXRate;
                    System.out.print(" cxr= "+cxr+" xOldArr= "+xOldArr);
                    resXCells.add(new double[]{xOldArr,cxr});// Номер колонки старого массива и его попавшая площадь
                    currXRate--;// Убираем 1 из распределяемого соотношения
                    xOldArr++;// Следующая колонка старого массива
                }
                    
                    System.out.println("\n i= "+i+" j= "+j);
                    
                    totalKoeff = 0;
                    ceilVal = 0;
//      По всем выявленным ячейкам старого массива для текущей ячейки нового массива                    
                    for ( double[] yy : resYCells){
                        for ( double[] xx : resXCells){
                            
                            totalKoeff += yy[1]*xx[1];// Суммируем произведения попавших площадей по вертикали и горизонтали для выявленных ячеек
                            
                            ceilVal += yy[1]*xx[1] * oldArray[(int)yy[0]][(int)xx[0]];// Суммируем произведения попавших площадей на значения ячеек
                            
                            System.out.println("\t y= "+yy[0]+" yy= "+yy[1]+" x= "+xx[0]+" xx= "+xx[1]+" valOld= "+oldArray[(int)yy[0]][(int)xx[0]]);
                        }
                    }
//      Вычисляем значение новых ячеек                    
                    newArray[i][j] = ceilVal / totalKoeff;
                    System.out.println("\t val= "+newArray[i][j]);
            }
        }
    }

    public double[][] getNewArray() {
        return newArray;
    }

    
}
