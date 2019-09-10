import javax.swing.*;
import java.awt.*;
import java.util.Stack;

//computes the moves played by the computer
public class PlayGame {
    Sudoku sudoku;
    public int[][][] board = new int[9][9][9]; //a 9*9*9 matrix that stores which all numbers can be placed in a particular box
    PlayArea playArea;
    int cellsFilled=0; // lets us keep track of the no. of cells filled

    public PlayGame(Sudoku sudoku,PlayArea playArea){
        this.sudoku=sudoku;
        this.playArea= playArea;
    }

    public boolean isSudokuComplete(){
        return (cellsFilled==81);
    }

    //choosing a particular node/box from the whole sudoku
    public void pickNode(){

        for(int row=0;row<9;row++){
            for(int col=0;col<9;col++){
                //only going further with this node if it is empty
                if(sudoku.matrix[row][col]==0){
                    computeOptions(row,col);
                }
                else cellsFilled++;
            }
        }
        insertNum();
    }

    //Computes which all numbers are possible in a particular node/box
    public void computeOptions(int row,int col){
        for(int i=0;i<9;i++){

            //applying row constraints to discard values that can't occur in that box
            if(sudoku.matrix[row][i]!=0){
                int val = sudoku.matrix[row][i]-1;
                board[row][col][val]=-1;
            }
            //applying column constraints
            if(sudoku.matrix[i][col]!=0){
                int val = sudoku.matrix[i][col]-1;
                board[row][col][val]=-1;
            }
            blockConstraint(row,col);
        }
    }

    //applying block constraint separately
    public void blockConstraint(int row, int col){
        int startRow, startCol; //choosing start indexes of block to check for block constraint
        if(row==0 || row==1|| row==2)
            startRow=0;
        else if(row==3 || row==4 || row==5)
            startRow=3;
        else startRow=6;
        if(col==1 || col==2 || col==0)
            startCol=0;
        else if(col==3 || col==4 || col==5)
            startCol=3;
        else startCol=6;

        //running loop to update options available in a particular box by applying grid/block constraint
        for(int i=startRow;i<startRow+3;i++){
            for(int j=startCol;j<startCol+3;j++){
                if(sudoku.matrix[i][j]!=0){
                    int val= sudoku.matrix[i][j]-1;
                    //val already present in another position in the block, so cannot come in that particular box
                    board[row][col][val]=-1;
                }
            }
        }
    }

    //Insert the computed number in the node
    public void insertNum() {
        Stack<Input> values= new Stack<Input>();
        int pos = -1;
        try{
            for (int row = 0; row < 9; row++) {
                for (int col = 0; col < 9; col++) {
                    int count = 0;
                    if (sudoku.matrix[row][col] == 0) {
                        for (int i = 0; i < 9; i++) {
                            if (board[row][col][i] != -1) {
                                count++;
                                pos = i;
                            }
                        }
                        //constraint not satisfied
                        if(count==0){
                            throw new ConstraintException();
                        }
                        //putting the value directly if only one value option present for a node
                        if (count == 1) {
                            sudoku.matrix[row][col] = pos + 1;
                            playArea.guiSudoku.get((row) * 9 + col).setBackground(Color.YELLOW);
                            playArea.guiSudoku.get((row) * 9 + col).setText(Integer.toString(pos + 1));
                            values.push(new Input(row,col,pos+1));
                            cellsFilled++;
                            updateOptions(row, col, pos);
                            row = 0;
                            col = 0;
                        }

                    }
                }
            }
            int countMin=9;
            int rowCM=0, colCM=0;
            for (int row = 0; row < 9; row++) {
                for (int col = 0; col < 9; col++) {
                    int count = 0;
                    if (sudoku.matrix[row][col] == 0) {
                        for (int i = 0; i < 9; i++) {
                            if (board[row][col][i] != -1) {
                                count++;
                            }

                        }
                    }
                    if(countMin>count){
                        countMin=count;
                        rowCM=row;
                        colCM=col;
                    }
                }
            }
            for(int i=0;i<9;i++){
                if (board[rowCM][colCM][i] != -1) {
                    sudoku.matrix[rowCM][colCM] = i + 1;
                    playArea.guiSudoku.get((rowCM) * 9 + colCM).setBackground(Color.YELLOW);
                    playArea.guiSudoku.get((rowCM) * 9 + colCM).setText(Integer.toString(i + 1));
                    cellsFilled++;
                    updateOptions(rowCM, colCM, i);
                    insertNum();
                    if(isSudokuComplete()){
                        return;
                    }
                    else{
                        cellsFilled--;
                        playArea.resetCell(rowCM,colCM);
                        sudoku.matrix[rowCM][colCM]=0;
                        clearBoard();
                        for(int row=0;row<9;row++){
                            for(int col=0;col<9;col++){
                                //only going further with this node if it is empty
                                if(sudoku.matrix[row][col]==0){
                                    computeOptions(row,col);
                                }

                            }
                        }
                    }
                }
            }
        }
        catch(ConstraintException e){
            JOptionPane.showMessageDialog(null,"Constraint not satisfied");
            while(values.size()!=0) {
                Input temp=values.pop();
                playArea.resetCell(temp.i,temp.j);
                sudoku.matrix[temp.i][temp.j]=0;
                cellsFilled--;
            }
        }
        return;
    }

    public void clearBoard(){
        for(int row=0;row<9;row++){
            for(int col=0;col<9;col++){
                for(int i=0;i<9;i++){
                    board[row][col][i]=0;
                }
            }
        }

    }

    //computing the updated options for the nodes which have been affected by the inputtedValue, i.e. nodes present in that row, column or block.
    public void updateOptions(int row,int col,int val){
        for(int i=0;i<9;i++){
            board[i][col][val]=-1;
            board[row][i][val]=-1;
            //updating block
            int startRow, startCol; //choosing start indexes of block
            if(row==0 || row==1|| row==2)
                startRow=0;
            else if(row==3 || row==4 || row==5)
                startRow=3;
            else startRow=6;
            if(col==1 || col==2 || col==0)
                startCol=0;
            else if(col==3 || col==4 || col==5)
                startCol=3;
            else startCol=6;

            for(int j=startRow;i<startRow+3;i++){
                for(int k=startCol;j<startCol+3;j++){
                    if(sudoku.matrix[j][k]!=0)
                        board[j][k][val]=-1;
                }
            }

        }
    }



}
