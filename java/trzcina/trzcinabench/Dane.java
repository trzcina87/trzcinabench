package trzcina.trzcinabench;

import java.io.Serializable;

public class Dane implements Serializable {

    private static int rozmiar = 1000;

    private int tablica[][];

    public Dane() {

    }

    public void wypelnij() {
        tablica = new int[rozmiar][rozmiar];
        for(int i = 0; i < rozmiar; i++) {
            for(int j = 0; j < rozmiar; j++) {
                tablica[i][j] = i + j;
            }
        }
    }
}
