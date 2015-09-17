package parse;

import java.util.ArrayList;
import java.util.List;

public class Token_List {
     private List<StringBuilder> list=null;
	 public Token_List(){
		this.list=new ArrayList<StringBuilder>();
	}
	 //把所有的token加入到列表中
	 public boolean appendToken(StringBuilder sb){
		boolean flag;
		flag=this.list.add(sb);
		 
		 return flag;
		 
	 }
	 //返回一个token
	 public String getToken(int index){
		 StringBuilder sb=null;
		 if(index>-1 && index<this.list.size()){
			 sb=this.list.get(index);
		 }
		 else{
			 System.exit(0);  //否则退出虚拟机
		 }
		 
		 return sb.toString();
	 }
	 public int getsize(){
		 return this.list.size();
	 }
	
}
