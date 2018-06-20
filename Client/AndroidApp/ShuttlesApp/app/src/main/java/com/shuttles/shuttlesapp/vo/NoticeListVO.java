package com.shuttles.shuttlesapp.vo;
/**
 * Created by domin on 2018-01-28.
 */

public class NoticeListVO {
    private String notice_id;
    private String notice_subject;
    private String notice_date;
    private String title;
    private String content;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
