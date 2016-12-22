package corp.kingsea.reymar.conecta4;

import android.util.Log;

import java.util.Random;

import static android.R.attr.x;
import static android.os.Build.VERSION_CODES.M;

/**
 * Created by reyma on 20/11/2016.
 */

public class Game {

    static final int NUMERO_FILAS = 6;//vertical de la matriz del juego
    static final int NUMERO_COLUMNAS = 7;//horizontal de la matriz del juego
    static final int VACIO = 0;
    static final int JUGADOR = 1;
    static final int MAQUINA = 2;
    static final int EMPATE = 3;

    private int tablero[][];//esta variable almacena todos los movimientos de los jugadores 1 y 2

    public Game(){
        //inicializo en 0 ta¿odas las posiciones del tablero
        construirTableroLogico();

    }

    public void construirTableroLogico() {
        //inicializo el tablero y le doy valores vacios, cuando se crea el tablero AS o construye en filas de abajo hacia arriba
        tablero = new int[NUMERO_FILAS][NUMERO_COLUMNAS];

        for(int i = 0; i < NUMERO_FILAS; i++){
            for(int j = 0; j < NUMERO_COLUMNAS; j++){
                tablero[i][j] = VACIO;
            }
        }
    }

    public int[][] tablero(){
        return tablero;
    }

    public int buscarFila(int id, int ids[][]) {//devuleve la fila del boton seleccionado
        for (int i = 0; i < Game.NUMERO_FILAS; i++)
            for (int j = 0; j < Game.NUMERO_COLUMNAS; j++)
                if (ids[i][j] == id)
                    return i;
        return -1;
    }

    public int buscarColumna(int id, int ids[][]) {//devuleve la columna del boton seleccionado
        for (int i = 0; i < Game.NUMERO_FILAS; i++)
            for (int j = 0; j < Game.NUMERO_COLUMNAS; j++)
                if (ids[i][j] == id)
                    return j;
        return -1;
    }
    //recibo el identificador del view seleccionado y obtengo su fila y columna
    public boolean movimientoPermitido(int i, int j){
        return (validarPosicionMasBaja(i, j) && colocarFicha(i, j));//con la fila y columna valido en la matriz logica que no se encuentre ningun jugador
    }
    //recorre la columna seleccionada por el jugador y valida el primer campo de valor 0, si es igual al pulsado es el mas bajo
    private boolean validarPosicionMasBaja(int fila, int columna) {
        int menorDisponible = -1;//campo vacio de la columna en la posicion mas baja

        for (int i = 0; i < NUMERO_FILAS; i++) {
            if (tablero[i][columna] == VACIO) {//recorro la columna por medio del indice fila hasta encontrar i = 0(vacio)
                menorDisponible = i;//capturo la posicion i mas baja de la columna
                break;//no necesito validar mas cuando ya encontre el valor menor
            }
        }
        return(menorDisponible == fila);
    }
    //si la posicion dada en el tanlero esta vacia puedo colocar la ficha
    private boolean colocarFicha(int i, int j) {
        return (tablero[i][j] == VACIO);
    }

    public void juegaMaquina(){
        int fila = -1, columna;
        Random r = new Random();

        do {
            columna = r.nextInt(NUMERO_COLUMNAS);//el random genera un numero entre 0(incluyente) y el numero de columnas(excluyente)
            for (int i = 0; i < NUMERO_FILAS; i++)
                if (tablero[i][columna] == VACIO) {
                    fila = i;
                    break;
                }
        } while (fila < 0);

        tablero[fila][columna] = MAQUINA;
    }

    public void posicionarFicha(int i, int j){
        tablero[i][j] = JUGADOR;
    }
    //valido si hay ganador en el tablero
    public int validarFinJuego(int fila, int columna) {//-----------------se puede reemplazar si envio mensajes al main

        int horizontal = validacionHorizontal();
        int vertical = validacionVertical();
        int diagonal = validacionMatrizDiagonal();
        int ganador = 0;

        if(horizontal != 0){
            ganador = horizontal;
            return ganador;
        }
        if(vertical != 0){
            ganador = vertical;
            return ganador;
        }
        if(diagonal != 0){
            ganador = diagonal;
            return ganador;
        }
        if(ganador != 0 && validacionTableroLLeno()){
            return EMPATE; //la opcion tres sera tomada como empate cuando no se halla retornado un ganador en la validacion de las lineas anteriores
        }
        return VACIO;

    }
    // valida las columnas 0 y 6 hacia abajo y hacia arriba para cubrir todas las posibilidades en diagonal de la matriz
    private int validacionMatrizDiagonal() {

        int ascensoIzquierda = validacionAscensoIzquierda();
        int ascensoDerecha = validacionAscensoDerecha();
        int descensoIzquierda = validacionDescensoIzquierda();
        int descensoDerecha = validacionDescensoDerecha();

        if(ascensoIzquierda != 0){
            Log.d("CREATION", "ascensoIzquierda");
            return ascensoIzquierda;
        }if(ascensoDerecha != 0){
            Log.d("CREATION", "ascensoDerecha");
            return ascensoDerecha;
        }if(descensoIzquierda != 0){
            Log.d("CREATION", "descensoIzquierda");
            return descensoIzquierda;
        }if(descensoDerecha != 0){
            Log.d("CREATION", "descensoDerecha");
            return descensoDerecha;
        }
        return VACIO;//no hay ganador por tanto se devuelve 0

    }

