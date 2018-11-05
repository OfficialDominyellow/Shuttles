package com.shuttles.shuttlesapp.View;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shuttles.shuttlesapp.ConnectionController.ConnectionImpl;
import com.shuttles.shuttlesapp.ConnectionController.ConnectionResponse;
import com.shuttles.shuttlesapp.ConnectionController.RequestData;
import com.shuttles.shuttlesapp.ConnectionController.RequestHandler;
import com.shuttles.shuttlesapp.ConnectionController.RestAPI;
import com.shuttles.shuttlesapp.R;
import com.shuttles.shuttlesapp.Utils.LoadingDialog;
import com.shuttles.shuttlesapp.vo.NoticeListVO;

import java.util.ArrayList;
import java.util.List;

public class NoticeActivity extends AppCompatActivity implements ConnectionImpl{
    private String TAG = "NoticeActivity";
    private String mNoticeData;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_drink_order_detail);
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_white_12dp); // your drawable
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Implemented by activity
            }
        });

        loadAllNotice();
    }

    private void loadAllNotice() {
        RequestData requestData = new RequestData(RestAPI.Method.GET, RestAPI.NOTICE, RestAPI.REQUEST_TYPE.NOTICE);
        sendRequestData(requestData);
    }

    @Override
    public void sendRequestData(RequestData requestData) {
        loadingDialog = new LoadingDialog(NoticeActivity.this);
        loadingDialog.show();
        new RequestHandler(this).execute(requestData);
    }

    @Override
    public void requestCallback(ConnectionResponse connectionResponse) {
        loadingDialog.dismiss();

        switch (connectionResponse.getRequestType()){
            case FAILED:
                Log.i(TAG,"callback failed");
                break;

            case NOTICE:
                Log.i(TAG,"Notice response success");

                ListViewCompat lvNotice = (ListViewCompat) findViewById(R.id.lv_notice);

                lvNotice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        NoticeListVO noticeListVO = (NoticeListVO) adapterView.getItemAtPosition(i);

                        String subject = noticeListVO.getNotice_subject();
                        String date = noticeListVO.getNotice_date();

                        //Toast.makeText(getApplicationContext(), "title : " + subject + ", content : " + date + ", pos : " + i, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), NoticeDetailActivity.class);
                        intent.putExtra("notice", noticeListVO);
                        startActivity(intent);
                    }
                });

                final NoticeListViewAdapter noticeListViewAdapter = new NoticeListViewAdapter();
                Gson gson = new Gson();
                mNoticeData = connectionResponse.getResult();
                List<NoticeListVO> noticeList = gson.fromJson(mNoticeData, new TypeToken<List<NoticeListVO>>(){}.getType());

                for(NoticeListVO e : noticeList) {
                    noticeListViewAdapter.addItem(e);
                }

                lvNotice.setAdapter(noticeListViewAdapter);

                break;
        }
    }
}

class NoticeListViewAdapter extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<NoticeListVO> listViewItemList = new ArrayList<>() ;

    // NoticeListViewAdapter 생성자
    public NoticeListViewAdapter() {

    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.notice_list_row, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView tvNoticeTitle = (TextView) convertView.findViewById(R.id.tv_notice_title) ;
        TextView tvNoticeContent = (TextView) convertView.findViewById(R.id.tv_notice_content) ;

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        NoticeListVO noticeListVO = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        tvNoticeTitle.setText(noticeListVO.getNotice_subject());
        tvNoticeContent.setText(noticeListVO.getNotice_date());

        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(NoticeListVO noticeElement) {
        listViewItemList.add(noticeElement);
    }
}