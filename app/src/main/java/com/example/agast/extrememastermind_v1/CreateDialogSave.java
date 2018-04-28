package com.example.agast.extrememastermind_v1;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;

/*Boite de dialogue pour la demande de création d'une sauvegarde*/
public class CreateDialogSave extends DialogFragment {
    boolean bool; //sert à indiquer si l'utilisateur a choisi de sauvegarder ou non.

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_save, null))
                .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        getActivity().finish();
                        Intent intent = new Intent(getActivity(), Save_tab.class);
                        bool = true;
                        intent.putExtra("save", bool);
                        //On passe la valeur du bouléen à la classe save pour qu'elle puisse ajouter la ligne de sauvegarde
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        getActivity().finish();
                        Intent intent = new Intent(getActivity(), Save_tab.class);
                        bool = false;
                        //Si l'utilisateur ne sauvegarde pas, on passe le booléen négatif
                        intent.putExtra("save", bool);
                        startActivity(intent);
                    }
                });
        return builder.create();
    }}
