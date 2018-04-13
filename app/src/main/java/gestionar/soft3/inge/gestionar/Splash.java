package gestionar.soft3.inge.gestionar;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

import gestionar.soft3.inge.gestionar.Utilidades.Utilidades;

public class Splash extends AppCompatActivity {

    private long timeSplash = 4000;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run()
            {
                String login = Utilidades.obtener_preferencia(getApplicationContext(), "log");
                if (login.equals(""))
                {
                    intent = new Intent(getApplicationContext(), Login.class);
                    startActivity(intent);
                    finish();
                }
                else
                    {
                        intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }

            }
        };
        Timer timer = new Timer();
        timer.schedule(timerTask, timeSplash);
    }
}
