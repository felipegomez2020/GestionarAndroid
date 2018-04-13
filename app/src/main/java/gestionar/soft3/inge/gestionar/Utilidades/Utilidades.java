package gestionar.soft3.inge.gestionar.Utilidades;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utilidades
{


    public static SharedPreferences obtener_preferencias(Context context)
    {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void escribir_preferencia(Context context, String key, String value)
    {
        SharedPreferences pre = obtener_preferencias(context);
        SharedPreferences.Editor editor = pre.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String obtener_preferencia(Context context, String key)
    {
        SharedPreferences pre = obtener_preferencias(context);
        return pre.getString(key, "");
    }


    public static boolean validarCampo(String campo)
    {
        return (campo.equals("")?true:false);
    }
    public static boolean isNumeric(String cadena) {

        boolean resultado;

        try {
            Integer.parseInt(cadena);
            resultado = true;
        } catch (NumberFormatException excepcion) {
            resultado = false;
        }

        return resultado;
    }




}
