package com.example.compilelib.parse;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

public class TokenReader {
	TokenList tk=null;
	public TokenReader(){
	}

	public void getToken() {
		try {

//			String script    ="D:\\android_dev_project\\source\\CompileApplication\\compilelib\\token.py";
//			String directory = new File(script).getParent();
//			String exeCmd ="C:\\Users\\wuzhao\\AppData\\Local\\Programs\\Python\\Python38\\python.exe";
//			ProcessBuilder processBuilder = new ProcessBuilder(exeCmd, script);
			// 实际上把代码文件 test.t里面的代码字符解析成功token的逻辑主要在 token.py里面，结果输出到流里，然后通过Java读取进来
			Process pr=Runtime.getRuntime().exec("C:\\Users\\wuzhao\\AppData\\Local\\Programs\\Python\\Python38\\python.exe ./compilelib/token.py ./compilelib/test.t");
			BufferedReader in =new BufferedReader(new InputStreamReader(pr.getInputStream()));
			String line;
			tk=new TokenList();
			
			String [] wrong=null;
			int result= pr.waitFor();
			if (result == 0){
				System.out.println("python 成功");
			}else {
				System.out.println("python 进程执行失败");
			}
			// 输出token，(type:char:line:row)
			while((line=in.readLine())!=null){
				wrong=line.split(":");

				if(wrong[0].equals("word")){
					System.out.println(line);
					tk=null;
					break;
				}
				if(!wrong[1].equals("\"")){
				tk.appendToken(new StringBuilder(line));
//				System.out.println(line);
				}
			}
			in.close();
		} catch (Exception e) {

			e.printStackTrace();
		}
	}
  public TokenList getTokenList(){
	  if (tk == null) {
		  getToken();
	  }
	  return this.tk;
  }
}


