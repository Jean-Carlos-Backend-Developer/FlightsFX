package com.jeanespin.flightsfx;
import com.jeanespin.flightsfx.model.Flight;
import com.jeanespin.flightsfx.utils.FileUtils;
import com.jeanespin.flightsfx.utils.MessageUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

public class FXMLMainViewController {
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnFiltrar;
    @FXML
    private Button btnDelete;
    @FXML
    private TableView<Flight> tableFlights;
    @FXML
    private TableColumn<Flight, String> colNumVuelo;
    @FXML
    private TableColumn<Flight, String> colDestino;
    @FXML
    private TableColumn<Flight, LocalDateTime> colSalida;
    @FXML
    private TableColumn<Flight, LocalTime> colDuracion;
    @FXML
    private ChoiceBox<String> cbFiltros;
    ObservableList<Flight> vuelos;
    public List<Flight> getFlight() {
        return vuelos;
    }

    @FXML
    private TextField txtDestino;
    @FXML
    private TextField txtDuracion;
    @FXML
    private TextField txtNumVuelo;
    @FXML
    private TextField txtSalida;

    //Añadir vuelos ====================================================================================================
    @FXML
    void addFlight() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");

        if (txtDestino.getText().equals("") || txtDuracion.getText().equals("") ||
                txtSalida.getText().equals("") || txtNumVuelo.getText().equals("")) {
            MessageUtils.showError("Error añadiendo datos","Ninguna fila debe estar vacía.");
        } else {
            try {
                vuelos.add(
                        new Flight(
                                txtNumVuelo.getText(),
                                txtDestino.getText(),
                                LocalDateTime.parse(txtSalida.getText(),dateTimeFormatter),
                                LocalTime.parse(txtDuracion.getText(),timeFormatter)
                        )
                );
                FileUtils.saveFlights(vuelos);
                //Vaciar los campos
                txtNumVuelo.setText("");
                txtSalida.setText("");
                txtDuracion.setText("");
                txtDestino.setText("");
                MessageUtils.showMesage("Información","Vuelo añadido correctamente.");
            } catch (DateTimeParseException e) {
                MessageUtils.showError("Formato incorrecto","El formato de la fecha y/o la duración es incorrecto.");
            }
        }
    }

    //Borrar vuelos ====================================================================================================
    @FXML
    private void deleteFlight() {
        Flight selectedFlight = tableFlights.getSelectionModel().getSelectedItem();
        if (selectedFlight != null) {
            btnDelete.setDisable(false);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmación");
            alert.setHeaderText("Eliminar vuelo");
            alert.setContentText("¿Estás seguro de que quieres eliminar este vuelo?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                vuelos.remove(selectedFlight);
                FileUtils.saveFlights(vuelos);
            }
        }
        else {
            Alert dialog = new Alert(Alert.AlertType.ERROR);
            dialog.setTitle("Error");
            dialog.setHeaderText("Error borrando datos");
            dialog.setContentText("No hay ninguna fila seleccionada");
            dialog.showAndWait();
        }
    }

    //Añadir filtros ===================================================================================================
    @FXML
    void addFilter(ActionEvent event) {
        int selectedIndex = cbFiltros.getSelectionModel().getSelectedIndex();

        switch (selectedIndex) {
            case 0: //Ver todos los vuelos.
                tableFlights.setItems(vuelos);
                break;
            case 1: //Mostrar vuelos de la ciudad seleccionada.
                Flight vueloSeleccionado = tableFlights.getSelectionModel().getSelectedItem();
                if (vueloSeleccionado != null) {
                    String ciudadSeleccionada = String.valueOf(tableFlights.getSelectionModel().getSelectedItem().getDestino());
                    ObservableList<Flight> vuelosCiudad = vuelos.stream()
                            .filter(v -> v.getDestino().equals(ciudadSeleccionada))
                            .collect(Collectors.toCollection(FXCollections::observableArrayList));
                    tableFlights.setItems(vuelosCiudad);
                } else {
                    MessageUtils.showError("Fila sin seleccionar","Primero debes selecionar una fila de la tabla de vuelos.");
                }
                break;
            case 2: //Mostrar vuelos largos.
                ObservableList<Flight> vuelosLargos = vuelos.stream()
                        .filter(v -> v.getDuracion().toSecondOfDay() > 3 * 3600) //Filtrar los vuelos cuya duración sea mayor a 3 horas (3 * 3600 segundos).
                        .collect(Collectors.toCollection(FXCollections::observableArrayList));
                tableFlights.setItems(vuelosLargos);
                break;
            case 3: //Mostrar los siguientes 5 vuelos.
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                String fechaFiltrar = "10/10/2017 00:00"; //Fecha para filtrar vuelos con esa fecha específica
                LocalDateTime fechaFiltrarParseada = LocalDateTime.parse(fechaFiltrar,dateTimeFormatter);

                ObservableList<Flight> vuelosProximos = vuelos.stream()
                        .filter(v -> v.getSalida().isAfter(fechaFiltrarParseada)) //Filtrar los vuelos cuya fecha sea después de la fecha que le paso.
                        .limit(5)
                        .collect(Collectors.toCollection(FXCollections::observableArrayList));
                tableFlights.setItems(vuelosProximos);
                break;
            case 4: //Mostrar duración media de los vuelos.
                OptionalDouble media = vuelos.stream()
                        .mapToDouble(v -> v.getDuracion().toSecondOfDay())
                        .average();
                long duracionMediaSegundos = (long) media.orElse(0);
                Duration duracionMedia = Duration.ofSeconds(duracionMediaSegundos);
                long horas = duracionMedia.toHours();
                long minutos = duracionMedia.minusHours(horas).toMinutes();
                String mensaje = "La duración media de los vuelos es " + String.format("%02d:%02d", horas, minutos);
                MessageUtils.showMesage("Cálculo duración media de vuelos",mensaje);
                break;
        }
    }

    //Capturar evento cerrar ventana ===================================================================================
    public void setOnCloseListener(Stage stage) {
        stage.setOnCloseRequest(e -> {
            //Captura el evento de cerrar la ventana y guarda la lista de vuelos.
            FileUtils.saveFlights(vuelos);
        });
    }

    //Ir a vista gráfico ===============================================================================================
    @FXML
    void goToChart(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ChartView.fxml"));
        Parent view1 = loader.load();
        Scene view1Scene = new Scene(view1);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.hide();
        stage.setScene(view1Scene);
        stage.show();
    }

    //Initialize =======================================================================================================
    public void initialize() {
        //Añadir filtros al ChoiceBox
        cbFiltros.setItems(
                FXCollections.observableArrayList("Ver todos los vuelos.",
                        "Mostrar vuelos de la ciudad seleccionada.",
                        "Mostrar vuelos largos.",
                        "Mostrar los siguientes 5 vuelos.",
                        "Mostrar duración media de los vuelos.")
        );

        //Añadir vuelos a la TableView
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        vuelos = FXCollections.observableArrayList(FileUtils.loadFlights());
        colNumVuelo.setCellValueFactory(new PropertyValueFactory("numVuelo"));
        colDestino.setCellValueFactory(new PropertyValueFactory("destino"));
        colSalida.setCellValueFactory(new PropertyValueFactory("salida"));
        colDuracion.setCellValueFactory(new PropertyValueFactory("duracion"));

        //Formatear la columna "salida" tardé años en buscar como hacerlo xD
        colSalida.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(item.format(dateTimeFormatter));
                }
            }
        });

        tableFlights.setItems(vuelos);

        //Desactivar el boton delete
        btnDelete.setDisable(true);

        tableFlights.setOnMouseClicked(event -> {
            if (tableFlights.getSelectionModel().getSelectedItem() != null) {
                btnDelete.setDisable(false); //Activar el botón al seleccionar una fila
            }
        });

    }
}
