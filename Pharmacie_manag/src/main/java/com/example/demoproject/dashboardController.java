package com.example.demoproject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.net.URL;
import java.security.cert.Extension;
import java.sql.*;
import java.util.*;
import java.util.Date;

import static javafx.scene.control.Alert.*;
import static javafx.stage.FileChooser.*;

public class dashboardController implements Initializable {

    @FXML
    private Button addMed_btn;

    @FXML
    private Button addMedicines_addBtn;

    @FXML
    private TextField addMedicines_brand;

    @FXML
    private Button addMedicines_clearBtn;

    @FXML
    private TableView<medicineData> addMedicines_tableView;

    @FXML
    private TableColumn<medicineData, String> addMedicines_col_brand;

    @FXML
    private TableColumn<medicineData, String> addMedicines_col_date;

    @FXML
    private TableColumn<medicineData, String> addMedicines_col_medicineid;

    @FXML
    private TableColumn<medicineData, String> addMedicines_col_price;

    @FXML
    private TableColumn<medicineData, String> addMedicines_col_productName;

    @FXML
    private TableColumn<medicineData, String> addMedicines_col_statut;

    @FXML
    private TableColumn<medicineData, String> addMedicines_col_type;

    @FXML
    private Button addMedicines_deleteBtn;

    @FXML
    private AnchorPane addMedicines_form;

    @FXML
    private ImageView addMedicines_imageview;

    @FXML
    private Button addMedicines_importBtn;

    @FXML
    private TextField addMedicines_medicineID;

    @FXML
    private TextField addMedicines_price;

    @FXML
    private TextField addMedicines_productName;

    @FXML
    private TextField addMedicines_search;

    @FXML
    private ComboBox<String> addMedicines_statut;

    @FXML
    private ComboBox<String> addMedicines_type;

    @FXML
    private Button addMedicines_updateBtn;

    @FXML
    private Button dash_close;

    @FXML
    private Label dashboard_availableMed;

    @FXML
    private Button dashboard_btn;

    @FXML
    private AreaChart<?, ?> dashboard_chart;

    @FXML
    private AnchorPane dashboard_form;

    @FXML
    private Label dashboard_totalCustomers;

    @FXML
    private Label dashboard_totalIncome;

    @FXML
    private Button logout_btn;

    @FXML
    private AnchorPane main_form;

    @FXML
    private Button minimize;

    @FXML
    private Button purchase_addBtn;

    @FXML
    private TextField purchase_amount;

    @FXML
    private Label purchase_balance;

    @FXML
    private ComboBox<?> purchase_brand;

    @FXML
    private Button purchase_btn;

    @FXML
    private TableColumn<customerData, String> purchase_co_brand;

    @FXML
    private TableColumn<customerData, String> purchase_co_medicineid;

    @FXML
    private TableColumn<customerData, String> purchase_co_price;

    @FXML
    private TableColumn<customerData, String> purchase_co_productName;

    @FXML
    private TableColumn<customerData, String> purchase_co_qty;

    @FXML
    private TableColumn<customerData, String> purchase_co_type;

    @FXML
    private AnchorPane purchase_form;

    @FXML
    private ComboBox<?> purchase_medicineID;

    @FXML
    private Button purchase_payBtn;

    @FXML
    private ComboBox<?> purchase_productName;

    @FXML
    private TableView<customerData> purchase_tableView;

    @FXML
    private Label purchase_total;

    @FXML
    private ComboBox<?> purchase_type;

    @FXML
    private Spinner<Integer> purchase_quantity;

    @FXML
    private Label username_para;


    private Connection connect;
    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet result;



    //_____________________________________________________________________________________________
    
