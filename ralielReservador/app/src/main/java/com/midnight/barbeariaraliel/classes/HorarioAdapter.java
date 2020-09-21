package com.midnight.barbeariaraliel.classes;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import java.util.HashMap;
import java.util.List;


public class HorarioAdapter extends BaseAdapter {

    private List<Horario> horarioList = new ArrayList<>();

    public void setAct(Fragment act) {
        this.act = act;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    private Handler handler;

    private Fragment act;

    public void setHorarioList(List<Horario> horarioList) {
        this.horarioList = horarioList;

        notifyDataSetChanged();
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

    private String convertMounth(String mounth){

        HashMap<String, String> mounths = new HashMap<>();
        mounths.put("1", "Janeiro");
        mounths.put("2", "Fevereiro");
        mounths.put("3", "Março");
        mounths.put("4", "Abril");
        mounths.put("5", "Maio");
        mounths.put("6", "Junho");
        mounths.put("7", "Julho");
        mounths.put("8", "Agosto");
        mounths.put("9", "Setembro");
        mounths.put("10", "Outubro");
        mounths.put("11", "Novembro");
        mounths.put("12", "Dezembro");

        String formatedDate[] = mounth.split(" ");

        Log.d("HoraFormat", formatedDate[0].split("-")[1]);

        return formatedDate[0].split("-")[2] + " de " + mounths.get(formatedDate[0].split("-")[1]) + " " + formatedDate[1].split(":")[0]+":"+formatedDate[1].split(":")[1];
    }

    private String getCorte(int indexCorte){
        List <String> corte = new ArrayList<>();
        corte.add("Social");
        corte.add("Degrade");
        corte.add("Navalhado");
        corte.add("Sobrancelha na pinca");
        corte.add("Sobrancelha na Na navalha");
        corte.add("Barba");

        return corte.get(indexCorte-1);
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

        horarioList.get(position).setHorarioReservado(convertMounth(horarioList.get(position).getHorario()));
        if(cancelButton!= null){
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final AlertDialog.Builder confirmDelete = new AlertDialog.Builder(act.getActivity());

                    confirmDelete.setMessage("Deseja mesmo cancelar este horário?");

                    confirmDelete.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            final RequestMakerCancelHorario cancelHorario = new RequestMakerCancelHorario();
                            cancelHorario.meus_horariosClass = (meus_horarios)act;
                            cancelHorario.handler = handler;
                            cancelHorario.execute(horarioList.get(position).getHorario(), horarioList.get(position).getNomeBarbeiro(), MainActivity.id, Integer.toString(position));
                        }
                    });

                    confirmDelete.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });

                    AlertDialog alertDelete = confirmDelete.create();
                    alertDelete.show();
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
        hora.setText(horario.getHorarioReservado());
        if(layout == R.layout.seus_horarios_reservados){
            nomeBarbeiro.setText(getCorte(horario.getCorteIndex()));
        }

        return view;
    }


    public void removerReservado(int position){
        horarioList.remove(position);
        Log.d("Horarios", "size "+horarioList.size());
        if(horarioList.isEmpty()){
            handler.sendEmptyMessage(1);
        }
        notifyDataSetChanged();

    }


    public void addItem(Horario horario){
        horarioList.add(horario);
        notifyDataSetChanged();
    }
}
