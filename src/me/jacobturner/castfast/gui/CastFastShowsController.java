package me.jacobturner.castfast.gui;

import java.util.ArrayList;
import java.util.Collections;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import me.jacobturner.castfast.CastFastSQL;

public class CastFastShowsController {
	@FXML
	private ChoiceBox<String> showSelector;
	@FXML
	private TextField showName;
	@FXML
	private TextField djs;
	@FXML
	private TextField djEmail;
	@FXML
	private TextField dateAndTime;
	@FXML
	private Button addButton;
	@FXML
	private Button updateButton;
	@FXML
	private Button deleteButton;
	@FXML
	private Button closeButton;
	
	private CastFastSQL sqlFile = new CastFastSQL();
	private ArrayList<String> showList = sqlFile.getNames();
	
	public void initialize() {
		Collections.sort(showList);
		showSelector.getItems().add("<new show>");
		showSelector.getItems().addAll(showList);
		showSelector.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue<? extends Number> ov, Number old_toggle, Number new_toggle) {
				if ((int)new_toggle >= 0) {
					String selectedShow = showSelector.getItems().get((int)new_toggle);
					if (selectedShow.equals("<new show>")) {
						showName.setText("");
						djs.setText("");
						djEmail.setText("");
						dateAndTime.setText("");
						addButton.setDisable(false);
						updateButton.setDisable(true);
						deleteButton.setDisable(true);
					} else {
						ArrayList<String> selectedShowData = sqlFile.getShow(selectedShow);
						showName.setText(selectedShowData.get(0));
						djs.setText(selectedShowData.get(1));
						djEmail.setText(selectedShowData.get(2));
						dateAndTime.setText(selectedShowData.get(3));
						addButton.setDisable(true);
						updateButton.setDisable(false);
						deleteButton.setDisable(false);
					}
				}
			}
		});
		closeButton.setOnAction(event -> {
			sqlFile.close();
			try {
				Stage stage = (Stage)closeButton.getScene().getWindow();
			    stage.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		showSelector.getSelectionModel().selectFirst();
		addButton.setDisable(false);
		updateButton.setDisable(true);
	}
	
	@FXML
	public void addShow() {
		sqlFile.addShow(showName.getText(), djs.getText(), djEmail.getText(), dateAndTime.getText());
		showSelector.getItems().add(showName.getText());
		showSelector.getSelectionModel().select(showName.getText());
	}
	
	@FXML
	public void deleteShow() {
		String tempShowName = showName.getText();
		sqlFile.deleteShow(tempShowName);
		showSelector.getItems().remove(tempShowName);
		showSelector.getSelectionModel().select("<new show>");
		showName.setText("");
		djs.setText("");
		djEmail.setText("");
		dateAndTime.setText("");
		addButton.setDisable(false);
		updateButton.setDisable(true);
	}
	
	@FXML
	public void updateShow() {
		String oldShow = showSelector.getSelectionModel().getSelectedItem();
		showSelector.getItems().remove(oldShow);
		sqlFile.updateShow(oldShow, showName.getText(), djs.getText(), djEmail.getText(), dateAndTime.getText());
		showSelector.getItems().add(showName.getText());
		showSelector.getSelectionModel().select(showName.getText());
	}
}
