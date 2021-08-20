package com.example.voice_assi;

import android.util.Log;

import java.util.Calendar;

public class function {

    public static String wishme(){
        String s="";
        Calendar c=Calendar.getInstance();
        int time=c.get(Calendar.HOUR_OF_DAY);
        if(time>=0 && time<12){
            s="Good morning ";
        }else if(time>=12 && time<16){
            s="good noon";
        }else if(time>=16 && time<19){
            s="good evening";
        }else if(time>=19 && time<24){
            s="good night";
        }
        return s;
    }
    public static String fetchName(String msgs){
        String name="";
        boolean flag=false;
        String[] data=msgs.split(" ");
        for (int i=0;i<data.length;i++){
            String d=data[i];
            if (d.equals("call")){
                if(data[(i+1)].equals("to")){
                    flag=false;
                }else {flag=true;}
            }else if(d.equals("and")|| d.equals(".")){
                flag=false;
            }else if(data[(i-1)].equals("call")){
                if (d.equals("to")) {
                    flag=true;
                }
            }
            if(flag){
                if(!d.equals("call")&&!d.equals("to")){
                    name=name.concat(" "+d);
                }
            }
        }
        Log.d("Name",name);
        return name;
    }

}
