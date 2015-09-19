import os,sys
msg_table=['func','main','if','while',
'else','num','string','bool','(',')',
';',',','{','}','[',']','=','+','-','*'
,'/','%','&','|','>','<','==','>=','<=','"','"','id']
def read_token(file):
    with open(file,'r')as f:
        return f.readlines()

def token_recogn(lines):
    msg_line=0
    #k='qwertyuioplkjhgfdsazxvcbnm_'
    #s=k.upper()
    q1='>='
    q2='<='
    q3='=='
    m=',;+=*/-(){}""><'
    wrong_mark='；，。；“”【】'
    for line in lines:
        words=''
        msg_line+=1
        msg_row=0
        str_i=0
        msg=''
        for word in line:
            if word ==' ' or word =='\t' or word=='\n':
                if(word=='\t'):
                    msg_row+=4
                if words != '':
                    deal_words(words, msg_line, msg_row)
                    words=''
            elif word in m :
                if(msg_row+1<len(line) and line[msg_row+1]=='='):
                    msg=msg+word
                else:
                    if len(words)>0:

                        deal_words(words,msg_line,msg_row-1)
                        deal_words(msg+word, msg_line,msg_row)
                        msg=''
                    else:
                        deal_words(msg+word,msg_line,msg_row)
                        msg=''
                    words=''
            elif word in wrong_mark:
                if len(words)>0:
                    
                    deal_words(words,msg_line,msg_row-1)
                    deal_words(word, msg_line,msg_row)

                else:
                    deal_words(word,msg_line,msg_row)
            else:               
                words=words+word
            msg_row+=1
def check_id(s):
        m='qwertyuiopasdfghjklzxcvbnm_'
        n=m.upper()
        k='0987654321'
        flag=True
        #print(n)
        j=0
        for i in s:
            if j==0:
                j+=1
                if i in k:
                    flag=False
                    break
            
            if i not in m and i not in n and i not in k:

                flag=False
                break
        return flag
def check_num(s):
    k="0987654321"
    flag=True
    for i in s:
        if i  not in k:
            flag=False
            break
    return flag  
last_word=''  #为了读取字符串
count=0            
def deal_words(words,msg_line,msg_row):
    i=0
    # print(words)
    global msg_table,last_word,count
    length=len(msg_table)
    #print('-----------------------------------',words)
    if words=='"':
        count+=1
    if (count%2)!=0 and last_word=='"':
        last_word=''
        # 表示字符常量
        print('id_string'+':'+words+':'+str(msg_line)+':'+str(msg_row))
        return
    
    for t in msg_table:    
        if i==length-1:

            if check_id(words):
                print('id'+':'+words+':'+str(msg_line)+':'+str(msg_row))
                break
            elif check_num(words):
                # 表示常数变量
                print('id_num'+':'+words+':'+str(msg_line)+':'+str(msg_row))
                break
            else:
                error(words,msg_line,msg_row)
                break
        if words==t:
            if i<8:
                
                print(words+':'+words+':'+str(msg_line)+':'+str(msg_row))
            elif i<length-1:
                print(words+':'+words+':'+str(msg_line)+':'+str(msg_row))
            break
            #return msg_table[i]
        i+=1
        last_word=words;
def error(s,line,row):
    print('word:'+s+' was illegal, in ','line: '+str(line),"row: "+str(row))

if __name__=="__main__":
    filename=sys.argv[1]
    list_token=read_token(filename)
    token_recogn(list_token)
