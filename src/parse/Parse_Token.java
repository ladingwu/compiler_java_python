package parse;

public class Parse_Token {
	boolean flag_main=false; //寻找main()
	boolean flag_token=false,flag_print=false;//防止扫描token时错开
	Token_List tk_list=null;
	static int  index=0;
	int count=0;//count 记录一共有多少个错误，index全局变量是用于取token
	Exp_func exp_fun=null;
	analyize_error.Error error=null;
	public Parse_Token(){
		this.tk_list=new HaveToken().getTokenList();
		if (this.tk_list!=null){
			error=new analyize_error.Error();
			parse_start();
		}
	}
	public String[] getToken(int index){		
		return this.tk_list.getToken(index).toString().split(":");
	}
	public void parse_start(){
		while(index<this.tk_list.getsize()){
			String []s=getToken(index);
//			System.out.println(s[0]);
		if(s[0].equals("func")){
			index++;
			parse_P();
		}
//		else if(getToken(index)[0].equals("num") || getToken(index)[0].equals("string") || getToken(index)[0].equals("bool")){
//			
//			parse_M();
//		}
		else{
			count++;
			error.error_print(0, getToken(index)[1], getToken(index)[2], getToken(index)[3]);
			index++;
		}
		}
		if(count>0){
			exp_fun=null;  //如果语法分析有错误，则将抽象语法树置空；
		}else{
		System.out.println(count+" error(s)");
		System.out.println("parse over!!");
		}
	}
	//比较token
	public boolean compare_token(String s){
		boolean flag=false;
		
		if(getToken(index)[0].equals(s)){
			flag=true;
		
		}
		return flag;
	}
	//验证标识符
	public void parse_mark(String s){
		print(s);
		if(getToken(index)[0].equals(s)){
			index++;
		}else{
			if(flag_token){
				if(getToken(index-1)[0].equals(s)){
					flag_token=false;   //在此条件下，游标不移动。
				}
			}else{
			flag_token=true;
			
			count++;
			String k[]=getToken(index);
						index++;
						error.error_print(6, s, k[2], k[3]);
			}
		}
	
	}
	//验证关键字
	public boolean recognize_keyword(String key){
		//print(key);
		boolean flag=false;
		if(getToken(index)[0].equals(key)){
			index++;
			flag=true;
		}else{
			
			if(flag_token==true){//若前面的token漏写了，则整体扫描会错位
				if(getToken(index-1)[0].equals(key)){
//					index++;
					flag=true;
					flag_token=false;
				}
			}else{

				count++;
				error.error_print(7,getToken(index)[1],getToken(index)[2],getToken(index)[3]);
			    flag_token=true;
//			    index++;
			}
		}
		return flag;
	}
	
	public Exp_func parse_P(){
//		recognize_keyword("void");
		exp_fun=new Exp_func();
		exp_fun.type=type_cal.FUNC;
		exp_fun.name=parse_N(); //填充name属性
		parse_mark("(");
		if(!getToken(index)[0].equals(")")){
			error.error_print(0, getToken(index)[1], getToken(index)[2], getToken(index)[3]);
			index++;
//			exp_fun.setExp(parse_J());  //添加函数的传递参数；
		}
		parse_mark(")");
		parse_mark("{");
		//parse_B();
		String next_token=getToken(index)[0];
//		boolean flag=true;
		while(!next_token.equals("}")){	

			   exp_fun.setExp(parse_if_wh());

			next_token=getToken(index)[0];
		}
			index++;
	   return exp_fun;
	}
	
