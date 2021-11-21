package com.example.compilelib.parse;

import com.example.compilelib.Error;

public class ParseToken {
    boolean flag_main = false;  //寻找main()
    boolean flag_token = false, //防止扫描token时错开
            flag_print = false;
    TokenList tk_list = null;
    static int index = 0;
    int count = 0;  //count 记录一共有多少个错误，index全局变量是用于取token
    Exp_func exp_fun = null;
    Error error = null;

    public ParseToken(TokenList tokenList) {
        this.tk_list = tokenList;
    }

    public void startParse(){
        if (this.tk_list != null) {
            error = new Error();
            parse_start();
        }
    }
    public Exp_func getExpFun(){
        return exp_fun;
    }
    private String[] getToken(int index) {
        return this.tk_list.getToken(index).toString().split(":");
    }
    private void printSelf() {
        int i = 0;
        while (i < this.tk_list.getsize()) {
            String[] s = getToken(i);
            System.out.println(s[0]+" "+s[1]+" "+s[2]+" "+s[3]);
            i++;
        }
    }
    private void parse_start() {
        printSelf();
        while (index < this.tk_list.getsize()) {
            String[] s = getToken(index);

            if (s[0].equals("func")) {
                index++;
                parse_P();
            } else {
                count++;
                error.error_print(0, getToken(index)[1], getToken(index)[2], getToken(index)[3]);
                index++;
                break;
            }
        }
        if (count > 0) {
            exp_fun = null;  //如果语法分析有错误，则将抽象语法树置空；
        } else {
            System.out.println(count + " error(s)");
            System.out.println("parse over!!");
        }
    }
    //比较token
    private boolean compare_token(String s) {
        boolean flag = false;

        if (getToken(index)[0].equals(s)) {
            flag = true;

        }
        return flag;
    }
    //验证标识符
    private void parse_mark(String s) {
        print(s);
        if (getToken(index)[0].equals(s)) {
            index++;
        } else {
            if (flag_token) {
                if (getToken(index - 1)[0].equals(s)) {
                    flag_token = false; //在此条件下，游标不移动。
                }
            } else {
                flag_token = true;
                count++;
                String[] k = getToken(index);
                index++;
                error.error_print(6, s, k[2], k[3]);
            }
        }

    }
    //验证关键字
    private boolean recognize_keyword(String key) {
        //print(key);
        boolean flag = false;
        if (getToken(index)[0].equals(key)) {
            index++;
            flag = true;
        } else {

            if (flag_token == true) { //若前面的token漏写了，则整体扫描会错位
                if (getToken(index - 1)[0].equals(key)) {
//					index++;
                    flag = true;
                    flag_token = false;
                }
            } else {

                count++;
                error.error_print(7, getToken(index)[1], getToken(index)[2], getToken(index)[3]);
                flag_token = true;
//			    index++;
            }
        }
        return flag;
    }

    private Exp_func parse_P() {

        exp_fun = new Exp_func();
        exp_fun.type = type_cal.FUNC;
        exp_fun.name = parse_N();
        parse_mark("(");
        if (!getToken(index)[0].equals(")")) {
            error.error_print(0, getToken(index)[1], getToken(index)[2], getToken(index)[3]);
            index++;
        }
        parse_mark(")");
        parse_mark("{");

        String next_token = getToken(index)[0];

        while (!next_token.equals("}")) {
            exp_fun.setExp(parse_if_wh());
            next_token = getToken(index)[0];
        }
        index++;
        return exp_fun;
    }

