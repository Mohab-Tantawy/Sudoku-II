package com.company;
import java.io.*;


public class Grid {



    public static int[][] getGrid(String filename) {
        int[][] grid= new int[9][9];
        try(BufferedReader br= new BufferedReader(new FileReader(filename))){
            String line;
            int row=0;
            while(((line= br.readLine() )!=null)){
                String[] numbers= line.split(",");
                for(int i=0;i<9;i++){
                    grid[row][i]=Integer.parseInt(numbers[i]);
                }
                row++;
            }

        }catch (IOException e){
            System.out.println("File Does Not Exist!");
            return new int[9][9];
        }
        return grid;
    }

}
