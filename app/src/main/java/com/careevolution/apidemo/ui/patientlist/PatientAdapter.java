package com.careevolution.apidemo.ui.patientlist;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.careevolution.apidemo.R;

import org.hl7.fhir.dstu3.model.HumanName;
import org.hl7.fhir.dstu3.model.Patient;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.PatientViewHolder> {

    private List<Patient> patients = new ArrayList<>();

    static class PatientViewHolder extends RecyclerView.ViewHolder {
        TextView patientNameView;
        TextView patientDescriptionView;

        PatientViewHolder(@NonNull View itemView) {
            super(itemView);
            patientNameView = itemView.findViewById(R.id.name);
            patientDescriptionView = itemView.findViewById(R.id.description);
        }
    }

    private Resources resources;

    @Inject
    public PatientAdapter(Resources resources) {
        this.resources = resources;
    }

    @NonNull
    @Override
    public PatientViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View root = layoutInflater.inflate(R.layout.item_patient, viewGroup, false);
        return new PatientViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientViewHolder patientViewHolder, int position) {
        Patient patient = patients.get(position);
        patientViewHolder.patientNameView.setText(createPatientName(patient));
        patientViewHolder.patientDescriptionView.setText(createPatientDescription(patient));
    }

    private String createPatientName(Patient patient) {
        HumanName name = patient.getNameFirstRep();

        String givenName = name.getGivenAsSingleString();
        boolean hasGivenName = !TextUtils.isEmpty(givenName);

        String familyName = name.getFamily();
        boolean hasFamilyName = !TextUtils.isEmpty(familyName);

        if (hasGivenName && hasFamilyName) {
            return resources.getString(
                    R.string.PatientListActivity_patientName,
                    givenName,
                    familyName
            );
        } else if (hasFamilyName) {
            return familyName;
        } else if (hasGivenName) {
            return givenName;
        } else {
            return resources.getString(R.string.PatientListActivity_nameNotProvided);
        }
    }

    private String createPatientDescription(Patient patient) {
        List<String> values = new ArrayList<>();

        if (patient.getBirthDate() != null) {
            DateFormat df = SimpleDateFormat.getDateInstance(DateFormat.SHORT);
            values.add(resources.getString(
                    R.string.PatientListActivity_dateOfBirth,
                    df.format(patient.getBirthDate())
            ));
        }

        if (patient.getGender() != null) {
            values.add(resources.getString(
                    R.string.PatientListActivity_gender,
                    patient.getGender().getDisplay()
            ));
        }

        return TextUtils.join(" • ", values);
    }

    @Override
    public int getItemCount() {
        return patients.size();
    }

    public void clearPatients() {
        patients.clear();
        notifyDataSetChanged();
    }

    public void addPatients(List<Patient> l) {
        patients.addAll(l);
        // TODO: Notify for a specific range.
        notifyDataSetChanged();
    }

}
