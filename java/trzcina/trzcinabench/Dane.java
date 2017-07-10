package trzcina.trzcinabench;

import java.io.Serializable;

public class Dane implements Serializable {

    private int tablica[][];

    public Dane() {

    }

    public void wypelnij() {
        tablica = new int[3000][3000];
        for(int i = 0; i < 3000; i++) {
            for(int j = 0; j < 3000; j++) {
                tablica[i][j] = i + j;
            }
        }
    }
}
