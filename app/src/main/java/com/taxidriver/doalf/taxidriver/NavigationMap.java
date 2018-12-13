package com.taxidriver.doalf.taxidriver;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class NavigationMap extends Fragment implements OnMapReadyCallback, RoutingListener {
    private View mView;
    GoogleMap mMap;
    MapView mMapView;

    private LatLng startPosition;
    private LatLng destinationPosition;
    private List<LatLng> polylineList;
    private PolylineOptions blackPolylineOptions;
    private Marker carMarker;
    private Polyline blackPolyline;
    private Handler handler;
    private int index;
    private int next;
    private float v;
    private double lat, lng;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_map, container, false);

        MainActivity activity = (MainActivity) getActivity();
        if(activity.storedAcceptedRequestList.size() > 0){
            startPosition = new LatLng(55.3949906,10.4458525);
            destinationPosition = new LatLng(activity.storedAcceptedRequestList.get(0).latitude, activity.storedAcceptedRequestList.get(0).longitude);
        }

        return mView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        mMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        MainActivity activity = (MainActivity) getActivity();

        if(activity.storedAcceptedRequestList.size() > 0){
            for(EntityRequest request : activity.storedAcceptedRequestList){
                mMap.addMarker(new MarkerOptions().position(new LatLng(request.latitude, request.longitude)).title(request.customer.name));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(request.latitude, request.longitude), 18));
            }
        }

        makeRoute(startPosition);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMapView = mView.findViewById(R.id.map);
        if(mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
    }

    @Override
    public void onRoutingFailure(RouteException e) {

    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(final ArrayList<Route> arrayList, final int i) {
        polylineList = arrayList.get(i).getPoints();
        blackPolylineOptions = new PolylineOptions();
        blackPolylineOptions.color(Color.BLACK);
        blackPolylineOptions.width(5);
        blackPolylineOptions.addAll(polylineList);
        blackPolyline = mMap.addPolyline(blackPolylineOptions);

        carMarker = mMap.addMarker(new MarkerOptions().position(startPosition).flat(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.car)));

        //car moving
        handler = new Handler();
        index = 0;
        next = 1;

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(index < polylineList.size()-1) {
                    index++;
                    next = index+1;
                }
                if(index < polylineList.size()-1) {
                    startPosition = polylineList.get(index);
                    destinationPosition = polylineList.get(next);
                }

                ValueAnimator valueAnimator = ValueAnimator.ofFloat(0,1);
                valueAnimator.setDuration(1000);
                valueAnimator.setInterpolator(new LinearInterpolator());
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        v = animation.getAnimatedFraction();
                        lng = v*destinationPosition.longitude+(1-v)*startPosition.longitude;
                        lat = v*destinationPosition.latitude+(1-v)*startPosition.latitude;
                        LatLng newPos = new LatLng(lat, lng);
                        carMarker.setPosition(newPos);
                        carMarker.setAnchor(0.5f, 0.5f);
                        //carMarker.setRotation(getBearing(startPosition, newPos));
                        float rotation = getBearing(startPosition, newPos);
                        if(carMarker.getRotation() > rotation) {
                            carMarker.setRotation(carMarker.getRotation() - animation.getAnimatedFraction());
                        } else {
                            carMarker.setRotation(carMarker.getRotation() + animation.getAnimatedFraction());
                        }
                    }
                });
                valueAnimator.start();
                handler.postDelayed(this,1000);



            }
        }, 1000);
    }

    @Override
    public void onRoutingCancelled() {

    }

    private void makeRoute(LatLng pos) {
        mMap.clear();
        MainActivity activity = (MainActivity) getActivity();

        if(activity.storedAcceptedRequestList.size() > 0){
            try {

                Routing routing = new Routing.Builder()
                        .travelMode(Routing.TravelMode.DRIVING)
                        .withListener(this)
                        .waypoints(pos, new LatLng(activity.storedAcceptedRequestList.get(0).latitude, activity.storedAcceptedRequestList.get(0).longitude))
                        .key("AIzaSyAA7Z1CpXCo7KAFRzyWRuXR8EayP4KclQo")
                        .build();
                routing.execute();
            } catch (Exception e) {
                Toast.makeText(getActivity(), "Unable to Route", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(getActivity(), "No request has been accepted", Toast.LENGTH_SHORT).show();
        }
    }

    //Courtesy of Google Inc.
    private float getBearing(LatLng orign, LatLng dest) {
        double lat = Math.abs(orign.latitude - dest.latitude);
        double lng = Math.abs(orign.longitude - dest.longitude);
        if (orign.latitude < dest.latitude && orign.longitude < dest.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)));
        else if (orign.latitude >= dest.latitude && orign.longitude < dest.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 90);
        else if (orign.latitude >= dest.latitude && orign.longitude >= dest.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)) + 180);
        else if (orign.latitude < dest.latitude && orign.longitude >= dest.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 270);
        return -1;
    }
}
