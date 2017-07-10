package trzcina.trzcinabench;

import java.io.Serializable;

public class Dane implements Serializable {

    public static int rozmiar = 1000;

    public int tablica[][];

    public Dane() {

    }

    public void inicjuj() {
        tablica = new int[rozmiar][rozmiar];
    }

    public void wypelnij() {
        tablica = new int[rozmiar][rozmiar];
        for(int i = 0; i < rozmiar; i++) {
            for(int j = 0; j < rozmiar; j++) {
                tablica[i][j] = 1;
            }
        }
    }

    public long suma() {
        long suma = 0;
        for(int i = 0; i < rozmiar; i++) {
            for(int j = 0; j < rozmiar; j++) {
                suma = suma + tablica[i][j];
            }
        }
        return suma;
    }
}
