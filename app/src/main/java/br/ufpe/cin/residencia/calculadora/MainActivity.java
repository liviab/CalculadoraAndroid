package br.ufpe.cin.residencia.calculadora;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button button1 = findViewById(R.id.btn_1);
        final Button button2 = findViewById(R.id.btn_2);
        final Button button3 = findViewById(R.id.btn_3);
        final Button button4 = findViewById(R.id.btn_4);
        final Button button5 = findViewById(R.id.btn_5);
        final Button button6 = findViewById(R.id.btn_6);
        final Button button7 = findViewById(R.id.btn_7);
        final Button button8 = findViewById(R.id.btn_8);
        final Button button9 = findViewById(R.id.btn_9);

        button1.setOnClickListener(onClickListener);
        button2.setOnClickListener(onClickListener);
        button3.setOnClickListener(onClickListener);
        button4.setOnClickListener(onClickListener);
        button5.setOnClickListener(onClickListener);
        button6.setOnClickListener(onClickListener);
        button7.setOnClickListener(onClickListener);
        button8.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(final View v) {

            switch(v.getId()){

                case R.id.btn_Add:
                    break;
                default:
                    TextView calcText = findViewById(R.id.text_calc);
                    Button buttonText = findViewById(v.getId());

                    calcText.setText(calcText.getText().toString() + buttonText.getText().toString());

                    break;
            }
        }
    };

    //Como usar a função:
    // eval("2+2") == 4.0
    // eval("2+3*4") = 14.0
    // eval("(2+3)*4") = 20.0
    //Fonte: https://stackoverflow.com/a/26227947
    public static double eval(final String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Caractere inesperado: " + (char)ch);
                return x;
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)`
            //        | number | functionName factor | factor `^` factor

            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if      (eat('+')) x += parseTerm(); // adição
                    else if (eat('-')) x -= parseTerm(); // subtração
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if      (eat('*')) x *= parseFactor(); // multiplicação
                    else if (eat('/')) x /= parseFactor(); // divisão
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor(); // + unário
                if (eat('-')) return -parseFactor(); // - unário

                double x;
                int startPos = this.pos;
                if (eat('(')) { // parênteses
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // números
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') { // funções
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = str.substring(startPos, this.pos);
                    x = parseFactor();
                    if (func.equals("sqrt")) x = Math.sqrt(x);
                    else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
                    else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
                    else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
                    else throw new RuntimeException("Função desconhecida: " + func);
                } else {
                    throw new RuntimeException("Caractere inesperado: " + (char)ch);
                }

                if (eat('^')) x = Math.pow(x, parseFactor()); // potência

                return x;
            }
        }.parse();
    }
}
