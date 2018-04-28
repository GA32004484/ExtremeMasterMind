package com.example.agast.extrememastermind_v1;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/*Boite de dialogue personnalisée permettant de rentrer le nom du joueur*/
public class CreateDialogName extends DialogFragment {

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_wyn, null))
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(getActivity(), game_activity.class);
                        EditText alertdialogname = (EditText) getDialog().findViewById(R.id.profile);
                        String nom = alertdialogname.getText().toString(); //on récupérer le String entré par le joueur
                        intent.putExtra("nom_joueur", nom);//on fait passer cette information à l'activité du jeu
                        startActivity(intent);
                    }
                });
        return builder.create();
    }
}
