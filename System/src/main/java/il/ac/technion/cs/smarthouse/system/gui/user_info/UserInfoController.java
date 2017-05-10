package il.ac.technion.cs.smarthouse.system.gui.user_info;

import java.net.URL;
import java.util.ResourceBundle;

import il.ac.technion.cs.smarthouse.mvp.SystemPresenter;
import il.ac.technion.cs.smarthouse.system.EmergencyLevel;
import il.ac.technion.cs.smarthouse.system.SystemCore;
import il.ac.technion.cs.smarthouse.system.user_information.Contact;
import il.ac.technion.cs.smarthouse.system.user_information.UserInformation;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class UserInfoController extends SystemPresenter {
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

    @FXML private TableView<ContactGUI> contactsTable;
    @FXML public TableColumn<ContactGUI, String> nameColumn;
    @FXML public TableColumn<ContactGUI, String> idColumn;
    @FXML public TableColumn<ContactGUI, String> phoneColumn;
    @FXML public TableColumn<ContactGUI, String> emailColumn;
    @FXML public TableColumn<ContactGUI, String> eLevelColumn;

    private class ContactGUI {
        public Contact contact;
        public EmergencyLevel eLevel;

        public ContactGUI(final Contact contact, final EmergencyLevel eLevel) {
            this.contact = contact;
            this.eLevel = eLevel;
        }

    }

    private void setButtons() {
        userSaveField.setOnAction(event -> {

            final String name = userNameField.getText(), id = userIDField.getText(), phoneNum = userPhoneNumField.getText(),
                    address = userHomeAddressField.getText();
            if (!validateUserInput(name, id, phoneNum, address))
                alertMessageUnvalidInput();
            else if (getModel().isUserInitialized()) {
                final UserInformation temp = getModel().getUser();
                temp.setHomeAddress(address);
                temp.setPhoneNumber(phoneNum);
            } else {
                getModel().initializeUser(name, id, phoneNum, address);
                userNameField.setEditable(false);
                userIDField.setEditable(false);
            }
        });

        saveButton.setOnAction(event -> {
            if (getModel().isUserInitialized())
                if (validateUserInput(addNameField.getText(), addIDField.getText(), addPhoneField.getText(), addEmailField.getText()))
                    addContactToTable(event);
                else
                    alertMessageUnvalidInput();
            else {
                final Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("User Not Registered");
                alert.setContentText("Make sure to register the user before adding any contacts");
                alert.showAndWait();
            }
        });
    }

    private void setCellsFactories() {
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().contact.getName()));
        idColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().contact.getId()));
        phoneColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().contact.getPhoneNumber()));
        emailColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().contact.getEmailAddress()));
        eLevelColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().eLevel + ""));

        contactsTable.setEditable(true);

        phoneColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        phoneColumn.setOnEditCommit(¢ -> ¢.getTableView().getItems().get(¢.getTablePosition().getRow()).contact.setPhoneNumber(¢.getNewValue()));

        emailColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        emailColumn.setOnEditCommit(¢ -> ¢.getTableView().getItems().get(¢.getTablePosition().getRow()).contact.setEmailAddress(¢.getNewValue()));

        eLevelColumn.setCellFactory(
                ComboBoxTableCell.<ContactGUI, String> forTableColumn(FXCollections.observableArrayList(EmergencyLevel.stringValues())));
        eLevelColumn.setOnEditCommit(new EventHandler<CellEditEvent<ContactGUI, String>>() {

            @Override public void handle(final CellEditEvent<ContactGUI, String> ¢) {
                getModel().getUser().setContactEmergencyLevel(¢.getTableView().getItems().get(¢.getTablePosition().getRow()).contact.getId(),
                        ¢.getNewValue());
            }

        });
    }

    private void costumizeContactsTab() {

        HBox.setHgrow(addNameField, Priority.ALWAYS);
        HBox.setHgrow(addELevelField, Priority.ALWAYS);
        HBox.setHgrow(addIDField, Priority.ALWAYS);
        HBox.setHgrow(addPhoneField, Priority.ALWAYS);
        HBox.setHgrow(addEmailField, Priority.ALWAYS);
        HBox.setHgrow(saveButton, Priority.ALWAYS);

        addELevelField.setPromptText("Emergency Level");
        addELevelField.getItems().addAll(EmergencyLevel.values());

        final int btnCount = buttonBox.getChildren().size();
        addNameField.prefWidthProperty().bind(buttonBox.widthProperty().divide(btnCount));
        addELevelField.prefWidthProperty().bind(buttonBox.widthProperty().divide(btnCount));
        addIDField.prefWidthProperty().bind(buttonBox.widthProperty().divide(btnCount));
        addPhoneField.prefWidthProperty().bind(buttonBox.widthProperty().divide(btnCount));
        addEmailField.prefWidthProperty().bind(buttonBox.widthProperty().divide(btnCount));
        saveButton.prefWidthProperty().bind(buttonBox.widthProperty().divide(btnCount));

    }

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

    @FXML private void addContactToTable(final ActionEvent __) {
        final Contact contact = new Contact(addIDField.getText(), addNameField.getText(), addPhoneField.getText(), addEmailField.getText());
        getModel().getUser().addContact(contact, addELevelField.getValue());
        final ContactGUI guiContact = new ContactGUI(contact, addELevelField.getValue());
        addNameField.clear();
        addIDField.clear();
        addPhoneField.clear();
        addEmailField.clear();
        addELevelField.setValue(null);
        contactsTable.getItems().add(guiContact);

    }

    @Override public void init(SystemCore model, URL location, ResourceBundle __) {
        setButtons();
        setCellsFactories();
        costumizeContactsTab();
    }

}