    //由于if 和while经常交叉使用，相互包含，故孤立出一个函数
    private Exp parse_if_wh() {
        Exp exp = null;
        String nexttoken = getToken(index)[0];
        switch (nexttoken) {
            case "if":
                exp = parse_I();
                break;
            case "while":
                exp = parse_W();
                break;
//		case "else":exp=parse_I();break;
            case "num":
            case "string":
            case "bool":
                exp = parse_M();
                break;
            default:
                exp = parse_E();
                break;
        }
        return exp;
    }
    //验证while语句块：
    private Exp_whi parse_W() {
        Exp_whi whi = new Exp_whi();
        whi.type = type_cal.WHI;
        recognize_keyword("while");
        parse_mark("(");
        whi.condition = parse_C();
        parse_mark(")");
        parse_mark("{");
        boolean flag = true;
        Exp_B exp_b = new Exp_B();
        while (flag) {
//				parse_if_wh();
            Exp exp = null;
            String nexttoken = getToken(index)[0];
            switch (nexttoken) {
                case "if":
                    exp = parse_I();
                    break;
                case "while":
                    exp = parse_W();
                    break;
//				case "else":exp=parse_I();break;
                case "num":
                case "string":
                case "bool":
                    exp = parse_M();
                    break;
                default:
                    exp = parse_E();
                    break;
            }

            exp_b.setList_exp(exp);

            flag = !compare_token("}");
            if (flag && flag_token) {
                if (getToken(index - 1)[0].equals("}")) {
                    flag = false;
                }
            }
        }
        if (!flag_token) {
            index++;
        } else {
            flag_token = false;
        }
        whi.next = exp_b;
        return whi;
    }

    private Exp parse_I() {
        Exp_ife ife = null;
//		Exp_B exp_b=null;
        if (ife == null) {
            ife = new Exp_ife();

        }
        ife.type = type_cal.IFE;
        if (recognize_keyword("if")) {
            parse_mark("(");
            ife.condition = parse_C();
            parse_mark(")");
            parse_mark("{");
            Exp_B exp_b = new Exp_B();
            while (!compare_token("}")) {
//			parse_if_wh();
                Exp exp = null;
                String nexttoken = getToken(index)[0];
                switch (nexttoken) {
                    case "if":
                        exp = parse_I();
                        break;
                    case "while":
                        exp = parse_W();
                        break;
//			case "else":exp=parse_I();break;
                    case "num":
                    case "string":
                    case "bool":
                        exp = parse_M();
                        break;
                    default:
                        exp = parse_E();
                        break;


                }
                exp_b.setList_exp(exp);
                ife.left = exp_b;
            }
            index++;
        }
        if (getToken(index)[1].equals("else")) {   //此处不同recognize_key()函数是考虑到没有else，也不必报错
            index++;
            parse_mark("{");
            Exp_B exp_b = new Exp_B();
            while (!compare_token("}")) {
//				parse_if_wh();
//				ife.right=dothis();
                Exp exp = null;
                String nexttoken = getToken(index)[0];
                switch (nexttoken) {
                    case "if":
                        exp = parse_I();
                        break;
                    case "while":
                        exp = parse_W();
                        break;
//				case "else":exp=parse_I();break;
                    case "num":
                    case "string":
                    case "bool":
                        exp = parse_M();
                        break;
                    default:
                        exp = parse_E();
                        break;

                }
                exp_b.setList_exp(exp);
                ife.right = exp_b;
            }
            index++;
        }

        return ife;
    }

    //验证表达式
    private Exp_mark parse_E() {
//		Exp_mark exp_m=(Exp_mark) getLine_Row(new Exp_mark(),getToken(index));
//		exp_m.type=type_cal.ID;
//		exp_m.value=parse_V();
        Exp_mark exp_m = parse_id();
        if (getToken(index)[0].equals("=")) {
            index++;
            exp_m.got = parse_K();
        }
        parse_mark(";");
        return exp_m;
    }


    //验证表达式=右边
    /*
     * K->GG'
     * G'->+GG'
     * G'->$
     */
    //一个表达式的抽象语法树的根节点没有type;
    private Exp_calcul parse_K() {
        Exp_calcul exp_calcul = (Exp_calcul) getLine_Row(new Exp_calcul(), getToken(index));
        exp_calcul.left = parse_G();
        exp_calcul.right = parse_G_();
        return exp_calcul;
    }

