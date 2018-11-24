package np.com.naveenniraula.rectify;

import android.support.design.widget.TextInputLayout;
import android.widget.EditText;

public class Validator {

    public enum Rule {
        NOTEMPTY, EMAIL, PASSWORD
    }

    private String errorMessage;
    private TextInputLayout textInputLayout;
    private Rule rule;
    private boolean isInputValid;

    Validator(TextInputLayout textInputLayout, String errorMessage, Rule rule) {
        this.errorMessage = errorMessage;
        this.textInputLayout = textInputLayout;
        this.rule = rule;
        isInputValid = false;
    }

    /**
     * Begins the process of validation.
     * Checks the rule against the input values.
     */
    public void validate() {

        String inputValue = getInputText(textInputLayout);
        ValidationCore validationCore = new ValidationCore(rule, inputValue);

        isInputValid = validationCore.check();
        toggleErrorMessage();
    }

    /**
     * Checks the validity of the field and sets or clears the error message.
     */
    private void toggleErrorMessage() {

        if (textInputLayout == null) {
            return;
        }

        if (isInputValid) {
            textInputLayout.setErrorEnabled(false);
            return;
        }

        textInputLayout.setErrorEnabled(true);
        textInputLayout.setError(errorMessage);
    }

    /**
     * Extract input value from the @{TextInputLayout}
     *
     * @param textInputLayout
     * @return value contained in this TextInputLayout
     */
    private String getInputText(TextInputLayout textInputLayout) {

        EditText editText = textInputLayout.getEditText();

        if (editText != null) {

            return editText.getText().toString();
        }

        return null;
    }

    /**
     * Checks if this @{TextInputLayout} contains valid values.
     * These values are checked against the @{Rule} specified before.
     *
     * @return true if the rule is satisfied.
     */
    public boolean isValid() {
        return isInputValid;
    }

    /**
     * Move the focus to this @{TextInputLayout}
     */
    public void focusThis() {
        textInputLayout.requestFocus();
    }
}
