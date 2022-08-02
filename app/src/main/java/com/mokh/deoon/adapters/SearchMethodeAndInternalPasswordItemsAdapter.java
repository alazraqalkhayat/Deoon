package com.mokh.deoon.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.mokh.deoon.R;
import com.mokh.deoon.models.SearchMethodeAndInternalPasswordModel;

import java.util.List;


public class SearchMethodeAndInternalPasswordItemsAdapter extends ArrayAdapter<SearchMethodeAndInternalPasswordModel> {


    public SearchMethodeAndInternalPasswordItemsAdapter(@NonNull Context context, List<SearchMethodeAndInternalPasswordModel> brand_of_service_spinner_items) {
        super(context, 0, brand_of_service_spinner_items);
    }

    @Override
    public View getView(int position,  View convertView,  ViewGroup parent) {


        return spinnerView(position,convertView,parent);
    }

    @Override
    public View getDropDownView(int position,  View convertView,  ViewGroup parent) {

        return spinnerView(position,convertView,parent);
    }

    public View spinnerView(int position,  View convertView,  ViewGroup parent){

        if (convertView==null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.search_method_items_and_internal_password_itens, parent, false);
        }

        SearchMethodeAndInternalPasswordModel items=getItem(position);
        TextView item=convertView.findViewById(R.id.search_method_and_internal_password_queistion_text_view);

        if(items!=null){

            item.setText(items.getItem());
        }

        return convertView;
    }




}

