package com.jeanespin.flightsfx;

import com.jeanespin.flightsfx.model.Flight;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ChartViewController {
    @FXML
    private PieChart pieChart;

    @FXML
    void back(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("FXMLMainView.fxml"));
        Parent view1 = loader.load();
        Scene view1Scene = new Scene(view1);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.hide();
        stage.setScene(view1Scene);
        stage.show();
    }

    public void initialize() {
        //Obtener el controllador para obtener la lista de vuelos
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLMainView.fxml"));
        try {
            Parent root = (Parent) loader.load();
        } catch (Exception e) {}

        FXMLMainViewController controller = (FXMLMainViewController) loader.getController();

        List<Flight> vuelos = controller.getFlight();

        //Actualiza el gráfico con la lista de vuelos
        pieChart.getData().clear();

        // Obtenemos un mapa con el número de vuelos por destino
        Map<String, Long> result = vuelos.stream()
                .collect(Collectors.groupingBy(flight -> flight.getDestino(), Collectors.counting()));

        // Añadimos una entrada por cada destino
        result.forEach((destino, count) -> {
            pieChart.getData().add(new PieChart.Data(destino, count));
        });
    }
}
