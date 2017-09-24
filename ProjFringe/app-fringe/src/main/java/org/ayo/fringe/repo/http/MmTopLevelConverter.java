package org.ayo.fringe.repo.http;

import org.ayo.http.StringTopLevelModel;
import org.ayo.http.TopLevelConverter;
import org.ayo.http.utils.JsonUtils;

/**
 * Created by Administrator on 2016/8/16.
 */
public class MmTopLevelConverter implements TopLevelConverter<MmTopLevelConverter.TopLevelBean> {
    @Override
    public MmTopLevelConverter.TopLevelBean convert(String s) {
        TopLevelBean bean = JsonUtils.getBean(s, TopLevelBean.class);
        bean.raw = s;
        return bean;
    }

    public static class TopLevelBean extends StringTopLevelModel{
        public int code;
        public String message;
        public String status;
        public String raw;

        public boolean isOk(){
            return code == 200;
        }

        @Override
        public String getResult() {
            return raw;
        }

        @Override
        public int getErrorCode() {
            return code;
        }

        public String getErrorMsg(){
            return message;
        }
    }
}