    private Exp_calcul parse_G_() {
        Exp_calcul exp_calcul = null;
        if (getToken(index)[0].equals("+")) {

            exp_calcul = (Exp_calcul) getLine_Row(new Exp_calcul(), getToken(index));
            index++;
            exp_calcul.type = type_cal.ADD;
            exp_calcul.left = parse_G();
            exp_calcul.right = parse_G_();

        } else if (getToken(index)[0].equals("-")) {
            exp_calcul = (Exp_calcul) getLine_Row(new Exp_calcul(), getToken(index));
            index++;
            exp_calcul.type = type_cal.ADD;
            exp_calcul.left = parse_G();
            exp_calcul.right = parse_G_();

        } else {

        }

        return exp_calcul;
    }

    /*消除左递归：G->G*A|A
     * G->VA_
     * A_->*VA_
     * A_->$
     */
    private Exp_calcul parse_G() {
//		Exp_mark exp_m=null;
        Exp_calcul exp_calcul = new Exp_calcul();
        exp_calcul.left = parse_id();
        exp_calcul.right = parse_A_();
        return exp_calcul;
    }

    //在此处解析的时候回返回一个以计算符号为根节点的树，无法保证右子树是否为空
    private Exp_calcul parse_A_() {
//		Exp_mark exp_m=null;
        Exp_calcul exp_calcul = null;

        if (getToken(index)[0].equals("*")) {

            exp_calcul = (Exp_calcul) getLine_Row(new Exp_calcul(), getToken(index));
            index++;
            exp_calcul.type = type_cal.MUL;
            exp_calcul.left = parse_id();
            exp_calcul.right = parse_A_();
        } else if (getToken(index)[0].equals("/")) {
            exp_calcul = (Exp_calcul) getLine_Row(new Exp_calcul(), getToken(index));
            index++;
            exp_calcul.type = type_cal.DIV;
            exp_calcul.left = parse_id();
            exp_calcul.right = parse_A_();
            //do nothing
        } else {

        }
        return exp_calcul;
    }

    private Exp_mark parse_id() {
        String[] s = getToken(index);
        Exp_mark exp = null;
        exp = (Exp_mark) getLine_Row(new Exp_mark(), s);

        if (compare_token("id")) {
            exp.value = s[1];
            exp.type = type_cal.ID;
            index++;
        } else if (compare_token("id_string")) {
            index++;
            exp.value = s[1];
            exp.type = type_cal.NORMAL_STRING;

        } else if (compare_token("id_num")) {
            index++;
            exp.value = s[1];
            exp.type = type_cal.NORMAL_NUM;
        } else {
            count++;
            error.error_print(1, getToken(index)[1], getToken(index)[2], getToken(index)[3]);
            index++;
        }

        return exp;
    }



//	public String parse_(){
//		String[] s=getToken(index);
////		Exp_mark exp=null;
////		exp=(Exp_mark) getLine_Row(new Exp_mark(),s);
//		
//		if(compare_token("id")){
////			exp.value=s[1];
////			exp.type=type_cal.ID;
//			index++;
//		}else if(compare_token("id_string")){
//			index++;
////			exp.value=s[1];
////			exp.type=type_cal.NORMAL_STRING;
//			
//		}else if(compare_token("id_num")){
//			index++;
////			exp.value=s[1];
////			exp.type=type_cal.NORMAL_NUM;
//		}
//		else{
//			count++;
//			error.error_print(1, getToken(index)[1], getToken(index)[2], getToken(index)[3]);
//			index++;
//		}
//		
//		return s[1];
//	}
    //��֤�������ʽ
    private Exp_cond parse_C() {
        Exp_cond exp_condition = new Exp_cond();
        exp_condition.left = parse_id();
        exp_condition.exp_comp = parse_comp();
        exp_condition.right = parse_id();
        if (getToken(index)[0].equals("&") || getToken(index).equals("|")) {
            index++;
            parse_C();
        }

//			index++;
        return exp_condition;
    }

