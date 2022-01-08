package webbrowser;

import org.apache.commons.validator.routines.UrlValidator;

import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker.State;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;
import javafx.scene.web.WebHistory.Entry;

public class TabContent extends VBox{
	private WebView webView = new WebView();
	private HBox hBox;
	private VBox vBox;
	private Button launchButton;
	private Button refreshButton;
	private Button homeButton;
	private Button backButton;
	private Button forwardButton;
//	private Button bookmarkButton;
	private Button historyButton;
	private TextField textField = new TextField();
	private ComboBox<Entry> comboBox;
	private String htLink = "http://";
	private String www = "www.";
	private String homePage = "https://www.google.com";
	private WebEngine engine;
	private WebHistory history;
	private ObservableList<WebHistory.Entry> historyEntryList;
	private SimpleListProperty<Entry> list = new SimpleListProperty<>();
	public TabContent() {
		hBox = new HBox();
		setLaunchButton();
		setRefreshButton();
		setHomeButton();
		setBackButton();
		setForwardButton();
//		setBookmarkButton();
		setHistoryButton();
		hBox.getChildren().addAll(backButton,forwardButton,refreshButton,textField,launchButton,homeButton,historyButton);
		
		this.getChildren().addAll(hBox, this.webView);
		this.engine = this.webView.getEngine();
		this.history = this.engine.getHistory();
		this.historyEntryList = this.history.getEntries();
		viewHistory();
		
		VBox.setVgrow(webView, Priority.ALWAYS);
		HBox.setHgrow(hBox, Priority.ALWAYS);
		HBox.setHgrow(textField, Priority.SOMETIMES);
		setButtonAction();
	}

	public void setLaunchButton() {
		ImageView launchButtonImage = new ImageView(new Image(getClass().getResourceAsStream("launch.png")));
		launchButtonImage.setFitHeight(18);
		launchButtonImage.setFitWidth(18);
		launchButton = new Button("",launchButtonImage);
	}

	public void setRefreshButton() {
		ImageView refreshButtonImage = new ImageView(new Image(getClass().getResourceAsStream("refresh.png")));
		refreshButtonImage.setFitHeight(18);
		refreshButtonImage.setFitWidth(18);
		refreshButton = new Button("",refreshButtonImage);
	}

	public void setHomeButton() {
		ImageView homePageButtonImage = new ImageView(new Image(getClass().getResourceAsStream("home.png")));
		homePageButtonImage.setFitHeight(18);
		homePageButtonImage.setFitWidth(18);
		homeButton = new Button("",homePageButtonImage);
	}

	public void setBackButton() {
		ImageView backButtonImage = new ImageView(new Image(getClass().getResourceAsStream("back.png")));
		backButtonImage.setFitHeight(18);
		backButtonImage.setFitWidth(18);
		backButton = new Button("",backButtonImage);
	}

	public void setForwardButton() {
		ImageView forwardButtonImage = new ImageView(new Image(getClass().getResourceAsStream("forward.png")));
		forwardButtonImage.setFitHeight(18);
		forwardButtonImage.setFitWidth(18);
		forwardButton = new Button("",forwardButtonImage);
	}
//
//	public void setBookmarkButton() {
//		ImageView bookmarkButtonImage = new ImageView(new Image(getClass().getResourceAsStream("bookmark.png")));
//		bookmarkButtonImage.setFitHeight(18);
//		bookmarkButtonImage.setFitWidth(18);
//		bookmarkButton = new Button("",bookmarkButtonImage);
//	}

	public void setHistoryButton() {
		ImageView historyButtonImage = new ImageView(new Image(getClass().getResourceAsStream("history.png")));
		historyButtonImage.setFitHeight(18);
		historyButtonImage.setFitWidth(18);
		historyButton = new Button("",historyButtonImage);
	}

	public void setMenuButton() {
		ImageView menuButtonImage = new ImageView(new Image(getClass().getResourceAsStream("menu.png")));
		menuButtonImage.setFitHeight(18);
		menuButtonImage.setFitWidth(18);
		historyButton = new Button("",menuButtonImage);
	}