	//由于if 和while经常交叉使用，相互包含，故孤立出一个函数
	public Exp parse_if_wh(){
		Exp exp=null;
		String nexttoken=getToken(index)[0];
		switch(nexttoken){
		case "if" :exp=parse_I();break;
		case "while":exp=parse_W();break;
//		case "else":exp=parse_I();break;
		case "num":
		case "string":
		case "bool":exp=parse_M();break;
		default: exp=parse_E();break;
		}
		return exp;
	}
	//验证函数体内部
//	public void parse_B(){
//		String next_token=getToken(index)[0];
////		boolean flag=true;
//		while(!next_token.equals("}")){	
////			next_token=getToken(index)[0];
////			if (next_token.equals("return")){
////			index++;	
////			parse_R();
////			}else{
//			   parse_if_wh();
////			}
////			flag=!compare_token("}");
//			next_token=getToken(index)[0];
//		}
//	index++;
//	}
	//验证while语句块：
	public Exp_whi parse_W(){
			Exp_whi whi=new Exp_whi();
			whi.type=type_cal.WHI;
			recognize_keyword("while");
			parse_mark("(");
			whi.condition=parse_C();
			parse_mark(")");
			parse_mark("{");
			boolean flag=true;
			Exp_B exp_b=new Exp_B();
			while(flag){
//				parse_if_wh();
				Exp exp=null;
				String nexttoken=getToken(index)[0];
				switch(nexttoken){
				case "if" :exp=parse_I();break;
				case "while":exp=parse_W();break;
//				case "else":exp=parse_I();break;
				case "num":
				case "string":
				case "bool":exp=parse_M();break;
				default: exp=parse_E();break;
				}
				
				exp_b.setList_exp(exp);
				
				flag=!compare_token("}");
				if(flag && flag_token){
					if (getToken(index-1)[0].equals("}")){
						flag=false;
					}
				}
			}
			if(!flag_token){
				index++;
			}else{
				flag_token=false;
			}
			whi.next=exp_b;
			return whi;
	}
	//验证return语句：
//	public void parse_R(){
//		parse_V();
//		parse_mark(";");
//	}
//	//验证else语句块
//	public void parse_EL(){
//		recognize_keyword("else");
//		
//		parse_mark("{");
//		while(!compare_token("}")){
//			parse_if_wh();
//		}
//		index++;
//	}
	//验证if-else语句块
	public Exp parse_I(){
		Exp_ife ife=null;
//		Exp_B exp_b=null;
		if(ife==null){
			ife=new Exp_ife();
			
		}
		ife.type=type_cal.IFE;
		if(recognize_keyword("if")){
		parse_mark("(");
		ife.condition=parse_C();
		parse_mark(")");
		parse_mark("{");
		Exp_B exp_b=new Exp_B();
		while(!compare_token("}")){
//			parse_if_wh();
			Exp exp=null;
			String nexttoken=getToken(index)[0];
			switch(nexttoken){
			case "if" :exp=parse_I();break;
			case "while":exp=parse_W();break;
//			case "else":exp=parse_I();break;
			case "num":
			case "string":
			case "bool":exp=parse_M();break;
			default: exp=parse_E();break;
			
			
			}
			exp_b.setList_exp(exp);
			ife.left=exp_b;
		  }
		 index++;
		}
		if(getToken(index)[1].equals("else")){  //此处不同recognize_key()函数是考虑到没有else，也不必报错
			index++;//相应的，index++
			parse_mark("{");
			Exp_B exp_b=new Exp_B();
			while(!compare_token("}")){
//				parse_if_wh();
//				ife.right=dothis();
				Exp exp=null;
				String nexttoken=getToken(index)[0];
				switch(nexttoken){
				case "if" :exp=parse_I();break;
				case "while":exp=parse_W();break;
//				case "else":exp=parse_I();break;
				case "num":
				case "string":
				case "bool":exp=parse_M();break;
				default: exp=parse_E();break;
				
				}
				exp_b.setList_exp(exp);
				ife.right=exp_b;
			}
			index++;
		}
		
		return ife;
	}

