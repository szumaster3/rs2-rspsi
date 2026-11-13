package com.rspsi;

import java.io.File;
import java.util.function.Consumer;

import com.rspsi.resources.ResourceLoader;
import com.rspsi.util.FXUtils;
import com.rspsi.util.FilterMode;
import com.rspsi.util.RetentionFileChooser;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SelectXTEAWindow {

	private Stage stage;
	private boolean okClicked;

	@FXML
	private TextField landscapeText;

	@FXML
	private Button landscapeBrowse;

	@FXML
	private Button okButton;

	@FXML
	private Button cancelButton;

	public SelectXTEAWindow() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/xteapicker.fxml"));
			loader.setController(this);

			Parent content = loader.load();

			stage = new Stage();
			Scene scene = new Scene(content);

			stage.setTitle("Please select file to load");
			stage.initStyle(StageStyle.UTILITY);
			stage.setScene(scene);
			stage.getIcons().add(ResourceLoader.getSingleton().getLogo64());

			stage.centerOnScreen();
			FXUtils.centerStage(stage);
			stage.setAlwaysOnTop(true);

			setupEvents();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setupEvents() {

		Consumer<TextField> finishBrowse = textField -> {

			File startDir = null;

			if(textField.getText() != null && !textField.getText().isEmpty()) {
				File f = new File(textField.getText());

				if(f.isDirectory()) {
					startDir = f;
				} else if(f.getParentFile() != null && f.getParentFile().exists()) {
					startDir = f.getParentFile();
				}
			}

			File selected = RetentionFileChooser.showOpenDialog(stage, String.valueOf(startDir), FilterMode.JSON);

			if(selected != null && selected.exists()) {
				textField.setText(selected.getAbsolutePath());
			}
		};

		landscapeBrowse.setOnAction(evt -> finishBrowse.accept(landscapeText));

		stage.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
			if (event.getCode() == KeyCode.ENTER) {
				okClicked = true;
				stage.hide();
			}
		});

		okButton.setOnAction(evt -> {
			okClicked = true;
			stage.hide();
		});

		cancelButton.setOnAction(evt -> {
			reset();
			stage.hide();
		});
	}

	public void show() {
		stage.sizeToScene();
		okButton.requestFocus();
		stage.showAndWait();

		if (!okClicked) {
			reset();
		}
	}

	public String getJsonLocation() {
		return landscapeText.getText();
	}

	public void reset() {
		okClicked = false;
	}

	public boolean valid() {
		return okClicked && !landscapeText.getText().isEmpty();
	}

	public void setLocation(String currentXTEALoc) {
		landscapeText.setText(currentXTEALoc);
	}
}