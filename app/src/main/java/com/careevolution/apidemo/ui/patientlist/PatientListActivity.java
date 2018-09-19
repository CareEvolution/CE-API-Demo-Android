package com.careevolution.apidemo.ui.patientlist;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.careevolution.apidemo.R;
import com.careevolution.apidemo.di.components.InjectingActivity;
import com.careevolution.apidemo.ui.authorization.AuthorizationActivity;

import org.hl7.fhir.dstu3.model.Patient;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class PatientListActivity extends InjectingActivity {

    @Inject
    PatientListPresenter patientListPresenter;
    @Inject
    PatientAdapter patientAdapter;

    @BindView(R.id.patients)
    RecyclerView patientView;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refreshView;

    public static Intent createIntent(Context context) {
        return Intent.makeRestartActivityTask(new ComponentName(context, PatientListActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_list);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        patientView.setHasFixedSize(true);
        patientView.setAdapter(patientAdapter);
        patientView.setLayoutManager(linearLayoutManager);
        patientView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        patientView.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (linearLayoutManager.findLastVisibleItemPosition() == linearLayoutManager.getItemCount() - 1) {
                patientListPresenter.onPaginate();
            }
        });

        refreshView.setOnRefreshListener(patientListPresenter::onRefresh);

        getLifecycle().addObserver(patientListPresenter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            patientListPresenter.onLogout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showLoadingIndicator() {
        runOnUiThread(() -> refreshView.setRefreshing(true));
    }

    public void hideLoadingIndicator() {
        runOnUiThread(() -> refreshView.setRefreshing(false));
    }

    public void navigateToAuthorizationActivity() {
        startActivity(AuthorizationActivity.createIntent(this));
    }

    public void clearPatients() {
        runOnUiThread(() -> patientAdapter.clearPatients());
    }

    public void addPatients(List<Patient> patients) {
        runOnUiThread(() -> patientAdapter.addPatients(patients));
    }

    public void showMessage(@StringRes int resId) {
        runOnUiThread(() -> Toast.makeText(this, resId, Toast.LENGTH_SHORT).show());
    }

}
