package il.ac.technion.cs.smarthouse.gui.controllers.user_info;

import java.net.URL;
import java.util.ResourceBundle;

import il.ac.technion.cs.smarthouse.gui.controllers.SystemGuiController;
import il.ac.technion.cs.smarthouse.gui_controller.GuiController;
import il.ac.technion.cs.smarthouse.system.SystemCore;
import il.ac.technion.cs.smarthouse.system.SystemMode;
import il.ac.technion.cs.smarthouse.system.services.alerts_service.EmergencyLevel;
import il.ac.technion.cs.smarthouse.system.user_information.Contact;
import il.ac.technion.cs.smarthouse.system.user_information.UserInformation;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;

public class UserInfoController extends SystemGuiController {
    @FXML public TextField userNameField;
    @FXML public TextField userIDField;
    @FXML public TextField userPhoneNumField;
    @FXML public TextField userHomeAddressField;
    @FXML public Button userSaveField;

    @FXML public ImageView nameStatus;
    @FXML public ImageView idStatus;
    @FXML public ImageView phoneStatus;
    @FXML public ImageView addressStatus;

    @FXML public Label nameMessage;
    @FXML public Label idMessage;
    @FXML public Label phoneMessage;
    @FXML public Label addressMessage;

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

    private void openSuccessDialog(String title, String header, String content) {
        Alert a = new Alert(AlertType.INFORMATION, content, ButtonType.OK);
        a.setHeaderText(header);
        a.setTitle(title);
        a.showAndWait();
    }

    private void setButtons() {
        userSaveField.setOnAction(event -> {

            final String name = userNameField.getText(), id = userIDField.getText(),
                            phoneNum = userPhoneNumField.getText(), address = userHomeAddressField.getText();
            if (!validateUserInput(name, id, phoneNum, address))
                alertMessageUnvalidInput("Make sure to enter only valid names and phone numbers.");
            else {
                if (getModel().isUserInitialized()) {
                    final UserInformation temp = getModel().getUser();
                    temp.setHomeAddress(address);
                    temp.setPhoneNumber(phoneNum);
                    openSuccessDialog("Successful Update", "Thanks for the update!",
                                    "Your information was updated successfully.");
                } else {
                    getModel().initializeUser(name, id, phoneNum, address);
                    userNameField.setEditable(false);
                    userIDField.setEditable(false);
                    openSuccessDialog("Successful Registration", "Hello " + userNameField.getText() + "!",
                                    "You registered successfully.");
                }
                clearAfterUserSave();
            }
        });

        saveButton.setOnAction(event -> {
            if (!getModel().isUserInitialized()) {
                final Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("User Not Registered");
                alert.setContentText("Make sure to register the user before adding any contacts.");
                alert.showAndWait();
            } else if (!validateEmergencyLevel(addELevelField.getValue()))
                alertMessageUnvalidInput("Please select an emergency level.");
            else if (!validateContact(addNameField.getText(), addIDField.getText(), addPhoneField.getText(),
                            addEmailField.getText()))
                alertMessageUnvalidInput("Make sure to enter only valid names and phone numbers.");
            else {
                addContactToTable(event);
                openSuccessDialog("Successful Update", addNameField.getText() + " is an emergency contact now.",
                                "The emergency contact was added successfully.");
                clearAfterContactSave();
            }
        });
    }

