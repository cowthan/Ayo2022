package org.ayo.fringe.api2;

import org.ayo.http.StringTopLevelModel;
import org.ayo.http.TopLevelConverter;
import org.ayo.http.utils.JsonUtils;

/**
 * Created by Administrator on 2016/8/16.
 */
public class TopLevelResponseWeibo implements TopLevelConverter<TopLevelResponseWeibo.TopLevelBean> {
    @Override
    public TopLevelBean convert(String s) {

        TopLevelBean bean = JsonUtils.getBean(s, TopLevelBean.class);
        bean.raw = s;
        return bean;
    }

    public static class TopLevelBean extends StringTopLevelModel{
        public String error;
        public int error_code;

        public String raw;

        public boolean isOk(){
            return error == null || error.equals("");
        }

        @Override
        public String getResult() {
            return raw;
        }

        @Override
        public int getErrorCode() {
            return error_code;
        }

        public String getErrorMsg(){
            return error;
        }
    }
}
