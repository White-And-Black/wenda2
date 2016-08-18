package com.Qiao;

import org.springframework.stereotype.Controller;

/**
 * Created by white and black on 2016/8/16.
 */
class Container{
    Object val;
}
public class StringTest {



    static void change(Container obj,Object newVal){
        obj.val = newVal;
    }

    static void swap(Container val1,Container val2){
        Object temp = val1.val;
        val1.val = val2.val;
        val2.val = temp;
    }

    public static void main(String[] args){
        String begin="ABCD";
        System.out.println("begin in main :"+begin.hashCode());
        int a=19;
        String end="ABCD";
        change(begin,a);
        System.out.println(begin.hashCode());
        StringBuffer s=new StringBuffer();
        s.append("hh");
        s.append("ss");
        System.out.println(s.toString());

//        System.out.println(a);
//        Container c = new Container();
//        c.val = new String("hello");
//        System.out.println(c.val);
//        change(c,"hahahah");
//        System.out.println(c.val);

//        Container a = new Container();
//        a.val = 10;
//        Container b = new Container();
//        b.val = 20;
//        System.out.printf("a:%d b:%d\n",a.val,b.val);
//        swap(a,b);
//        System.out.printf("a:%d b:%d\n",a.val,b.val);
    }
    public static void change(String begin,int a){
        begin=new String("EFGH");
        System.out.printf("begin address : %d\n",begin.hashCode());
        a=10;
    }
}
