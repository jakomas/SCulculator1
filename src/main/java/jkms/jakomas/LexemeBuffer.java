package jkms.jakomas;

import java.util.ArrayList;
import jkms.jakomas.Main.LexemeType;

public class LexemeBuffer {
    private final ArrayList<Lexeme> lexemeArrayList;
    private int pos;


    public LexemeBuffer(ArrayList<Lexeme> lexemeArrayList){
        this.lexemeArrayList = lexemeArrayList;
    }


    public int getPos() {return pos;}
    public Lexeme getNextLexeme() {return lexemeArrayList.get(pos++);}
    public void backPos() {pos--;}

    //вернуть предыдущую лексему
    public LexemeType getBeforeLexeme() {return lexemeArrayList.get(pos-1).type;}

}