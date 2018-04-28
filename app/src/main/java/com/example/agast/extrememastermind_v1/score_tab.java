package com.example.agast.extrememastermind_v1;
//Oublie de la majuscule, très contraignant à modifier après avoir tout fait.
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
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;

/*Activité gérant la listeView des scores (miroir de l'activité Save_tab*/
public class score_tab extends AppCompatActivity {
    SharedPreferences settings; //contient les shares préférences liées aux parties des différents joueurs (ligne de scores)
    ArrayList<String> myStringArray; //Array pour manipulations plus facile des données contenues dans set
    ArrayAdapter<String> adapter;
    Set<String> set; //Contiendra les données des parties enregistrées
    int pos;//position du click de l'utilisateur dans la liste

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_tab);
        myStringArray = new ArrayList<String>();

        //on obtient toutes les données des scores des parties enregistrées
        settings = getSharedPreferences("Partie", 0);
        set = settings.getStringSet("Parties", null);
        retriveSharedValue();


        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, myStringArray);
        ListView listView = (ListView) findViewById(R.id.liste_scores);

        //On ajoute un click listener sur la liste
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
                view.setSelected(true);
                pos = position;

                //On demande confirmation à l'utilisateur
                AlertDialog alertDialog = new AlertDialog.Builder(score_tab.this).create();
                alertDialog.setTitle(R.string.supp);
                alertDialog.setMessage("Supprimer ?");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //On met à jour l'array des données
                                myStringArray.remove(myStringArray.get(pos));
                                adapter.notifyDataSetChanged(); //on met à jour l'affichage de la liste
                                //On met à jour le set
                                if (set != null) {
                                    set.clear();
                                    set.addAll(myStringArray);
                                }

                                //On met à jour les sharedpreferences contenant toutes les données des scores des parties
                                SharedPreferences.Editor editor = settings.edit();
                                editor.clear();
                                editor.commit();
                                editor.putStringSet("Parties" , set);
                                editor.apply();

                                dialog.dismiss();
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Annuler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                alertDialog.show();
            }
        });
        listView.setAdapter(adapter);
    }


    /*Fonction permettant la récupération des données des shared preference + de classe les données par odre décroissant selon le score*/
    private void retriveSharedValue() {
        Set<String> set = settings.getStringSet("Partie", null);

        if(set != null){myStringArray.addAll(set);
            Collections.sort(myStringArray,new Comparator<String>() {
                @Override
                public int compare(String s, String t1) {
                    //Création d'un comparateur personnel

                    //Le score s'exprime : (score , nom du joueur, date)
                    String var1[] = s.split(","); //On récupère le score de l'objet 1
                    String var2[] = t1.split(","); //On récupère le score de l'objet 2
                    //On les compare
                    if (Integer.parseInt(var1[0])>=Integer.parseInt(var2[0])){
                        return -1;
                    }
                    return 1;
                }
            });}
    }
}
