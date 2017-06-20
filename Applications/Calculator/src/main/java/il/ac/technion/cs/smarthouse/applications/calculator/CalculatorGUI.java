package il.ac.technion.cs.smarthouse.applications.calculator;

import il.ac.technion.cs.smarthouse.developers_api.SmarthouseApplication;
import il.ac.technion.cs.smarthouse.developers_api.application_builder.GuiBinderObject;

/**
 * @author Inbal Zukerman
 * @since 20-07-17
 */
public class CalculatorGUI extends SmarthouseApplication {

	public static void main(String[] args) throws Exception {
		launch();
	}

	@Override
	public void onLoad() throws Exception {
		getAppBuilder().getCustomRegionBuilder().add("calculator_ui.fxml", new GuiBinderObject<>());

	}

	@Override
	public String getApplicationName() {
		return "Calculator Application";
	}
}
