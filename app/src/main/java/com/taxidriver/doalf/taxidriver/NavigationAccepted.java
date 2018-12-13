package com.taxidriver.doalf.taxidriver;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class NavigationAccepted extends Fragment {
    private View mView;
    ListView acceptedListView;
    CustomAdapterAccepted acceptedCustomAdapter;
    List<EntityRequest> acceptedRequestList;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_accepted, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        acceptedRequestList = new ArrayList<>();
        MainActivity activity = (MainActivity) getActivity();

        if(activity.storedAcceptedRequestList.size() > 0){
            acceptedRequestList = activity.storedAcceptedRequestList;
        }

        updateListView();
    }

    public void updateListView(){
        acceptedCustomAdapter = new CustomAdapterAccepted(getContext(), acceptedRequestList);
        acceptedListView = (ListView) getView().findViewById(R.id.acceptedRequestListView);
        acceptedListView.setAdapter(acceptedCustomAdapter);
    }
}
