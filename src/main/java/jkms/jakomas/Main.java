package jkms.jakomas;


import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    //объект класса с ошибками
    static Errors_userInput_sqr e_ui_sqr = new Errors_userInput_sqr();
    public enum LexemeType {
        L_BRACKET, R_BRACKET,//левая и правая скобки
        NUMBER,             //число
        OP_POW,             //умножить
        OP_L_SQR, OP_R_SQR, //L+R - левая и права границы подкоренного выражения
        OP_MUL, OP_DIV,     //делить - умножить
        OP_PLUS, OP_MINUS,  //плюс и минус
        DONE                //завершить работу с лексемами
    }

    static ArrayList<Lexeme> analyzeUserInput(String user_input) {
        ArrayList<Lexeme> lexemeArrayList = new ArrayList<>();
        int pos=0;

        while (pos<user_input.length()) {
            char ch = user_input.charAt(pos);
            switch (ch) {
                case '(' -> {lexemeArrayList.add(new Lexeme(LexemeType.L_BRACKET, ch)); pos++;}
                case ')' -> {lexemeArrayList.add(new Lexeme(LexemeType.R_BRACKET, ch)); pos++;}
                case '*' -> {lexemeArrayList.add(new Lexeme(LexemeType.OP_MUL, ch)); pos++;}
                case '/' -> {lexemeArrayList.add(new Lexeme(LexemeType.OP_DIV, ch)); pos++;}
                case '^' -> {lexemeArrayList.add(new Lexeme(LexemeType.OP_POW, ch)); pos++;}
                case '+' -> {lexemeArrayList.add(new Lexeme(LexemeType.OP_PLUS, ch)); pos++;}
                case '-' -> {lexemeArrayList.add(new Lexeme(LexemeType.OP_MINUS, ch)); pos++;}
                //case '#' -> {lexemeArrayList.add(new Lexeme(LexemeType.OP_SQR, ch)); pos++;}

                //мы не можем встретить 'закрытие корня' раньше и без 'открытия корня'
                case '<' -> {//только, если открывается корень
                    pos++;
                    if(pos >= user_input.length()){
                        break;
                    }
                    ch = user_input.charAt(pos);
                    if(ch == '|'){//подтверждает открытие корня
                        lexemeArrayList.add(new Lexeme(LexemeType.OP_L_SQR, "<|"));
                    }
                    else {//если не было подтверждено открытие корня '<' + '|'
                        throw new RuntimeException(e_ui_sqr.get_error_sqr_user_input(0));
                    }
                    //если проверка пройдена - идём дальше
                    pos++;
                }

                //неоднозначный парный символ корня
                case '|' -> {//не понятно частью какого оператора является '<|' или '|>'
                    //выяснить, есть ли дальше для него логическая "пара" - '>'
                    pos++;
                    if(pos >= user_input.length()){
                        break;
                    }
                    ch = user_input.charAt(pos);
                    if(ch == '>'){//если пара равна '|>' - это операция закрытия корня
                        lexemeArrayList.add(new Lexeme(LexemeType.OP_R_SQR, "|>"));
                    }
                    //если логической пары не встретили, возможно
                    //пользователь допустил синтаксическую ошибку
                    else {
                        //проверяем, был ли до этого символ "открывающий" корень, проверка на лексему
                        //решение пока что не верное, идти нужно с конца массива в обратную сторону!
                        for(Lexeme lt : lexemeArrayList){
                            if(lt.type == LexemeType.OP_L_SQR){
                                //да, левая скобка корня есть, неверно закрыли
                                throw new RuntimeException(e_ui_sqr.get_error_sqr_user_input(2));
                            } else if (lt.type == LexemeType.OP_R_SQR) {
                                //нет, до этого уже была правая скобка корня, неверно открыли новую
                                throw new RuntimeException(e_ui_sqr.get_error_sqr_user_input(1));
                            }
                        }

                    }
                    pos++;
                }

                default -> {
                    if(ch <= '9' && ch >= '0') {
                        StringBuilder sbNumeric = new StringBuilder();
                        do {
                            sbNumeric.append(ch);
                            pos++;
                            if(pos>=user_input.length()){
                                break;
                            }
                            ch = user_input.charAt(pos);

                        } while (ch <= '9' && ch >= '0');
                        lexemeArrayList.add(new Lexeme(LexemeType.NUMBER, sbNumeric.toString()));
                    } else {
                        //если встретили один из парных символов закрывающей скобки корня, то
                        //это - синтаксическая ошибка пользователя
                        if(ch == '>')
                            //throw new RuntimeException("Неверная запись границ подкоренного выражения.");
                            throw new RuntimeException(e_ui_sqr.get_error_sqr_user_input(3));
                            //не скобки не операции не числа - не предусмотрено
                        else
                            throw new RuntimeException("\nНепредусмотренный символ или неизвестная операция: "
                                    +ch+" index: "+pos);
                    }
                }
            }
        }
        //завершить формирование массива лексем последней лексемой DONE
        lexemeArrayList.add(new Lexeme(LexemeType.DONE, ""));
        //вернуть массив лексем
        return lexemeArrayList;
    }
    

    static int checkOnStart(LexemeBuffer lexemeBuffer) {
        Lexeme lexeme = lexemeBuffer.getNextLexeme();
        if(lexeme.type == LexemeType.DONE)
            throw new RuntimeException("Некорректное выражение");
        else {
            lexemeBuffer.backPos();
            return plus_minus(lexemeBuffer);
        }
    }
    static int factor(LexemeBuffer lexemeBuffer) {
        Lexeme lexeme = lexemeBuffer.getNextLexeme();

        switch (lexeme.type) {
            case NUMBER -> { return Integer.parseInt(lexeme.value); }
            case OP_L_SQR -> {
                int value = plus_minus(lexemeBuffer);
                lexeme = lexemeBuffer.getNextLexeme();

                if(lexeme.type != LexemeType.OP_R_SQR) {
                    throw new RuntimeException("""
                            
                            Не обозначены границы подкоренного выражения: |>
                            Вы не "закрыли" выражение под корнем.
                            Выражение из под корня имеет вид:
                            <|  открыть выражение корня.
                            |>  закрыть выражение корня.
                            перепроверьте выражение, может быть ошибка.""");
                }
                //посчитали подкоренное выражение, извлекли из него корень, вернули
                return (int) Math.sqrt( value );
            }
            case L_BRACKET -> {
                int value = plus_minus(lexemeBuffer);
                lexeme = lexemeBuffer.getNextLexeme();

                if (lexeme.type != LexemeType.R_BRACKET) {
                    //break;
                    throw new RuntimeException("Нет закрывающей скобки. Некорректное выражение.");
                }
                //проверка пройдена, скобка закрыта, выражение внутри посчитано, вернуть результат
                return value;
            }
            default ->
                    throw new RuntimeException("Неизвестный тип лексемы: " +lexeme.type
                            + " на позиции: "+lexemeBuffer.getPos());
        }
    }



    static int pow(LexemeBuffer lexemeBuffer) {
        int value = factor(lexemeBuffer);

        while (true) {
            Lexeme lexeme = lexemeBuffer.getNextLexeme();
            if (lexeme.type == LexemeType.OP_POW) {
                value = (int) Math.pow(value, factor(lexemeBuffer));
            } else {
                lexemeBuffer.backPos();
                return value;
            }
        }
    }