    //DASHBOARD
    public void homeM() {
    	connect =database.connectDb();
    	String sql ="SELECT COUNT(medicine_id) FROM medicine WHERE status = 'Disponible'";
    	int countAM =0;
    	try {
    		prepare =connect.prepareStatement(sql);
    		result =prepare.executeQuery();
    		while(result.next()) {
    			countAM =result.getInt("COUNT(medicine_id)");
    			
    			
    		}
    		dashboard_availableMed.setText(String.valueOf(countAM));
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    
    
    public void home_income() {
    	connect =database.connectDb();
    	String sql ="SELECT SUM(total) FROM customer_info";
    	double totalincome =0;
    	try {
    		prepare =connect.prepareStatement(sql);
    		result =prepare.executeQuery();
    		while(result.next()) {
    			totalincome =result.getDouble("SUM(total)");
    		}
    		dashboard_totalIncome.setText(String.valueOf(totalincome) +" DH");
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    

    public void home_TotCustomers() {
    	connect =database.connectDb();
    	String sql ="SELECT COUNT(id) FROM customer_info";
    	int countTC =0;
    	try {
    		prepare =connect.prepareStatement(sql);
    		result =prepare.executeQuery();
    		while(result.next()) {
    			countTC =result.getInt("COUNT(id)");
    			
    		}
    		dashboard_totalCustomers.setText(String.valueOf(countTC));
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    
    
    
    public void home_graph() {
    	connect =database.connectDb();
    	dashboard_chart.getData().clear();
    	String sql ="SELECT date, SUM(total) FROM customer_info GROUP BY date ORDER BY TIMESTAMP(date) ASC LIMIT 9";
    	try {
    		XYChart.Series chart =new XYChart.Series();
    		prepare =connect.prepareStatement(sql);
    		result =prepare.executeQuery();
    		while(result.next()) {
    			chart.getData().add(new XYChart.Data(result.getString(1), result.getInt(2)));
    			
    		}
    		dashboard_chart.getData().add(chart);
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    
  
    
    public  void addMedicinesAdd(){

        String sql = "INSERT INTO medicine (medicine_id, brand, productName, type, status, price, date) "
                + "VALUES(?,?,?,?,?,?,?)";

        connect = database.connectDb();
        try {

            Alert alert;
            if (addMedicines_medicineID.getText().isEmpty()
                    || addMedicines_brand.getText().isEmpty()
                    || addMedicines_productName.getText().isEmpty()
                    || addMedicines_type.getSelectionModel().getSelectedItem() == null
                    || addMedicines_statut.getSelectionModel().getSelectedItem() == null
                    || addMedicines_price.getText().isEmpty()
                    ){
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error message");
                alert.setHeaderText(null);
                alert.setContentText("Remplir toutes les cases vides!");
                alert.showAndWait();
            }else {

                String checkData = "SELECT medicine_id FROM medicine WHERE medicine_id = '"
                        +addMedicines_medicineID.getText()+"'";

                statement = connect.createStatement();
                result = statement.executeQuery(checkData);

                if (result.next()){
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error message");
                    alert.setHeaderText(null);
                    alert.setContentText("ID medicament " + addMedicines_medicineID.getText() +" existe déjà!");
                    alert.showAndWait();

                }else {
                    prepare = connect.prepareStatement(sql);
                    prepare.setString(1, addMedicines_medicineID.getText());
                    prepare.setString(2, addMedicines_brand.getText());
                    prepare.setString(3, addMedicines_productName.getText());
                    prepare.setString(4, (String) addMedicines_type.getSelectionModel().getSelectedItem());
                    prepare.setString(5, (String) addMedicines_statut.getSelectionModel().getSelectedItem());
                    prepare.setString(6, addMedicines_price.getText());



                    Date date = new Date();
                    java.sql.Date sqlDate = new java.sql.Date(date.getTime());

                    prepare.setString(7, String.valueOf(sqlDate));


                }
            }


            prepare = connect.prepareStatement(sql);
            prepare.setString(1, addMedicines_medicineID.getText());
            prepare.setString(2, addMedicines_brand.getText());
            prepare.setString(3, addMedicines_productName.getText());
            prepare.setString(4, (String) addMedicines_type.getSelectionModel().getSelectedItem());
            prepare.setString(5, (String) addMedicines_statut.getSelectionModel().getSelectedItem());
            prepare.setString(6, addMedicines_price.getText());



            Date date = new Date();
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());

            prepare.setString(7, String.valueOf(sqlDate));

            prepare.executeUpdate();
            alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Information Message");
            alert.setHeaderText(null);
            alert.setContentText("Le medicament à été ajouté avec succès");
            alert.showAndWait();

            addMedicineShowListData();
            addMedicineReset();


        }catch (Exception e){e.printStackTrace();}
    }


    public void addMedicineUpdate(){


        String sql = "UPDATE medicine SET brand = '"
                +addMedicines_brand.getText()+"', productName = '"
                +addMedicines_productName.getText()+"', type = '"
                +addMedicines_type.getSelectionModel().getSelectedItem()+"', status = '"
                +addMedicines_statut.getSelectionModel().getSelectedItem()+"', price = '"
                +addMedicines_price.getText()+"' WHERE medicine_id = '"
                +addMedicines_medicineID.getText()+"'";


        connect = database.connectDb();

        try {
            Alert alert;
            if (addMedicines_medicineID.getText().isEmpty()
                    || addMedicines_brand.getText().isEmpty()
                    || addMedicines_productName.getText().isEmpty()
                    || addMedicines_type.getSelectionModel().getSelectedItem() == null
                    || addMedicines_statut.getSelectionModel().getSelectedItem() == null
                    || addMedicines_price.getText().isEmpty()) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error message");
                alert.setHeaderText(null);
                alert.setContentText("Remplir toutes les cases vides!");
                alert.showAndWait();
            }else {
                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirmation message");
                alert.setHeaderText(null);
                alert.setContentText("Vous êtes sûr de vouloir modifier le medicament d'ID:" +addMedicines_medicineID.getText() + " ?");
                Optional<ButtonType> option = alert.showAndWait();

                if(option.get().equals(ButtonType.OK)){
                    statement = connect.createStatement();

                    statement.executeUpdate(sql);

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information message");
                    alert.setHeaderText(null);
                    alert.setContentText("Le medicament à été modifié avec succès!");
                    alert.showAndWait();

                    addMedicineShowListData();
                    addMedicineReset();

                }


            }

        }catch(Exception e){e.printStackTrace();}
    }


    public void addMedicineDelete(){
        String sql = "DELETE FROM medicine WHERE medicine_id = '"+addMedicines_medicineID.getText()+"'";
        connect = database.connectDb();
        try {

            Alert alert;
            if (addMedicines_medicineID.getText().isEmpty()
                    || addMedicines_brand.getText().isEmpty()
                    || addMedicines_productName.getText().isEmpty()
                    || addMedicines_type.getSelectionModel().getSelectedItem() == null
                    || addMedicines_statut.getSelectionModel().getSelectedItem() == null
                    || addMedicines_price.getText().isEmpty() ){
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error message");
                alert.setHeaderText(null);
                alert.setContentText("Remplir toutes les cases vides!");
                alert.showAndWait();
            }else {
                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirmation message");
                alert.setHeaderText(null);
                alert.setContentText("Vous êtes sûr de vouloir supprimer le medicament d'ID:" +addMedicines_medicineID.getText() + " ?");
                Optional<ButtonType> option = alert.showAndWait();

                if(option.get().equals(ButtonType.OK)){
                    statement = connect.createStatement();

                    statement.executeUpdate(sql);

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information message");
                    alert.setHeaderText(null);
                    alert.setContentText("Le medicament à été supprimé avec succès!");
                    alert.showAndWait();

                    addMedicineShowListData();
                    addMedicineReset();

                }


            }
        }catch (Exception e){e.printStackTrace();}
    }

    public void addMedicineReset(){
        addMedicines_medicineID.setText("");
        addMedicines_brand.setText("");
        addMedicines_productName.setText("");
        addMedicines_price.setText("");
        addMedicines_type.getSelectionModel().clearSelection();
        addMedicines_statut.getSelectionModel().clearSelection();


    }

    private String[] addMedicineListT = {"Antibiotiques", "Antalgiques", "Antihistaminiques", "Antidépresseurs", "Anti-inflammatoires"};
    public void addMedicineListType(){
        List<String> listT = new ArrayList<>();

        for (String data: addMedicineListT){
            listT.add(data);
        }

        ObservableList listData = FXCollections.observableArrayList(listT);

        addMedicines_type.setItems(listData);
    }

    private String[] addMedicineStatus = {"Disponible", "Non Diponible", "Retiré du Stock"};
    public void addMedicineListStatus(){
        List<String> listS = new ArrayList<>();

        for (String data: addMedicineStatus){
            listS.add(data);
        }

        ObservableList listData = FXCollections.observableArrayList(listS);
        addMedicines_statut.setItems(listData);

    }

    public ObservableList<medicineData> addMedicinesListData(){

        String sql = "SELECT * FROM medicine";

        ObservableList<medicineData> listData = FXCollections.observableArrayList();

        connect = database.connectDb();

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            medicineData medData;

            while(result.next()){
                medData = new medicineData(result.getInt("medicine_id"), result.getString("brand")
                        , result.getString("productName"), result.getString("type")
                        , result.getString("status"), result.getDouble("price")
                        , result.getDate("date"));

                listData.add(medData);
            }

        }catch (Exception e){e.printStackTrace();}
        return listData;
    }


    private ObservableList<medicineData> addMedicineList;
    public void addMedicineShowListData(){
        addMedicineList = addMedicinesListData ();

        addMedicines_col_medicineid.setCellValueFactory(new PropertyValueFactory<>("medicineId"));
        addMedicines_col_brand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        addMedicines_col_productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        addMedicines_col_type.setCellValueFactory(new PropertyValueFactory<>("type"));
        addMedicines_col_statut.setCellValueFactory(new PropertyValueFactory<>("status"));
        addMedicines_col_price.setCellValueFactory(new PropertyValueFactory<>("price"));
        addMedicines_col_date.setCellValueFactory(new PropertyValueFactory<>("date"));

        addMedicines_tableView.setItems(addMedicineList);
    }


    public void clear() {
        connect =database.connectDb();
        String sql ="TRUNCATE TABLE medicine";
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setContentText("Vous êtes sûr de vouloir vider la table? ?");
        Optional<ButtonType> option = alert.showAndWait();
        if(option.get().equals(ButtonType.OK)) {
            try {


                prepare =connect.prepareStatement(sql);
                prepare.executeUpdate();
                addMedicines_tableView.getItems().clear();
                alert = new Alert(AlertType.INFORMATION);

                alert.setHeaderText(null);
                alert.setContentText("La table a été vidée avec succès!");
                alert.showAndWait();



            }catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void addMedicineSearch() {
        addMedicines_search.textProperty().addListener((Observable, oldValue, newValue) -> {
            addMedicines_tableView.setItems(filterMedicineData(newValue));
        });
    }

    private FilteredList<medicineData> filterMedicineData(String searchKey) {
        if (searchKey == null || searchKey.isEmpty()) {
            return new FilteredList<>(addMedicineList, e -> true);
        }

        String lowerCaseSearchKey = searchKey.toLowerCase();

        return addMedicineList.filtered(predicateMedicineData ->
                predicateMedicineData.getMedicineId().toString().contains(lowerCaseSearchKey)
                        || predicateMedicineData.getBrand().toLowerCase().contains(lowerCaseSearchKey)
                        || predicateMedicineData.getProductName().toLowerCase().contains(lowerCaseSearchKey)
                        || predicateMedicineData.getType().toLowerCase().contains(lowerCaseSearchKey)
                        || predicateMedicineData.getStatus().toLowerCase().contains(lowerCaseSearchKey)
                        || predicateMedicineData.getPrice().toString().contains(lowerCaseSearchKey)
                        || predicateMedicineData.getDate().toString().contains(lowerCaseSearchKey)
        );
    }

    private void setupTableView() {
        SortedList<medicineData> sortList = new SortedList<>(addMedicines_tableView.getItems());
        sortList.comparatorProperty().bind(addMedicines_tableView.comparatorProperty());
        addMedicines_tableView.setItems(sortList);
    }


    public void addMedicineSelect(){

        medicineData medData = addMedicines_tableView.getSelectionModel().getSelectedItem();
        int num = addMedicines_tableView.getSelectionModel().getSelectedIndex();

        if ((num -1) < -1){return; }

        addMedicines_medicineID.setText(String.valueOf(medData.getMedicineId()));
        addMedicines_brand.setText(medData.getBrand());
        addMedicines_productName.setText(medData.getProductName());
        addMedicines_price.setText(String.valueOf(medData.getPrice()));

        addMedicines_type.getSelectionModel().select(medData.getType());
        addMedicines_statut.getSelectionModel().select(medData.getStatus());

    }

    private double totalP;
    public void purchaseAdd() throws SQLException {

        int quantityValue = purchase_quantity.getValue();
        if (quantityValue <= 0) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("La quantité doit être supérieure strictement à zéro");
            alert.showAndWait();
            return; // Arrêter l'exécution de la méthode si la quantité est inférieure ou égale à zéro
        }

        connect = database.connectDb();
        purchaseCustomerId();



        String sql = "INSERT INTO customer (customer_id,type, medicine_id, brand, productName, quantity, price, date) "
                +"VALUES(?,?,?,?,?,?,?,?)";



        String retrieveDataQuery = "SELECT * FROM customer";
        PreparedStatement retrieveDataStatement = connect.prepareStatement(retrieveDataQuery);
        ResultSet resultSet = retrieveDataStatement.executeQuery();

// Effacer les anciennes données du modèle de données du tableau
        purchase_tableView.getItems().clear();

// Parcourir les résultats et ajouter chaque entrée dans le modèle de données du tableau
        while (resultSet.next()) {
            Integer customerId = resultSet.getInt("customer_id");
            String type = resultSet.getString("type");
            Integer medicineId = resultSet.getInt("medicine_id");
            String brand = resultSet.getString("brand");
            String productName = resultSet.getString("productName");
            Integer quantity = resultSet.getInt("quantity");
            Double price = resultSet.getDouble("price");
            Date date = resultSet.getDate("date");

            // Créer un objet CustomerData avec les valeurs récupérées
            customerData customerData = new customerData(customerId, type, medicineId, brand, productName, quantity, price, (java.sql.Date) date);

            // Ajouter l'objet au modèle de données du tableau
            purchase_tableView.getItems().add(customerData);
        }

        try {
            Alert alert;
            if (purchase_type.getSelectionModel().getSelectedItem() == null
                    || purchase_medicineID.getSelectionModel().getSelectedItem() == null
                    || purchase_brand.getSelectionModel().getSelectedItem() == null
                    || purchase_productName.getSelectionModel().getSelectedItem() == null){
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Remplir Toutes Les Cases Vides");
                alert.showAndWait();
            }else {

                prepare = connect.prepareStatement(sql);
                prepare.setString(1, String.valueOf(customerId));
                prepare.setString(2, (String) purchase_type.getSelectionModel().getSelectedItem());
                prepare.setString(3, (String) purchase_medicineID.getSelectionModel().getSelectedItem());
                prepare.setString(4, (String) purchase_brand.getSelectionModel().getSelectedItem());
                prepare.setString(5, (String) purchase_productName.getSelectionModel().getSelectedItem());
                prepare.setString(6, String.valueOf(qty));

                String checkData = "SELECT price FROM medicine WHERE medicine_id = '"
                        +purchase_medicineID.getSelectionModel().getSelectedItem()+"'";

                statement = connect.createStatement();
                result = statement.executeQuery(checkData);
                double priceD = 0;
                if (result.next()){
                    priceD = result.getDouble("price");
                }


                totalP = (priceD * qty);

                prepare.setString(7, String.valueOf(totalP));

                Date date = new Date();
                java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                prepare.setString(8, String.valueOf(sqlDate));

                prepare.executeUpdate();

                purchaseShowListData();
                purchaseDisplayTotal();

            }
            prepare = connect.prepareStatement(sql);
        }catch(Exception e){e.printStackTrace();}
    }

    private SpinnerValueFactory<Integer> spinner;
    public void purchaseShowValue(){
        spinner  = new  SpinnerValueFactory.IntegerSpinnerValueFactory(0, 20, 0);
        purchase_quantity.setValueFactory(spinner);
    }

    private int qty;
    public void purchaseQuantity(){
        qty = purchase_quantity.getValue();
    }

    public ObservableList<customerData> purchaseListData(){
        connect = database.connectDb();
        purchaseCustomerId();
        String sql = "SELECT * FROM customer WHERE customer_id = '"+customerId+"'";

        ObservableList<customerData> listData = FXCollections.observableArrayList();


        try {
            customerData customerD;
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()){
                customerD = new customerData(result.getInt("customer_id")
                        , result.getString("type"), result.getInt("medicine_id")
                        , result.getString("brand"), result.getString("productName")
                        , result.getInt("quantity"), result.getDouble("price")
                        , result.getDate("date"));
                listData.add(customerD);

            }

        }catch(Exception e){e.printStackTrace();}
        return listData;
    }

    private ObservableList<customerData> purchaseList;
    public void purchaseShowListData(){
        purchaseList = purchaseListData();

        purchase_co_medicineid.setCellValueFactory(new PropertyValueFactory<>("medicineId"));
        purchase_co_brand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        purchase_co_productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        purchase_co_type.setCellValueFactory(new PropertyValueFactory<>("type"));
        purchase_co_qty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        purchase_co_price.setCellValueFactory(new PropertyValueFactory<>("price"));

        purchase_tableView.setItems(purchaseList);

    }
    private int customerId;
    public void purchaseCustomerId(){
        connect = database.connectDb();
        String sql = "SELECT customer_id FROM customer";


        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()){
                customerId = result.getInt("customer_id");
            }
            int cID = 0;
            String checkData = "SELECT customer_id FROM customer_info";

            statement = connect.createStatement();
            result = statement.executeQuery(checkData);

            while (result.next()){
                cID = result.getInt("customer_id");
            }

            if (customerId == 0){
                customerId+=1;
            }else if(cID == customerId){
                customerId=cID+1;
            }


        }catch(Exception e){e.printStackTrace();}

    }

    
    
    public void purchaseMedicineId(){
        connect = database.connectDb();
        String sql  = "SELECT * FROM medicine WHERE type = '"
                +purchase_type.getSelectionModel().getSelectedItem()+"'";


        try {
            ObservableList listData = FXCollections.observableArrayList();

            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()){
                listData.add(result.getString("medicine_id"));
            }
            purchase_medicineID.setItems(listData);

            purchaseBrand();
        }catch (Exception e){e.printStackTrace();}
    }
 
    public void purchaseBrand(){
        connect = database.connectDb();
        String sql = "SELECT * FROM medicine WHERE medicine_id = '"
                +purchase_medicineID.getSelectionModel().getSelectedItem()+"'";


        try {
            ObservableList listData = FXCollections.observableArrayList();

            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()){
                listData.add(result.getString("brand"));
            }
            purchase_brand.setItems(listData);
            purchaseProductName();



        }catch (Exception e) {e.printStackTrace();}

    }
    
    
    public void purchaseProductName(){
        connect = database.connectDb();
        String sql = "SELECT * FROM medicine WHERE brand = '"
                +purchase_brand.getSelectionModel().getSelectedItem()+"'";


        try {
            ObservableList listData = FXCollections.observableArrayList();

            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()){
                listData.add(result.getString("productName"));
            }
            purchase_productName.setItems(listData);
        }catch (Exception e) {e.printStackTrace();}
    }
    
