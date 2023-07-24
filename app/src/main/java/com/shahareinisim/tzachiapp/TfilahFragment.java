package com.shahareinisim.tzachiapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class TfilahFragment extends Fragment {

    public enum Tfilah {SHACHRIT, MINCHA, HARVIT, BIRCAT_HAMAZON, BIRCAT_HALEVANA}

    Tfilah tfilah;
    FloatingActionButton navigator;
    RecyclerView rvTfilah;
    TextView title;
    public  TfilahFragment(Tfilah tfilah) {
        this.tfilah = tfilah;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View parent = inflater.inflate(R.layout.fragment_tfilah, container, false);
        parent.setFocusable(true);

        rvTfilah = parent.findViewById(R.id.rv_tfilah);
        rvTfilah.setLayoutManager(new LinearLayoutManager(requireContext()));

        navigator = parent.findViewById(R.id.navigator);

        title = parent.findViewById(R.id.title);

        int tfilahFileRes = 0;

        switch (tfilah) {
            case SHACHRIT:
                tfilahFileRes = R.raw.shachrit;
                title.setText(R.string.shachrit);
                break;
            case MINCHA:
                tfilahFileRes = R.raw.mincha;
                title.setText(R.string.mincha);
                break;
            case HARVIT:
                tfilahFileRes = R.raw.harvit;
                title.setText(R.string.harvit);
                break;
            case BIRCAT_HAMAZON:
                tfilahFileRes = R.raw.bircat_hamazon;
                title.setText(R.string.birkat_hamazon);
                break;
            case BIRCAT_HALEVANA:
                tfilahFileRes = R.raw.bircat_halevana;
                title.setText(R.string.birkat_halevana);
                break;
        }

        try {
            InputStreamReader inputStreamReader = new InputStreamReader(requireActivity().getResources().openRawResource(tfilahFileRes));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            rvTfilah.setAdapter(initTfilahAdapter(bufferedReader));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return parent;
    }

    @SuppressLint("InflateParams")
    public TfilahAdapter initTfilahAdapter(BufferedReader bufferedReader) throws IOException {
        ArrayList<TfilahPart> parts = new ArrayList<>();
        ArrayList<TfilahTitlePart> titleParts = new ArrayList<>();

        String part;
        while ((part = bufferedReader.readLine()) != null) {
            String key = "";
            if (part.length()>3) key = part.substring(0,3);
            else if (part.equals("")) key = "[e]";
            if (key.equals("[t]")) {
                titleParts.add(new TfilahTitlePart(parts.size(), part));
            }

            parts.add(new TfilahPart(key, part));
        }

        PopupNavigator popupNav = new PopupNavigator(requireContext(), navigator);
        TitleAdapter tAdapter = new TitleAdapter(requireActivity(), titleParts);
        tAdapter.setPostClickListener(position -> {
            ((LinearLayoutManager) Objects.requireNonNull(
                    rvTfilah.getLayoutManager())).scrollToPositionWithOffset(position,0);
//            Toast.makeText(requireContext(), "pos: " + position, Toast.LENGTH_SHORT).show();
            popupNav.dismiss();
        });
        popupNav.setAdapter(tAdapter);

        navigator.setOnClickListener(view -> popupNav.show());

        return new TfilahAdapter(parts);
    }
}