package zhukic.sample;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import zhukic.sectionedrecyclerview.R;

/**
 * Created by RUS on 04.03.2017.
 */

public class NewMovieDialog extends DialogFragment {

    interface DialogListener {
        void onMovieCreated(Movie movie);
    }

    private DialogListener dialogListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        dialogListener = (DialogListener) getTargetFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(R.layout.fragment_dialog_movie);
        builder.setPositiveButton(android.R.string.ok, (dialog, which) -> onPositiveButtonClicked());
        builder.setNegativeButton(android.R.string.cancel, ((dialog, which) -> dialog.dismiss()));
        return builder.create();
    }

    private void onPositiveButtonClicked() {
        final String name = ((EditText) getDialog().findViewById(R.id.et_name)).getText().toString();
        final String genre = ((EditText) getDialog().findViewById(R.id.et_genre)).getText().toString();
        final Integer year = Integer.parseInt(((EditText) getDialog().findViewById(R.id.et_year)).getText().toString());
        dialogListener.onMovieCreated(new Movie(name, year, genre));
    }
}
