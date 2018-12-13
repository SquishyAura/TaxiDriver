package com.taxidriver.doalf.taxidriver;

import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class CustomAdapterAccepted extends BaseAdapter {
    Context context;
    List<EntityRequest> acceptedRequest;
    Geocoder geocoder;

    public CustomAdapterAccepted(Context context, List<EntityRequest> acceptedRequest){
        this.context = context;
        this.acceptedRequest = acceptedRequest;
    }

    @Override
    public int getCount() {
        return acceptedRequest.size();
    }

    @Override
    public Object getItem(int i) {
        return acceptedRequest.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater.from(context));
        view = inflater.inflate(R.layout.listview_accepted, null);
        geocoder = new Geocoder(context, Locale.getDefault());

        TextView customerName = (TextView) view.findViewById(R.id.customerNameText);
        TextView customerAddress = (TextView) view.findViewById(R.id.customerAddressText);
        customerAddress.setText(getAddressFromLatLon(acceptedRequest.get(i)));
        ImageView customerImage = (ImageView) view.findViewById(R.id.customerImage);

        customerName.setText(acceptedRequest.get(i).customer.name);
        customerImage.setImageBitmap(acceptedRequest.get(i).customer.image);

        alternateBackgroundColors(i, view);

        return view;
    }

    private void alternateBackgroundColors(int i, View view){
        if(i % 2 == 0){
            view.findViewById(R.id.customerBackground).setBackgroundColor(Color.parseColor("#ededed"));
        }
        else
        {
            view.findViewById(R.id.customerBackground).setBackgroundColor(Color.parseColor("#f7f7f7"));
        }
    }

    private String getAddressFromLatLon(EntityRequest request){
        String address = null;
        try {
            List<Address> addresses = geocoder.getFromLocation(request.latitude, request.longitude, 1);
            address = addresses.get(0).getAddressLine(0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return address;
    }
}
