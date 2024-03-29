package jkms.jakomas;

public class Errors_userInput_sqr {
    private final String e_open_op_l_sqr;
    private final String e_open2_op_l_sqr;
    private final String e_close_op_sqr;
    private final String e_close2_op_sqr ;
    String[] e_value = {
            e_open_op_l_sqr = """
                               
                    Одиночный символ '<'.
                    Используется в указании границ подкоренного выражения.
                    Неправильно "открыт" корень.
                    
                    Выражение из под корня имеет вид:
                    <|  открыть выражение корня.
                    |>  закрыть выражение корня.
                    перепроверьте выражение, может быть ошибка.""",
            e_open2_op_l_sqr = """
                                
                    Одиночный символ '|'.
                    Используется в указании границ подкоренного выражения.
                    Неправильно "открыт" корень.
                    
                    Выражение из под корня имеет вид:
                    <|  открыть выражение корня.
                    |>  закрыть выражение корня.
                    перепроверьте выражение, может быть ошибка.""",
            e_close_op_sqr = """
                                
                    Одиночный символ '|'.
                    Используется в указании границ подкоренного выражения.
                    Неправильно "закрыт" корень.
                    
                    Выражение из под корня имеет вид:
                    <|  открыть выражение корня.
                    |>  закрыть выражение корня.
                    перепроверьте выражение, может быть ошибка.""",
            e_close2_op_sqr = """
                                                
                    Одиночный символ '>'.
                    Используется в указании границ подкоренного выражения.
                    Неправильно "закрыт" корень.
                                                    
                    Выражение из под корня имеет вид:
                    <|  открыть выражение корня.
                    |>  закрыть выражение корня.
                    перепроверьте выражение, может быть ошибка."""
    };
    public String get_error_sqr_user_input(int error_num) {
        return e_value[error_num];
    }

}
