package com.creative.share.apps.wash_squad_driver.activities_fragments.activity_map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import com.creative.share.apps.wash_squad_driver.R;
import com.creative.share.apps.wash_squad_driver.activities_fragments.activity_cancel_order.CancelOrderActivity;
import com.creative.share.apps.wash_squad_driver.activities_fragments.activity_order_details.OrderDetailsActivity;
import com.creative.share.apps.wash_squad_driver.activities_fragments.activity_work1.Work1Activity_Step1;
import com.creative.share.apps.wash_squad_driver.activities_fragments.activity_work1.Work1Activity_step2;
import com.creative.share.apps.wash_squad_driver.activities_fragments.activity_work1.Work1Activity_step3;
import com.creative.share.apps.wash_squad_driver.activities_fragments.activity_work1.Work1Activity_step4;
import com.creative.share.apps.wash_squad_driver.activities_fragments.activity_work2.Work2Activity;
import com.creative.share.apps.wash_squad_driver.activities_fragments.activity_work2.Work2Activity_Step1;
import com.creative.share.apps.wash_squad_driver.activities_fragments.activity_work2.Work2Activity_step2;
import com.creative.share.apps.wash_squad_driver.activities_fragments.activity_work2.Work2Activity_step3;
import com.creative.share.apps.wash_squad_driver.activities_fragments.activity_work2.Work2Activity_step4;
import com.creative.share.apps.wash_squad_driver.databinding.ActivityMapBinding;
import com.creative.share.apps.wash_squad_driver.interfaces.Listeners;
import com.creative.share.apps.wash_squad_driver.language.LanguageHelper;
import com.creative.share.apps.wash_squad_driver.models.Order_Model;
import com.creative.share.apps.wash_squad_driver.remote.Api;
import com.creative.share.apps.wash_squad_driver.share.Common;
import com.creative.share.apps.wash_squad_driver.tags.Tags;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, Listeners.BackListener {
    private ActivityMapBinding binding;
    private String lang;
    private double lat = 0.0, lng = 0.0;
    private String address = "";
    private GoogleMap mMap;
    private Marker marker;
    private float zoom = 15.0f;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private final String fineLocPerm = Manifest.permission.ACCESS_FINE_LOCATION;
    private final int loc_req = 1225;
    private Order_Model.Data data;
    private int counter;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, "en"));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_map);
        initView();
    }

    private void initView() {
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setBackListener(this);

        data = (Order_Model.Data) getIntent().getExtras().getSerializable("detials");
        binding.setLang(lang);
        binding.setOrderModel(data);
        if (data.getStatus() == 11) {
            binding.btnGo.setBackground(getResources().getDrawable(R.drawable.rounded_gray));
            binding.btnGo.setEnabled(false);
            binding.btnArrival.setAlpha(.9f);
            binding.btnArrival.setEnabled(true);
            binding.flTime.setVisibility(View.VISIBLE);

        }
        binding.imUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                counter += 1;
                binding.tvtime.setText(counter + getResources().getString(R.string.minute));
            }
        });
        binding.imDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counter > 0) {
                    counter -= 1;
                    binding.tvtime.setText(counter + getResources().getString(R.string.minute));
                }
            }
        });
        binding.btnCancel.setOnClickListener(view -> {
            Intent intent = new Intent(this, CancelOrderActivity.class);
            intent.putExtra("detials", data);
            startActivityForResult(intent, 1001);
        });
        binding.btnGo.setOnClickListener(view -> {

            goArrive("11");

        });

        binding.btnArrival.setOnClickListener(view -> {
            goArrive("12");
//            if(data.getStatus()==1&&data.getStep().equals("0")){
//            Intent intent = new Intent(this, OrderDetailsActivity.class);
//            intent.putExtra("detials", data);
//
//            startActivityForResult(intent, 1002);

//            }
//            else if(data.getStatus()==2&&data.getStep().equals("0")){
//
////                Intent intent = new Intent(this, Work1Activity_Step1.class);
////                intent.putExtra("detials",data);
////
////                startActivityForResult(intent,1002);
//             //   step1();
//            }
//            else if(data.getStatus()==2&&data.getStep().equals("1")){
//                Intent intent = new Intent(this, Work1Activity_step2.class);
//                intent.putExtra("detials",data);
//
//                startActivityForResult(intent,1002);
//            }
//            else if(data.getStatus()==2&&data.getStep().equals("2")){
//                Intent intent = new Intent(this, Work1Activity_step3.class);
//                intent.putExtra("detials",data);
//
//                startActivityForResult(intent,1002);
//            }
//            else if(data.getStatus()==2&&data.getStep().equals("3")){
//                Intent intent = new Intent(this, Work1Activity_step4.class);
//                intent.putExtra("detials",data);
//
//                startActivityForResult(intent,1002);
//            }
//            else if(data.getStatus()==2&&data.getStep().equals("4")){
//                Intent intent = new Intent(this, Work2Activity_Step1.class);
//                intent.putExtra("detials",data);
//
//                startActivityForResult(intent,1002);
//            }
//            else if(data.getStatus()==2&&data.getStep().equals("5")){
//                Intent intent = new Intent(this, Work2Activity_step2.class);
//                intent.putExtra("detials",data);
//
//                startActivityForResult(intent,1002);
//            }
//            else if(data.getStatus()==2&&data.getStep().equals("6")){
//                Intent intent = new Intent(this, Work2Activity_step3.class);
//                intent.putExtra("detials",data);
//
//                startActivityForResult(intent,1002);
//            }
//            else if(data.getStatus()==2&&data.getStep().equals("7")){
//                Intent intent = new Intent(this, Work2Activity_step4.class);
//                intent.putExtra("detials",data);
//
//                startActivityForResult(intent,1002);
//            }
//            else if(data.getStatus()==2&&data.getStep().equals("8")){
//                Intent intent = new Intent(this, Work2Activity.class);
//                intent.putExtra("detials",data);
//
//                startActivityForResult(intent,1002);
//            }



        });
        updateUI();
        //CheckPermission();
    }

    private void CheckPermission() {
        if (ActivityCompat.checkSelfPermission(this, fineLocPerm) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{fineLocPerm}, loc_req);
        } else {

            initGoogleApi();
        }
    }

    private void initGoogleApi() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        googleApiClient.connect();
    }

    private void cancelOrder(int reason) {
        finish();
    }

    private void updateUI() {

        SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        fragment.getMapAsync(this);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        if (googleMap != null) {
            mMap = googleMap;
            mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.maps));
            mMap.setTrafficEnabled(false);
            mMap.setBuildingsEnabled(false);
            mMap.setIndoorEnabled(true);
            mMap.setMaxZoomPreference(8.0f);
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    if (binding.card1.getVisibility() == View.GONE && binding.card.getVisibility() == View.GONE) {
                        binding.card.setVisibility(View.VISIBLE);
                        binding.card1.setVisibility(View.VISIBLE);
                    } else {
                        binding.card.setVisibility(View.GONE);
                        binding.card1.setVisibility(View.GONE);
                    }
                    return false;
                }
            });
            AddMarker(data.getLatitude(), data.getLongitude());
        }
    }

    private void AddMarker(final double lat, final double lng) {

        this.lat = lat;
        this.lng = lng;
        if (marker == null) {
            IconGenerator iconGenerator = new IconGenerator(this);
            iconGenerator.setBackground(null);
            View view = LayoutInflater.from(this).inflate(R.layout.search_map_icon, null);
            iconGenerator.setContentView(view);
            ImageView im = view.findViewById(R.id.map_icon);
            binding.imageMap.setOnClickListener(v -> {

                String uri = String.format(Locale.ENGLISH, "geo:%f,%f", lat, lng);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));

                startActivity(intent);


            });
            marker = mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).icon(BitmapDescriptorFactory.fromBitmap(iconGenerator.makeIcon())).anchor(iconGenerator.getAnchorU(), iconGenerator.getAnchorV()));
            //  marker.setTitle(oInnerData.getAddress());
            marker.showInfoWindow();
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), zoom));
        } else {
            //   marker.setTitle(oInnerData.getAddress());
            marker.showInfoWindow();
            marker.setPosition(new LatLng(lat, lng));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), zoom));


        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        initLocationRequest();
    }

    private void initLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setFastestInterval(1000);
        locationRequest.setInterval(60000);
        LocationSettingsRequest.Builder request = new LocationSettingsRequest.Builder();
        request.addLocationRequest(locationRequest);
        request.setAlwaysShow(false);


        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, request.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                Status status = locationSettingsResult.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        startLocationUpdate();
                        break;

                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(MapActivity.this, 100);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                        break;

                }
            }
        });

    }

    @Override
    public void onConnectionSuspended(int i) {
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @SuppressLint("MissingPermission")
    private void startLocationUpdate() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                onLocationChanged(locationResult.getLastLocation());
            }
        };
        LocationServices.getFusedLocationProviderClient(this)
                .requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }

    @Override
    public void onLocationChanged(Location location) {
        lat = location.getLatitude();
        lng = location.getLongitude();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
       /* if (googleApiClient!=null)
        {
            if (locationCallback!=null)
            {
                LocationServices.getFusedLocationProviderClient(this).removeLocationUpdates(locationCallback);
                googleApiClient.disconnect();
                googleApiClient = null;
            }
        }*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == loc_req) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initGoogleApi();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {

            startLocationUpdate();
        } else if (requestCode == 1001 && resultCode == RESULT_OK && data != null) {
            int reason = data.getIntExtra("reason", 0);
            cancelOrder(reason);
        } else if (requestCode == 1002) {
            finish();
        }

    }

    //    private void step1() {
//        final Dialog dialog = Common.createProgressDialog(MapActivity.this, getString(R.string.wait));
//        dialog.setCancelable(false);
//        dialog.show();
//        RequestBody id_part = Common.getRequestBodyText(data.getId() + "");
//
//        RequestBody status_part = Common.getRequestBodyText("1");
//        RequestBody step_part = Common.getRequestBodyText("1");
//        RequestBody type_part1 = Common.getRequestBodyText("1");
//        RequestBody type_part2 = Common.getRequestBodyText("2");
//
////        List<MultipartBody.Part> partimageInsideList = getMultipartBodyList(imageInsideList, "images1[]");
////        List<MultipartBody.Part> partimageOutsideList = getMultipartBodyList(imageOutsideList, "images2[]");
//        try {
//            Api.getService(lang, Tags.base_url)
//                    .Step1(id_part, status_part,type_part1,type_part2,step_part).enqueue(new Callback<ResponseBody>() {
//                @Override
//                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                    dialog.dismiss();
//                    if (response.isSuccessful()) {
//                        // Common.CreateSignAlertDialog(adsActivity,getResources().getString(R.string.suc));
//                        Toast.makeText(MapActivity.this, getString(R.string.suc), Toast.LENGTH_SHORT).show();
//
//                        //  adsActivity.finish(response.body().getId_advertisement());
//                        Intent intent = new Intent(MapActivity.this, Work1Activity_step4.class);
//                        intent.putExtra("detials",data);
//
//                        startActivityForResult(intent, 1003);
//                        finish();
//                    } else {
//                        try {
//
//                            Toast.makeText(MapActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
//                            Log.e("Error", response.code() + "" + response.message() + "" + response.errorBody() + response.raw() + response.body() + response.headers());
//                        } catch (Exception e) {
//
//
//                        }
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<ResponseBody> call, Throwable t) {
//                    dialog.dismiss();
//                    try {
//                        Toast.makeText(MapActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
//                        Log.e("Error", t.getMessage());
//                    } catch (Exception e) {
//
//                    }
//                }
//            });
//        } catch (Exception e) {
//            dialog.dismiss();
//            Log.e("error", e.getMessage().toString());
//        }
//    }
    private void goArrive(String status) {
        final Dialog dialog = Common.createProgressDialog(MapActivity.this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();


        try {
            Api.getService(lang, Tags.base_url)
                    .go(data.getId() + "", status).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            dialog.dismiss();
                            if (response.isSuccessful()) {
                                // Common.CreateSignAlertDialog(adsActivity,getResources().getString(R.string.suc));
                                Toast.makeText(MapActivity.this, getString(R.string.suc), Toast.LENGTH_SHORT).show();

                                //  adsActivity.finish(response.body().getId_advertisement());
                                if (status.equals("12")) {
                                    Intent intent = new Intent(MapActivity.this, OrderDetailsActivity.class);
                                    data.setStatus(12);
                                    intent.putExtra("detials", data);

                                    startActivityForResult(intent, 1002);
                                } else {
                                    binding.btnGo.setBackground(getResources().getDrawable(R.drawable.rounded_gray));
                                    binding.btnGo.setEnabled(false);
                                    binding.btnArrival.setAlpha(.9f);
                                    binding.btnArrival.setEnabled(true);
                                    binding.flTime.setVisibility(View.VISIBLE);

                                }
                            } else {
                                try {

                                    Toast.makeText(MapActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                    Log.e("Error", response.code() + "" + response.message() + "" + response.errorBody().string() + response.raw() + response.body() + response.headers());
                                } catch (Exception e) {


                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            dialog.dismiss();
                            try {
                                Toast.makeText(MapActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                Log.e("Error", t.getMessage());
                            } catch (Exception e) {

                            }
                        }
                    });
        } catch (Exception e) {
            dialog.dismiss();
            Log.e("error", e.getMessage().toString());
        }
    }


    @Override
    public void back() {
        finish();
    }
}
