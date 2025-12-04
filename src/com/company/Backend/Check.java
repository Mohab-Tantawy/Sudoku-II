
package com.company;


    public class Check {
    int[][] grid;

    public Check(int[][] grid) {
        this.grid = grid;
    }


    public boolean checkRow(int row){
        row--;
        boolean[] seen=new boolean[10];
        for(int col=0;col<9;col++) {
            int num = grid[row][col];
            if (num != 0) {
                if (seen[num]) {
                    return false;
                }
                seen[num] = true;
            }
        }
        return true;
    }

    public boolean checkColumn(int col){
        col--;
        boolean[] seen=new boolean[10];
        for(int row=0;row<9;row++) {
            int num = grid[row][col];
            if (num != 0) {
                if (seen[num]) {
                    return false;
                }
                seen[num] = true;
            }
        }
        return true;
    }

    public boolean checkBox(int box){
        boolean[] seen=new boolean[10];
        box--;
        int brow= (box/3)*3;
        int bcol= (box%3)*3;
        for(int row=brow;row<brow+3;row++){
            for(int col=bcol;col<bcol+3;col++) {
                int num = grid[row][col];
                if (num != 0) {
                    if (seen[num]) {
                        return false;
                    }
                    seen[num] = true;
                }
            }
        }
        return true;
    }

    public void printRepeatedinRow(int row){
        row--;
        int[] rep = new int[10];
        for(int c=0;c<9;c++){
            if(grid[row][c]!=0) {
                rep[grid[row][c]]++;
            }
        }

        for(int num=1;num<10;num++){
            if(rep[num]>1){
                System.out.print("ROW "+(row+1)+", #"+num+", [");
                boolean first= true;
                for(int col=0;col<9;col++){
                    if(grid[row][col]==num){
                        if(!first)
                            System.out.print(",");
                        System.out.print(col+1);
                        first=false;
                    }
                }
                System.out.println("]");
            }

        }

    }

    public void printRepeatedinColumn(int col){
        col--;
        int[] rep = new int[10];
        for(int r=0;r<9;r++) {
            if (grid[r][col] != 0) {
                rep[grid[r][col]]++;
            }
        }

        for(int num=1;num<10;num++){
            if(rep[num]>1){
                System.out.print("COL "+(col+1)+", #"+num+", [");
                boolean first= true;
                for(int row=0;row<9;row++){
                    if(grid[row][col]==num){
                        if(!first)
                            System.out.print(",");
                        System.out.print(row+1);
                        first=false;
                    }
                }
                System.out.println("]");
            }
        }
    }

    public void printRepeatedinBox(int box){
        box--;
        int brow= (box/3)*3;
        int bcol= (box%3)*3;
        int[] rep=new int[10];

        for(int row=brow;row<brow+3;row++){
            for(int col=bcol;col<bcol+3;col++){
                if(grid[row][col]!=0) {
                    rep[grid[row][col]]++;
                }
            }
        }
        for(int num=1;num<10;num++){
            if(rep[num]>1){
                System.out.print("BOX "+(box+1)+", #"+num+", [");
                boolean first= true;
                for(int row=brow;row<brow+3;row++){
                    for(int col=bcol;col<bcol+3;col++){
                    if(grid[row][col]==num){
                        int i=(row-brow)*3 +(col-bcol) ;
                        if(!first)
                            System.out.print(",");
                        int posInBox = (row - brow) * 3 + (col - bcol) + 1;
                        System.out.print(posInBox);
                        first=false;
                    }
                }
                }
                System.out.println("]");
            }
        }
    }

}
