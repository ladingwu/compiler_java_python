package analyize_error;

public class Error {
     public Error(){
    	 
     }
     public void error_print(int id,String content,String line,String row){
    	 String error_content=null;
    	 switch(id){
    		 case 0:error_content="expression was in wrong place. ";break;
    		 case 1:error_content="expression was not wrote rightly. ";break;
    		 case 2:error_content="misss "+content+" .";break;
    		 case 3:error_content="variable "+content+" was not defined. ";break;
    		 case 4:error_content="the type was wrong (type only num,string,bool).";break;
    		 case 5: error_content="the variable"+content+" can't be used. ";break;
    		 case 6:error_content="the mark "+content+" was missing. ";break;
    		 case 7 :error_content="the key word "+content+" was wrong.";break;
    		 case 8: error_content="the type "+content+" can not match.";break;
    		 case 9 :error_content="the type "+content+" can not be calculatered. ";break;
    		 default:error_content="something wrong--";break;
    	 }
    	 print(error_content,line,row);
    	 
     }
     public void print(String content,String line,String row){
    	System.out.println(content+"in line:"+line+" row:"+row); 
     }
     
}
