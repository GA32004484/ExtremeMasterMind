package com.example.agast.extrememastermind_v1;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;

/*Classe liée à la boite de dialogue personnalisée pour l'affichage du message PERDU*/
public class CreateDialogLose extends DialogFragment {

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_lose, null))
                .setPositiveButton("Sauvegarder Score", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        getActivity().finish();
                        /*Si on choisit de sauvegarder on revient sur la page de jeu pour une nouvelle partie*/
                        Intent intent = new Intent(getActivity(), game_activity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Quitter", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id){
                        /*Si on choisit de quitter, on revient sur la page d'accueil*/
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                    }
                });
        return builder.create();
    }
}
