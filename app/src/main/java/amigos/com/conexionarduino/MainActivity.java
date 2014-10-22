package amigos.com.conexionarduino;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.buttonDropset).setOnClickListener(this);
        findViewById(R.id.buttonPosNeg).setOnClickListener(this);
        findViewById(R.id.buttonNegative).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        Intent intent = null;

        switch (v.getId()) {
            case R.id.buttonDropset:
                intent = new Intent(this, DropsetActivity.class);
                break;
            case R.id.buttonPosNeg:
                intent = new Intent(this, PosNegActivity.class);
                break;
            case R.id.buttonNegative:
                intent = new Intent(this, NegativeActivity.class);
                break;
        }
        startActivity(intent);

    }
}
