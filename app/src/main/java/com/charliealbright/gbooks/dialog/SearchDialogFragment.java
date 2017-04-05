package com.charliealbright.gbooks.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Button;

import com.charliealbright.gbooks.R;

/**
 * Created by Charlie on 4/4/17.
 */

public class SearchDialogFragment extends BottomSheetDialogFragment {

    private TextInputLayout mSearchQueryLayout;
    private Button mSearchButton;

    private SearchDialogFragmentListener mSearchDialogFragmentListener;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.search_dialog_fragment, null);
        mSearchQueryLayout = (TextInputLayout)contentView.findViewById(R.id.search_query);
        mSearchButton = (Button)contentView.findViewById(R.id.search_button);
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String queryText = mSearchQueryLayout.getEditText().getText().toString();
                if (!queryText.isEmpty()) {
                    mSearchDialogFragmentListener.onSearchInitiated(true, queryText);
                } else {
                    mSearchDialogFragmentListener.onSearchInitiated(false, "");
                }
                dismiss();
            }
        });
        dialog.setContentView(contentView);
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the EditNameDialogListener so we can send events to the host
            mSearchDialogFragmentListener = (SearchDialogFragmentListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement EditNameDialogListener");
        }
    }

    public interface SearchDialogFragmentListener {
        void onSearchInitiated(boolean valid, String query);
    }
}
