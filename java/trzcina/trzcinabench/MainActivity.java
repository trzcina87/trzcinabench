package trzcina.trzcinabench;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

public class MainActivity extends AppCompatActivity {

    private TextView wynik;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wynik = (TextView)findViewById(R.id.wynik);
        findViewById(R.id.bitmapy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wczytajBitmapy();
            }
        });
        findViewById(R.id.alokacja).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alokacjaPamieci();
            }
        });
        findViewById(R.id.zapis).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zapisDanych();
            }
        });
        findViewById(R.id.odczyt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                odczytDanych();
            }
        });
        findViewById(R.id.podzial).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                podzialPliku();
            }
        });
        findViewById(R.id.zapistab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zapisDanychReczny();
            }
        });
        findViewById(R.id.odczyttab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                odczytDanychTab();
            }
        });
    }

    private void ustawWynik(final String string) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                wynik.setText(string);
            }
        });
    }

    private void wczytajBitmapy() {
        wynik.setText("");
        new Thread(new Runnable() {
            @Override
            public void run() {
                long start = System.currentTimeMillis();
                Bitmap tmp;
                int size = 0;
                try {
                    tmp = BitmapFactory.decodeResource(getResources(), R.mipmap.b1);
                    size = size + tmp.getWidth();
                    tmp = null;
                    tmp = BitmapFactory.decodeResource(getResources(), R.mipmap.b2);
                    size = size + tmp.getWidth();
                    tmp = null;
                    tmp = BitmapFactory.decodeResource(getResources(), R.mipmap.b3);
                    size = size + tmp.getWidth();
                    tmp = null;
                    tmp = BitmapFactory.decodeResource(getResources(), R.mipmap.b4);
                    size = size + tmp.getWidth();
                    tmp = null;
                    tmp = BitmapFactory.decodeResource(getResources(), R.mipmap.b5);
                    size = size + tmp.getWidth();
                    tmp = null;
                } catch (OutOfMemoryError e) {
                }
                long koniec = System.currentTimeMillis();
                long czas = koniec - start;
                ustawWynik("Wczytanie 5 bitmap (suma szer: " + size + "): " + czas);
            }
        }).start();
    }

    private void alokacjaPamieci() {
        wynik.setText("");
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        final int pamiec = am.getMemoryClass();
        final int pamiecmax = am.getLargeMemoryClass();
        new Thread(new Runnable() {
            @Override
            public void run() {
                long bajtylong = Long.BYTES;
                long tablica[][] = new long[1000][];
                boolean dzialam = true;
                int i = 0;
                while(dzialam) {
                    try {
                        tablica[i] = new long[128000];
                        i = i + 1;
                        Thread.sleep(5);
                    } catch (OutOfMemoryError e) {
                        dzialam = false;
                    } catch (InterruptedException e) {

                    }
                }
                ustawWynik("Long: " + bajtylong + " bajty\nPamiec: " + pamiec + "\nPamiec Max: " + pamiecmax + "\nZaalokowano: " + i + " MB");
            }
        }).start();
    }

    private void zapisDanych() {
        wynik.setText("");
        new Thread(new Runnable() {
            @Override
            public void run() {
                long start = System.currentTimeMillis();
                Dane dane = new Dane();
                dane.wypelnij();
                boolean sukces = true;
                try {
                    FileOutputStream cachefileoutput = MainActivity.this.openFileOutput("dane", Context.MODE_PRIVATE);
                    ObjectOutputStream cacheobjectoutput = new ObjectOutputStream(cachefileoutput);
                    cacheobjectoutput.writeObject(dane);
                    cacheobjectoutput.close();
                    cachefileoutput.close();
                } catch (Exception e) {
                    sukces = false;
                }
                long koniec = System.currentTimeMillis();
                long czas = koniec - start;
                long suma = dane.suma();
                if(sukces) {
                    ustawWynik("Powodzenie: " + czas + " Suma: " + suma);
                } else {
                    ustawWynik("Blad: " + czas + " Suma: " + suma);
                }
            }
        }).start();
    }

    private void zapisDanychReczny() {
        wynik.setText("");
        new Thread(new Runnable() {
            @Override
            public void run() {
                long start = System.currentTimeMillis();
                Dane dane = new Dane();
                dane.wypelnij();
                boolean sukces = true;
                try {
                    ByteBuffer bajtytablicy = ByteBuffer.allocate(4 * Dane.rozmiar * Dane.rozmiar);
                    IntBuffer intbajtytablicy = bajtytablicy.asIntBuffer();
                    for(int i = 0; i < Dane.rozmiar; i++) {
                        intbajtytablicy.put(dane.tablica[i]);
                    }
                    FileOutputStream plik = MainActivity.this.openFileOutput("danev2", Context.MODE_PRIVATE);
                    plik.write(bajtytablicy.array(), 0, 4 * Dane.rozmiar * Dane.rozmiar);
                    plik.close();
                } catch (Exception e) {
                    sukces = false;
                    e.printStackTrace();
                }
                long koniec = System.currentTimeMillis();
                long czas = koniec - start;
                long suma = dane.suma();
                if(sukces) {
                    ustawWynik("Powodzenie: " + czas + " Suma: " + suma);
                } else {
                    ustawWynik("Blad: " + czas + " Suma: " + suma);
                }
            }
        }).start();
    }

    private void odczytDanych() {
        wynik.setText("");
        new Thread(new Runnable() {
            @Override
            public void run() {
                long start = System.currentTimeMillis();
                boolean sukces = true;
                Dane dane = null;
                try {
                    FileInputStream cachefileinput = MainActivity.this.openFileInput("dane");
                    ObjectInputStream cahceobjectinput = new ObjectInputStream(cachefileinput);
                    dane = (Dane)cahceobjectinput.readObject();
                    cahceobjectinput.close();
                    cachefileinput.close();
                } catch (Exception e) {
                    sukces = false;
                }
                long koniec = System.currentTimeMillis();
                long czas = koniec - start;
                long suma = dane.suma();
                if(sukces) {
                    ustawWynik("Powodzenie: " + czas + " Suma: " + suma);
                } else {
                    ustawWynik("Blad: " + czas + " Suma: " + suma);
                }
            }
        }).start();
    }

    private void odczytDanychTab() {
        wynik.setText("");
        new Thread(new Runnable() {
            @Override
            public void run() {
                long start = System.currentTimeMillis();
                boolean sukces = true;
                Dane dane = new Dane();
                dane.inicjuj();
                try {
                    ByteBuffer bajtytablicy = ByteBuffer.allocate(4 * Dane.rozmiar * Dane.rozmiar);
                    FileInputStream plik = MainActivity.this.openFileInput("danev2");
                    plik.read(bajtytablicy.array(), 0, 4 * Dane.rozmiar * Dane.rozmiar);
                    plik.close();
                    IntBuffer intbajtytablicy = bajtytablicy.asIntBuffer();
                    for(int i = 0; i < Dane.rozmiar; i++) {
                        intbajtytablicy.get(dane.tablica[i], 0, Dane.rozmiar);
                    }
                } catch (Exception e) {
                    sukces = false;
                }
                long koniec = System.currentTimeMillis();
                long czas = koniec - start;
                long suma = dane.suma();
                if(sukces) {
                    ustawWynik("Powodzenie: " + czas + " Suma: " + suma);
                } else {
                    ustawWynik("Blad: " + czas + " Suma: " + suma);
                }
            }
        }).start();
    }

    private void podzialPliku() {
        wynik.setText("");
        new Thread(new Runnable() {
            @Override
            public void run() {
                long start = System.currentTimeMillis();
                InputStream raw = getResources().openRawResource(R.raw.merkator);
                BufferedReader rawreader = new BufferedReader(new InputStreamReader(raw));
                String line;
                boolean sukces = true;
                double[] merkator = new double[90002];
                int i = 0;
                try {
                    while ((line = rawreader.readLine()) != null) {
                        String[] lines = line.split(",");
                        merkator[i] = Double.valueOf(lines[1]);
                        i++;
                    }
                } catch (IOException e) {
                    sukces = false;
                }
                long koniec = System.currentTimeMillis();
                long czas = koniec - start;
                if(sukces) {
                    ustawWynik("Powodzenie: " + czas);
                } else {
                    ustawWynik("Blad: " + czas);
                }
            }
        }).start();
    }
}
