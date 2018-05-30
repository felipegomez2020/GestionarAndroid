package gestionar.soft3.inge.gestionar.Utilidades;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utilidades
{



    public static String stringMonetarioToDouble(String str, Integer opt) {
        String retorno = "0";
        try {
            if (opt==1) {
                String userInput = "" + str.toString().replaceAll("[^\\d]", "");
                StringBuilder cashAmountBuilder = new StringBuilder(userInput);

                while (cashAmountBuilder.length() > 3 && cashAmountBuilder.charAt(0) == '0') {
                    cashAmountBuilder.deleteCharAt(0);
                }
                while (cashAmountBuilder.length() < 3) {
                    cashAmountBuilder.insert(0, '0');
                }
                cashAmountBuilder.insert(cashAmountBuilder.length() - 2, ',');
                if (cashAmountBuilder.length() >= 7) {
                    cashAmountBuilder.insert(cashAmountBuilder.length() - 6, '.');
                }
                if (cashAmountBuilder.length() == 11) {
                    cashAmountBuilder.insert(cashAmountBuilder.length() - 10, '.');
                }
                //cashAmountBuilder.insert(0, '$');
                retorno = cashAmountBuilder.toString();
            }else if(opt==3)
            {
                str =str.replaceAll("[\\.]", "");
                str= str.replaceAll("[\\,]", "");
                str = str.substring(0,str.length()-2);

                Log.i("VALOR REAL", retorno);
                retorno = str;
            }
            else {
                str =str.replaceAll("[\\.]", "");
                str= str.replaceAll("[\\,]", ".");


                Log.i("VALOR REAL", retorno);
                retorno = str;
            }
        } catch (NumberFormatException e) {

        }

        return retorno;
    }
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
