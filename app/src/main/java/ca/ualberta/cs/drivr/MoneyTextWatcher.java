package ca.ualberta.cs.drivr;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.math.BigDecimal;
import java.text.NumberFormat;

/**
 * A text watcher for properly formatting currency inputs.
 *
 * Modified from:
 * <ul>
 *     <li>URL: http://stackoverflow.com/a/24621325</li>
 *     <li>Author: ToddH, Paulo Rodrigues</li>
 *     <li>Accessed: November 9, 2016</li>
 * </ul>
 *
 * @see TextWatcher
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


    /**
     * Does not make any modifications to the text.
     * @param s The text to watch.
     * @param start The index of the beginning of a change.
     * @param count How many characters after start were changed.
     * @param after The length of the new text that will replace the existing text beginning at
     *              start.
     */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

    /**
     * Does not make nay modifications to the text.
     * @param s The text to watch.
     * @param start The index of the beginning of a change.
     * @param before How many characters after start were changed.
     * @param count How many characters replaced the text after start.
     */
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
        String cleanString = s.replaceAll("[$,.]", "");
        BigDecimal parsed = new BigDecimal(cleanString).setScale(2, BigDecimal.ROUND_FLOOR).divide(new BigDecimal(100), BigDecimal.ROUND_FLOOR);
        String formatted = NumberFormat.getCurrencyInstance().format(parsed);
        editText.setText(formatted);
        editText.setSelection(formatted.length());
        editText.addTextChangedListener(this);
    }
}