	//验证表达式
	public Exp_mark parse_E(){
//		Exp_mark exp_m=(Exp_mark) getLine_Row(new Exp_mark(),getToken(index));
//		exp_m.type=type_cal.ID;
//		exp_m.value=parse_V();
		Exp_mark exp_m=parse_id();
		if (getToken(index)[0].equals("=")){
			index++;
			exp_m.got=parse_K();
		}
		parse_mark(";");
		return exp_m;
	}
	//y验证表达式=右边
	/*
	 * K->GG'
	 * G'->+GG'
	 * G'->$
	 */
	//一个表达式的抽象语法树的根节点没有type;
	public Exp_calcul parse_K(){
		Exp_calcul exp_calcul=(Exp_calcul) getLine_Row(new Exp_calcul(),getToken(index));
		exp_calcul.left=parse_G();
		exp_calcul.right=parse_G_();
		return exp_calcul;
	}
	public Exp_calcul parse_G_(){
		Exp_calcul exp_calcul=null;
		if(getToken(index)[0].equals("+")){
			
			exp_calcul=(Exp_calcul) getLine_Row(new Exp_calcul(),getToken(index));
			index++;
			exp_calcul.type=type_cal.ADD;
			exp_calcul.left=parse_G();
			exp_calcul.right=parse_G_();
			
//			parse_G();
//			parse_G_();
		}else if(getToken(index)[0].equals("-")){
			exp_calcul=(Exp_calcul) getLine_Row(new Exp_calcul(),getToken(index));
			index++;
			exp_calcul.type=type_cal.ADD;
			exp_calcul.left=parse_G();
			exp_calcul.right=parse_G_();
			
		}else{
			
		}
		
		return exp_calcul;
	}
	
	/*消除左递归：G->G*A|A
	 * G->VA_
	 * A_->*VA_
	 * A_->$
	 */
	public Exp_calcul parse_G(){
//		Exp_mark exp_m=null;
		Exp_calcul exp_calcul=new Exp_calcul();
		exp_calcul.left=parse_id();
		exp_calcul.right=parse_A_();
		return exp_calcul;
	}
	
	//在此处解析的时候回返回一个以计算符号为根节点的树，无法保证右子树是否为空
	public Exp_calcul parse_A_(){
//		Exp_mark exp_m=null;
		Exp_calcul exp_calcul=null;
		
		if(getToken(index)[0].equals("*")){
			
			exp_calcul=(Exp_calcul) getLine_Row(new Exp_calcul(),getToken(index));
			index++;
			exp_calcul.type=type_cal.MUL;
			exp_calcul.left=parse_id();
			exp_calcul.right=parse_A_();
		}else if(getToken(index)[0].equals("/")){
			exp_calcul=(Exp_calcul) getLine_Row(new Exp_calcul(),getToken(index));
			index++;
			exp_calcul.type=type_cal.DIV;
			exp_calcul.left=parse_id();
			exp_calcul.right=parse_A_();
			//do nothing
		}else{
			
		}
		return exp_calcul;
	}
	
	public Exp_mark parse_id(){
		String[] s=getToken(index);
		Exp_mark exp=null;
		exp=(Exp_mark) getLine_Row(new Exp_mark(),s);
		
		if(compare_token("id")){
			exp.value=s[1];
			exp.type=type_cal.ID;
			index++;
		}else if(compare_token("id_string")){
			index++;
			exp.value=s[1];
			exp.type=type_cal.NORMAL_STRING;
			
		}else if(compare_token("id_num")){
			index++;
			exp.value=s[1];
			exp.type=type_cal.NORMAL_NUM;
		}
		else{
			count++;
			error.error_print(1, getToken(index)[1], getToken(index)[2], getToken(index)[3]);
			index++;
		}
		
		return exp;
	}
	
	
//	//验证变量或者常量
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
	//验证条件表达式
	public Exp_cond parse_C(){
		Exp_cond exp_condition=new Exp_cond();
		exp_condition.left=parse_id();
		exp_condition.exp_comp=parse_comp();
		exp_condition.right=parse_id();
	if(getToken(index)[0].equals("&")||getToken(index).equals("|")){
				index++;
				parse_C();
			}

//			index++;
	return exp_condition;
		}
	
