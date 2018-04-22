package com.example.didonglin.testapplication;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.text.InputType;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;

import com.example.didonglin.testapplication.util.ParseExpression;

import java.lang.reflect.Method;


public class CalLayout extends GridLayout implements OnClickListener {
	private Context context;
	private final static String ERROR = "格式错误";
	private String parenthesis = "( )";
	private String[] btTexts = new String[]{
			"C", "÷", "×", "D",
			"7", "8", "9", "-",
			"4", "5", "6", "+",
			"1", "2", "3", parenthesis,
			"0", ".", "%", "="
	};
	
	private GridLayout gridLayout;
	private EditText printET;
	private TextView historyTV;
	
	private String resultString;
	private String exp;
	private String expAndResult;
	private boolean cursorEnd = true;	//光标是否在尾端，默认为真
	private String frontExp = exp;	//光标前的表达式,默认光标在尾端
	private String rearExp = "";	//光标后的表达式,默认光标在尾端
	private String regOperator = "\\+|-|×|÷";
	private char lastChar = ' ';

	private StringBuilder historySB = new StringBuilder();	//保存历史记录
	private Class<EditText> cls;
	private Method method;
	private Point size;
	private WindowManager wm;
	private Display display;
	private int rowCount;
	private int cellWidth;
	private int columnCount;
	private int cellHeight;
	private int row;
	private int column;
	private Spec rowSpec;
	private Spec columnSpec;
	private LayoutParams cellParams;
	private LayoutParams tvParams;
	private Button bt;
	private String btText;
	private String inputString;
	private String penultCharString;
	

	public CalLayout(Context context) {
		super(context);
		this.context = context;
		LayoutInflater.from(context).inflate(R.layout.calculator_layout, this);
		printET = (EditText)findViewById(R.id.printET);
		disableShowSoftInput(printET);  // 禁用默认的软键盘
		initVal();  // 初始化赋值
		initCalUI();  // 初始化计算机界面
	}


    /**
     * 禁止EditText弹出输入法，光标仍正常显示
     * @param editText
     */
	public void disableShowSoftInput(EditText editText){
		if (android.os.Build.VERSION.SDK_INT <= 10) {
				editText.setInputType(InputType.TYPE_NULL);  //强制关闭软键盘，但是编辑框没有闪烁的光标
        } 
		else {
			//设置输入模式，窗体获得焦点，始终隐藏软键盘  
			((Activity) getContext()).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                     
	        cls = EditText.class;
	        try { 
	            method = cls.getMethod("setShowSoftInputOnFocus",boolean.class);  
	            method.setAccessible(true);  //设置是可访问，为true，表示禁止访问 
	            method.invoke(editText, false);  
	        }catch (Exception e) {
	        	e.printStackTrace();
	        }
	        
	        try { 
	            method = cls.getMethod("setSoftInputShownOnFocus",boolean.class);  
	            method.setAccessible(true);  
	            method.invoke(editText, false);  
	        }catch (Exception e) {
	        	e.printStackTrace();			
	        }
        } 
	}


	public String getHistory(){
		return historySB.toString();
	}



	public void clearCalHistory(){
		this.historySB.setLength(0);
	}


    /**
     * 初始化赋值
     */
    private void initVal(){
    	resultString = "";
    	exp = "";
    	expAndResult = "";
    	frontExp = exp;
    	rearExp = "";
    	cursorEnd = true;
    	printET.setText("");
    	printET.setSelection(exp.length());
    }


