package com.shuttles.shuttlesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.shuttles.shuttlesapp.vo.NoticeListVO;

public class NoticeDetailActivity extends AppCompatActivity {
    private NoticeListVO noticeVO;
    private TextView noticeSubject;
    private TextView noticeDate;
    private TextView noticeContent;

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
        //noticeContent.setText(noticeVO.getNotice_content());

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

