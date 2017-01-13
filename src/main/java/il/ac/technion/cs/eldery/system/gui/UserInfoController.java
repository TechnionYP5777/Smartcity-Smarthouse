package il.ac.technion.cs.eldery.system.gui;

import java.net.URL;
import java.util.ResourceBundle;

import il.ac.technion.cs.eldery.system.EmergencyLevel;
import il.ac.technion.cs.eldery.system.userInformation.Contact;
import il.ac.technion.cs.eldery.system.userInformation.UserInformation;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class UserInfoController implements Initializable {

    private UserInformation user;

    @FXML public TextField userNameField;
    @FXML public TextField userIDField;
    @FXML public TextField userPhoneNumField;
    @FXML public TextField userHomeAddressField;
    @FXML public Button userSaveField;

    @FXML public TextField addNameField;
    @FXML public TextField addIDField;
    @FXML public TextField addPhoneField;
    @FXML public TextField addEmailField;
    @FXML public ComboBox<EmergencyLevel> addELevelField;
    @FXML public Button saveButton;
    @FXML public HBox buttonBox;

    @FXML private TableView<Contact> contactsTable;
    @FXML public TableColumn<Contact, String> nameColumn;
    @FXML public TableColumn<Contact, String> idColumn;
    @FXML public TableColumn<Contact, String> phoneColumn;
    @FXML public TableColumn<Contact, String> emailColumn;
    @FXML public TableColumn<Contact, EmergencyLevel> eLevelColumn;

    private static boolean validateUserInput(final String name, final String id, final String phone, final String address) {
        return name != null && id != null && phone != null && address != null && !"".equals(name) && !"".equals(id) && !"".equals(phone)
                && !"".equals(address) && name.chars().allMatch(Character::isLetter) && id.chars().allMatch(Character::isDigit)
                && phone.chars().allMatch(Character::isDigit);
    }

    private static void alertMessageUnvalidInput() {
        final Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setHeaderText("Bad Input");
        alert.setContentText("Make sure to enter only valid names and phone numbers");
        alert.showAndWait();
    }

    @FXML private void addContactToTable(@SuppressWarnings("unused") final ActionEvent __) {
        final ObservableList<Contact> data = contactsTable.getItems();
        final Contact contact = new Contact(addIDField.getText(), addNameField.getText(), addPhoneField.getText(), addEmailField.getText());
        // user.addContact(contact, addELevelField.getValue()); TODO!
        addNameField.clear();
        addIDField.clear();
        addPhoneField.clear();
        addEmailField.clear();
        data.add(contact);

    }

    @Override public void initialize(final URL arg0, final ResourceBundle arg1) {

        addELevelField.getItems().addAll(EmergencyLevel.CALL_EMERGENCY_CONTACT, EmergencyLevel.SMS_EMERGENCY_CONTACT, EmergencyLevel.NOTIFY_ELDERLY,
                EmergencyLevel.CONTACT_POLICE, EmergencyLevel.CONTACT_FIRE_FIGHTERS, EmergencyLevel.CONTACT_HOSPITAL);

        userSaveField.setOnAction(event -> {

            final String name = userNameField.getText();
            final String id = userIDField.getText();
            final String phoneNum = userPhoneNumField.getText();
            final String address = userHomeAddressField.getText();

            if (!validateUserInput(name, id, phoneNum, address))
                alertMessageUnvalidInput();
            else {
                user = new UserInformation(name, id, phoneNum, address);
                userNameField.setEditable(false);
                userIDField.setEditable(false);
            }
        });

        HBox.setHgrow(addNameField, Priority.ALWAYS);
        HBox.setHgrow(addELevelField, Priority.ALWAYS);
        HBox.setHgrow(addIDField, Priority.ALWAYS);
        HBox.setHgrow(addPhoneField, Priority.ALWAYS);
        HBox.setHgrow(addEmailField, Priority.ALWAYS);
        HBox.setHgrow(saveButton, Priority.ALWAYS);

        final int btnCount = buttonBox.getChildren().size();
        addNameField.prefWidthProperty().bind(buttonBox.widthProperty().divide(btnCount));
        addELevelField.prefWidthProperty().bind(buttonBox.widthProperty().divide(btnCount));
        addIDField.prefWidthProperty().bind(buttonBox.widthProperty().divide(btnCount));
        addPhoneField.prefWidthProperty().bind(buttonBox.widthProperty().divide(btnCount));
        addEmailField.prefWidthProperty().bind(buttonBox.widthProperty().divide(btnCount));
        saveButton.prefWidthProperty().bind(buttonBox.widthProperty().divide(btnCount));

        addELevelField.setPromptText("Emergency Level");

        nameColumn.setCellValueFactory(new PropertyValueFactory<Contact, String>("name"));
        idColumn.setCellValueFactory(new PropertyValueFactory<Contact, String>("id"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<Contact, String>("phoneNumber"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<Contact, String>("emailAddress"));

        saveButton.setOnAction(event -> {
            if (validateUserInput(addNameField.getText(), addIDField.getText(), addPhoneField.getText(), addEmailField.getText()))
                addContactToTable(event);
            else
                alertMessageUnvalidInput();
        });

    }

}
