package com.shuttles.shuttlesapp.View;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.chrisbanes.photoview.PhotoView;
import com.shuttles.shuttlesapp.ConnectionController.ConnectionImpl;
import com.shuttles.shuttlesapp.ConnectionController.ConnectionResponse;
import com.shuttles.shuttlesapp.ConnectionController.ImageLoadHandler;
import com.shuttles.shuttlesapp.ConnectionController.RequestData;
import com.shuttles.shuttlesapp.ConnectionController.RequestHandler;
import com.shuttles.shuttlesapp.R;
import com.shuttles.shuttlesapp.Utils.Constants;
import com.shuttles.shuttlesapp.Utils.LoadingDialog;
import com.shuttles.shuttlesapp.Utils.Utils;
import com.shuttles.shuttlesapp.vo.NoticeListVO;

import java.io.FileInputStream;
import java.io.IOException;


public class NoticeDetailActivity extends AppCompatActivity implements ConnectionImpl{
    private NoticeListVO noticeVO;
    private TextView noticeSubject;
    private TextView noticeDate;
    private TextView noticeContent;
    private PhotoView noticeImage;

    private LoadingDialog loadingDialog;

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
            sendRequestData(null);
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

    @Override
    public void sendRequestData(RequestData requestData) {
        loadingDialog = new LoadingDialog(NoticeDetailActivity.this);
        loadingDialog.show();
        new ImageLoadHandler(this).execute(noticeVO.getNotice_picture());
    }

    @Override
    public void requestCallback(ConnectionResponse connectionResponse) {
        loadingDialog.dismiss();

        switch (connectionResponse.getRequestType()) {
            case IMAGE_LOAD:
                noticeImage = (PhotoView) findViewById(R.id.notice_image);

                try {
                    FileInputStream fis = this.openFileInput((Utils.convertURLtoFileName(noticeVO.getNotice_picture())));
                    int len;
                    byte buf[] = new byte[fis.available()];
                    while ((len = fis.read(buf)) != -1) {
                        Log.i(Constants.LOG_TAG, "Load image from storage " + len);
                    }
                    fis.close();
                    Bitmap bitmap = BitmapFactory.decodeByteArray(buf, 0, buf.length);
                    noticeImage.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                    noticeImage.setImageBitmap(null);
                }
                break;
        }
    }
}

