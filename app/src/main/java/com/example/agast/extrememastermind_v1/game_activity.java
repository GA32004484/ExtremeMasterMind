package com.example.agast.extrememastermind_v1;

import android.content.SharedPreferences;
import android.app.DialogFragment;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/*Activité gérant le jeu Mastermind*/
public class game_activity extends AppCompatActivity implements View.OnClickListener {
    int combinaison[] = {Color.GREEN, Color.BLUE, Color.MAGENTA, Color.RED}; //Combinaison proposée de la base à l'utilisateur
    int liste_couleurs_all[] = {Color.RED, Color.BLUE, Color.YELLOW, Color.MAGENTA, Color.GREEN, Color.BLACK}; //Liste des couleurs disponibles
    int combi_soluce[]; //La combinaison solution

    SharedPreferences settings;
    ArrayList<String> partie, data;

    ListView liste;
    CustomListViewAdapter adapter;

    List<int[]> liste_couleurs; //Liste des lignes proposées par le joueur

    int nbrow = 0; //nombre de lignes
    int nbmalplace, nbbienplace; //Nombre de bien placés, mal placé
    int score = 12; //Score de départ
    String nom; //Nom du joueur

    //Image view des cercles contenant la solution
    ImageView ball1;
    ImageView ball2;
    ImageView ball3;
    ImageView ball4;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_activity);

        //on récupère les images de la proposition du joueur (la ligne dont il va pouvoir modifier l'entrée)
        ImageView rep1 = (ImageView) this.findViewById(R.id.rep1);
        ImageView rep2 = (ImageView) this.findViewById(R.id.rep2);
        ImageView rep3 = (ImageView) this.findViewById(R.id.rep3);
        ImageView rep4 = (ImageView) this.findViewById(R.id.rep4);
        //On ajoute les couleurs de base et un listener
        rep1.setColorFilter(Color.GREEN);
        rep2.setColorFilter(Color.BLUE);
        rep3.setColorFilter(Color.MAGENTA);
        rep4.setColorFilter(Color.RED);
        rep1.setOnClickListener(this);
        rep2.setOnClickListener(this);
        rep3.setOnClickListener(this);
        rep4.setOnClickListener(this);

        //On lie les image à leur cercles respectifs pour la solution
        ball1 = (ImageView) this.findViewById(R.id.ball1);
        ball2 = (ImageView) this.findViewById(R.id.ball2);
        ball3 = (ImageView) this.findViewById(R.id.ball3);
        ball4 = (ImageView) this.findViewById(R.id.ball4);



        partie = new ArrayList<>();
        settings = getSharedPreferences("Partie", 0);
        Set<String> set = settings.getStringSet("Partie", null);
        if(set != null){partie.addAll(set);}

        //On importe le nom du joueur de la boite de dialogue précédente
        nom = (String) getIntent().getSerializableExtra("nom_joueur");

        //On crée la combinaison à trouver
        create_combi();
    }

    /*Bouton pour revenir au menu principal*/
    public void quit_menu(View view) {
        //Dans les sharedpreference "data" on va mettre la ligne concernant la partie actuelle du joueur
        data = new ArrayList<>();
        settings = getSharedPreferences("data", 0);
        Set<String> set = settings.getStringSet("data", null);
        if(set != null){data.addAll(set);}

        //Création de la date
        //N'étant pas demandé dans le CCTP, j'ai préféré l'indiquer pour que l'utilisateur se souvienne de quelle sauvegarde il veut importer
        SimpleDateFormat format = new SimpleDateFormat("dd/MM HH:mm");
        Date date = new Date();
        String result = format.format(date);
        data.add(""+ score +",  " +  nom + ",  " + result + "  (SAVE)");

        //On modifie les shared preference correspondant
        SharedPreferences.Editor editor = settings.edit();
        Set<String> set2 = new HashSet<String>();
        set2.addAll(data);
        editor.putStringSet("data" , set2);
        editor.apply();

        //on demande ensuite s'il veut sauvegarder sa partie ou non
        DialogFragment newFragment = new CreateDialogSave();
        newFragment.show(getFragmentManager(), "save");
    }

    /*Onclick pour changer les couleurs de la combinaison à rentrer*/
    public void onClick(View v) {
        int bouton = 0; //correspondra à l'id du bouton manipulé par le joueur
        ImageView circle = (ImageView) this.findViewById(v.getId());

        if (v.getId() == R.id.rep1) {
            bouton = 0;
        } else if (v.getId() == R.id.rep2) {
            bouton = 1;
        } else if (v.getId() == R.id.rep3) {
            bouton = 2;
        } else if (v.getId() == R.id.rep4) {
            bouton = 3;
        }

        //on fait tourner les couleurs à chaque click de l'utilisateur
        for (int i = 0; i < 6; i++) {
            if (combinaison[bouton] == liste_couleurs_all[i]) {
                if (i == 5) {
                    circle.setColorFilter(liste_couleurs_all[0]);
                    combinaison[bouton] = liste_couleurs_all[0];
                } else {
                    circle.setColorFilter(liste_couleurs_all[i + 1]);
                    combinaison[bouton] = liste_couleurs_all[i + 1];
                    break;
                }
            }
        }
    }

    /*Ajout d'une ligne à la listView*/
    public void new_row(View view) {
        //Si le joueur vient de commencer à jouer, on initialise la listeview
        if (nbrow == 0) {
            liste_couleurs = new ArrayList<int[]>();
            liste = (ListView) findViewById(R.id.all_combi);
            adapter = new CustomListViewAdapter(this, R.layout.row, liste_couleurs);
            liste.setAdapter(adapter);
        }

        //Test contient toutes les couleurs choisies par le joueur + les test effectués sur cette proposition
        int[] test = new int[8];
        test[0] = combinaison[0];
        test[1] = combinaison[1];
        test[2] = combinaison[2];
        test[3] = combinaison[3];
        test[4] = 0;
        test[5] = 0;
        test[6] = 0;
        test[7] = 0;

        nbrow++;//On augmente le nombre de lignes

        check(test, 3); //on test les valeurs de la proposition pour déterminer les biens placés, mal placés
        liste_couleurs.add(test); //on ajoute toute cette ligne dans la liste des couleurs pour l'afficher sur la listeview
        adapter.notifyDataSetChanged();//On prévient que la liste est modifiée
        score(); //on met à jour le score
    }

    /*Création de la combinaison*/
    public void create_combi() {
        combi_soluce = new int[4];

        //On choisit aléatoirement parmi toutes les couleurs disponibles
        for (int i = 0; i < 4; i++) {
            int aleatoire = (int) (Math.random() * 6);
            combi_soluce[i] = liste_couleurs_all[aleatoire];
        }

        //on modifie la visibilité et la couleur du cercle
        ball1.setColorFilter(combi_soluce[0]);
        ball1.setVisibility(View.INVISIBLE);
        ball2.setColorFilter(combi_soluce[1]);
        ball2.setVisibility(View.INVISIBLE);
        ball3.setColorFilter(combi_soluce[2]);
        ball3.setVisibility(View.INVISIBLE);
        ball4.setColorFilter(combi_soluce[3]);
        ball4.setVisibility(View.INVISIBLE);

    }

    /*Boite de dialogue pour apprendre à jouer*/
    public void ltp(View view) {
        DialogFragment newFragment = new Createdialogltp();
        newFragment.show(getFragmentManager(), "help_me");
    }

    /*Fonction pour vérifier combien placé et si victoire*/
    public void check(int[] tab, int x) {
        boolean win = false;
        int indexx = x;
        nbbienplace = 0;
        nbmalplace = 0;
        int[] verifa = new int[4]; //tableau servant à marquer les couleurs déjà testées ou non pour la combinaison soluce
        int[] verifb = new int[4]; //pour la combinaison proposée

        //on test si on trouve les bonnes couleurs aux bons endroits
        for (int i = 0; i < 4; i++) {
            if (combi_soluce[i] == combinaison[i]) {
                nbbienplace++;
                indexx += 1;
                tab[indexx] = Color.CYAN;
                //on coche pour montrer que ces cercles sont déjà comparés et valides
                verifa[i] = 1;
                verifb[i] = 1;}
        }


        //on compare les cercles restants entre eux pour vérifier ceux qui sont mal placés
        for (int i = 0; i <= 3; i++) {
            for (int j = 0; j <= 3; j++) {
                //S'ils sont de la même couleurs et non marqués ils sont mal placés
                if ((combi_soluce[i] == combinaison[j]) && verifa[i] != 1 && verifb[j] != 1) {
                    nbmalplace++;
                    indexx += 1;
                    tab[indexx] = Color.rgb(223, 152, 20);
                    verifa[i] = 1;
                    verifb[j] = 1;}
            }
        }

        //Si tout est bien placé, le joueur a gagné
        if (nbbienplace == 4) {
            win();
            win = true;
        }

        //si le joueur dépasse 10 lignes, il perds
        if (nbrow >= 10 && !win) {
            lose();
        }
    }

    /*Incrémentation du score à chaque ajout d'une ligne*/
    public void score() {
        score = 12 - (nbrow - 1);
        TextView hs2 = (TextView) findViewById(R.id.hs2);
        hs2.setText("HighScore : " + score);
    }

    /*Affichage de la combinaison soluce et une popup de félicitation*/
    public void win() {
        //on montre la combinaison gagnante
        ball1.setVisibility(View.VISIBLE);
        ball2.setVisibility(View.VISIBLE);
        ball3.setVisibility(View.VISIBLE);
        ball4.setVisibility(View.VISIBLE);

        //on affiche la pop-up
        DialogFragment newFragment = new CreateDialogWin();
        newFragment.show(getFragmentManager(), "you_win");

        //on actualise le score
        actu_score();
    }

    /*Affichage de la combinaison soluce et une popup de dommage*/
    public void lose() {
        ball1.setVisibility(View.VISIBLE);
        ball2.setVisibility(View.VISIBLE);
        ball3.setVisibility(View.VISIBLE);
        ball4.setVisibility(View.VISIBLE);

        DialogFragment newFragment1 = new CreateDialogLose();
        newFragment1.show(getFragmentManager(), "you_lose");
        actu_score();
    }

    /*On contruit une ligne à possiblement ajouter aux tableaux des highscores*/
    public void actu_score(){
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/YYYY HH:mm");
        Date date = new Date();
        String result = format.format(date);
        partie.add(""+ score +",  " +  nom + ",  " + result);
        Collections.sort(partie, new Comparator<String>() {
            @Override
            public int compare(String s, String t1) {
                    return s.compareTo(t1);
                }
        });
        //Stockés dans les shared préférences
        packagesharedPreferences();
    }

    /*Met à jour les shared préférences pour la partie*/
    private void packagesharedPreferences() {
        settings = getSharedPreferences("Partie", 0);
        SharedPreferences.Editor editor = settings.edit();
        Set<String> set = new HashSet<String>();
        set.addAll(partie);
        editor.putStringSet("Partie" , set);
        editor.apply();
    }


}
