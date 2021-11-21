package com.example.compilelib.parse;

import java.awt.SystemColor;
import java.util.ArrayList;
import java.util.List;

//enum type_id {NUM,STRING,BOOL,NORMAL_NUM,NORMAL_STRING};
enum type_com {BIG, LITTLE, EQUAL, BIG_EQUAL, LITTLE_EQUAL};

enum type_cal {
    BIG, LITTLE, EQUAL, BIG_EQUAL, LITTLE_EQUAL,
    GOT, ADD, SUB, MUL, DIV, ID, NUM, STRING, BOOL, NORMAL_NUM,
    NORMAL_STRING, TRANS_NUM, TRANS_STRING, TRANS_BOOL, FUNC, WHI, IFE
};

enum type_ret {NUM, STRING, BOOL};

class Exp {
    type_cal type;
    String line;
    String row;
}

//数值或者变量
class Exp_mark extends Exp {
    String value;
    List<Exp> list_value = null;

    public void setListValue(Exp s) {
        if (list_value == null) {
            list_value = new ArrayList<Exp>();
        }
        list_value.add(s);
    }

    Exp got = null;  //判断变量后面是否是一个等号
}

class Exp_type extends Exp {
    Exp Id = null; //定义的变量
}

class Exp_calcul extends Exp {
    Exp left;
    Exp right;
}

//条件
class Exp_cond extends Exp {
    Exp left;
    Exp_comp exp_comp;
    Exp right;
}

//比较符号
class Exp_comp extends Exp {

}

//函数定义
class Exp_func extends Exp {
    Exp_funcname name;

    List<Exp> list_exp = null;


    public void setExp(Exp exp) {
        if (list_exp == null) {
            list_exp = new ArrayList<Exp>();
        }
        list_exp.add(exp);
    }

}

class Exp_funcname extends Exp {
    String name = null;
}


class Exp_whi extends Exp {

    Exp_cond condition;

    Exp next = null;

}

class Exp_ife extends Exp {
    Exp_cond condition;

    Exp left = null;
    Exp right = null;

}

class Exp_B extends Exp {

    List<Exp> list_exp = null;

    public void setList_exp(Exp exp) {
        if (list_exp == null) {
            list_exp = new ArrayList<Exp>();
        }
        list_exp.add(exp);
    }
}