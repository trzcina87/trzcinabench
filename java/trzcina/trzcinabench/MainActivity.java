package trzcina.trzcinabench;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private TextView wynik;
    private ProgressBar progress;

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
        findViewById(R.id.grafika).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                grafika();
            }
        });
        findViewById(R.id.kompaktowanie).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kompaktowanie();
            }
        });
        Date buildDate = new Date(BuildConfig.TIMESTAMP);
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        String data = dateFormat.format(buildDate);
        setTitle("TrzcinaBench " + data);
        progress = (ProgressBar)findViewById(R.id.progressbar);
        progress.getIndeterminateDrawable().setColorFilter(Color.RED, android.graphics.PorterDuff.Mode.MULTIPLY);
    }

    private void ustawWynik(final String string) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                wynik.setText(string);
            }
        });
    }

    private void pokrazBar() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progress.setVisibility(View.VISIBLE);
            }
        });
    }

    private void ukryjBar() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progress.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void wczytajBitmapy() {
        pokrazBar();
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
                ukryjBar();
            }
        }).start();
    }

    private void alokacjaPamieci() {
        pokrazBar();
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
                        tablica[i] = new long[131072];
                        i = i + 1;
                        Thread.sleep(5);
                    } catch (OutOfMemoryError e) {
                        dzialam = false;
                    } catch (InterruptedException e) {

                    }
                }
                ustawWynik("Long: " + bajtylong + " bajty\nPamiec: " + pamiec + "\nPamiec Max: " + pamiecmax + "\nZaalokowano: " + i + " MB");
                ukryjBar();
            }
        }).start();
    }

    private void zapisDanych() {
        pokrazBar();
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
                ukryjBar();
            }
        }).start();
    }

    private void zapisDanychReczny() {
        pokrazBar();
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
                ukryjBar();
            }
        }).start();
    }

    private void odczytDanych() {
        pokrazBar();
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
                ukryjBar();
            }
        }).start();
    }

    private void odczytDanychTab() {
        pokrazBar();
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
                ukryjBar();
            }
        }).start();
    }

    private void podzialPliku() {
        pokrazBar();
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
                ukryjBar();
            }
        }).start();
    }

    private void czekaj(int milisekundy) {
        try {
            Thread.sleep(milisekundy);
        } catch (InterruptedException e) {
        }
    }

    private void rysuj(int x, int y, MainSurface surface, Bitmap testowy) {
        Canvas canvass = surface.surfaceholder.lockCanvas();
        canvass.drawColor(Color.WHITE);
        canvass.drawBitmap(testowy, x, y, null);
        surface.surfaceholder.unlockCanvasAndPost(canvass);
    }

    private void grafika() {
        pokrazBar();
        final MainSurface surface = new MainSurface(this);
        final RelativeLayout layout = (RelativeLayout)(findViewById(R.id.activity));
        layout.addView(surface);
        wynik.setText("");
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(surface.gotowe == false) {
                    czekaj(1);
                }
                final Point rozdzielczosc = new Point();
                rozdzielczosc.x = surface.getWidth() / 2;
                rozdzielczosc.y = surface.getHeight() / 2;
                Bitmap testowyobraz = BitmapFactory.decodeResource(getResources(), R.mipmap.test);
                Bitmap testowy = Bitmap.createBitmap(rozdzielczosc.x, rozdzielczosc.y , Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(testowy);
                canvas.drawBitmap(testowyobraz, new Rect(0, 0, testowyobraz.getWidth(), testowyobraz.getHeight()), new Rect(0, 0, rozdzielczosc.x, rozdzielczosc.y), null);
                long start = System.currentTimeMillis();
                int ilosc = 0;
                if (surface.surfaceholder.getSurface().isValid()) {
                    for(int x = 0; x <= rozdzielczosc.x; x++) {
                        rysuj(x, 0, surface, testowy);
                        ilosc++;
                    }
                    for(int y = 0; y <= rozdzielczosc.y; y++) {
                        rysuj(rozdzielczosc.x, y, surface, testowy);
                        ilosc++;
                    }
                    for(int x = rozdzielczosc.x; x >= 0; x--) {
                        rysuj(x, rozdzielczosc.y, surface, testowy);
                        ilosc++;
                    }
                    for(int y = rozdzielczosc.y; y >= 0; y--) {
                        rysuj(0, y, surface, testowy);
                        ilosc++;
                    }
                }
                long koniec = System.currentTimeMillis();
                long czas = koniec - start;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        layout.removeView(surface);
                    }
                });
                float fps = (float)ilosc / (float)czas * 1000F;
                ustawWynik("Czas: " + czas + " FPS: " + String.format("%.2f", fps));
                ukryjBar();
            }
        }).start();
    }

    private void kompaktowanie() {
        pokrazBar();
        wynik.setText("");
        new Thread(new Runnable() {
            @Override
            public void run() {
                long tablica[][] = new long[1000][];
                boolean dzialam = true;
                int i = 0;
                while(dzialam) {
                    try {
                        tablica[i] = new long[131072];
                        i = i + 1;
                        Thread.sleep(5);
                    } catch (OutOfMemoryError e) {
                        dzialam = false;
                    } catch (InterruptedException e) {

                    }
                }
                int zwolniono = 0;
                for(int j = 0; j < i; j++) {
                    if(j % 2 == 0) {
                        tablica[j] = null;
                        zwolniono = zwolniono + 1;
                    }
                }
                Bitmap tmp;
                int size = 0;
                boolean alokacja = false;
                try {
                    tmp = BitmapFactory.decodeResource(getResources(), R.mipmap.b1);
                    size = tmp.getWidth();
                    tmp = null;
                    alokacja = true;
                } catch (OutOfMemoryError e) {
                }
                ustawWynik("Zwolniono: " + zwolniono + "MB, Alokacja Bitmapy: " + alokacja + " Rozmiar: " + size);
                ukryjBar();
            }
        }).start();
    }
}
