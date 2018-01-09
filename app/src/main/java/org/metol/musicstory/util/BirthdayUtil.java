package org.metol.musicstory.util;

/**
 * Created by Broccoli.Huang on 2018/1/9.
 */

public class BirthdayUtil {
    public static String fbBirthday(String birthday){
        if(birthday.length()==4){//YYYY
            return birthday+"0000";
        }else if(birthday.length()==5) {//MM/DD
            return "0000"+birthday.replace("/", "");
        }else if(birthday.length()==10) {//MM/DD/YYYY
            return birthday.split("/")[2]+birthday.split("/")[0]+birthday.split("/")[1];
        }else{
            return "";
        }
    }
}
