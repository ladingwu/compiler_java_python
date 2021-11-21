package com.example.compilelib;

import com.example.compilelib.parse.TokenReader;
import com.example.compilelib.parse.ParseSemantics;
import com.example.compilelib.parse.ParseToken;
import com.example.compilelib.parse.TokenList;

public class Main {

	public static void main(String[] args) {
		System.out.println("开始分析");
		TokenList tokenList = new TokenReader().getTokenList(); // 通过读取代码文件，获取token列表
		ParseToken pt =new ParseToken(tokenList);
		pt.startParse(); // 把token解析成语法树
		if(pt.getExpFun() != null){
			System.out.println("语法树构建完成");
			ParseSemantics ps=new ParseSemantics(pt.getExpFun());
			ps.parseSemantics(); // 解析语法树
		}
	}

}
