package test.takemetohome.dialog.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;

import test.takemetohome.R;
import test.takemetohome.activity.MapsActivity;
import test.takemetohome.root.Constants;
import test.takemetohome.utils.SharedPrefUtil;
import test.takemetohome.view.AppButton;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by admin on 13/12/17.
 */

public class SaveMyLocationDialogFragment extends AppCompatDialogFragment
{
    public static final String TAG = SaveMyLocationDialogFragment.class.getSimpleName();
    private final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 5554;
    AppButton btnChooseOnMap;
    AppButton btnClose;

    private Context context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        context = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        context = getContext();
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.df_save_my_location, null);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        btnChooseOnMap = view.findViewById(R.id.df_save_location_btn_choose_on_map);
        btnClose = view.findViewById(R.id.df_save_location_btn_close);

        attachListeners();
    }

    private void attachListeners()
    {
        btnChooseOnMap.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                try
                {
                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).build(getActivity());
                    getActivity().startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                }
                catch (GooglePlayServicesRepairableException e)
                {
                    // TODO: Handle the error.
                }
                catch (GooglePlayServicesNotAvailableException e)
                {
                    // TODO: Handle the error.
                }
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dismiss();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode)
        {
            case RESULT_OK:
            {
                switch (requestCode)
                {
                    case PLACE_AUTOCOMPLETE_REQUEST_CODE:
                    {
                        Place place = PlaceAutocomplete.getPlace(getContext(), data);
                        LatLng latLng = place.getLatLng();
                        double latitude = latLng.latitude;
                        double longitude = latLng.longitude;

                        SharedPrefUtil.getInstance().setStringToSharedPref(Constants.getInstance().DESTINATION_LATITUDE, String.valueOf(latitude));
                        SharedPrefUtil.getInstance().setStringToSharedPref(Constants.getInstance().DESTINATION_LONGITUDE, String.valueOf(longitude));

                        Toast.makeText(context, "Destination Address selected!", Toast.LENGTH_LONG).show();
                        showAddShortCutAlertDialog();
                        break;
                    }
                }
                break;
            }

            case RESULT_CANCELED:
            {
                Toast.makeText(context, "Please select valid destination Address!", Toast.LENGTH_LONG).show();
                break;
            }
        }

    }

    private void showAddShortCutAlertDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Add Shortcut")
                .setMessage("Do you want to have quick access to your saved location from your home page?")
                /*.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                    }
                })*/
                .setCancelable(false)
                .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        addShortcut();
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void addShortcut()
    {
        //Adding shortcut for MainActivity
        //on Home screen
        Intent shortcutIntent = new Intent(context, MapsActivity.class);
        shortcutIntent.setAction(Intent.ACTION_MAIN);

        Intent addIntent = new Intent();
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, getResources().getString(R.string.app_name));
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(context, R.drawable.ic_launcher_background));
        addIntent.putExtra("duplicate", false);
        addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        context.sendBroadcast(addIntent);

        SharedPrefUtil.getInstance().setBooleanForKey(Constants.getInstance().IS_SHORT_CUT_CREATED, true);
        Toast.makeText(context, "Short cut added to your home screen!!!", Toast.LENGTH_LONG).show();
    }
}
