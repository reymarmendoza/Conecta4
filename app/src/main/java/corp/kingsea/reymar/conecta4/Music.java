package corp.kingsea.reymar.conecta4;

import android.content.Context;
import android.media.MediaPlayer;

public class Music {
	private static MediaPlayer player;
	//reproducir musica(id representa un recurso de audio o video)
	public static void play(Context context, int id) {
		//se pasan los parametros a la libreria mediaplayer que se encarga de audio y video
		player = MediaPlayer.create(context, id);
		//la reproduccion del fuchero se repite indefinidamente
		player.setLooping(true);
		//arranca o reanuda la reproduccion
		player.start();
	}
	//detener la musica
	public static void stop(Context context) {
		if (player != null) {
			//detiene la reproduccion
			player.stop();
			//liberar los recursos usados por el reproductor mediaplayer
			player.release();
			player = null;
		}
	}
}