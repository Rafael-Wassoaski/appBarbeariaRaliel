package com.midnight.barbeariaraliel.classes;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.midnight.barbeariaraliel.R;

import java.util.ArrayList;
import java.util.List;

public class CorteAdapter extends BaseAdapter {

    private List<String> cortes = new ArrayList<>();

    private Activity activity;

    public CorteAdapter(Activity activity) {
        cortes.add("Social 30 - min");
        cortes.add("Degrade 30 - min");
        cortes.add("Navalhado 45 - min");
        cortes.add("Sobrancelha na pinca - 10 min");
        cortes.add("Sobrancelha na Na navalha - 5 min ");
        cortes.add("Barba - 15 min");
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return cortes.size();
    }

    @Override
    public Object getItem(int i) {
        return cortes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        view = activity.getLayoutInflater().inflate(R.layout.corte_layout, viewGroup, false);

        TextView corte = view.findViewById(R.id.corte);
        corte.setText(cortes.get(position));
        return view;
    }
}
