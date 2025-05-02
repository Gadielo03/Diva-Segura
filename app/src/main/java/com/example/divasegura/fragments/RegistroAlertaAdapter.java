package com.example.divasegura.fragments;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;
import com.example.divasegura.R;
import com.example.divasegura.modelos.RegistroAlerta;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RegistroAlertaAdapter extends RecyclerView.Adapter<RegistroAlertaAdapter.RegistroViewHolder> {

    private final List<RegistroAlerta> registros;
    private final Context context;

    public RegistroAlertaAdapter(List<RegistroAlerta> registros, Context context) {
        this.registros = registros;
        this.context = context;
    }

    @NonNull
    @Override
    public RegistroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_registro_alerta, parent, false);
        return new RegistroViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RegistroViewHolder holder, int position) {
        RegistroAlerta registro = registros.get(position);

        if(holder.ultimoRegistroId == registro.getId()) {
            return; // Evitar duplicados
        }
        // Formatear fecha (asumiendo que fecha es un timestamp largo o string en formato ISO)
        String[] fechaHora = formatFecha(registro.getFecha()).split(" ");

        holder.tvFecha.setText(fechaHora[0]);  // Fecha
        holder.tvHora.setText(fechaHora[1]);   // Hora
        holder.tvUbicacion.setText(String.format(Locale.getDefault(),
                "Lat: %.6f, Long: %.6f", registro.getLatitud(), registro.getLongitud()));

        // Determinar tipo de alerta basado en datos (ejemplo)
        String tipoAlerta = determinarTipoAlerta(registro);
        holder.tvTipo.setText(tipoAlerta);
        holder.tvTipo.setBackgroundResource(obtenerColorFondo(tipoAlerta));
        mostrarFoto(registro.getFotografia(),holder);
    }

    @Override
    public int getItemCount() {
        return registros.size();
    }

    // Métodos auxiliares
    private String formatFecha(String fechaOriginal) {
        try {
            // Asume que fechaOriginal es un timestamp (ajusta según tu formato real)
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            if (fechaOriginal.matches("\\d+")) { // Si es timestamp
                Date date = new Date(Long.parseLong(fechaOriginal));
                return sdf.format(date);
            }
            return fechaOriginal; // Si ya está formateada
        } catch (Exception e) {
            return fechaOriginal;
        }
    }

    private String determinarTipoAlerta(RegistroAlerta registro) {
        // Lógica para determinar el tipo de alerta (personaliza según tus necesidades)
        if (registro.getLatitud() == 0 && registro.getLongitud() == 0) {
            return "Incompleta";
        } else if (registro.getFotografia() == null || registro.getFotografia().isEmpty()) {
            return "Básica";
        } else {
            return "Completa";
        }
    }

    private int obtenerColorFondo(String tipoAlerta) {
        switch (tipoAlerta) {
            case "Incompleta":
                return R.drawable.bg_tipo_urgente; // Rojo
            case "Básica":
                return R.drawable.bg_tipo_prevencion; // Ámbar
            default:
                return R.drawable.bg_tipo_alerta; // Morado
        }
    }

    private void mostrarFoto(String imagePath, RegistroViewHolder holder) {
        if (imagePath == null) {
            holder.ivFoto.setImageResource(R.drawable.baseline_image_not_supported_24); // Imagen por defecto
            return;
        }

        File imgFile = new File(imagePath);
        if (imgFile.exists()) {
            // Usa FileProvider para evitar problemas de permisos en Android 7+
            Uri photoUri = FileProvider.getUriForFile(
                    context,
                    context.getPackageName() + ".provider",
                    imgFile
            );

            holder.ivFoto.setImageURI(photoUri);
        } else {
            holder.ivFoto.setImageResource(R.drawable.ic_launcher_background);
        }
    }

    // ViewHolder
    public static class RegistroViewHolder extends RecyclerView.ViewHolder {
        TextView tvFecha, tvHora, tvUbicacion, tvTipo;
        ImageView ivFoto;
        long ultimoRegistroId = -1;
        public RegistroViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvHora = itemView.findViewById(R.id.tvHora);
            tvUbicacion = itemView.findViewById(R.id.tvUbicacion);
            tvTipo = itemView.findViewById(R.id.tvTipo);
            ivFoto = itemView.findViewById(R.id.ivFoto);
        }
    }
}