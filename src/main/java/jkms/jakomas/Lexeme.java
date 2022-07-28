package jkms.jakomas;

import jkms.jakomas.Main.LexemeType;

public class Lexeme {
    LexemeType type;
    String value;
    int value_pow;

    Lexeme(LexemeType type, String value){
        this.type = type;
        this.value = value;
    }
    Lexeme(LexemeType type, Character value){
        this.type = type;
        this.value = value.toString();
    }

    //отдельный конструктор для лексемы корня квадратного, где
    //value_pow - степень корня (кубический)
    Lexeme(LexemeType type, Character value, Character value_pow){
        this.type = type;
        this.value = value.toString();
        this.value_pow = Integer.valueOf(value_pow);
    }


    @Override
    public String toString() {
        return "Lexeme{" +
                "type=" + type +
                ", value='" + value + '\'' +
                '}';
    }
}