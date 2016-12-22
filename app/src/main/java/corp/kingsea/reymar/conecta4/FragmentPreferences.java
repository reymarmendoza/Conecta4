package corp.kingsea.reymar.conecta4;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class FragmentPreferences extends PreferenceFragment {

    public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
    }
}