package com.example.agast.extrememastermind_v1;

import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

/*C'est la classe qui gère les ligne de la listeview contenant les essais précédents du joueur
* ayant des lignes personnalisées (4 ronds suivant de 4 ronds plus petit contenant les indices),
* il faut créer un adaptateur personnalisé*/
public class CustomListViewAdapter extends ArrayAdapter<int[]> {
    Context context;
    List<int[]> row_color; //Contient la liste des couleurs (dans un tableau[4] de int) que l'utilisateur a rentré
    //Constructeur
    public CustomListViewAdapter(Context context, int resourceId, List<int[]> liste_couleurs) {
        super(context, resourceId, liste_couleurs);
        this.context = context;
        row_color = liste_couleurs;
    }

    /*private view holder class*/
    /*Il contient tous les éléments propres à la ligne de la listeView*/
    private class ViewHolder {
        ImageView aff1, aff2, aff3, aff4, ind1, ind2,ind3, ind4;
    }

    /*C'est la fonction qui permet d'ajouter une ligne*/
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        int[] color = row_color.get(position); //on récupère les couleurs d'une ligne

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.row, null); //on précise la ligne
            holder = new ViewHolder();
            //On link les variables avec leurs objets respectifs
            //Tous les aff sont pour les cercles contenant la proposition du joueur
            holder.aff1 = convertView.findViewById(R.id.aff1);
            holder.aff2 = convertView.findViewById(R.id.aff2);
            holder.aff3 = convertView.findViewById(R.id.aff3);
            holder.aff4 = convertView.findViewById(R.id.aff4);

            //Tous les ind contiennent les indices après la validation de la proposition du joueur (mal placé, bien placé)
            holder.ind1 = convertView.findViewById(R.id.ind1);
            holder.ind2 = convertView.findViewById(R.id.ind2);
            holder.ind3 = convertView.findViewById(R.id.ind3);
            holder.ind4 = convertView.findViewById(R.id.ind4);

            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        //On change les couleurs en fonction de la propostion
        holder.aff1.setColorFilter(color[0]);
        holder.aff2.setColorFilter(color[1]);
        holder.aff3.setColorFilter(color[2]);
        holder.aff4.setColorFilter(color[3]);
        //Si la couleur proposée par le joueur ne correspond ni à bien placé, ni à mal placé, alors on la laisse blanche
        if (color[4] != 0){holder.ind1.setColorFilter(color[4]);}
        else{holder.ind1.setColorFilter(Color.WHITE);}
        if (color[5] != 0){holder.ind2.setColorFilter(color[5]);}
        else{holder.ind2.setColorFilter(Color.WHITE);}
        if (color[6] != 0){holder.ind3.setColorFilter(color[6]);}
        else{holder.ind3.setColorFilter(Color.WHITE);}
        if (color[7] != 0){holder.ind4.setColorFilter(color[7]);}
        else{holder.ind4.setColorFilter(Color.WHITE);}

        return convertView;
    }
}