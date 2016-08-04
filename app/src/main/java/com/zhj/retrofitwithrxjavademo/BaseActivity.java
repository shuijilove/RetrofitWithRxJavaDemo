package com.zhj.retrofitwithrxjavademo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.zhj.retrofitwithrxjavademo.http.APIFactory;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by hjzhang on 2016/7/27.
 */
public class BaseActivity extends AppCompatActivity {
    public static final APIFactory retrofitUtil = (APIFactory) APIFactory.getInstance();

    private CompositeSubscription mCompositeSubscription;

    public CompositeSubscription getCompositeSubscription() {
        if (this.mCompositeSubscription == null) {
            this.mCompositeSubscription = new CompositeSubscription();
        }

        return this.mCompositeSubscription;
    }


    public void addSubscription(Subscription s) {
        if (this.mCompositeSubscription == null) {
            this.mCompositeSubscription = new CompositeSubscription();
        }

        this.mCompositeSubscription.add(s);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (this.mCompositeSubscription != null) {
            this.mCompositeSubscription.unsubscribe();
        }
    }
}
