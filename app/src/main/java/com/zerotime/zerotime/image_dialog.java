package com.zerotime.zerotime;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class image_dialog extends AppCompatDialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater= Objects.requireNonNull(getActivity()).getLayoutInflater();
        View view=inflater.inflate(R.layout.dialog_chat_image,null);
        builder.setView(view);
        return builder.create();

    }
}
