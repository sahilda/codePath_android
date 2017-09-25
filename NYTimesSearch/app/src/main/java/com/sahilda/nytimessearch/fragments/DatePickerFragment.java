package com.sahilda.nytimessearch.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.sahilda.nytimessearch.R;
import com.sahilda.nytimessearch.models.SearchQuery;

import org.parceler.Parcels;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DatePickerFragment extends DialogFragment {

    SearchQuery mSearchQuery;
    DatePicker dpBeginDate;
    String date;

    public DatePickerFragment() {

    }

    public static DatePickerFragment newInstance(SearchQuery searchQuery) {
        DatePickerFragment frag = new DatePickerFragment();
        Bundle args = new Bundle();
        args.putParcelable("searchQuery", Parcels.wrap(searchQuery));
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_date_picker, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dpBeginDate = (DatePicker) view.findViewById(R.id.dpBeginDate);
        mSearchQuery = (SearchQuery) Parcels.unwrap(getArguments().getParcelable("searchQuery"));

        String date = new SimpleDateFormat("MM/dd/yyyy").format(new Date());
        if (mSearchQuery.getBeginDate() != null) {
            date = mSearchQuery.getBeginDate();
        }

        int year = Integer.valueOf(date.split("/")[2]);
        int month = Integer.valueOf(date.split("/")[0]) - 1;
        int day = Integer.valueOf(date.split("/")[1]);

        dpBeginDate.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                sendBackResult();
            }
        });
    }

    public interface DatePickerDialogListener {
        void onFinishEditDialog(String date);
    }

    public void sendBackResult() {
        DatePickerDialogListener listener = (DatePickerDialogListener) getTargetFragment();
        date = String.format("%02d", dpBeginDate.getMonth() + 1) + "/" + String.format("%02d", dpBeginDate.getDayOfMonth()) + "/" + String.format("%04d", dpBeginDate.getYear());
        listener.onFinishEditDialog(date);
        dismiss();
    }

}