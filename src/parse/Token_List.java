package parse;

import java.util.ArrayList;
import java.util.List;

public class Token_List {
     private List<StringBuilder> list=null;
	 public Token_List(){
		this.list=new ArrayList<StringBuilder>();
	}
	 //�����е�token���뵽�б���
	 public boolean appendToken(StringBuilder sb){
		boolean flag;
		flag=this.list.add(sb);
		 
		 return flag;
		 
	 }
	 //����һ��token
	 public String getToken(int index){
		 StringBuilder sb=null;
		 if(index>-1 && index<this.list.size()){
			 sb=this.list.get(index);
		 }
		 else{
			 System.exit(0);  //�����˳������
		 }
		 
		 return sb.toString();
	 }
	 public int getsize(){
		 return this.list.size();
	 }
	
}