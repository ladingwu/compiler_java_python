# compiler_java_python
简易编译器实现，最终翻译成伪汇编代码
##编译器主要编译t语言（暂且这么称呼）

##t语言关键字：
- func  用于定义函数
- main  主函数名，文件中必须包含主函数
- while  循环关键字
- if 条件分支 关键字
- else 必须与if 一起才能识别
- num 数字类型
- string 字符串类型
- bool 布尔值类型

## t语言的比较符号：
- >
- <
- >=
- <=
- ==

示例：


    func  main(){
    
    num a,b;
    
    a=1;
    
    b=0;
    
    while(b<5){
	    
	    num c,k;
	    
	    b=b+1;
	    
	    if(b<3){
		    
		    c=b+b*a;
	    
	    }else{
		  
		    c=b;
	    
	    }
    }
    
    num d,c;
    
    c=1;
    
    a=b\*a\*c+b*4;

  }

输出伪汇编代码（三地址码的四元式）格式为：<操作符，操作数1，操作数2，结果存放>
以上示例的输出结果：
- DEFINE , num , null , a
- DEFINE , num , null , b
- SEND , 1 , null , a
- SEND , 0 , null , b
- LABEL , null , null , Label1
- CJMP_L , b , 5 , Label2
- DEFINE , num , null , c
- DEFINE , num , null , k
- ADD , b , 1 , $
- SEND , $ , null , b
- CJMP_L , b , 3 , Label3
- MUL , b , a , $
- ADD , b , $ , $
- SEND , $ , null , c
- LABEL , null , null , Label3
- SEND , b , null , c
- JMP , null , null , Label1
- LABEL , null , null , Label2
- DEFINE , num , null , d
- DEFINE , num , null , c
- SEND , 1 , null , c
- MUL , b , a , $
- MUL , $ , c , $
- MUL , b , 4 , $
- ADD , $ , $ , $
- SEND , $ , null , a




##个别操作符的含义：
- DEFINE: 定义变量
- SEND : 赋值
- JMP: 无条件跳转
- CJMP_E: 等于时执行下面的语句，否则跳转到某个label
- CJMP_B : 大于时........
- CJMP_B_E : 大于等于的时候...... 
- LABEL: 定义一个标签（用于跳转）
- $ : 用于存放左边操作数运算的结果，或者代表上面的运算结果，主要作用是把被分解的表达式连接起来



## 补充说明：
1. 为了简化编译工作，t语言只支持一个mian(),函数，所以不分析函数之间的跳转
2. 为了简化编译工作，t语言只支持变量之间的加减乘除，自动识别运算优先级，不允许使用（）
3. 为了简化编译工作，t语言以只支持while这一种循环，而且不支持break跳出循环。
4. 由于第一次编写编译器，所以很多工作可能不是很好，但是对于理解编译原理，还是有很大帮助的，有意向的同学欢迎交流....
