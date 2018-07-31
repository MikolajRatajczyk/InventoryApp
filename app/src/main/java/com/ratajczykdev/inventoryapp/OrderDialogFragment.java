package com.ratajczykdev.inventoryapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/**
 * DialogFragment for making order
 *
 * @author Mikolaj Ratajczyk <mikolaj.ratajczyk@gmail.com>
 */
public class OrderDialogFragment extends DialogFragment {
    /**
     * instance of the interface to deliver action events
     */
    OrderDialogListener listener;

    /**
     * Custom View for OrderDialogFragment
     */
    private View customView;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.order_dialog_title);
        builder.setNegativeButton(R.string.order_dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                listener.onDialogNegativeClick(OrderDialogFragment.this);
            }
        });
        builder.setPositiveButton(R.string.order_dialog_make_order, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                listener.onDialogPositiveClick(OrderDialogFragment.this);
            }
        });

        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        customView = layoutInflater.inflate(R.layout.fragment_order_dialog, null);
        builder.setView(customView);

        return builder.create();
    }

    /**
     * Override the Fragment.onAttach() method to instantiate the OrderDialogListener
     *
     * @param context context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //  Verify that the host activity implements the callback interface
        try {
            listener = (OrderDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OrderDialogListener");
        }
    }

    /**
     * Gets quantity from EditText
     *
     * @return quantity
     */
    public int getQuantity() {
        EditText editTextQuantity = customView.findViewById(R.id.fragment_dialog_order_quantity_edittext);
        String quantityString = editTextQuantity.getText().toString().trim();
        return Integer.parseInt(quantityString);
    }

    /**
     * The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it.
     */
    public interface OrderDialogListener {
        void onDialogPositiveClick(DialogFragment dialog);

        void onDialogNegativeClick(DialogFragment dialog);
    }
}