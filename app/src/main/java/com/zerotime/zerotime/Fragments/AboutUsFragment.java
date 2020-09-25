package com.zerotime.zerotime.Fragments;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

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
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.zerotime.zerotime.R;
import com.zerotime.zerotime.databinding.UserFragmentAboutUsBinding;

import java.util.Objects;


public class AboutUsFragment extends Fragment implements OnMapReadyCallback {
    private UserFragmentAboutUsBinding binding;
    private GoogleMap mMap;
    private GoogleMap googleMap;
    private GoogleApiClient mGoogleApiClient;
    protected Location mLastLocation;
    ExpandableTextView expandableTextView;
    String longText = "شركه زيرو تايم للنقل والشحن السريع , تأسست عام 2016 , لو عندك بيزنس اونلاين بتحتاج شركه شحن تحافظ على مستوى البراند وتوصل شحناتك بأمان وسرعه وثقه من غير اي حيره زيرو تايم هي راحتك وراحه عميله , زيرو تايم شركه مرخصه بريدياً ولديها سجل تجاري وبطاقه ضريبيه , زيرو تايم بتوصل لأغلب محافظات مصر  ولسه هنكمل لمحافظات مصر كلها ," +
            "زيرو تايم بتستلم وتسلم الشحنات من الباب للباب وده بيكون من خلال مندوبنا المتدربين , زيرو تايم بتوصلك تحصيلك باكثر من طريقه وفي الميعاد الى بتحدده وانت اختار الى يناسبك , زيرو تايم بتساعدك  تريح عميلك من خلال خدمات اختياريه زي خدمه طرد مقابل طرد او خدمه فتح الشحنات وده بيكون بناءا على اختيارك انت , زيرو تايم بتسلم مرتجعاتك وده بيكون على مدار ثلاثه ايام في الأسبوع. \n" +
            " خدمة عملاء متاحه لمساعدتك في الرد على جميع الاستفسارات وحل جميع المشاكل الي بتواجهها ";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = UserFragmentAboutUsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();


        expandableTextView = view.findViewById(R.id.expand_text_view);
        expandableTextView.setText(longText);
        binding.aboutUsMap.onCreate(savedInstanceState);
        binding.aboutUsMap.getMapAsync(this);

        return view;
    }

    @Override
    public void onResume() {

        super.onResume();

        Objects.requireNonNull(getView()).setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener((v, keyCode, event) -> {

            if (keyCode == KeyEvent.KEYCODE_BACK) {
                assert getFragmentManager() != null;
                getFragmentManager().popBackStackImmediate();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
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