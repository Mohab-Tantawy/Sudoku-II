package com.company;

public class runSingleThread implements SudokuValidator {

    private int validRows = 0 ;
    private int validColumns = 0 ;
    private int validBoxes = 0 ;
    private final int TOTAL_CHECKS = 9 ;

    @Override
    public void validate(int[][] grid){
        System.out.println("\n Mode 1: SINGLE THREADED VALIDATION");
        System.out.println("=".repeat(50));

        Check check = new Check(grid);
        validRows = 0;
        validColumns = 0;
        validBoxes = 0;

        System.out.println();
        for (int i = 1; i <= TOTAL_CHECKS; i++) {
            if (check.checkRow(i)) {
                validRows++;
                //System.out.println("Row " + i + ":  Valid\n");
            } else {
                //System.out.println("Row " + i + ": Invalid\n");
                check.printRepeatedinRow(i);
                System.out.println();
            }}
        System.out.println("-".repeat(50));
        for (int i = 1; i <= TOTAL_CHECKS; i++) {
            if (check.checkColumn(i)) {
                validColumns++;
              //  System.out.println("Column " + i + ": Valid\n");
            } else {
                // System.out.println("Column " + i + ": Invalid\n");
                check.printRepeatedinColumn(i);
                System.out.println();
            }}
        System.out.println("-".repeat(50));
        for (int i = 1; i <= TOTAL_CHECKS; i++) {
            if (check.checkBox(i)) {
                validBoxes++;
              //  System.out.println("Box " + i + ": Valid\n");
            } else {
                // System.out.println("Box " + i + ": Invalid\n");
                check.printRepeatedinBox(i);
                System.out.println();
            }

        }
        System.out.println("-".repeat(50));
        displayFinalResults();
    }
    private void displayFinalResults(){
        System.out.println("\n" + "=".repeat(50));
        System.out.println("FINAL VALIDATION RESULTS:");
        System.out.println("=".repeat(50));
        System.out.println("Valid Rows: " + validRows + "/" + TOTAL_CHECKS);
        System.out.println("Valid Columns: " + validColumns + "/" + TOTAL_CHECKS);
        System.out.println("Valid Boxes: " + validBoxes + "/" + TOTAL_CHECKS);
        if(validRows == TOTAL_CHECKS && validColumns == TOTAL_CHECKS && validBoxes == TOTAL_CHECKS){
            System.out.println("\n🎉 SUCCESS: Valid Sudoku Solution!");
        }else{
            System.out.println("\n❌ FAILED: Invalid Sudoku Solution!");
        }
        System.out.println("=".repeat(50));
    }
}
