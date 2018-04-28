package com.example.agast.extrememastermind_v1;

import android.app.DialogFragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


/*Menu d'accueil simple*/
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void NewGame(View view) {
        /*Boite de dialogue permettant l'entrée du nom du joueur, elle se lancera à chaque nouvelle partie*/
        DialogFragment newFragment1 = new CreateDialogName();
        newFragment1.show(getFragmentManager(), "wyn");
    }

    public void Save(View v){
        Intent intent = new Intent(MainActivity.this, Save_tab.class);
        startActivity(intent);
        
    }

    public void quit(View view) {
        finish();
    }

    public void highscores(View view) {
        Intent intent = new Intent(MainActivity.this, score_tab.class);
        startActivity(intent);
    }
}
