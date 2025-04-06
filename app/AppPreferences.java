import android.content.Context;
import android.content.SharedPreferences;

public class AppPreferences {
    private static final String PREFS_NAME = "MyAppPreferences";
    private static final String KEY_FIRST_RUN = "is_first_run";

    // Verifica si es la primera ejecución
    public static boolean isFirstRun(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return preferences.getBoolean(KEY_FIRST_RUN, true);
    }

    // Marca que la app ya se ejecutó al menos una vez
    public static void setFirstRunCompleted(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        preferences.edit().putBoolean(KEY_FIRST_RUN, false).apply();
    }

    // Método para debug/reset (opcional)
    public static void resetFirstRun(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        preferences.edit().putBoolean(KEY_FIRST_RUN, true).apply();
    }
}