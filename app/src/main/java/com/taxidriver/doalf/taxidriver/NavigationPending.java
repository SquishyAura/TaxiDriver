package com.taxidriver.doalf.taxidriver;

import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class NavigationPending extends Fragment {
    private View mView;
    ListView pendingListView;
    CustomAdapterPending pendingCustomAdapter;
    List<EntityRequest> pendingRequestList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_pending, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        MainActivity activity = (MainActivity) getActivity();

        if(activity.storedPendingRequestList == null){
            pendingRequestList = new ArrayList<>();

            EntityCustomer customer1 = new EntityCustomer("Parakeesh", BitmapFactory.decodeResource(getContext().getResources(), R.drawable.parakeesh));
            EntityRequest request1 = new EntityRequest(customer1,55.366571, 10.429367);
            pendingRequestList.add(request1);

            EntityCustomer customer2 = new EntityCustomer("Bob", BitmapFactory.decodeResource(getContext().getResources(), R.drawable.bob));
            EntityRequest  request2 = new EntityRequest(customer2, 55.37777427, 10.43192843);
            pendingRequestList.add(request2);

            EntityCustomer customer3 = new EntityCustomer("Peter", BitmapFactory.decodeResource(getContext().getResources(), R.drawable.peter));
            EntityRequest  request3 = new EntityRequest(customer3, 40.741895, -73.989308);
            pendingRequestList.add(request3);

            updateListView();
        }
        else
        {
            pendingRequestList = activity.storedPendingRequestList;
            updateListView();
        }
    }

    @Override
    public void onStop(){
        super.onStop();
        MainActivity activity = (MainActivity) getActivity();
        activity.storedPendingRequestList = pendingRequestList;
    }

    public void updateListView(){
        pendingCustomAdapter = new CustomAdapterPending(getContext(), pendingRequestList);
        pendingListView = (ListView) getView().findViewById(R.id.pendingRequestListView);
        pendingListView.setAdapter(pendingCustomAdapter);
    }

    public void removeCustomer(int index){
        pendingRequestList.remove(index);

        updateListView();
    }
}
