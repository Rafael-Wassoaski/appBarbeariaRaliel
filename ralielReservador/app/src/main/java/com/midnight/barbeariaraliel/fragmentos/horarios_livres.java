package com.midnight.barbeariaraliel.fragmentos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.midnight.barbeariaraliel.R;
import com.midnight.barbeariaraliel.asyncTasks.RequestMaker;
import com.midnight.barbeariaraliel.classes.Horario;
import com.midnight.barbeariaraliel.classes.HorarioAdapter;
import com.midnight.barbeariaraliel.interfaces.Horarios_livres_async;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link horarios_livres#newInstance} factory method to
 * create an instance of this fragment.
 */
public class horarios_livres extends Fragment implements Horarios_livres_async {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private  ListView listViewlivres;
    private  HorarioAdapter adapterLivres;
    private SwipeRefreshLayout swipeRefreshLayout;
    private  View root;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Horarios_livres_async horarios_livres = this;
    private RequestMaker requestMaker;
    private Handler handler;
    private Handler listViewHandler;

    public horarios_livres() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment horarios_livres.
     */
    // TODO: Rename and change types and number of parameters
    public static horarios_livres newInstance(String param1, String param2) {
        horarios_livres fragment = new horarios_livres();
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
        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                swipeRefreshLayout.setRefreshing(false);
            }
        };

        listViewHandler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                root.findViewById(R.id.com_vagas).setVisibility(View.INVISIBLE);
                root.findViewById(R.id.sem_vagas).setVisibility(View.VISIBLE);
            }
        };

        requestMaker = new RequestMaker();
        requestMaker.handler = handler;
        requestMaker.response = horarios_livres;
        requestMaker.execute();

    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        root = inflater.inflate(R.layout.fragment_horarios_livres, container, false);
        listViewlivres = (ListView) root.findViewById(R.id.listViewLivres);
        swipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.swiperefresh);
        adapterLivres = new HorarioAdapter();
        adapterLivres.setAct(this);
        adapterLivres.setLayout(R.layout.horarios_livres_layout);
        root.findViewById(R.id.sem_vagas).setVisibility(View.INVISIBLE);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    requestMaker = new RequestMaker();
                    requestMaker.handler = handler;
                    requestMaker.response = horarios_livres;
                    requestMaker.execute();
                }
            });
        return root;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("TEste", "result");
        if(requestCode == 200){
            if(resultCode == Activity.RESULT_OK){
                int position = data.getIntExtra("position", -1);

                if(position >-1){
                    adapterLivres.removerReservado(position);
                }
            }
        }
    }

    public void setHorarios(String json, ListView listViewSet, int opc){
        try {
            ArrayList<Horario> horarioLivres = new ArrayList<>();

            if(json == null || json.compareTo("{\"horarios\":[]}") == 1){
                root.findViewById(R.id.com_vagas).setVisibility(View.INVISIBLE);
                root.findViewById(R.id.sem_vagas).setVisibility(View.VISIBLE);
            }else {
                root.findViewById(R.id.com_vagas).setVisibility(View.VISIBLE);
                root.findViewById(R.id.sem_vagas).setVisibility(View.INVISIBLE);
                JSONObject newJson = new JSONObject(json);
                for (int horarios = 0; horarios < newJson.getJSONArray("horarios").length(); horarios++) {
                    Log.d("Horarios", newJson.getJSONArray("horarios").toString());

                    Horario horarioLivre = new Horario(newJson.getJSONArray("horarios").getJSONObject(horarios).getString("hora"),
                            newJson.getJSONArray("horarios").getJSONObject(horarios).getString("nome"),
                            newJson.getJSONArray("horarios").getJSONObject(horarios).getString("telefone"),
                            newJson.getJSONArray("horarios").getJSONObject(horarios).getInt("idBarbeiro"),
                            1);
                    horarioLivres.add(horarioLivre);
                }

                adapterLivres.setHorarioList(horarioLivres);
                adapterLivres.setHandler(listViewHandler);
                if(listViewSet == null){
                    listViewlivres.setAdapter(adapterLivres);
                }else{
                    listViewSet.setAdapter(adapterLivres);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}