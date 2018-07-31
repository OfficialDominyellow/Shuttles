package com.shuttles.shuttlesapp.vo;

import java.io.Serializable;

/**
 * Created by domin on 2018-01-28.
 */

public class NoticeListVO implements Serializable {
    private String notice_id;
    private String notice_subject;
    private String notice_content;
    private String notice_picturl;
    private String notice_date;

    public String getNotice_id() {
        return notice_id;
    }

    public void setNotice_id(String notice_id) {
        this.notice_id = notice_id;
    }

    public String getNotice_subject() {
        return notice_subject;
    }

    public void setNotice_subject(String notice_subject) {
        this.notice_subject = notice_subject;
    }

    public String getNotice_date() {
        return notice_date;
    }

    public void setNotice_date(String notice_date) {
        this.notice_date = notice_date;
    }

    public String getNotice_content() {
        return notice_content;
    }

    public void setNotice_content(String notice_content) {
        this.notice_content = notice_content;
    }

    public String getNotice_picturl() {
        return notice_picturl;
    }

    public void setNotice_picturl(String notice_picturl) {
        this.notice_picturl = notice_picturl;
    }
}
