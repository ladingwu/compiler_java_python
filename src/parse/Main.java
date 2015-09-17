package parse;

public class Main {

	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		Parse_Token pt =new Parse_Token();
		if(pt.exp_fun!=null){
			System.out.println("抽象语法树构建完毕");
		}
		Parse_Semantics ps=new Parse_Semantics(pt.exp_fun);
	}

}
