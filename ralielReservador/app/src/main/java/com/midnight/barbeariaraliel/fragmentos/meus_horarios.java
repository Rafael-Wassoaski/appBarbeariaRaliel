package com.midnight.barbeariaraliel.fragmentos;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.midnight.barbeariaraliel.R;
import com.midnight.barbeariaraliel.asyncTasks.RequestMakerMeusHorarios;
import com.midnight.barbeariaraliel.classes.Horario;
import com.midnight.barbeariaraliel.classes.HorarioAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link meus_horarios#newInstance} factory method to
 * create an instance of this fragment.
 */
public class meus_horarios extends Fragment{

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
    private Handler handler;
    private RequestMakerMeusHorarios requestMakerMeusHorarios;

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
        contextMeusHorarios = getContext();
        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                setHorarios(msg.getData().getString("dados"), null);
            }
        };

        requestMakerMeusHorarios = new RequestMakerMeusHorarios();
        requestMakerMeusHorarios.handler = handler;
        requestMakerMeusHorarios.execute();

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

    public void setHorarios(String json, ListView listViewSet){
        try {

            Log.d("Horarios","Chamou2");

            ArrayList<Horario> horarioLivres = new ArrayList<>();

            if(json == null || json.compareTo("{\"horarios\":[]}") == 1){
                Horario horarioLivre = new Horario("Sem horários livres", "" , "", -1, 1);
                horarioLivres.add(horarioLivre);
            }else {

                JSONObject newJson = new JSONObject(json);
                Log.d("Token", newJson.isNull("horarios")+"");


                for (int horarios = 0; horarios < newJson.getJSONArray("horarios").length(); horarios++) {
                    Log.d("Horarios", newJson.getJSONArray("horarios").toString());

                    Horario horarioLivre = new Horario(newJson.getJSONArray("horarios").getJSONObject(horarios).getString("hora"),
                            newJson.getJSONArray("horarios").getJSONObject(horarios).getString("nome"),
                            newJson.getJSONArray("horarios").getJSONObject(horarios).getString("telefone"),
                            newJson.getJSONArray("horarios").getJSONObject(horarios).getInt("idBarbeiro"),
                            newJson.getJSONArray("horarios").getJSONObject(horarios).getInt("corte"));
                    horarioLivres.add(horarioLivre);
                }

                adapter.setHorarioList(horarioLivres);
                if(listViewSet == null){
                    listView.setAdapter(adapter);
                }else{
                    listViewSet.setAdapter(adapter);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}