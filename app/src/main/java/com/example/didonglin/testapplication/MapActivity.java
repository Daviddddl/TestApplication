package com.example.didonglin.testapplication;

import android.content.DialogInterface;
import android.location.Geocoder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

public class MapActivity extends AppCompatActivity implements View.OnClickListener{
    private MapView mapView;
    private BaiduMap baiduMap;
    private MapStatusUpdate update;
    private LocationClient location;

    private Button loc;
    private Button latlng;
    private Button desc;
    private Button mypoi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        location = new LocationClient(getApplicationContext());
        location.registerLocationListener(new MyLocation());
        setContentView(R.layout.activity_map);
        mapView = findViewById(R.id.map);
        baiduMap = mapView.getMap();
        baiduMap.setMyLocationEnabled(true);
        location.start();

        loc = findViewById(R.id.loc);
        latlng = findViewById(R.id.latlng);
        desc = findViewById(R.id.desc);
        mypoi = findViewById(R.id.mypoi);

        loc.setOnClickListener(this);
        latlng.setOnClickListener(this);
        desc.setOnClickListener(this);
        mypoi.setOnClickListener(this);
    }
    class MyLocation implements BDLocationListener{
        @Override
        public void onReceiveLocation(final BDLocation bdLocation){
            runOnUiThread(new Runnable(){
                @Override
                public void run(){
                    /*if(bdLocation.getLocType() == BDLocation.TypeGpsLocation)
                        Log.d("bebug","gps");
                    if(bdLocation.getLocType() == BDLocation.TypeNetWorkLocation)
                        Log.d("bebug","network");
                        */
                    Double lat = bdLocation.getLatitude();
                    Double lng = bdLocation.getLongitude();
                    LatLng latLng = new LatLng(lat,lng);
                    update = MapStatusUpdateFactory.newLatLng(latLng);
                    baiduMap.animateMapStatus(update);
                    baiduMap.setMyLocationData(initMyLocationData(lat,lng));
                    /*update = MapStatusUpdateFactory.zoomBy(16f);
                    baiduMap.animateMapStatus(update);*/
                }
            });
        }
    }
    private MyLocationData initMyLocationData(Double lat,Double lng){
        MyLocationData data = new MyLocationData.Builder()
                .latitude(lat)
                .longitude(lng)
                .build();
        return data;
    }
    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.loc:
                location.requestLocation();
                break;
            case R.id.latlng:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                View view = LayoutInflater.from(this).inflate(R.layout.layout_latlng,null);
                builder.setView(view);
                final EditText latEdit = view.findViewById(R.id.lat_edit);
                final EditText lngEdit = view.findViewById(R.id.lng_edit);
                builder.setCancelable(true)
                        .setPositiveButton("确定",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog,int id){
                                Double lat = Double.valueOf(latEdit.getText().toString());
                                Double lng = Double.valueOf(lngEdit.getText().toString());
                                LatLng latLng = new LatLng(lat,lng);
                                update = MapStatusUpdateFactory.newLatLng(latLng);
                                baiduMap.animateMapStatus(update);
                                baiduMap.setMyLocationData(initMyLocationData(lat,lng));
                                /*update = MapStatusUpdateFactory.zoomTo(16f);
                                baiduMap.animateMapStatus(update);*/
                            }
                        });
                builder.create().show();
                break;
            case R.id.desc:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                View view1 = LayoutInflater.from(this).inflate(R.layout.layout_desc,null);
                builder1.setView(view1);
                final EditText descEdit = view1.findViewById(R.id.desc_edit);
                builder1.setCancelable(true)
                        .setPositiveButton("确定",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog,int id){
                                String desc = descEdit.getText().toString();
                                GeoCoder search = GeoCoder.newInstance();
                                OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener(){
                                    public void onGetGeoCodeResult(GeoCodeResult result){
                                        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR){
                                            Toast.makeText(MapActivity.this,"没找到！！！",Toast.LENGTH_LONG).show();
                                        }
                                        LatLng latLng = result.getLocation();
                                        update = MapStatusUpdateFactory.newLatLng(latLng);
                                        baiduMap.animateMapStatus(update);
                                        baiduMap.setMyLocationData(initMyLocationData(latLng.latitude,latLng.longitude));
                                    }
                                    @Override
                                    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result){
                                    }
                                };
                                search.setOnGetGeoCodeResultListener(listener);
                                search.geocode(new GeoCodeOption().city("北京").address(descEdit.getText().toString()));
                                search.destroy();
                            }
                        });
                builder1.create().show();
                break;
            default:
                break;
        }
    }
    @Override
    protected void onResume(){
        super.onResume();
        mapView.onResume();
    }
    @Override
    protected void onPause(){
        super.onPause();
        mapView.onPause();
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        location.stop();
        mapView.onDestroy();
    }
}
