package jkms.jakomas;

import jkms.jakomas.Main.LexemeType;

public class Lexeme {
    LexemeType type;
    String value;

    Lexeme(LexemeType type, String value){
        this.type = type;
        this.value = value;
    }
    Lexeme(LexemeType type, Character value){
        this.type = type;
        this.value = value.toString();
    }
    @Override
    public String toString() {
        return "Lexeme{" +
                "type=" + type +
                ", value='" + value + '\'' +
                '}';
    }
}