package com.sahilda.nytimessearch.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;

import com.sahilda.nytimessearch.R;
import com.sahilda.nytimessearch.models.NewsDeskType;
import com.sahilda.nytimessearch.models.SearchQuery;
import com.sahilda.nytimessearch.models.SortType;

import org.parceler.Parcels;

public class FiltersFragment extends DialogFragment {

    SearchQuery mSearchQuery;
    CheckBox cbArts;
    CheckBox cbFashionAndStyle;
    CheckBox cbSports;
    Spinner spOrder;

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
        setupCheckboxes(view);
        spOrder = (Spinner) view.findViewById(R.id.spOrder);
        setupSpinner(spOrder);
        mSearchQuery = (SearchQuery) Parcels.unwrap(getArguments().getParcelable("searchQuery"));
    }

    public void setupSpinner(Spinner spinner) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                String selected_item = adapter.getItemAtPosition(position).toString();
                if (selected_item.equals(SortType.NEWEST.getType())) {
                    mSearchQuery.setSortType(SortType.NEWEST);
                } else if (selected_item.equals(SortType.OLDEST.getType())) {
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

    public void setupCheckboxes(View view) {
        cbArts = (CheckBox) view.findViewById(R.id.cbArts);
        cbFashionAndStyle = (CheckBox) view.findViewById(R.id.cbFashionAndStyle);
        cbSports = (CheckBox) view.findViewById(R.id.cbSports);

        cbArts.setSelected(mSearchQuery.getNewsDeskType().contains(NewsDeskType.ARTS));
        cbFashionAndStyle.setSelected(mSearchQuery.getNewsDeskType().contains(NewsDeskType.FASHION_AND_STYLE));
        cbSports.setSelected(mSearchQuery.getNewsDeskType().contains(NewsDeskType.SPORTS));

        cbArts.setOnCheckedChangeListener(checkListener);
        cbFashionAndStyle.setOnCheckedChangeListener(checkListener);
        cbSports.setOnCheckedChangeListener(checkListener);
    }

    public interface EditNameDialogListener {
        void onFinishEditDialog(String inputText);
    }

}
