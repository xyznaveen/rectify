package np.com.naveenniraula.rectify;

import android.content.Context;
import android.support.design.widget.TextInputLayout;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class Rectify {

    private List<Validator> validatorList;
    private int moveFocusTo = 0;
    private boolean isStisfied = false;

    public Rectify() {
        validatorList = new ArrayList<>();
    }

    /**
     * Add new @{Validator} to list. It will be validated later.
     *
     * @param validator
     */
    public void add(Validator validator) {

        validatorList.add(validator);
    }

    /**
     * Adds validation for checking whether fields are empty
     *
     * @param textInputLayout the @{View} to be validated
     */
    public void basic(TextInputLayout textInputLayout) {

        validatorList.add(new ValidatorBuilder(textInputLayout.getContext())
                .setErrorMessage(R.string.rec_error_required)
                .setRule(Validator.Rule.NOTEMPTY)
                .setTextInputLayout(textInputLayout)
                .build());
    }

    public void email(TextInputLayout textInputLayout) {

        validatorList.add(new ValidatorBuilder(textInputLayout.getContext())
                .setErrorMessage(R.string.rec_email_required)
                .setRule(Validator.Rule.EMAIL)
                .setTextInputLayout(textInputLayout)
                .build());
    }

    public void password(TextInputLayout textInputLayout) {
        validatorList.add(new ValidatorBuilder(textInputLayout.getContext())
                .setErrorMessage(R.string.rec_password_invalid)
                .setRule(Validator.Rule.PASSWORD)
                .setTextInputLayout(textInputLayout)
                .build());
    }

    /**
     * Iterate through each element which requires validation.
     */
    public void validate() {

        int index = 0;
        for (Validator validator :
                validatorList) {

            validator.validate();
            if (!validator.isValid() && !isStisfied) {
                moveFocusTo = moveFocusTo;
                isStisfied = true;
            } else {
                moveFocusTo = index + 1;
            }
            index++;
        }

        try {

            validatorList.get(moveFocusTo).focusThis();
        } catch (IndexOutOfBoundsException ignore) {
            // if everything is valid moveFocusTo will be same as the size of validatorList
            // hence leading to this crash

            // move the focus to the first input
            validatorList.get(0).focusThis();
        }
    }

    /**
     * Builder class to help build the @{Validator} Instance.
     */
    public static class ValidatorBuilder {

        private final String INVALID_CONTEXT = "The provided context is null.";

        private WeakReference<Context> contextWeakReference;
        private Validator.Rule rule;
        private TextInputLayout textInputLayout;
        private String errorMessage;

        public ValidatorBuilder(Context context) {
            contextWeakReference = new WeakReference<>(context);
        }

        public ValidatorBuilder setRule(Validator.Rule rule) {
            this.rule = rule;
            return this;
        }

        public ValidatorBuilder setTextInputLayout(TextInputLayout textInputLayout) {
            this.textInputLayout = textInputLayout;
            return this;
        }

        public ValidatorBuilder setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
            return this;
        }

        public ValidatorBuilder setErrorMessage(int errorMessageResId) {
            if (contextWeakReference.get() != null) {
                errorMessage = contextWeakReference.get().getString(errorMessageResId);
            }
            return this;
        }

        public Validator build() {

            return new Validator(textInputLayout, errorMessage, rule);
        }
    }

}
