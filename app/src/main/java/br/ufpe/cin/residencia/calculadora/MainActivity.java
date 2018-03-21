package br.ufpe.cin.residencia.calculadora;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

		Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn8, btn0;
		Button btnEqual, btnDiv, btnMult, btnSub, btnSum, btnDot, btnLParen, btnRParen, btnPow, btnErase;
	
		/* Referencia os campos dos numeros */
        btn1 = findViewById(R.id.btn_1);
        btn2 = findViewById(R.id.btn_2);
        btn3 = findViewById(R.id.btn_3);
        btn4 = findViewById(R.id.btn_4);
        btn5 = findViewById(R.id.btn_5);
        btn6 = findViewById(R.id.btn_6);
        btn7 = findViewById(R.id.btn_7);
        btn8 = findViewById(R.id.btn_8);
        btn9 = findViewById(R.id.btn_9);
		btn0 = findViewById(R.id.btn_0);

		/* Referencia os campos dos operadores */
		btnEqual = findViewById(R.id.btn_Equal);
		btnDiv = findViewById(R.id.btn_Divide);
		btnMult = findViewById(R.id.btn_Multiply);
		btnSub = findViewById(R.id.btn_Subtract);
		btnSum = findViewById(R.id.btn_Add);
		btnDot = findViewById(R.id.btn_Dot);
		btnLParen = findViewById(R.id.btn_LParen);
		btnRParen = findViewById(R.id.btn_RParen);
		btnPow = findViewById(R.id.btn_Power);
		btnErase = findViewById(R.id.btn_Clear);
		
		/* Define os eventos de click */
        button1.setOnClickListener(this); button2.setOnClickListener(this);
        button3.setOnClickListener(this); button4.setOnClickListener(this);
        button5.setOnClickListener(this); button6.setOnClickListener(this);
        button7.setOnClickListener(this); button8.setOnClickListener(this);
		button9.setOnClickListener(this); button0.setOnClickListener(this);
		
		btnEqual.setOnClickListener(this);
		btnDiv.setOnClickListener(this);
		btnMult.setOnClickListener(this);
		btnSub.setOnClickListener(this);
		btnSum.setOnClickListener(this);
		btnDot.setOnClickListener(this);
		btnLParen.setOnClickListener(this);
		btnRParen.setOnClickListener(this);
		btnPow.setOnClickListener(this);
		btnErase.setOnClickListener(this);
		
    }

	@Override
    public void onClick(View v) {
      
		switch(v.getId()){

			// Se for o click do botao Igual, calcula a expressao
			case R.id.btnEqual:
				
				try{
					TextView infoText = findViewById(R.id.text_info);
					EditText calcText = findViewById(R.id.text_calc);
					
					String toEval = String.valueOf(eval(calcText.getText().toString()));
					infoText.setText(toEval);
					calcText.setText("");
	
				}catch(Exception e){
					// Clica no botao para limpar tudo
					Button btn = findViewById(R.id.btn_Clear);
					btn.performClick();
					// Mostra mensagem de expressao errada
					Toast.makeText(getApplicationContext(), “Expressão errada! Tente de novo.”, Toast.LENGTH_SHORT).show();
				}
				break;
			// Se for o click do botao Apagar, apaga os campos
			case R.id.Erase:
					TextView infoText = findViewById(R.id.text_info);
					EditText calcText = findViewById(R.id.text_calc);
					
					infoText.setText("");
					calcText.setText("");
				break;
			// Se for o click de qualquer botao operador ou numero, adiciona a expressao
			default:
					EditText calcText = findViewById(R.id.text_calc);
					Button buttonText = findViewById(v.getId());

					calcText.setText(calcText.getText().toString() + buttonText.getText().toString());

				break;
		}
			
    }
	
	
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
