package com.android.news24x7.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.news24x7.R;
import com.android.news24x7.adapter.NewsRecyclerViewAdapter;


public class NewsFragment extends Fragment {


    String tab;
    String[] web;
    String[] datesd;
    int[] imageId;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NewsFragment() {
        // Required empty public constructor
    }

    public NewsFragment(String s) {
        tab=s;
    }
    public static NewsFragment newInstance(String param1, String param2) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        if (param1.equalsIgnoreCase("ENGLISH")) {
            //THIS IS INSTANCE OF ENGLISH TYPE DATA
        } else if (param1.equalsIgnoreCase("SCIENCE")) {
            //THIS IS INSTANCE OF SCIENCE TYPE DATA
        }else if (param1.equalsIgnoreCase("SOCIAL")) {
            //THIS IS INSTANCE OF SOCIAL TYPE DATA
        }
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tab = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_news, container, false);

        ListView gridView=(ListView)v.findViewById(R.id.list);
        switch (tab){
            case "HEADLINES":
                Toast.makeText(getContext(), "Tab My" + tab, Toast.LENGTH_SHORT).show();
                web = new String[]{
                        "Google",
                        "Github",
                        "Instagram",
                        "Facebook",
                        "Flickr",
                };
                datesd = new String[]{
                        "12/12/2015",
                        "15/12/2016",
                        "Instagram",
                        "Facebook",
                        "Flickr",
                };
                imageId = new int[]{
                        R.drawable.titled,
                        R.drawable.titled,
                        R.drawable.titled,
                        R.drawable.titled,
                        R.drawable.titled,

                };
               break;
            default:  Toast.makeText(getContext(), "Tab My" + tab, Toast.LENGTH_SHORT).show();
                web = new String[]{
                        "Google",
                        "Github",
                        "Instagram",
                        "Facebook",
                        "Flickr",
                };
                datesd = new String[]{
                        "12/12/2015",
                        "15/12/2016",
                        "Instagram",
                        "Facebook",
                        "Flickr",
                };
                imageId = new int[]{
                        R.drawable.ic_discuss,
                        R.drawable.ic_done,
                        R.drawable.ic_dashboard,
                        R.drawable.titled,
                        R.drawable.titled,

                };
        }



        NewsRecyclerViewAdapter gridAdapter = new NewsRecyclerViewAdapter(getActivity(), R.layout.news_list,web,imageId,datesd);
        gridView.setAdapter(gridAdapter);

        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}
