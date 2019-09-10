import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

//creates the main frame and stores the value of soduku in a matrix
public class Sudoku extends JFrame {
    PlayArea playArea;

    //constructor used when entering value in GUI
    public Sudoku(){
        playArea = new PlayArea(this);
        playArea.menu.add(playArea.solve);
        playArea.menu.add(playArea.reset);
        playArea.mb.add(playArea.menu);
        this.setTitle("SUDOKU");
        this.setJMenuBar(playArea.mb);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(500, 500);
        this.setVisible(true);
        add(playArea);
    }

    //sudoku stored in matrix
    public int[][] matrix = new int[9][9];

    //constructor used when entering values through csv file
    public Sudoku(List<Input> l){
        this();
        setValueGui(l);
    }

    public void setValueGui(List<Input> l){
        for(Input input:l){
            matrix[input.i][input.j]=input.val;
            playArea.setGuiValue(input.val,input.i,input.j);
        }
    }

    //to store the inputted value in a matrix
    public void setValue(Input input){
        if(matrix[input.i][input.j]==0)
            matrix[input.i][input.j]=input.val;
    }

    //to display the sudoku state in console
    public void display(){
        for(int row=0;row<9;row++){
            for(int col=0;col<9;col++){

                System.out.print(matrix[row][col]!=0?matrix[row][col]:"_"+" ");
            }
            System.out.println("");
        }
    }
}
