package zhukic.sample;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import zhukic.sectionedrecyclerview.R;

public class NewMovieDialogFragment extends DialogFragment {

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
        builder.setPositiveButton(android.R.string.ok, null);
        builder.setNegativeButton(android.R.string.cancel, ((dialog, which) -> dialog.dismiss()));
        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        ((AlertDialog) getDialog()).getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(v -> onPositiveButtonClicked());
    }

    @SuppressWarnings("ConstantConditions")
    private void onPositiveButtonClicked() {
        final TextInputLayout tilName = (TextInputLayout) getDialog().findViewById(R.id.tilName);
        final TextInputLayout tilGenre = (TextInputLayout) getDialog().findViewById(R.id.tilGenre);
        final TextInputLayout tilYear = (TextInputLayout) getDialog().findViewById(R.id.tilYear);

        if (TextUtils.isEmpty(tilName.getEditText().getText().toString())
                || TextUtils.isEmpty(tilGenre.getEditText().getText().toString())
                || TextUtils.isEmpty(tilYear.getEditText().getText().toString())) {

            tilName.setError(null);
            tilGenre.setError(null);
            tilYear.setError(null);

            if (TextUtils.isEmpty(tilName.getEditText().getText().toString())) {
                tilName.setError("Name is required");
            }
            if (TextUtils.isEmpty(tilGenre.getEditText().getText().toString())) {
                tilGenre.setError("Genre is required");
            }
            if (TextUtils.isEmpty(tilYear.getEditText().getText().toString())) {
                tilYear.setError("Year is required");
            }

        } else {
            dialogListener.onMovieCreated(new Movie(tilName.getEditText().getText().toString(),
                    Integer.parseInt(tilYear.getEditText().getText().toString()),
                    tilGenre.getEditText().getText().toString()));
            getDialog().dismiss();
        }
    }
}