	public void setButtonAction() {
		launchButton.setOnMouseClicked(e -> {
			launch();
		});
		refreshButton.setOnMouseClicked(e -> {
			refresh();
		});
		homeButton.setOnMouseClicked(e -> {
			engine.load(homePage);
		});
		backButton.setOnMouseClicked(e -> {
			goBackward();
		});
		forwardButton.setOnMouseClicked(e -> {
			goForward();
		});
//		bookmarkButton.setOnMouseClicked(e -> {
//		});
		historyButton.setOnMouseClicked(e -> {
			viewHistory();
		});

//		menuButton.setOnMouseClicked(e -> {
//						
//		});
		textField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent ke) {
				if (ke.getCode().equals(KeyCode.ENTER)) {
					launch();
				}
			}
		});
		
		textField.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				engine.load(textField.getText());
			}
        });
		
	}

	public void launch() {
		String addressLink = textField.getText();
		String url = "";
		if (addressLink != null && !addressLink.isEmpty()) {
			if (addressLink.contains(htLink) && addressLink.contains(www))
				url = addressLink;
			else if ((!addressLink.contains(htLink)) && addressLink.contains(www))
				url = htLink + addressLink;
			else if (!(addressLink.contains(htLink)) && !(addressLink.contains(www)))
				url = htLink + www + addressLink;
		}
		//Solution to check invalid url from Chathura
		if(urlFormatValidator(url)) {
			engine.getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {
				@Override
				public void changed(ObservableValue <? extends State> observable, State oldValue, State newValue) {
					if (newValue == State.FAILED) {
						engine.loadContent("This site can’t be reached", "text/html");
					}
				}
			});
			engine.load(url);

		}else {
			engine.loadContent("Malformed URL", "text/html");
		}
	}

	public static boolean urlFormatValidator(String url) {
		String[] schemes = new String[] { "http", "https" };
		UrlValidator urlValidator = new UrlValidator(schemes);
		if (urlValidator.isValid(url)) {
			return true;
		} else {
			return false;
		}
	}

	public void refresh() {
		engine.reload();
	}

	public void viewHistory() {
		comboBox = new ComboBox();
		this.hBox.getChildren().add(comboBox);
		comboBox.setPrefWidth(60);
		history.getEntries().addListener(
				(ListChangeListener.Change<? extends WebHistory.Entry> c) -> {
					c.next();
					c.getRemoved().stream().forEach((e) -> {
						comboBox.getItems().remove(e);
					});
					c.getAddedSubList().stream().forEach((e) -> {

						comboBox.getItems().add(e);
					});
				});
		comboBox.setOnAction(e -> {
			int offset
			= comboBox.getSelectionModel().getSelectedIndex()
			- history.getCurrentIndex();
			history.go(offset);
		});
	}

	public void goBackward() {
		if(historyEntryList.size() > 1 && history.getCurrentIndex() > 0){
			ObservableList<WebHistory.Entry> entries = history.getEntries();
			history.go(-1);
			textField.setText(entries.get(history.getCurrentIndex()).getUrl());
		}else{
			Alert alert = new Alert(Alert.AlertType.INFORMATION, "No more previous page", ButtonType.OK);
			alert.setTitle("Oops...");
			alert.show();
		}
	}
	/*if (history.getCurrentIndex() >= 1) {
						textField1.setText(history.getEntries().get(history.getCurrentIndex() - 1).getUrl());
						history.go(-1);
					}
	 */
	public void goForward() {
		if(historyEntryList.size() > 1 && history.getCurrentIndex() < historyEntryList.size() - 1){
			history = engine.getHistory();
			ObservableList<WebHistory.Entry> entries = history.getEntries();
			history.go(1);
			textField.setText(entries.get(history.getCurrentIndex()).getUrl());
		}else {
			Alert alert = new Alert(Alert.AlertType.INFORMATION, "No more pages ahead", ButtonType.OK);
			alert.setTitle("Oops...");
			alert.show();
		}
		//			history.go(historyEntryList.size() > 1 && history.getCurrentIndex() < historyEntryList.size() - 1 ? 1 : 0);
	}

	public TextField getTextField() {
		return textField;
	}

	public void setTextField(TextField textField) {
		this.textField = textField;
	}

	public WebView getWebView() {
		return webView;
	}
	
	public void setWebView(WebView webView) {
		this.webView = webView;
	}
	
	public HBox getHBox() {
		return hBox;
	}
	
	public void setHBox(HBox hBox) {
		this.hBox = hBox;
	}
	
}
