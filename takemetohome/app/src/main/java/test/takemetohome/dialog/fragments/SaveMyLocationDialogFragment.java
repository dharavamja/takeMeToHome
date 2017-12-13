package test.takemetohome.dialog.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import test.takemetohome.R;
import test.takemetohome.view.AppButton;

/**
 * Created by admin on 13/12/17.
 */

public class SaveMyLocationDialogFragment extends AppCompatDialogFragment
{
    public static final String TAG = SaveMyLocationDialogFragment.class.getSimpleName();
    private Context context;

    AppButton btnChooseOnMap;
    AppButton btnClose;

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
}