    /**
     * 初始化计算器界面
     */
    private void initCalUI(){
    	size = new Point();
    	wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
    	display = wm.getDefaultDisplay();
    	display.getSize(size);
        
    	rowCount = 7;
    	columnCount = 4;
        cellWidth = (int)( (size.x - 3) / columnCount);
        cellHeight = (int)( (size.y - 5) / rowCount );
        printET.setTextIsSelectable(true);
		printET.setBackgroundColor(Color.WHITE);
		tvParams = (LayoutParams)printET.getLayoutParams();
		tvParams.height = (int)( size.y - cellHeight * 5 - 44 );
		printET.setLayoutParams(tvParams);
		
        gridLayout = (GridLayout)findViewById(R.id.calculator_main);
        gridLayout.setBackgroundColor(Color.parseColor("#D1D1D1"));
        //批量初始化按钮
        Button btn[] = new Button[btTexts.length];

        for(int i = 0; i < btTexts.length; i++){
        	btn[i] = new Button(context);
        	//btn[i].setText(setButtonContent(btTexts[i]));   //设置按钮显示内容
			btn[i].setText(btTexts[i]);
        	btn[i].setTextColor(getResources().getColor(R.color.gray_text));
        	btn[i].setTextSize(30);
        	btn[i].setBackgroundColor(Color.WHITE);
        	
        	row = i/columnCount + 2;
        	column = i % columnCount;
        	rowSpec = GridLayout.spec(row);
        	columnSpec = GridLayout.spec(column);
        	cellParams = new LayoutParams(rowSpec, columnSpec);
            cellParams.width = cellWidth;  
            cellParams.height = cellHeight;
            if( column == (columnCount-1) )
            	cellParams.setMargins(0, 1, 0, 0);
            else
            	cellParams.setMargins(0, 1, 1, 0);
            gridLayout.addView(btn[i], cellParams);  

//          btn[i].setOnTouchListener(this);
            btn[i].setBackgroundResource(R.drawable.btn_bg_selector);
            
            btn[i].setOnClickListener(this);
        }
    }

    /*public String setButtonContent(String content){
        switch (content){
            case "0":
                return "〇";
            case "1" :
                return "①";
            case "2":
                return "②";
            case "3":
                return "③";
            case "4":
                return "④";
            case "5":
                return "⑤";
            case "6":
                return "⑥";
            case "7":
                return "⑦";
            case "8":
                return "⑧";
            case "9":
                return "⑨";
            case "C":
                return "清除";
            case "D":
                return "回退";
            default:
                return content;
        }
    }

	public String getButtonContent(String content){
		switch (content){
			case "〇":
				return "0";
			case "①" :
				return "1";
			case "②":
				return "2";
			case "③":
				return "3";
			case "④":
				return "4";
			case "⑤":
				return "5";
			case "⑥":
				return "6";
			case "⑦":
				return "7";
			case "⑧":
				return "8";
			case "⑨":
				return "9";
			case "清除":
				return "C";
			case "回退":
				return "D";
			default:
				return content;
		}
	}*/


