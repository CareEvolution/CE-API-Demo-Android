package com.careevolution.apidemo.di.components;

import android.support.annotation.CallSuper;

import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;

public abstract class InjectingActivity extends DaggerAppCompatActivity {

    @Override
    @CallSuper
    public void onContentChanged() {
        super.onContentChanged();
        ButterKnife.bind(this);
    }
}
