package test.takemetohome.controller;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.listener.PermissionGrantedResponse;

import test.takemetohome.R;


public class AndroidLocationController
{
    private static final int REQUEST_CHECK_SETTINGS = 1001;
    private Activity context;
    private LocationRequest locationRequest;
    private ILocationListener locationListener;
    private GoogleApiClient googleApiClient;
    private GoogleApiClient.OnConnectionFailedListener connectionFailedListener = new GoogleApiClient.OnConnectionFailedListener()
    {
        @Override
        public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
        {

        }
    };
    private LocationListener gpsLocationListener = new LocationListener()
    {
        @Override
        public void onLocationChanged(Location location)
        {
            if (locationListener != null)
            {
                locationListener.onLocation(location);
            }
        }
    };
    private GoogleApiClient.ConnectionCallbacks connectionCallbacks = new GoogleApiClient.ConnectionCallbacks()
    {
        @Override
        public void onConnected(@Nullable Bundle bundle)
        {
            //Log("AndroidLocationController---->onConnected");
            locationRequest = new LocationRequest();
            locationRequest.setInterval(100);
            locationRequest.setNumUpdates(1);
            locationRequest.setFastestInterval(50);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            checkForLocationSettings();
        }

        @Override
        public void onConnectionSuspended(int i)
        {
            //Log("AndroidLocationController---->onConnectionSuspended");
        }
    };

    public void setLocationListener(ILocationListener locationListener)
    {
        this.locationListener = locationListener;
    }

    public void getLocation(Activity context)
    {
        this.context = context;
        checkLocationPermissions(new IPermissionListener()
        {
            @Override
            public void onPermissionGranted()
            {
                checkSettings();
            }

            @Override
            public void onSettingsFailed()
            {
                if (locationListener != null)
                {
                    locationListener.onUserDeniedPermission();
                }
            }
        });
    }

    private void checkForLocationSettings()
    {
        //Log("AndroidLocationController---->checkForLocationSettings");
        LocationSettingsRequest locationSettingsRequest = new LocationSettingsRequest.Builder()
                .setAlwaysShow(true)
                .addLocationRequest(locationRequest).build();
        PendingResult<LocationSettingsResult> locationResult = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, locationSettingsRequest);
        locationResult.setResultCallback(new ResultCallback<LocationSettingsResult>()
        {
            @Override
            public void onResult(@NonNull LocationSettingsResult result)
            {
                final Status status = result.getStatus();
                //Log("AndroidLocationController---->LocationSettingsResult-->" + status.toString());
                switch (status.getStatusCode())
                {
                    case LocationSettingsStatusCodes.CANCELED:
                    {
                        //Log("User cancelled ");

                        break;
                    }
                    case LocationSettingsStatusCodes.SUCCESS:
                    {
                        //Log("Location Settings Success");
                        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, gpsLocationListener);
                        break;
                    }
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                    {
                        //Log("Location Settings Resolution Required");
                        try
                        {
                            status.startResolutionForResult(context, REQUEST_CHECK_SETTINGS);
                        }
                        catch (IntentSender.SendIntentException e)
                        {
                            e.printStackTrace();
                        }
                        break;
                    }
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                    {
                        //Log("Setting Change Unavailable ");
                        break;
                    }
                }
            }
        });
    }

    private void checkSettings()
    {
        googleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(connectionCallbacks)
                .addOnConnectionFailedListener(connectionFailedListener)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    private void checkLocationPermissions(final IPermissionListener permissionListener)
    {
        int sdkVersion = Build.VERSION.SDK_INT;
        if (sdkVersion >= 23)
        {
            DialogMultiPermissionListener dialogPermissionListener = DialogMultiPermissionListener.Builder
                    .withContext(context)
                    .withTitle(R.string.permission_denied)
                    .withMessage(R.string.location_permission_message)
                    .withButtonText(android.R.string.ok)
                    //.withIcon(R.mipmap.ic_launcher)
                    .withGrantedListener(new DialogMultiPermissionListener.IPermissionGrantedListener()
                    {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse)
                        {
                            permissionListener.onPermissionGranted();
                        }
                    })
                    .withDeniedListener(new DialogMultiPermissionListener.IPermissionDeniedListener()
                    {
                        @Override
                        public void onPermissionDeniedListener(DialogMultiPermissionListener dialogMultiPermissionListener)
                        {
                            permissionListener.onSettingsFailed();
                        }
                    })
                    .build();
            if (!Dexter.isRequestOngoing())
            {
                Dexter.checkPermissions(dialogPermissionListener, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION);
            }
        }
        else
        {
            permissionListener.onPermissionGranted();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        switch (resultCode)
        {
            case Activity.RESULT_OK:
            {
                switch (requestCode)
                {
                    case REQUEST_CHECK_SETTINGS:
                    {
                        checkForLocationSettings();
                        break;
                    }
                }
                break;
            }
            case Activity.RESULT_CANCELED:
            {
                switch (requestCode)
                {
                    case REQUEST_CHECK_SETTINGS:
                    {
                        if (locationListener != null)
                        {
                            locationListener.onUserDeniedLocationEnable();
                        }
                        break;
                    }
                }
                break;
            }
        }
    }

    public interface ILocationListener
    {
        void onLocation(Location location);

        void onLocationError();

        void onUserDeniedLocationEnable();

        void onUserDeniedPermission();
    }

    public interface IPermissionListener
    {
        void onPermissionGranted();

        void onSettingsFailed();
    }

}