    @Override
	public void onClick(View v) {
		bt = (Button)v;
		//btText = getButtonContent(bt.getText().toString());
		btText = bt.getText().toString();
		exp = printET.getText().toString();
        expAndResult = "";
        frontExp = exp;
        rearExp = "";
		inputString = bt.getText().toString();
		
		if ( resultString.matches(ERROR) ){
			initVal();
		}
		
		cursorEnd = true;
		int cursorIndex = printET.getSelectionStart();
		if ( cursorIndex != printET.getText().length()) {
			cursorEnd = false;
		}
		
		if ( ! cursorEnd ){
			if ( resultString == ""){
				if ( (exp.length() > 0) ){
					if ( cursorIndex > 0 ){
						frontExp = exp.substring(0, cursorIndex);
						rearExp = exp.substring(cursorIndex, exp.length());
					}
					else if ( cursorIndex == 0 ){
						frontExp = "";
						rearExp = exp;
					}
				}
			}
			else{
				initVal();
			}
		}
		
		
		if(btText.matches("[0-9]|\\+|-|×|÷|\\.|(\\( \\))|=")){
			//已经运算过一次并有了运算结果
			if((resultString != "")){
				//判断光标位置
				if ( cursorEnd ){	//光标在尾端
					if(ParseExpression.isOperator(inputString)){	//如果输入的是运算符，表明是继续运算
						exp = resultString;
						resultString = "";
					}
					else if(inputString.matches("=") ){	//若输入"="，继续重复上一次的运算符运算
						exp = exp.replaceAll("\n=.*", "");
						String[] op = ParseExpression.splitInfixExp(exp);
						int opLen = op.length;
						exp = resultString + op[opLen - 2] + op[opLen - 1];
						resultString = "";
					}
					else if(inputString == parenthesis){	//若输入括号
						exp = "(" + resultString;
						inputString = "";
						resultString = "";
					}
					else {	//重新开始新的运算
						initVal();
					}
				}
				else{	//光标不在尾端，则开始新的运算
					initVal();
				}
			}
			
			if ( inputString.matches("\\.") ) {
				if ( cursorEnd ) {
					if ( ParseExpression.appendDotValid(exp) ) {
						if ( exp.matches(".*?(" + regOperator + "|\\()$|()") )
							inputString = "0.";
					}
					else
						return;
				}
				else {
					if ( ParseExpression.appendDotValid(frontExp) ){
						if ( frontExp.matches(".*?(" + regOperator + "|\\()$|()") ){
							inputString = "0.";
						}
						else {
							inputString = ".";
						}
					}
					else{
						return;
					}
				}
			}
			else if( cursorEnd ){
				if ( inputString == parenthesis ) {
					inputString = ParseExpression.inputParenthesis(exp);
					if ( inputString.matches("\\)") ) {
						if(exp.length() > 1)
							lastChar = exp.charAt(exp.length()-1);
						if (lastChar == '.')
							exp = exp.substring(0, exp.length()-1);
					}
				}
				else if ( ParseExpression.isOperator(inputString) ) {
					if( exp.endsWith("(")){
						if( ! inputString.matches("-") )
							return;
					}
					//如果lastCHar是运算符且倒数第二个字符是数字，则替换成input输入的运算符
					else if(exp.length() > 1){
						lastChar = exp.charAt(exp.length()-1);
						penultCharString = String.valueOf(exp.charAt(exp.length()-2));
						if ( ParseExpression.isOperator(lastChar) ){
							if(penultCharString.matches("[0-9]|\\)|\\.|%"))
								exp = exp.substring(0, exp.length()-1);
							else{
								return;
							}
						}
						else if ( (lastChar == '.')){
							exp = exp.substring(0, exp.length()-1);
						}
					}
					else if ( exp.length() == 1 ){
						if( exp.matches("-") )
							return;
					}
					else if( ! inputString.matches("-") )
						return;
				}
				else if (inputString.matches("[0-9]")){
					if( (exp.length() > 0) ){
						lastChar = exp.charAt(exp.length()-1);
						if( (lastChar == ')') || (lastChar == '%') )
							return;
					}
					if(exp.endsWith("0")){
						if(exp.length() == 1)
							exp = "";
						else
							exp= exp.replaceAll("(.*?)(" + regOperator + "|\\()(0)$","$1$2");
					}
				}
				lastChar = ' ';
			}
			
			if (inputString.equals("=")) {
				//若表达式以运算符结尾，则去掉末端的运算符 再执行运算
				exp = exp.replaceAll("(.*?)(\\+|-|×|÷)(\\(?)$", "$1");
				if( !ParseExpression.isParenthesisMatch(exp)) {
				    printET.setText(ERROR);
                    return;
                }
				if( exp == "")
					return;
				
				resultString = ParseExpression.calInfix(exp);
				expAndResult = exp + "\n=" + resultString;
				historySB.append(expAndResult.replace("\n", ",") + ";");
				printET.setText(expAndResult);
			}
			else {
				if ( cursorEnd ){
					exp = exp + inputString;
				}
				else{
					if( inputString == parenthesis)
						inputString = "()";
					exp = frontExp + inputString + rearExp;
				}
				printET.setText("");
				printET.setText(exp);
			}
		}
		
		else if(btText.matches("%")){
			if ( cursorEnd ){
				if((resultString != "")){
					exp = resultString;
					resultString = "";
				}
				else if( !exp.matches(".*?(\\)|[0-9])$") ){
	    			return;
				}
				printET.setText(exp + "%");
			}
			else{
				if((resultString != ""))
					return;
				printET.setText(frontExp + "%" + rearExp);
			}
    	}
		
    	else if(btText.matches("C")){
			initVal();
			return;
    	}
		
    	else if(btText.matches("D")){
	        
			if (frontExp.equals(""))
				return;
			
			if(resultString != ""){
				initVal();
				return;
			}
			
			frontExp = frontExp.substring(0, frontExp.length()-1);
			printET.setText(frontExp + rearExp);
			printET.setSelection(cursorIndex - 1);
			return;
    	}
		
		if ( cursorEnd || (inputString == "=") )
			printET.setSelection(printET.getText().length());
		else if ( !cursorEnd && inputString.matches("(\\(\\))|(0\\.)") )
			printET.setSelection(cursorIndex + 2);
		else if ( cursorIndex < printET.getText().length() )
			printET.setSelection(cursorIndex + 1);
	}
}