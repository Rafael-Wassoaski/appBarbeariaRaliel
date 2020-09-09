package com.midnight.barbeariaraliel.classes;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.midnight.barbeariaraliel.R;
import com.midnight.barbeariaraliel.fragmentos.popUp;



import java.util.ArrayList;
import java.util.List;


public class HorarioAdapter extends BaseAdapter {

    private List<Horario> horarioList = new ArrayList<>();

    public void setAct(Activity act) {
        this.act = act;
    }

    private Activity act;

    public void setHorarioList(List<Horario> horarioList) {
        this.horarioList = horarioList;
    }

    public void setLayout(int layout) {
        this.layout = layout;
    }

    private int layout;

    @Override
    public int getCount() {
        return horarioList.size();
    }

    @Override
    public Object getItem(int position) {
        return horarioList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return horarioList.get(position).getIdBarbeiro();
    }

    @SuppressLint("RestrictedApi")
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
        View view;
        view = act.getLayoutInflater().inflate(layout, parent, false);

        final Horario horario = horarioList.get(position);
        TextView nomeBarbeiro = view.findViewById(R.id.Barbeiro);
        final TextView hora = view.findViewById(R.id.Horario);
        TextView barbeiro = view.findViewById(R.id.Cabeleireiro);
        TextView horarioText = view.findViewById(R.id.horarioText);
        FloatingActionButton botta = view.findViewById(R.id.floatingActionButton);

        if(view.findViewById(R.id.floatingActionButton) != null) {
            botta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("Click", "Clicou " + horarioList.get(position).getHorario());
                    Intent popup = new Intent(act.getApplicationContext(), popUp.class);
                    popup.putExtra("dados_horario_position", position);
                    popup.putExtra("dados_horario_id", horario.getIdBarbeiro());
                    popup.putExtra("dados_horario_data", horario.getHorario());
                    popup.putExtra("dados_hora_telefone", horario.getTelefoneBarbeiro());
                    popup.putExtra("dados_hora_nome", horario.getNomeBarbeiro());

                    act.startActivityForResult(popup, 200);


                }
            });
        }



        nomeBarbeiro.setText(horario.getNomeBarbeiro());
        hora.setText(horario.getHorario());

        return view;
    }


    public void removerReservado(int position){
        horarioList.remove(position);
        notifyDataSetChanged();
    }
}
