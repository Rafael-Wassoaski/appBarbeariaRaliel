package com.midnight.barbeariaraliel.classes;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.midnight.barbeariaraliel.MainActivity;
import com.midnight.barbeariaraliel.R;
import com.midnight.barbeariaraliel.asyncTasks.RequestMakerCancelHorario;
import com.midnight.barbeariaraliel.fragmentos.meus_horarios;
import com.midnight.barbeariaraliel.fragmentos.popUp;



import java.util.ArrayList;
import java.util.List;


public class HorarioAdapter extends BaseAdapter {

    private List<Horario> horarioList = new ArrayList<>();

    public void setAct(Fragment act) {
        this.act = act;
    }

    private Fragment act;

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

        final Handler handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                int position = msg.getData().getInt("position");
                removerReservado(position);
            }
        };

        final Button cancelButton = (Button) view.findViewById(R.id.cancelButton);

        if(cancelButton!= null){
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final RequestMakerCancelHorario cancelHorario = new RequestMakerCancelHorario();
                    cancelHorario.meus_horariosClass = (meus_horarios)act;
                    cancelHorario.handler = handler;
                    cancelHorario.execute(horarioList.get(position).getHorario(), horarioList.get(position).getNomeBarbeiro(), MainActivity.id, Integer.toString(position));
                }
            });
        }

        final Horario horario = horarioList.get(position);
        TextView nomeBarbeiro = view.findViewById(R.id.corte);
        final TextView hora = view.findViewById(R.id.Horario);
        FloatingActionButton botta = view.findViewById(R.id.floatingActionButton);



        if(view.findViewById(R.id.floatingActionButton) != null) {
            botta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("Click", "Clicou " + horarioList.get(position).getHorario());
                    Intent popup = new Intent(act.getContext(), popUp.class);
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
        Log.d("Horarios", "size "+horarioList.size());
        if(horarioList.isEmpty()){
            horarioList.add(new Horario("Sem horarios", "", ""));
        }
        notifyDataSetChanged();

    }

    public void addItem(Horario horario){
        horarioList.add(horario);
        notifyDataSetChanged();
    }
}
