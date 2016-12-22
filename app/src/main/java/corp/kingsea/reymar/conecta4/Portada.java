package corp.kingsea.reymar.conecta4;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * esta es la actividad de bienvenida para el usuario
 */

public class Portada extends Activity implements View.OnClickListener {
    //importado por butterknife
    @BindView(R.id.portadaImage)
    ImageView portadaImage;
    @BindView(R.id.portadaLayout)
    RelativeLayout portadaLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.portada);
        ButterKnife.bind(this);
        //en este caso el recurso es solo como se va comportar el objeto y no constituye ninguna vista OJO!!!
        portadaImage.startAnimation(AnimationUtils.loadAnimation(this, R.anim.initial));
        portadaImage.setOnClickListener(this);
    }
    //al pulsar sobre el texto se arranca la antividad principal
    @Override
    public void onClick(View v) {
        startActivity(new Intent(this, MainActivity.class));
    }

}
