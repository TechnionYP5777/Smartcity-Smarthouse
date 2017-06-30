package il.ac.technion.cs.smarthouse.applications.calculator;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * @author Inbal Zukerman
 * @since 20-07-17
 * 
 *        This class contains the logic of the calculator application.
 */
public class CalculatorController implements Initializable {

    private BigDecimal left;
    private String selectedOperator;
    private boolean numberInputting;

    @FXML private TextField display;

    public CalculatorController() {
        this.left = BigDecimal.ZERO;
        this.selectedOperator = "";
        this.numberInputting = false;
    }

    @FXML
    /**
     * Responds to all possible click events of the Calculator's buttons.
     * 
     * @param evt
     *            The button-clicked event.
     */
    protected void handleOnAnyButtonClicked(ActionEvent evt) {
        Button button = (Button) evt.getSource();
        final String buttonText = button.getText();
        if ("C".equals(buttonText)) {
            selectedOperator = "";
            numberInputting = false;
            display.setText("0");
        } else if (buttonText.matches("[0-9\\.]")) {
            if (!numberInputting) {
                numberInputting = true;
                display.clear();
            }
            display.appendText(buttonText);
        } else if (buttonText.matches("[＋－×÷]")) {
            left = new BigDecimal(display.getText());
            selectedOperator = buttonText;
            numberInputting = false;
        } else if ("=".equals(buttonText)) {
            final BigDecimal right = !numberInputting ? left : new BigDecimal(display.getText());
            left = calculate(selectedOperator, left, right);
            display.setText(left.toString());
            numberInputting = false;
            return;
        }
    }

    static BigDecimal calculate(String operator, BigDecimal left, BigDecimal right) {
        switch (operator) {
            case "＋":
                return left.add(right);
            case "－":
                return left.subtract(right);
            case "×":
                return left.multiply(right);
            case "÷":
                return left.divide(right);
            default:
        }
        return right;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javafx.fxml.Initializable#initialize(java.net.URL,
     * java.util.ResourceBundle)
     */
    @Override
    public void initialize(URL location, ResourceBundle b) {
        // No further GUI elements initialization is required
    }

}
