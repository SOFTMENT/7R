package in.softment.playrangers;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.util.concurrent.Callable;

import in.softment.playrangers.Services.Service;

public class MyAlertDialog {

    public static void show(Context context, String title, String message, Callable<Void> methodParam) {

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setCancelable(false);
            AlertDialog alertDialog = builder.create();
            alertDialog.setTitle(title);
            alertDialog.setMessage(message);
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    try {
                        alertDialog.hide();
                        methodParam.call();
                    }
                    catch (Exception ignored) {

                    }
                }
            });

            alertDialog.show();

    }
}