    private void setCellsFactories() {
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().contact.getName()));
        idColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().contact.getId()));
        phoneColumn.setCellValueFactory(
                        cellData -> new SimpleStringProperty(cellData.getValue().contact.getPhoneNumber()));
        emailColumn.setCellValueFactory(
                        cellData -> new SimpleStringProperty(cellData.getValue().contact.getEmailAddress()));
        eLevelColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().eLevel + ""));

        contactsTable.setEditable(true);

        phoneColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        phoneColumn.setOnEditCommit(¢ -> ¢.getTableView().getItems().get(¢.getTablePosition().getRow()).contact
                        .setPhoneNumber(¢.getNewValue()));

        emailColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        emailColumn.setOnEditCommit(¢ -> ¢.getTableView().getItems().get(¢.getTablePosition().getRow()).contact
                        .setEmailAddress(¢.getNewValue()));

        eLevelColumn.setCellFactory(ComboBoxTableCell.<ContactGUI, String>forTableColumn(
                        FXCollections.observableArrayList(EmergencyLevel.stringValues())));
        eLevelColumn.setOnEditCommit(¢ -> getModel().getUser().setContactEmergencyLevel(
                        ¢.getTableView().getItems().get(¢.getTablePosition().getRow()).contact.getId(),
                        ¢.getNewValue()));
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

    private static boolean validateContact(final String name, final String id, final String phone, final String email) {
        return validateName(name) && validateId(id) && validatePhone(phone) && validateEmail(email);
    }

    private static boolean validateUserInput(final String name, final String id, final String phone,
                    final String address) {
        return validateName(name) && validateId(id) && validatePhone(phone) && validateAddress(address);
    }

    static boolean validateName(final String name) {
        return name != null && !"".equals(name) && name.chars().allMatch(Character::isLetter);
    }

    static boolean validateId(final String id) {
        return id != null && !"".equals(id) && id.chars().allMatch(Character::isDigit);
    }

    static boolean validatePhone(final String phone) {
        return phone != null && !"".equals(phone) && phone.chars().allMatch(Character::isDigit);
    }

    static boolean validateAddress(final String address) {
        // TODO: add more checks
        return address != null && !"".equals(address);
    }

    static boolean validateEmail(final String email) {
        // TODO: add more checks
        return email != null && !"".equals(email);
    }

    static boolean validateEmergencyLevel(final EmergencyLevel eLevel) {
        return eLevel != null;
    }

    private static void alertMessageUnvalidInput(String message) {
        final Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setHeaderText("Bad Input");
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void addContactToTable(final ActionEvent __) {
        final Contact contact = new Contact(addIDField.getText(), addNameField.getText(), addPhoneField.getText(),
                        addEmailField.getText());
        getModel().getUser().addContact(contact, addELevelField.getValue());
        contactsTable.getItems().add(new ContactGUI(contact, addELevelField.getValue()));

    }

    void setStatus(ImageView statusImage, Label statusLabel, String message, boolean valid) {
        statusImage.setVisible(true);
        if (valid) {
            statusImage.setImage(new Image(getClass().getResourceAsStream("/icons/check-icon.png")));
            statusLabel.setText("");
        } else {
            statusImage.setImage(new Image(getClass().getResourceAsStream("/icons/cross-icon.png")));
            statusLabel.setText(message);
            statusLabel.setTextFill(Color.RED);
        }
    }

    private void setInputListeners() {
        userNameField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> b, Boolean oldValue, Boolean newValue) {
                if (!newValue) // Focusing out
                    setStatus(nameStatus, nameMessage, "Name can't be empty and must contain only letters",
                                    validateName(userNameField.getText()));
            }
        });

        userIDField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> b, Boolean oldValue, Boolean newValue) {
                if (!newValue) // Focusing out
                    setStatus(idStatus, idMessage, "ID can't be empty and must contain only digits",
                                    validateId(userIDField.getText()));
            }
        });

        userPhoneNumField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> b, Boolean oldValue, Boolean newValue) {
                if (!newValue) // Focusing out
                    setStatus(phoneStatus, phoneMessage, "Phone number can't be empty and must contain only digits",
                                    validatePhone(userPhoneNumField.getText()));
            }
        });

        userHomeAddressField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> b, Boolean oldValue, Boolean newValue) {
                if (!newValue) // Focusing out
                    setStatus(addressStatus, addressMessage, "Home address can't be empty",
                                    validateAddress(userHomeAddressField.getText()));
            }
        });
    }

    private void setContactListeners() {
        addNameField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> b, Boolean oldValue, Boolean newValue) {
                if (!newValue) // Focusing out
                    addNameField.setStyle(
                                    "-fx-border-color: " + (validateName(addNameField.getText()) ? "green" : "red"));
            }
        });

        addIDField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> b, Boolean oldValue, Boolean newValue) {
                if (!newValue) // Focusing out
                    addIDField.setStyle("-fx-border-color: " + (validateId(addIDField.getText()) ? "green" : "red"));
            }
        });

        addPhoneField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> b, Boolean oldValue, Boolean newValue) {
                if (!newValue) // Focusing out
                    addPhoneField.setStyle(
                                    "-fx-border-color: " + (validatePhone(addPhoneField.getText()) ? "green" : "red"));
            }
        });

        addEmailField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> b, Boolean oldValue, Boolean newValue) {
                if (!newValue) // Focusing out
                    addEmailField.setStyle(
                                    "-fx-border-color: " + (validateEmail(addEmailField.getText()) ? "green" : "red"));
            }
        });
    }

    private void clearAfterUserSave() {
        nameStatus.setVisible(false);
        idStatus.setVisible(false);
        phoneStatus.setVisible(false);
        addressStatus.setVisible(false);
    }

    private void clearAfterContactSave() {
        addNameField.clear();
        addIDField.clear();
        addPhoneField.clear();
        addEmailField.clear();
        addELevelField.setValue(null);

        addNameField.setStyle(null);
        addIDField.setStyle(null);
        addPhoneField.setStyle(null);
        addEmailField.setStyle(null);
    }

    @Override
    protected <T extends GuiController<SystemCore>> void initialize(SystemCore model, T parent, SystemMode m,
                    URL location, ResourceBundle b) {
        setButtons();
        setInputListeners();
        setContactListeners();
        setCellsFactories();
        costumizeContactsTab();
    }

}