	//比较运算符
	public Exp_comp parse_comp(){
		String[] s=getToken(index);
		Exp_comp exp_compare=(Exp_comp) getLine_Row(new Exp_comp(), s);
		if (s[0].equals("<") ){
			index++;
			exp_compare.type=type_cal.LITTLE;
		}else if(s[0].equals(">")){
			index++;
			exp_compare.type=type_cal.BIG;
		}else if(s[0].equals(">=")){
			index++;
			exp_compare.type=type_cal.BIG_EQUAL;
		}else if(s[0].equals("<=")){
			index++;
			exp_compare.type=type_cal.LITTLE_EQUAL;
		}else if(s[0].equals("==")){
			index++;
			exp_compare.type=type_cal.EQUAL;
		}
		else{
			if(flag_token){
				String[] s1=getToken(index-1);
				if(s1[0].equals(">") ||s1[0].equals(">")||s1[0].equals(">=")||s1[0].equals("<=")||s1[0].equals("==")){
					flag_token=false;
				}
			}else{
				count++;
				flag_token=true;
			error.error_print(1, s[1], s[2], s[3]);
			index++;
			}
		}
		return exp_compare;
	}
//	//只用于验证函数传递的参数定义
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
	//验证函数名
   public Exp_funcname parse_N(){
	   Exp_funcname fun_name=new Exp_funcname();
	   String s[]=getToken(index);
	   if (s[0].equals("main")){
		   flag_main=true;
		   index++;
		   fun_name.name=s[1];
		   fun_name.line=s[2];
		   fun_name.row=s[3];
		   fun_name.type=type_cal.STRING;
	   }else{
		   if(flag_token){
			   if(getToken(index-1)[0].equals("main")){
				   flag_token=false;
			   }
		   }else{
			   count++;
				flag_token=true;
				error.error_print(2, getToken(index-1)[1], getToken(index-1)[2], getToken(index-1)[3]);
				index++;
//		   parse_V();  暂时只允许一个主函数main
		   }
	   }
	   return fun_name;
   }
//验证定义变量的表达式
   /*
    * string a,b;
    */
	public Exp_type  parse_M(){
		Exp_type exp_type=(Exp_type) getLine_Row(new Exp_type(),getToken(index));
		exp_type.type=parse_T();
		exp_type.Id=parse_M__();
		return exp_type;
	}
	
	public Exp_mark parse_M__(){
		Exp_mark exp=parse_id();
		while(getToken(index)[0].equals(",")){
			index++;
			exp.set_listvalue(parse_id());
		}
		parse_mark(";");
		return exp;
	}
	//验证token的类型名
	public type_cal parse_T(){
		String[] s=getToken(index);
		type_cal type=type_cal.STRING;
		if(s[0].equals("num")){
			index++;
			type=type_cal.NUM;
		}
		else if(s[0].equals("bool")){
			index++;
			type=type_cal.BOOL;
		}else if(s[0].equals("string")){
			index++;
		}
		else{
			if(flag_token){
				if(getToken(index-1)[0].equals("num")||getToken(index-1)[0].equals("bool")||getToken(index-1)[0].equals("string")){
					flag_token=false;
				}
			}else{
			count++;
			
			flag_token=true;
			error.error_print(4, s[1], s[2], s[3]);
			index++;
			}
		}
		return type;
	}
	public void print(String s){
		if(flag_print){
		System.out.println(s);
		}
	}
//	//验证变量名
//	public void parse_(){
//		String s[]=getToken(index);
//		if(s[0].equals("id")){
//			index++;
//		}else{
//			if(flag_token){
//				if(getToken(index-1)[0].equals("id")){
//					flag_token=false;
//				}
//			}else{
//				count++;
//				index++;
//				flag_token=true;
//			error.error_print(5, s[1], s[2], s[3]);
//			}
//		}
//	
////		s=null;
//	}
public Exp getLine_Row(Exp exp,String[] s){
	exp.line=s[2];
	exp.row=s[3];
	return exp;
}
}
