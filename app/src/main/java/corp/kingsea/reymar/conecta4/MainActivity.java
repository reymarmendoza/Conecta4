package corp.kingsea.reymar.conecta4;
//https://courses.edx.org/courses/course-v1:UAMx+Android301x+3T2016/courseware/ec6827b73bd0405383c92a165e30a61e/8849605d120d40ed9662208bfd2eadd3/
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import butterknife.ButterKnife;

import static android.R.attr.start;
import static corp.kingsea.reymar.conecta4.R.xml.settings;

//onclicklistener se aplica al layout y oermite capturar las vistas que reciben clic
public class MainActivity extends Activity implements View.OnClickListener {//se cambio AppCompatActivity
    //este es el tablero con todos los identificadores del layout
    private final int ids[][] = {
            {R.id.button61,R.id.button62,R.id.button63,R.id.button64,R.id.button65,R.id.button66,R.id.button67},
            {R.id.button51,R.id.button52,R.id.button53,R.id.button54,R.id.button55,R.id.button56,R.id.button57},
            {R.id.button41,R.id.button42,R.id.button43,R.id.button44,R.id.button45,R.id.button46,R.id.button47},
            {R.id.button31,R.id.button32,R.id.button33,R.id.button34,R.id.button35,R.id.button36,R.id.button37},
            {R.id.button21,R.id.button22,R.id.button23,R.id.button24,R.id.button25,R.id.button26,R.id.button27},
            {R.id.button11,R.id.button12,R.id.button13,R.id.button14,R.id.button15,R.id.button16,R.id.button17}
    };
    Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        game = new Game();//traigo el tablero con las posiciones desde game
        habilitarBotones();//recorro todos los identificadores del tablero y los hago clickables para implementar el clicklistener
        //se define el valor de la preferencia por defecto, false indica que solo se hace la primera vez que se carga la app
        PreferenceManager.setDefaultValues(this, R.xml.settings, false);
    }

    @Override
    protected void onResume() {
        super.onResume();

        boolean play = false;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        if(sharedPreferences.contains(Preferences.PLAY_MUSIC_KEY)){
            play = sharedPreferences.getBoolean(
                    Preferences.PLAY_MUSIC_KEY,
                    Preferences.PLAY_MUSIC_DEFAULT);
        }
        //si la preferencia music es TRUE sera el unica escenario donde se ejecutara la musica
        if(play){
            //reproduce la musica con la libreria que se esta usando en esta clase
            Music.play(this,R.raw.funkandblues);
        }

    }
    /*
    //este metodo al igual que onPause son llamados cuando la actividad pasa a segundo plano con la exepcion de que ese no lo hace cuando se destruye
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("GRID",);
        super.onSaveInstanceState(outState);
    }
    */
    @Override
    protected void onPause() {
        super.onPause();
        Music.stop(this);
    }

    //se añade a la UI la opcion de los puntos en el menu, infla el recurso menu desde RES
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }
    //cuando uno de los items es pulsado se ejecuta este metodo

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //el menu tiene dos opciones de acuerdo a cual sea pulsado ejecuta
        switch ((item.getItemId())){
            case R.id.menuAbout:
                //en el manifest se declaro un tema para que no ocupase toda la pantallay se mostrara como una ventana de dialogo
                startActivity(new Intent(this, About.class));
                return true;
            case R.id.sendMessage:
                //se crea la intencion de correo
                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT,"Conecta 4");
                intent.putExtra(Intent.EXTRA_TEXT,"Tu juego es");
                startActivity(intent);
                return true;
            case R.id.settings:
                startActivity(new Intent(this, Preferences.class));
                return true;
        }
        return super.onOptionsItemSelected(item);

    }
    private void habilitarBotones() {
        for (int i = 0; i < Game.NUMERO_FILAS; i++) {
            for (int j = 0; j < Game.NUMERO_COLUMNAS; j++) {
                ImageButton button = (ImageButton) findViewById(ids[i][j]);
                button.setOnClickListener(this);
            }
        }
    }

    public void onClick(View view) {
        int i = (game.buscarFila(view.getId(), ids));
        int j = (game.buscarColumna(view.getId(), ids));
        //se debe validar si el punto seleccionado del tablero esta disponible para pulsar
        if(game.movimientoPermitido(i, j)){
            game.posicionarFicha(i, j);
            mensajeUsuario(game.validarFinJuego(i, j));//hay un bug--------
            game.juegaMaquina();
        }else{
            Toast.makeText(this, "Movimiento no permitido", Toast.LENGTH_SHORT).show();
        }
        dibujarTablero();

    }

    private void mensajeUsuario(int ganador) {

        if(ganador == Game.EMPATE){
            new AlertDialogFragment().show(getFragmentManager(), "ALERT DIALOG");
            Toast.makeText(this,"No es posible hacer mas movimientos", Toast.LENGTH_SHORT).show();
        }else if(ganador == Game.JUGADOR){
            Toast.makeText(this,"Has ganado", Toast.LENGTH_LONG).show();
            new AlertDialogFragment().show(getFragmentManager(), "ALERT DIALOG");
        }else if(ganador == Game.MAQUINA){
            Toast.makeText(this,"Has perdido", Toast.LENGTH_LONG).show();
            new AlertDialogFragment().show(getFragmentManager(), "ALERT DIALOG");
        }

    }

    public void dibujarTablero() {
        int id;
        int tablero[][];

        for (int i = 0; i < Game.NUMERO_FILAS; i++) {
            for (int j = 0; j < Game.NUMERO_COLUMNAS; j++) {
                tablero = game.tablero();//traigo la forma logica del tablero para saber las posiciones de los jugadores
                //se asigna el style segun quien pulso el boton por medio de los drawables
                if (tablero[i][j] == Game.JUGADOR) {
                    id = R.drawable.human_pressed_button;
                } else if (tablero[i][j] == Game.MAQUINA) {
                    id = R.drawable.machine_pressed_button;
                } else {
                    id = R.drawable.image_button_game;
                }

                ImageButton imageButton = (ImageButton) findViewById(ids[i][j]);
                imageButton.setImageResource(id);
            }
        }

    }

    public String getPlayerName(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String name = sharedPreferences.getString(Preferences.PLAYER_KEY, Preferences.PLAYER_DEFAULT);
        return name;
    }

    public Boolean music(){
        Boolean play = false;

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (sharedPreferences.contains(Preferences.PLAY_MUSIC_KEY))
            play = sharedPreferences.getBoolean(Preferences.PLAY_MUSIC_KEY, Preferences.PLAY_MUSIC_DEFAULT);

        return play;
    }

    public void setMusic (Boolean value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(Preferences.PLAY_MUSIC_KEY, value);
        editor.apply();
    }
}
