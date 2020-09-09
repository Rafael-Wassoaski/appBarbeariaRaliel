package com.midnight.barbeariaraliel.fragmentos;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.midnight.barbeariaraliel.MainActivity;
import com.midnight.barbeariaraliel.R;
import com.midnight.barbeariaraliel.asyncTasks.RequestMaker;
import com.midnight.barbeariaraliel.classes.Horario;
import com.midnight.barbeariaraliel.classes.HorarioAdapter;
import com.midnight.barbeariaraliel.db.DBGet;
import com.midnight.barbeariaraliel.db.DBSave;
import com.midnight.barbeariaraliel.interfaces.Horarios_lires_async;
import com.midnight.barbeariaraliel.interfaces.Meus_horarios_async;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link meus_horarios#newInstance} factory method to
 * create an instance of this fragment.
 */
public class meus_horarios extends Fragment implements Meus_horarios_async {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private  ListView listView;
    private  HorarioAdapter adapter;
    private View root;
    private static Context contextMeusHorarios;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public meus_horarios() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment meus_horarios.
     */
    // TODO: Rename and change types and number of parameters
    public static meus_horarios newInstance(String param1, String param2) {
        meus_horarios fragment = new meus_horarios();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        MainActivity.db.meus_horaios = this;
        contextMeusHorarios = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_meus_horarios, container, false);
        listView = (ListView) root.findViewById(R.id.listView_meus_horarios);
        adapter = new HorarioAdapter();
        adapter.setAct(this);
        adapter.setLayout(R.layout.seus_horarios_reservados);
        root.findViewById(R.id.sem_horario).setVisibility(View.INVISIBLE);
        DBGet dbGet = new DBGet();
        dbGet.meus_horarios_async = this;
        dbGet.execute(getActivity().getApplicationContext());
        return root;
    }


    public void addReserva(final Horario horario){

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                adapter.addItem(horario);
            }
        });

    }

    public void setHorariosReservados(ArrayList horarios, ListView listViewSet){

        ArrayList<Horario> horarioLivres = new ArrayList<>();
        if(horarios.isEmpty()){
            Horario horarioLivre = new Horario("Sem horários reservados ", "" , "", 0);
            horarioLivres.add(horarioLivre);
            root.findViewById(R.id.com_horarios).setVisibility(View.INVISIBLE);
            root.findViewById(R.id.sem_horario).setVisibility(View.VISIBLE);
        }else{
            horarioLivres = horarios;
        }
        adapter.setHorarioList(horarioLivres);
        if(listViewSet == null){
            listView.setAdapter(adapter);
        }else{
            listViewSet.setAdapter(adapter);
        }
    }
}