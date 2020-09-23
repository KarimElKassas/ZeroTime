package com.zerotime.zerotime.Fragments;

import android.location.Location;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.zerotime.zerotime.R;
import com.zerotime.zerotime.databinding.UserFragmentAboutUsBinding;

import java.util.Objects;


public class AboutUsFragment extends Fragment implements OnMapReadyCallback {
    private UserFragmentAboutUsBinding binding;
    private GoogleMap mMap;
    private GoogleMap googleMap;
    private GoogleApiClient mGoogleApiClient;
    protected Location mLastLocation;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = UserFragmentAboutUsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        binding.aboutUsMap.onCreate(savedInstanceState);
        binding.aboutUsMap.getMapAsync(this);
        /*assert getFragmentManager() != null;
        SupportMapFragment mapFragment = (SupportMapFragment) getFragmentManager()
                .findFragmentById(R.id.Frame_Content);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
*/

        return view;
    }
    @Override
    public void onResume() {

        super.onResume();

        Objects.requireNonNull(getView()).setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener((v, keyCode, event) -> {

            if( keyCode == KeyEvent.KEYCODE_BACK )
            {
                assert getFragmentManager() != null;
                getFragmentManager().popBackStackImmediate();
                return true;
            }

            return false;
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
       /* mGoogleMap = googleMap;
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        mGoogleMap.addMarker(new MarkerOptions().position(29.9721261, 30.9435154));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(*//*some location*//*, 10));*/
        mMap = googleMap;
        LatLng sydney = new LatLng(30.1195601, 31.3677548);
        mMap.addMarker(new MarkerOptions().position(sydney).title("شارع المدينة المنورة"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(30.1195601, 31.3677548), 10));
        mMap.getMaxZoomLevel();
        binding.aboutUsMap.onResume();
    }
}