    private int validacionAscensoIzquierda() {//se toma como referencia la esquina inferior derecha para validar hacia arriba

        int conectado = 0;

        for (int i = 0; i < NUMERO_FILAS; i++) {
            //repetido se asigna una sola vez en la diagonal, es cuando este tiene la primera comparacion, despues ingresa al for X y alli hace la primera
            //comparacion, despues de comparar se debe guardar el valor con el que se comparo repetido en la misma variable repetido para que el mismo
            //se compare con el siguiente item de la diagonal
            int repetido = tablero[i][6];//asigno el primero de la columna para empezar con el punto 0 en la comparacion

            for (int x = 1; x < NUMERO_FILAS - i; x++) {
                if (repetido == tablero[i + x][6 - x] && repetido != 0) {
                    conectado = conectado + 1;
                    if (conectado == 3) {
                        return repetido;
                    }
                } else {
                    conectado = 0;
                }
                repetido = tablero[i + x][6 - x];
            }
        }
        return 0;

    }

    private int validacionDescensoIzquierda() {//se toma como referencia la esquina superior derecha para validar hacia abajo

        int conectado = 0;

        for (int i = 1; i <= NUMERO_FILAS; i++) {

            int repetido = tablero[NUMERO_FILAS - i][6];//asigno el primero de la columna para empezar con el punto 0 en la comparacion

            for (int x = 1; x <= (NUMERO_FILAS - i); x++) {
                if (repetido == tablero[(NUMERO_FILAS - i) - x][6 - x] && repetido != 0) {
                    conectado = conectado + 1;
                    if (conectado == 3) {
                        return repetido;
                    }
                } else {
                    conectado = 0;
                }
                repetido = tablero[(NUMERO_FILAS - i) - x][6 - x];// despues de hacer la validacion cualquiera que sea el resultado debo guardar el resultado para compararlo con el siguiente
            }
        }
        return 0;

    }

    private int validacionDescensoDerecha() {//se toma como referencia la esquina superior izquierda para validar hacia abajo

        int conectado = 0;

        for (int i = 1; i <= NUMERO_FILAS; i++) {

            int repetido = tablero[NUMERO_FILAS - i][0];//asigno el primero de la columna para empezar con el punto 0 en la comparacion

            for (int x = 1; x < (NUMERO_FILAS - i); x++) {

                if (repetido == tablero[(NUMERO_FILAS - i) - x][0 + x] && repetido != 0) {
                    conectado = conectado + 1;
                    if (conectado == 3) {
                        return repetido;
                    }
                } else {
                    conectado = 0;
                }
                repetido = tablero[(NUMERO_FILAS - i) - x][0 + x];// despues de hacer la validacion cualquiera que sea el resultado debo guardar el resultado para compararlo con el siguiente
            }
        }
        return 0;

    }

    private int validacionAscensoDerecha() {//se toma como referencia la esquina inferior izquierda para validar hacia arriba

        int conectado = 0;

        for (int i = 0; i < NUMERO_FILAS; i++) {
            //repetido se asigna una sola vez en la diagonal, es cuando este tiene la primera comparacion, despues ingresa al for X y alli hace la primera
            //comparacion, despues de comparar se debe guardar el valor con el que se comparo repetido en la misma variable repetido para que el mismo
            //se compare con el siguiente item de la diagonal
            int repetido = tablero[i][0];//asigno el primero de la columna para empezar con el punto 0 en la comparacion

            for (int x = 1; x < NUMERO_FILAS - i; x++) {

                if (repetido == tablero[i + x][0 + x] && repetido != 0) {
                    conectado = conectado + 1;
                    if (conectado == 3) {
                        return repetido;
                    }
                } else {
                    conectado = 0;
                }
                repetido = tablero[i + x][0 + x];// despues de hacer la validacion cualquiera que sea el resultado debo guardar el resultado para compararlo con el siguiente
            }
        }
        return 0;

    }

    private int validacionHorizontal() {

        int conectado = 0;

        for (int i = 0; i < Game.NUMERO_FILAS; i++) {
            //la validacion que hago por fila compara con el numero siguiente, por tanto no necesito llegar al ultimo porque crashea
            for (int j = 0; j < Game.NUMERO_COLUMNAS - 1; j++) {

                if (tablero[i][j] == tablero[i][j + 1] && tablero[i][j] != 0) {//coincide el punto de la matriz con el punto anterior
                    conectado = conectado + 1;
                    if (conectado == 3) {
                        return tablero[i][j];
                    }
                } else {
                    conectado = 0;
                }
            }
            conectado = 0;//si no se encontro 4 en linea se reinicia el contador para hacer la evaluacion en la siguiente fila
        }
        return 0;
    }

    private int validacionVertical() {

        int conectado = 0;

        for (int j = 0; j < Game.NUMERO_COLUMNAS; j++) {
            //la validacion que hago por fila compara con el numero siguiente, por tanto no necesito llegar al ultimo porque crashea
            for (int i = 0; i < Game.NUMERO_FILAS - 1; i++) {

                if (tablero[i][j] == tablero[i + 1][j] && tablero[i][j] != 0) {//coincide el punto de la matriz con el punto anterior
                    conectado = conectado + 1;
                    if (conectado == 3) {
                        return tablero[i][j];
                    }
                } else {
                    conectado = 0;
                }
            }
            conectado = 0;//si no se encontro 4 en linea se reinicia el contador para hacer la evaluacion en la siguiente fila
        }
        return 0;
    }
    //este metodo valida los campos y si encuentra uno con valor 0 retorna falso, de lo contrario todos los campos estan ocupados por los jugadores y no hay movimientos posibles
    private boolean validacionTableroLLeno() {

        for (int i = 0; i < Game.NUMERO_FILAS; i++) {
            for (int j = 0; j < Game.NUMERO_COLUMNAS - 1; j++) {
                if(tablero[i][j] == 0){
                    return false;
                }
            }
        }
        return true;

    }

}