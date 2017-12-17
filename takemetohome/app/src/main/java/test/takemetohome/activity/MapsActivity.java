package test.takemetohome.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import test.takemetohome.R;
import test.takemetohome.controller.AndroidLocationController;
import test.takemetohome.dialog.fragments.SaveMyLocationDialogFragment;
import test.takemetohome.root.AppSettings;
import test.takemetohome.root.Constants;
import test.takemetohome.utils.SharedPrefUtil;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private AppCompatTextView tvSaveLocation;
    private GoogleMap mMap;
    private Context context;
    private AndroidLocationController androidLocationController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        context = getBaseContext();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (SharedPrefUtil.getInstance().getBooleanForKey(Constants.getInstance().IS_SHORT_CUT_CREATED)) {
            // redirect to google map.
            final String destinationLat = SharedPrefUtil.getInstance().getStringForKey(Constants.getInstance().DESTINATION_LATITUDE);
            final String destinationLng = SharedPrefUtil.getInstance().getStringForKey(Constants.getInstance().DESTINATION_LONGITUDE);

            if (!TextUtils.isEmpty(destinationLat) && !TextUtils.isEmpty(destinationLng)) {
                // get current location and make api call
                androidLocationController = new AndroidLocationController();
                androidLocationController.setLocationListener(new AndroidLocationController.ILocationListener() {
                    @Override
                    public void onLocation(Location location) {
                        double currentLat = location.getLatitude();
                        double currentLng = location.getLongitude();
                        double destLat = Double.valueOf(destinationLat);
                        double destLng = Double.valueOf(destinationLng);

                        /*double destLat = 23.2156354;
                        double destLng = 72.6369415;*/

                        openGoogleMap(currentLat, currentLng, destLat, destLng);
                    }

                    @Override
                    public void onLocationError() {

                    }

                    @Override
                    public void onUserDeniedLocationEnable() {

                    }

                    @Override
                    public void onUserDeniedPermission() {

                    }
                });

                androidLocationController.getLocation(this);
            } else {
                setUpUI();
            }
        } else {
            setUpUI();
        }

    }

    private void setUpUI() {
        tvSaveLocation = findViewById(R.id.a_main_tv_add_location);

        attachListeners();
    }

    private void attachListeners() {
        tvSaveLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveMyLocationDialogFragment fragment = new SaveMyLocationDialogFragment();
                fragment.show(getSupportFragmentManager().beginTransaction(), SaveMyLocationDialogFragment.TAG);
            }
        });
    }

    private void openGoogleMap(double currentLocationLatitude, double currentLocationLongitude, double latitude, double longitude) {
        //String uri = String.format(Locale.ENGLISH, "geo:%f,%f?q=%f,%f", latitude, longitude,latitude, longitude);
        String uri = String.format(AppSettings.getInstance().getLocale(), "http://maps.google.com/maps?saddr=%f,%f&daddr=%f,%f", currentLocationLatitude, currentLocationLongitude, latitude, longitude);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        mapIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (isIntentAvailable(context, mapIntent)) {
            context.startActivity(mapIntent);
            finish();
        } else {
            Toast.makeText(context, "Google Map Not Available!", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isIntentAvailable(Context ctx, Intent intent) {
        final PackageManager mgr = ctx.getPackageManager();
        List<ResolveInfo> list = mgr.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
