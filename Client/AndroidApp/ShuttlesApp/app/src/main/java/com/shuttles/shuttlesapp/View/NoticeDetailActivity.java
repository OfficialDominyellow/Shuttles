package com.shuttles.shuttlesapp.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.shuttles.shuttlesapp.R;
import com.shuttles.shuttlesapp.Utils.Constants;
import com.shuttles.shuttlesapp.vo.NoticeListVO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NoticeDetailActivity extends AppCompatActivity {
    private NoticeListVO noticeVO;
    private TextView noticeSubject;
    private TextView noticeDate;
    private TextView noticeContent;
    private TextView notice_url;

    private final String linkText = "공지사항 바로가기";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice_detail_layout);

        Intent intent = getIntent();
        noticeVO = (NoticeListVO)intent.getSerializableExtra("notice");

        noticeSubject = (TextView)findViewById(R.id.notice_subject);
        noticeSubject.setText(noticeVO.getNotice_subject());

        noticeDate = (TextView)findViewById(R.id.notice_date);
        noticeDate.setText(noticeVO.getNotice_date());

        noticeContent = (TextView)findViewById(R.id.notice_content);
        noticeContent.setText(noticeVO.getNotice_content());

        if(noticeVO.getNotice_picture() != null) {
            Log.i(Constants.LOG_TAG, noticeVO.getNotice_picture());
            notice_url = (TextView)findViewById(R.id.notice_url);
            notice_url.setText(linkText);

            Linkify.TransformFilter mTransform = new Linkify.TransformFilter() {
                @Override
                public String transformUrl(Matcher match, String url) {
                    return noticeVO.getNotice_picture();
                }
            };

            Pattern pattern = Pattern.compile(linkText);
            Linkify.addLinks(notice_url, pattern, "", null, mTransform);
        }
        else
            Log.i(Constants.LOG_TAG, "null");

        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_notice_detail);
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_white_12dp); // your drawable


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Implemented by activity
            }
        });
    }
}

