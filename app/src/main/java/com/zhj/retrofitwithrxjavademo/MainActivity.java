package com.zhj.retrofitwithrxjavademo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.zhj.retrofitwithrxjavademo.entity.ListDayDiscount;
import com.zhj.retrofitwithrxjavademo.entity.ProductBean;
import com.zhj.retrofitwithrxjavademo.http.ProgressSubscriber;
import com.zhj.retrofitwithrxjavademo.http.SubscriberOnNextListener;

public class MainActivity extends BaseActivity {
    private TextView tv_content;
    private Button bt_get;
    private SubscriberOnNextListener getResultOnNext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_content = (TextView) findViewById(R.id.tv_content);
        bt_get = (Button) findViewById(R.id.bt_get);
        bt_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retrofitUtil.setUseCache(false);
                retrofitUtil.getDayDiscountPost(new ProgressSubscriber(getResultOnNext, MainActivity.this,true),"{\"pageIndex\":1,\"pageSize\":10,\"version\":1}");
            }
        });
        getResultOnNext = new SubscriberOnNextListener<ListDayDiscount>() {
            @Override
            public void onNext(ListDayDiscount result) {
                StringBuilder sb = new StringBuilder();
                if(result!=null){
                    for (ProductBean bean : result.getListDayDiscount()){
                        sb.append(bean.getTeam_Title()).append("||");
                    }
                }
                tv_content.setText(sb.toString());
            }

            @Override
            public void onError(int code, String message) {
                Toast.makeText(MainActivity.this,code+"||"+message,Toast.LENGTH_SHORT).show();
            }
        };
    }
}