/*    static int mul_div(LexemeBuffer lexemeBuffer) {
        int value = factor(lexemeBuffer);

        while (true) {
            Lexeme lexeme = lexemeBuffer.getNextLexeme();//pos++
            switch (lexeme.type) {
                case OP_MUL -> { value *= factor(lexemeBuffer); }
                case OP_DIV -> { value /= factor(lexemeBuffer); }
                default -> {
                    lexemeBuffer.backPos();//pos--
                    return value;
                }
            }
        }
    }*/
static int mul_div(LexemeBuffer lexemeBuffer) {
    //int value = factor(lexemeBuffer);
    int value = pow(lexemeBuffer);

    while (true) {
        Lexeme lexeme = lexemeBuffer.getNextLexeme();//pos++
        switch (lexeme.type) {
            case OP_MUL -> value *= pow(lexemeBuffer);
            case OP_DIV -> value /= pow(lexemeBuffer);
            default -> {
                lexemeBuffer.backPos();//pos--
                return value;
            }
        }
    }
}
    static int plus_minus(LexemeBuffer lexemeBuffer) {
        int value = mul_div(lexemeBuffer);

        while (true) {
            Lexeme lexeme = lexemeBuffer.getNextLexeme();
            switch (lexeme.type) {
                case OP_PLUS -> value += mul_div(lexemeBuffer);
                case OP_MINUS -> value -= mul_div(lexemeBuffer);
                //не нашли операции сложение/вычитание
                default -> {
                    lexemeBuffer.backPos();//лексема была взята -> pos++, вернуть обратно
                    return value; //верни, value = mul_div()
                }
            }
        }
    }


    public static void main(String[] args) {
        String userInput = new Scanner(System.in).nextLine().trim().replace(" ", "");
        System.out.println("User input: " + userInput);
        ArrayList<Lexeme> lexemeArrayList = analyzeUserInput(userInput);
        LexemeBuffer lexemeBuffer = new LexemeBuffer(lexemeArrayList);
        System.out.println("Result: " + checkOnStart(lexemeBuffer));
        //2+2*2-3+6*<|(6/2)+1*(7-1)|>
        //2+2*2-3+6*2^<|(6/2)+1*(7-1)|>
        //2+2*<|4*4|>
    }
}

















