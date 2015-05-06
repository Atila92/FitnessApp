import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.example.atila.fitnessapp.R;

/**
 * Created by Atila on 06-05-2015.
 */
public class Preferences extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

    }
}
