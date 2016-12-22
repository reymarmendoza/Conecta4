package corp.kingsea.reymar.conecta4;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class AlertDialogFragment extends DialogFragment {
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final MainActivity main = (MainActivity) getActivity();

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				getActivity());
		alertDialogBuilder.setTitle(R.string.gameOverTitle);
		alertDialogBuilder.setMessage(R.string.gameOverMessage);
		alertDialogBuilder.setPositiveButton("Yes",	new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						//reinicio el juego accediendo a la instancia de game que se crea en el main
						main.game.construirTableroLogico();
						main.dibujarTablero();
						//destruyo el cuadro de dialogo
						dialog.dismiss();
					}
				});
		alertDialogBuilder.setNegativeButton("No",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						//main.finish();
						dialog.dismiss();
					}
				});
		return alertDialogBuilder.create();
	}
}