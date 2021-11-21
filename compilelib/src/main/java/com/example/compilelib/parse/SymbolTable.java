package com.example.compilelib.parse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/*
 *
 */

public class SymbolTable {
	Map<String, Table> symbolMap =null;
	public SymbolTable(){
	  symbolMap =new HashMap<String,Table>();
  }
	public void put(String key,Table value){
//		boolean flag=flase;
		if(symbolMap !=null){
			
			if(symbolMap.get(key)!=null){
				value.nexttable= symbolMap.get(key);
				symbolMap.put(key, value);
			}else{
				symbolMap.put(key, value);
			}
		}
	}
	public void delete(String key){
		if(symbolMap !=null){
			if(symbolMap.get(key).nexttable!=null){
				symbolMap.put(key, symbolMap.get(key).nexttable);
			}else{
				symbolMap.remove(key);
			}
		}
	}

	public type_cal check_def(String key){
		boolean flag=false;
		type_cal t=null;
		if(symbolMap !=null){
			flag= symbolMap.containsKey(key);
			if(flag){
				symbolMap.get(key).apoint++;
				t= symbolMap.get(key).type;
			}
		}
		return t;
	}
	
	public int get_appoint(String key){
		return symbolMap.get(key).apoint;
	}
	public List<Integer> check_Dimension(int dim){

		Iterator it= symbolMap.entrySet().iterator();
		List<String> list=new ArrayList<String>();

		while(it.hasNext()){
			
			Entry<String, Table> map=(Entry<String, Table>) it.next();
//			System.out.println(map.getKey());
//			if(m.get(map.getKey())!=null)
			if(symbolMap.get(map.getKey()).Dimension>dim){
//				delete(map.getKey());
//				System.out.println("---"+map.getKey());
				list.add(map.getKey());
			}
		}
		int i=0;
		List<Integer> list_int=null;
		while(i<list.size()){
			list_int=new ArrayList<Integer>();
			list_int.add(check_appoint(list.get(i)));
			
			delete(list.get(i));
			i++;
		}
		return list_int;
	}
	public int check_appoint(String key){
		int a=-1;
		if(symbolMap !=null){
			if(symbolMap.get(key)!=null){
				a= symbolMap.get(key).apoint;
			}
		}
		if(a==0){
			
		}
		return symbolMap.get(key).Dimension;
	}
}

class Table{
	type_cal type;
	int apoint=0;
	int Dimension=1;
	int code_index=0;
	Table nexttable=null;
}