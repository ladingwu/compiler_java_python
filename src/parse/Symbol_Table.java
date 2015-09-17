package parse;

import java.nio.MappedByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/*
 * 符号表主要用于管理变量，并且同时兼顾定义域
 */

public class Symbol_Table {
	Map<String, Table> m=null;
	public Symbol_Table(){
	  m=new HashMap<String,Table>();
  }
	public void put(String key,Table value){
//		boolean flag=flase;
		if(m!=null){
			
			if(m.get(key)!=null){
				value.nexttable=m.get(key);
				m.put(key, value);
			}else{
				m.put(key, value);
			}
		}
	}
	public void delete(String key){
		if(m!=null){
			if(m.get(key).nexttable!=null){
				m.put(key, m.get(key).nexttable);
			}else{
				m.remove(key);
			}
		}
	}
	//若查找正确，返回变量类型
	public type_cal check_def(String key){
		boolean flag=false;
		type_cal t=null;
		if(m!=null){
			flag=m.containsKey(key);
			if(flag){
				m.get(key).apoint++;
				t=m.get(key).type;
			}
		}
		return t;
	}
	
	public int get_appoint(String key){
		return m.get(key).apoint;
	}
	public List<Integer> check_Dimension(int dim){
		//删除大于当前维数的变量定义，确保变量的作用域。
		Iterator it=m.entrySet().iterator();
		List<String> list=new ArrayList<String>();
//		hashMap不支持在遍历数据的时候，修改数据，因为迭代器会试试检查数据的大小，如果有变化，就抛出异常
		while(it.hasNext()){
			
			Map.Entry<String, Table> map=(Entry<String, Table>) it.next();
//			System.out.println(map.getKey());
//			if(m.get(map.getKey())!=null)
			if(m.get(map.getKey()).Dimension>dim){
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
		if(m!=null){
			if(m.get(key)!=null){
				a=m.get(key).apoint;
			}
		}
		if(a==0){
			
		}
		return m.get(key).Dimension;
	}
}

class Table{
	type_cal type;
	int apoint=0;   //引用个数
	int Dimension=1; //维数
	int code_index=0;   //用于记录定义变量的语句的索引
	Table nexttable=null;
}