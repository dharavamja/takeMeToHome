package test.takemetohome.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import test.takemetohome.R;
import test.takemetohome.dialog.fragments.SaveMyLocationDialogFragment;
import test.takemetohome.root.Constants;
import test.takemetohome.utils.SharedPrefUtil;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback
{

    private AppCompatTextView tvSaveLocation;
    private GoogleMap mMap;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (SharedPrefUtil.getInstance().getBooleanForKey(Constants.getInstance().IS_SHORT_CUT_CREATED))
        {
            // redirect to google map.
            String destinationLat = SharedPrefUtil.getInstance().getStringForKey(Constants.getInstance().DESTINATION_LATITUDE);
            String destinationLng = SharedPrefUtil.getInstance().getStringForKey(Constants.getInstance().DESTINATION_LONGITUDE);



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
