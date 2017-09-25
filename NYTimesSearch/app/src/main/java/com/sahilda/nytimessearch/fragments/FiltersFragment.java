package com.sahilda.nytimessearch.fragments;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.sahilda.nytimessearch.R;
import com.sahilda.nytimessearch.models.NewsDeskType;
import com.sahilda.nytimessearch.models.SearchQuery;
import com.sahilda.nytimessearch.models.SortType;

import org.parceler.Parcels;

public class FiltersFragment extends DialogFragment implements DatePickerFragment.DatePickerDialogListener {

    SearchQuery mSearchQuery;
    CheckBox cbArts;
    CheckBox cbFashionAndStyle;
    CheckBox cbSports;
    Spinner spOrder;
    TextView tvDate;
    Button btnSave;

    public FiltersFragment() {

    }

    public static FiltersFragment newInstance(SearchQuery searchQuery) {
        FiltersFragment fragment = new FiltersFragment();
        Bundle args = new Bundle();
        args.putParcelable("searchQuery", Parcels.wrap(searchQuery));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_filters, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSearchQuery = (SearchQuery) Parcels.unwrap(getArguments().getParcelable("searchQuery"));

        setupSaveButton(view);
        setupDate(view);
        setupCheckboxes(view);
        setupSpinner(view);
    }

    private void setupSaveButton(View view) {
        btnSave = (Button) view.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    private void setupDate(View view) {
        tvDate = (TextView) view.findViewById(R.id.tvDate);
        tvDate.setPaintFlags(tvDate.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        if (mSearchQuery.getBeginDate() == null) {
            String date = "1900/01/01";
            tvDate.setText(date);
        } else {
            tvDate.setText(mSearchQuery.getBeginDate());
        }

        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });
    }

    public void setupSpinner(View view) {
        spOrder = (Spinner) view.findViewById(R.id.spOrder);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this.getContext(), R.array.sort_array, R.layout.spinner_item1);
        spOrder.setAdapter(adapter);

        spOrder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                String selected_item = adapter.getItemAtPosition(position).toString();
                if (selected_item.toLowerCase().equals(SortType.NEWEST.getType().toLowerCase())) {
                    mSearchQuery.setSortType(SortType.NEWEST);
                } else if (selected_item.toLowerCase().equals(SortType.OLDEST.getType().toLowerCase())) {
                    mSearchQuery.setSortType(SortType.OLDEST);
                } else {
                    mSearchQuery.setSortType(null);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                mSearchQuery.setSortType(null);
            }
        });

        if (mSearchQuery.getSortType() != null) {
            setSpinnerToValue(spOrder, mSearchQuery.getSortType().getType());
        }
    }

    public void setSpinnerToValue(Spinner spinner, String value) {
        int index = 0;
        SpinnerAdapter adapter = spinner.getAdapter();
        for (int i = 0; i < adapter.getCount(); i++) {
            String s = (String) adapter.getItem(i);
            if (s.toLowerCase().equals(value)) {
                index = i;
                break; // terminate loop
            }
        }
        spinner.setSelection(index);
    }

    public void setupCheckboxes(View view) {
        cbArts = (CheckBox) view.findViewById(R.id.cbArts);
        cbFashionAndStyle = (CheckBox) view.findViewById(R.id.cbFashionAndStyle);
        cbSports = (CheckBox) view.findViewById(R.id.cbSports);

        cbArts.setOnCheckedChangeListener(checkListener);
        cbFashionAndStyle.setOnCheckedChangeListener(checkListener);
        cbSports.setOnCheckedChangeListener(checkListener);

        for (NewsDeskType newsDeskType : mSearchQuery.getNewsDeskType()) {
            if (newsDeskType.getType().equals(NewsDeskType.ARTS.getType())) {
                cbArts.setChecked(true);
            } else if (newsDeskType.getType().equals(NewsDeskType.SPORTS.getType())) {
                cbSports.setChecked(true);
            } else if (newsDeskType.getType().equals(NewsDeskType.FASHION_AND_STYLE.getType())) {
                cbFashionAndStyle.setChecked(true);
            }
        }
    }

    CompoundButton.OnCheckedChangeListener checkListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton view, boolean checked) {
            switch(view.getId()) {
                case R.id.cbArts:
                    if (checked) {
                        mSearchQuery.getNewsDeskType().add(NewsDeskType.ARTS);
                    } else {
                        mSearchQuery.getNewsDeskType().remove(NewsDeskType.ARTS);
                    }
                    break;
                case R.id.cbFashionAndStyle:
                    if (checked) {
                        mSearchQuery.getNewsDeskType().add(NewsDeskType.FASHION_AND_STYLE);
                    } else {
                        mSearchQuery.getNewsDeskType().remove(NewsDeskType.FASHION_AND_STYLE);
                    }
                    break;
                case R.id.cbSports:
                    if (checked) {
                        mSearchQuery.getNewsDeskType().add(NewsDeskType.SPORTS);
                    } else {
                        mSearchQuery.getNewsDeskType().remove(NewsDeskType.SPORTS);
                    }
                    break;
            }
        }
    };

    private void showDatePickerDialog() {
        FragmentManager fm = getFragmentManager();
        DatePickerFragment editNameDialogFragment = DatePickerFragment.newInstance(mSearchQuery);
        editNameDialogFragment.setTargetFragment(FiltersFragment.this, 300);
        editNameDialogFragment.show(fm, "fragment_date_picker");
    }

    @Override
    public void onFinishEditDialog(String date) {
        tvDate.setText(date);
        mSearchQuery.setBeginDate(date);
    }

}
