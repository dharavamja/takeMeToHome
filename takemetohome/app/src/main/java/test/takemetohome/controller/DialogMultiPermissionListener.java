package test.takemetohome.controller;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.EmptyMultiplePermissionsListener;

import java.util.List;


public class DialogMultiPermissionListener extends EmptyMultiplePermissionsListener
{
    private Context context;
    private String title;
    private String message;
    private String positiveButtonText;
    private Drawable icon;
    private IPermissionGrantedListener permissionGrantedListener;
    private IPermissionDeniedListener permissionDeniedListener;

    public interface IPermissionGrantedListener
    {
        void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse);

    }

    public interface IPermissionDeniedListener
    {
        void onPermissionDeniedListener(DialogMultiPermissionListener dialogMultiPermissionListener);
    }

    private DialogMultiPermissionListener(Context context, String title,
                                          String message, String positiveButtonText, Drawable icon, IPermissionGrantedListener permissionGrantedListener, IPermissionDeniedListener permissionDeniedListener)
    {
        this.context = context;
        this.title = title;
        this.message = message;
        this.positiveButtonText = positiveButtonText;
        this.icon = icon;
        this.permissionGrantedListener = permissionGrantedListener;
        this.permissionDeniedListener = permissionDeniedListener;
    }


    @Override
    public void onPermissionsChecked(MultiplePermissionsReport report)
    {
        super.onPermissionsChecked(report);
        if (report.areAllPermissionsGranted())
        {
            if (permissionGrantedListener != null)
            {
                permissionGrantedListener.onPermissionGranted(null);
            }
        }
        else
        {
            if (permissionDeniedListener != null)
            {
                permissionDeniedListener.onPermissionDeniedListener(this);
            }
            else
            {
                showDialog();
            }
        }
    }

    @Override
    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token)
    {
        super.onPermissionRationaleShouldBeShown(permissions, token);
        token.continuePermissionRequest();
    }

    public void showDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setIcon(icon)
                .setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                    }
                });
        builder.show();
    }

    /**
     * Builder class to configure the displayed dialog.
     * Non set fields will be initialized to an empty string.
     */
    public static class Builder
    {
        private final Context context;
        private String title;
        private String message;
        private String buttonText;
        private Drawable icon;
        private IPermissionGrantedListener permissionGrantedListener;
        private IPermissionDeniedListener deniedListener;

        private Builder(Context context)
        {
            this.context = context;
        }

        public static Builder withContext(Context context)
        {
            return new Builder(context);
        }

        public Builder withTitle(String title)
        {
            this.title = title;
            return this;
        }

        public Builder withTitle(@StringRes int resId)
        {
            this.title = context.getString(resId);
            return this;
        }

        public Builder withMessage(String message)
        {
            this.message = message;
            return this;
        }

        public Builder withMessage(@StringRes int resId)
        {
            this.message = context.getString(resId);
            return this;
        }

        public Builder withButtonText(String buttonText)
        {
            this.buttonText = buttonText;
            return this;
        }

        public Builder withButtonText(@StringRes int resId)
        {
            this.buttonText = context.getString(resId);
            return this;
        }

        public Builder withIcon(Drawable icon)
        {
            this.icon = icon;
            return this;
        }

        /*public Builder withIcon(@DrawableRes int resId)
        {
            this.icon = ViewUtils.getDrawable(context.getResources(), resId);
            return this;
        }*/

        public Builder withDeniedListener(IPermissionDeniedListener deniedListener)
        {
            this.deniedListener = deniedListener;
            return this;
        }

        public Builder withGrantedListener(IPermissionGrantedListener grantedListener)
        {
            this.permissionGrantedListener = grantedListener;
            return this;
        }

        public DialogMultiPermissionListener build()
        {
            String title = this.title == null ? "" : this.title;
            String message = this.message == null ? "" : this.message;
            String buttonText = this.buttonText == null ? "" : this.buttonText;
            return new DialogMultiPermissionListener(context, title, message, buttonText, icon, permissionGrantedListener, deniedListener);
        }
    }

}
