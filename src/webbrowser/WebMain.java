package webbrowser;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class WebMain extends Application{
	TabPane tabPane = new TabPane();
	SelectionModel<Tab> selectionModel; 
	BorderPane mainPane = new BorderPane();
	@Override
	public void start(Stage primaryStage) throws Exception {
		Group root = new Group();
		Scene scene = new Scene(root, 960, 540);
		selectionModel = this.tabPane.getSelectionModel();
		//Create Tabs
		Tab tabA = new Tab();
		
		TabContent tcA = new TabContent();
		String home = "http://www.google.com/";
		TextField tfA = tcA.getTextField();
		tfA.setText(home);
		tcA.setTextField(tfA);
		tcA.launch();
		tabA.setText("Home");
		tabA.setContent(tcA);
		tcA.getWebView().getEngine().titleProperty().addListener(new ChangeListener<String>(){
            public void changed(ObservableValue<? extends String> ov,
                    final String oldvalue, final String newvalue) {
                tabA.setText(newvalue);
            }
        });
		tabPane.getTabs().add(tabA);
		
//		tcA.getWebView().getEngine().titleProperty().addListener(new ChangeListener<String>() {
//            public void changed(ObservableValue<? extends String> ov,
//                    final String oldvalue, final String newvalue) {
//                // Set the Title of the Stage
//                tabA.setText((String)newvalue);
//            }
//        });
//		
//		Tab tabB = new Tab();
//		tabB.setText("New Tab");
//		ImageView newTabButtonView = new ImageView(new Image(getClass().getResourceAsStream("plus.png")));
//		newTabButtonView.setFitWidth(18);
//		newTabButtonView.setFitHeight(18);
//		Button newTabButton = new Button("",newTabButtonView);
//		newTabButton.setStyle("-fx-background-color: transparent");
//		tabB.setGraphic(newTabButton);
//		tabPane.getTabs().add(tabB);
//
//		tabPane.getSelectionModel().selectedItemProperty().addListener(
//				new ChangeListener<Tab>() {
//					@Override
//					public void changed(ObservableValue<? extends Tab> ov, Tab t, Tab t1) {
//						System.out.println("Tab Selection changed: " + t1.getText());
//						t1.setContent(new TabContent());
//					}
//				});
		Tab newTab = new Tab();
		ImageView newTabButtonView = new ImageView(new Image(getClass().getResourceAsStream("plus.png")));
		newTabButtonView.setFitWidth(18);
		newTabButtonView.setFitHeight(18);
		Button newTabButton = new Button("",newTabButtonView);
		newTabButton.setStyle("-fx-background-color: transparent");
		newTab.setGraphic(newTabButton);
		newTab.setDisable(true);
		newTab.setClosable(false);
		tabPane.getTabs().add(newTab);
		tabPane.setTabClosingPolicy(TabClosingPolicy.ALL_TABS);
		
		newTabButton.setOnMouseClicked(e -> {
			Tab tab = new Tab();
			tab.setText("New Tab");
			TabContent tcNew = new TabContent();
			tab.setContent(tcNew);
			tabPane.getTabs().add(tabPane.getTabs().size() -1, tab);
			selectionModel.select(tab);
			
			tcNew.getWebView().getEngine().titleProperty().addListener(new ChangeListener<String>(){
	            public void changed(ObservableValue<? extends String> ov,
	                    final String oldvalue, final String newvalue) {
	                tab.setText(newvalue);
	            }
	        });
			
		});
		mainPane.setCenter(tabPane);
		mainPane.prefHeightProperty().bind(scene.heightProperty());
		mainPane.prefWidthProperty().bind(scene.widthProperty());

		root.getChildren().add(mainPane);

		Image browserImage = new Image(getClass().getResourceAsStream("browser.png"));
		primaryStage.getIcons().add(browserImage);
		primaryStage.setTitle("JBrowser");
		primaryStage.setScene(scene);
		primaryStage.sizeToScene();
		primaryStage.show();
		}

	public static void main(String[] args) {
		launch(args);
	}
}
