package test.takemetohome.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
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
import test.takemetohome.dialog.fragments.SaveMyLocationDialogFragment;
import test.takemetohome.root.AppSettings;
import test.takemetohome.root.Constants;
import test.takemetohome.utils.SharedPrefUtil;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback
{

    private AppCompatTextView tvSaveLocation;
    private GoogleMap mMap;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        context = getBaseContext();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (SharedPrefUtil.getInstance().getBooleanForKey(Constants.getInstance().IS_SHORT_CUT_CREATED))
        {
            // redirect to google map.
            String destinationLat = SharedPrefUtil.getInstance().getStringForKey(Constants.getInstance().DESTINATION_LATITUDE);
            String destinationLng = SharedPrefUtil.getInstance().getStringForKey(Constants.getInstance().DESTINATION_LONGITUDE);

            if (!TextUtils.isEmpty(destinationLat) && !TextUtils.isEmpty(destinationLng))
            {
                // get current location and make api call

              /*  double currentLat;
                double currentLng;


                openGoogleMap(currentLat,currentLng, destinationLat, destinationLng);*/
            }
            else
            {
                setUpUI();
            }
        }
        else
        {
            setUpUI();
        }
    }

    private void setUpUI()
    {
        tvSaveLocation = findViewById(R.id.a_main_tv_add_location);

        attachListeners();
    }

    private void attachListeners()
    {
        tvSaveLocation.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                SaveMyLocationDialogFragment fragment = new SaveMyLocationDialogFragment();
                fragment.show(getSupportFragmentManager().beginTransaction(), SaveMyLocationDialogFragment.TAG);
            }
        });
    }

    private void openGoogleMap(double currentLocationLatitude, double currentLocationLongitude, double latitude, double longitude)
    {
        //String uri = String.format(Locale.ENGLISH, "geo:%f,%f?q=%f,%f", latitude, longitude,latitude, longitude);
        String uri = String.format(AppSettings.getInstance().getLocale(), "http://maps.google.com/maps?saddr=%f,%f&daddr=%f,%f", currentLocationLatitude, currentLocationLongitude, latitude, longitude);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        mapIntent.setPackage("com.google.android.apps.maps");
        if (isIntentAvailable(context, mapIntent))
        {
            context.startActivity(mapIntent);
        }
        else
        {
            Toast.makeText(context, "Google Map Not Available!", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isIntentAvailable(Context ctx, Intent intent)
    {
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
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
