package com.careevolution.apidemo.ui.authorization;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.careevolution.apidemo.R;
import com.careevolution.apidemo.di.components.InjectingActivity;
import com.careevolution.apidemo.ui.patientlist.PatientListActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class AuthorizationActivity extends InjectingActivity {

    @Inject
    AuthorizationPresenter authorizationPresenter;

    @BindView(R.id.actionGroup)
    ViewSwitcher actionGroup;

    public static Intent createIntent(Context context) {
        return Intent.makeRestartActivityTask(new ComponentName(context, AuthorizationActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);
        getLifecycle().addObserver(authorizationPresenter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        authorizationPresenter.onActivityResult(requestCode, data);
    }

    @OnClick(R.id.connect)
    public void onConnectButtonClick() {
        authorizationPresenter.onConnectButtonClick();
    }

    public void showLoadingIndicator() {
        actionGroup.showNext();
    }

    public void hideLoadingIndicator() {
        actionGroup.showPrevious();
    }

    public void navigateToQueryActivity() {
        startActivity(PatientListActivity.createIntent(this));
    }

    public void showMessage(@StringRes int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }
}
