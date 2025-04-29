package com.example.divasegura.activities;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

public class Alert {
    private Context context;

    public Alert(Context context) {
        this.context = context;
    }

    public void call911() {
        try {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:8713445900"));

            if(callIntent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(callIntent);
            } else {
                Toast.makeText(context, "No hay aplicaciones para realizar llamadas", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(context, "Error al realizar la llamada", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

}