    //�Ƚ������
    private Exp_comp parse_comp() {
        String[] s = getToken(index);
        Exp_comp exp_compare = (Exp_comp) getLine_Row(new Exp_comp(), s);
        if (s[0].equals("<")) {
            index++;
            exp_compare.type = type_cal.LITTLE;
        } else if (s[0].equals(">")) {
            index++;
            exp_compare.type = type_cal.BIG;
        } else if (s[0].equals(">=")) {
            index++;
            exp_compare.type = type_cal.BIG_EQUAL;
        } else if (s[0].equals("<=")) {
            index++;
            exp_compare.type = type_cal.LITTLE_EQUAL;
        } else if (s[0].equals("==")) {
            index++;
            exp_compare.type = type_cal.EQUAL;
        } else {
            if (flag_token) {
                String[] s1 = getToken(index - 1);
                if (s1[0].equals(">") || s1[0].equals(">") || s1[0].equals(">=") || s1[0].equals("<=") || s1[0].equals("==")) {
                    flag_token = false;
                }
            } else {
                count++;
                flag_token = true;
                error.error_print(1, s[1], s[2], s[3]);
                index++;
            }
        }
        return exp_compare;
    }

    //	//ֻ������֤�������ݵĲ�������
//	public Exp_type parse_J(){
////		Exp_defvar def_var=new Exp_defvar();
//		Exp_type exp_type=(Exp_type) getLine_Row(new Exp_type(),getToken(index));
//		exp_type.type=parse_T();
//		exp_type.Id=parse_id();
//		if(exp_type.type==type_cal.NUM){
//			exp_type.type=type_cal.TRANS_NUM;
//		}else if(exp_type.type==type_cal.STRING){
//			exp_type.type=type_cal.TRANS_STRING;
//		}else{
//			exp_type.type=type_cal.TRANS_BOOL;
//		}
//		return exp_type;
//	}
    //��֤������
    private Exp_funcname parse_N() {
        Exp_funcname fun_name = new Exp_funcname();
        String s[] = getToken(index);
        if (s[0].equals("main")) {
            flag_main = true;
            index++;
            fun_name.name = s[1];
            fun_name.line = s[2];
            fun_name.row = s[3];
            fun_name.type = type_cal.STRING;
        } else {
            if (flag_token) {
                if (getToken(index - 1)[0].equals("main")) {
                    flag_token = false;
                }
            } else {
                count++;
                flag_token = true;
                error.error_print(2, getToken(index - 1)[1], getToken(index - 1)[2], getToken(index - 1)[3]);
                index++;

            }
        }
        return fun_name;
    }

    //验证定义变量的表达式
    /*
     * string a,b;
     */
    private Exp_type parse_M() {
        Exp_type exp_type = (Exp_type) getLine_Row(new Exp_type(), getToken(index));
        exp_type.type = parse_T();
        exp_type.Id = parse_M__();
        return exp_type;
    }

    private Exp_mark parse_M__() {
        Exp_mark exp = parse_id();
        while (getToken(index)[0].equals(",")) {
            index++;
            exp.setListValue(parse_id());
        }
        parse_mark(";");
        return exp;
    }

    //验证token的类型名
    private type_cal parse_T() {
        String[] s = getToken(index);
        type_cal type = type_cal.STRING;
        if (s[0].equals("num")) {
            index++;
            type = type_cal.NUM;
        } else if (s[0].equals("bool")) {
            index++;
            type = type_cal.BOOL;
        } else if (s[0].equals("string")) {
            index++;
        } else {
            if (flag_token) {
                if (getToken(index - 1)[0].equals("num") || getToken(index - 1)[0].equals("bool") || getToken(index - 1)[0].equals("string")) {
                    flag_token = false;
                }
            } else {
                count++;

                flag_token = true;
                error.error_print(4, s[1], s[2], s[3]);
                index++;
            }
        }
        return type;
    }

    private void print(String s) {
        if (flag_print) {
            System.out.println(s);
        }
    }

    private Exp getLine_Row(Exp exp, String[] s) {
        exp.line = s[2];
        exp.row = s[3];
        return exp;
    }
}
