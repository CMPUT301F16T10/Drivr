package ca.ualberta.cs.drivr;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.math.BigDecimal;
import java.text.NumberFormat;

/**
 * A text watcher for properly formatting currency inputs.
 *
 * From: http://stackoverflow.com/a/24621325
 * Author: ToddH, Paulo Rodrigues
 * Accessed: November 9, 2016
 */

public class MoneyTextWatcher implements TextWatcher {

    /**
     * The EditText the is being modified.
     */
    private final EditText editText;

    /**
     * Instantiate a new MoneyTextWatcher
     * @param editText The EditText to watch
     */
    public MoneyTextWatcher(EditText editText) {
        this.editText = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) { }

    /**
     * Reformats editable to properly show the currency value.
     * @param editable Represents the content of the EditText.
     */
    @Override
    public void afterTextChanged(Editable editable) {
        String s = editable.toString();
        editText.removeTextChangedListener(this);

        String cleanString = s.toString().replaceAll("[$,.]", "");
        BigDecimal parsed = new BigDecimal(cleanString).setScale(2, BigDecimal.ROUND_FLOOR).divide(new BigDecimal(100), BigDecimal.ROUND_FLOOR);
        String formatted = NumberFormat.getCurrencyInstance().format(parsed);
        editText.setText(formatted);
        editText.setSelection(formatted.length());

        editText.addTextChangedListener(this);
    }
}
