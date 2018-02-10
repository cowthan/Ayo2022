package com.app.core.utils;


import android.text.TextUtils;

import com.app.core.prompt.Toaster;

import org.ayo.core.Lang;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by qiaoliang on 2017/8/11.
 *
 * 表单验证
 */

public class FormValidator {

    public static boolean isNotEmpty(String s, String defaultNotify){
        boolean isOk = !TextUtils.isEmpty(s);
        if(!isOk && !TextUtils.isEmpty(defaultNotify)){
            Toaster.toastShort(defaultNotify);
        }
        return isOk;
    }

    public static boolean isLengthInRange(String s, int includedMin, int includedMax, String defaultNotify){
        int count = Lang.count(s);
        boolean isOk = count >= includedMin && count <= includedMax;
        if(!isOk && !TextUtils.isEmpty(defaultNotify)){
            Toaster.toastShort(defaultNotify);
        }
        return isOk;
    }

    public static boolean isEmail(String s, String defaultNotify){
        String str = "^([\\w-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([\\w-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(s);
        boolean isOk =  m.matches();
        if(!isOk && !TextUtils.isEmpty(defaultNotify)){
            Toaster.toastShort(defaultNotify);
        }
        return isOk;
    }

    public static boolean isMobileNumber(String s, String defaultNotify){
        boolean isOk = PhoneNumber.isPhoneLegal(s);
        if(!isOk && !TextUtils.isEmpty(defaultNotify)){
            Toaster.toastShort(defaultNotify);
        }
        return isOk;
    }

    public static boolean isFixedLineNumber(String s, String defaultNotify){
        boolean isOk = false;
        if (s.length() > 9) {
            Pattern p1 = null;
            Matcher m = null;
            p1 = Pattern.compile("^0(10|2[0-5789]|\\d{3})\\d{7,8}$"); // 验证带区号的
            m = p1.matcher(s);
            isOk = m.matches();
        } else {
            isOk = false;
        }

        if(!isOk && !TextUtils.isEmpty(defaultNotify)){
            Toaster.toastShort(defaultNotify);
        }
        return isOk;
    }

    public static boolean isTaxNumber(String s, String defaultNotify){
        int count = Lang.count(s);
        boolean isOk = true;
        if (count != 15 && count != 18 && count != 20) {
            isOk = false;
        }else{
            isOk = true;
        }

        if(!isOk && !TextUtils.isEmpty(defaultNotify)){
            Toaster.toastShort(defaultNotify);
        }
        return isOk;
    }


    private static final class PhoneNumber{
        /**
         * 大陆号码或香港号码均可
         */
        public static boolean isPhoneLegal(String str)throws PatternSyntaxException {
            return isChinaPhoneLegal(str) || isHKPhoneLegal(str);
        }

        /**
         * 大陆手机号码11位数，匹配格式：前三位固定格式+后8位任意数
         * 此方法中前三位格式有：
         * 13+任意数
         * 15+除4的任意数
         * 18+除1和4的任意数
         * 17+除9的任意数
         * 147
         */
        public static boolean isChinaPhoneLegal(String str) throws PatternSyntaxException {
            String regExp = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$";
            Pattern p = Pattern.compile(regExp);
            Matcher m = p.matcher(str);
            return m.matches();
        }

        /**
         * 香港手机号码8位数，5|6|8|9开头+7位任意数
         */
        public static boolean isHKPhoneLegal(String str)throws PatternSyntaxException {
            String regExp = "^(5|6|8|9)\\d{7}$";
            Pattern p = Pattern.compile(regExp);
            Matcher m = p.matcher(str);
            return m.matches();
        }
    }
}
