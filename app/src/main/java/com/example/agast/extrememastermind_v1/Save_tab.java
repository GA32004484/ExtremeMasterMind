package com.example.agast.extrememastermind_v1;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/*Activité enregistrant les sauvegardes du joueur*/
public class Save_tab extends AppCompatActivity {
    SharedPreferences settings1, settings2; //Servent à stocker respectivement les préférences liées aux sauvegardes déjà présentes et les données actuelle du joueur à ajouter aux sauvegardes

    ArrayList<String> myStringArray; //Array qui va contenir toutes les sauvegardes pour travailler plus facilement (à partir de la conversion d'un set<String>)
    ArrayAdapter<String> adapter; //Adaptateur de la listeview
    Set<String> set; //servira à contenir toutes les sauvegardes sorties des shared preferences
    boolean save; //Si le joueur a sauvegardé ou non
    int pos; //Position où le joueur a cliqué sur la liste

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_tab);
        //Récupération de l'information
        save = getIntent().getBooleanExtra("save",save);
        addSave(save);

        //On extrait dans une arrayliste les données déja présentes s'il y en a
        myStringArray = new ArrayList<String>();
        settings2 = getSharedPreferences("saves", 0);
        set = settings2.getStringSet("saves", null);
        if(set != null) myStringArray.addAll(set);

        //On crée l'adaptateur de la liste
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, myStringArray);
        ListView list = (ListView) findViewById(R.id.liste_saves);

        //On ajoute un listener sur la liste
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
                pos = position; //position où l'utilisateur a cliqué
                view.setSelected(true);

                //On demande à l'utilisateur s'il est sur de supprimer
                AlertDialog alertDialog = new AlertDialog.Builder(Save_tab.this).create();
                alertDialog.setTitle(R.string.supp);
                alertDialog.setMessage("Supprimer ?");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //on met à jour l'array et la liste view
                                myStringArray.remove(myStringArray.get(pos));
                                adapter.notifyDataSetChanged();

                                //On nettoie le set et on le met à jour
                                set.clear();
                                set.addAll(myStringArray);

                                //On vide l'éditeur et on le met à jour avec les nouvelles données
                                SharedPreferences.Editor editor = settings2.edit();
                                editor.clear();
                                editor.commit();
                                editor.putStringSet("saves" , set);
                                editor.apply();
                            }
                        }
                );
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Annuler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                alertDialog.show();
            }
        });
        list.setAdapter(adapter);
    }

    /*Fonction permettant l'ajout d'une sauvegarde à la sharedpreference "saves"*/
    public void addSave(boolean save){
        //Data contient la ligne de l'utilisateur qui souhaite sauvegarder
        settings1 = getSharedPreferences("data", 0);
        Set<String> set2 = settings1.getStringSet("data", null);

        if(save){
            settings2 = getSharedPreferences("saves", 0);
            ArrayList<String> temp = new ArrayList<String>();
            Set<String> set3 = settings2.getStringSet("saves", null);
            if(set3 != null) {temp.addAll(set3);}
            temp.addAll(set2);

            Set<String> set_maj = new HashSet<String>();
            set_maj.addAll(temp);

            SharedPreferences.Editor editor = settings2.edit();
            editor.putStringSet("saves" , set_maj); //On ajoute la ligne sous forme de set au set déjà présent sur "saves"
            editor.apply();
        }
        else{
            //Si l'utilisateur n'a pas sauvegarder on supprimer les données le concernant sur la partie
            SharedPreferences.Editor editor = settings1.edit();
            editor.clear();
            editor.commit();
        }
    }


}