    public void purchaseType(){

        connect= database.connectDb();
        String sql = "SELECT type FROM medicine WHERE status = 'Disponible'";


        try {
            ObservableList listData = FXCollections.observableArrayList();
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()){
                listData.add(result.getString("type"));
            }
            purchase_type.setItems(listData);

            purchaseMedicineId();

        }catch(Exception e){e.printStackTrace();}
    }

    private double totalPriceD;
    public void purchaseDisplayTotal() {
    	connect =database.connectDb();
    	String sql="SELECT SUM(price) FROM customer WHERE customer_id = '"+customerId+"'";
    	try {
    		prepare =connect.prepareStatement(sql);
    		result =prepare.executeQuery();
    		if(result.next()) {
    			totalPriceD =result.getDouble("SUM(price)");
    		}
    		purchase_total.setText(String.valueOf(totalPriceD)+ "DH");
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    }

    private double balance;
    private double amount;
    public void purchaseAmount() {
        purchase_amount.setOnKeyTyped(event -> {
            if (isValidAmount()) {
                amount = Double.parseDouble(purchase_amount.getText());
                if (totalPriceD > amount) {
                    purchase_balance.setText("");
                } else {
                    balance = amount - totalPriceD;
                    purchase_balance.setText(String.valueOf(balance) + " DH");
                }
            } else {
                purchase_balance.setText("");
            }
        });
    }

    private boolean isValidAmount() {
        String amountText = purchase_amount.getText();
        if (amountText.isEmpty()) {
            return false;
        }

        try {
            double value = Double.parseDouble(amountText);
            return value >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }




    public void purchasePay() {
        connect = database.connectDb();
        String sql = "INSERT INTO customer_info (customer_id, total, date) VALUES (?, ?, ?)";
        try {
            Alert alert;
            if (totalPriceD == 0) {
                alert = new Alert(AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Erreur : Le total est nul");
                alert.showAndWait();
            } else if (totalPriceD > amount) {
                alert = new Alert(AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Erreur : Le total est supérieur au montant");
                alert.showAndWait();
            } else {
                alert = new Alert(AlertType.CONFIRMATION);
                alert.setHeaderText(null);
                alert.setContentText("Êtes-vous sûr ?");
                Optional<ButtonType> option = alert.showAndWait();
                if (option.get().equals(ButtonType.OK)) {
                    prepare = connect.prepareStatement(sql);
                    prepare.setString(1, String.valueOf(customerId));
                    prepare.setString(2, String.valueOf(totalPriceD));
                    Date date = new Date();
                    java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                    prepare.setString(3, String.valueOf(sqlDate));
                    prepare.executeUpdate();
                    alert = new Alert(AlertType.INFORMATION);
                    alert.setHeaderText(null);
                    alert.setContentText("Paiement effectué avec succès");
                    alert.showAndWait();
                    purchase_amount.setText("");
                    purchase_balance.setText("0.0 DH");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }







    public void switchForm(ActionEvent event){

        if (event.getSource() == dashboard_btn){
            dashboard_form.setVisible(true);
            addMedicines_form.setVisible(false);
            purchase_form.setVisible(false);
            home_graph();
            homeM();
            home_income();
            home_TotCustomers();

        }else if (event.getSource() == addMed_btn){
            dashboard_form.setVisible(false);
            addMedicines_form.setVisible(true);
            purchase_form.setVisible(false);

            addMedicineShowListData();
            addMedicineListStatus();
            addMedicineListType();
            addMedicineSearch();

        }else if (event.getSource() == purchase_btn){
            dashboard_form.setVisible(false);
            addMedicines_form.setVisible(false);
            purchase_form.setVisible(true);
            
          //  database.connectDb();
            purchaseType();
            purchaseMedicineId();
            purchaseBrand();
            purchaseProductName();
            purchaseShowListData();
            purchaseShowValue();
            purchaseDisplayTotal();
          
            
        }

    }

    public void displayUsername(){
        String user = getData.username;

        username_para.setText(user.substring(0, 1).toUpperCase() + user.substring(1));

    }

    private double x = 0;
    private double y = 0;
    public  void logout(){

        try {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Message");
            alert.setHeaderText(null);
            alert.setContentText("Vous êtes sûr de se déconnecter?");
            Optional<ButtonType> option = alert.showAndWait();

            if (option.get().equals(ButtonType.OK)){
                logout_btn.getScene().getWindow().hide();
                Parent root = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(root);

                root.setOnMousePressed((MouseEvent event) ->{
                    x = event.getSceneX();
                    y = event.getSceneY();
                });
                root.setOnMouseDragged((MouseEvent event) ->{
                    stage.setX(event.getScreenX() -x);
                    stage.setY(event.getScreenY() -y);
                    stage.setOpacity(.8);
                });

                root.setOnMouseReleased((MouseEvent event) ->{
                    stage.setOpacity(1);
                });

                stage.initStyle(StageStyle.TRANSPARENT);

                stage.setScene(scene);
                stage.show();
            }
        }catch (Exception e){e.printStackTrace();}

    }

    public void minimize(){
        Stage stage = (Stage) main_form.getScene().getWindow();
        stage.setIconified(true);
    }

    public void close(){
        System.exit(0);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        displayUsername();
        
        
        homeM();
        home_income();
        home_TotCustomers();
        home_graph();
     
        addMedicineShowListData();
        addMedicineListStatus();
        addMedicineListType();


        purchaseType();
        purchaseMedicineId();
        purchaseBrand();
        purchaseProductName();
        purchaseShowListData();
        purchaseShowValue();
        purchaseDisplayTotal();

    }
}
