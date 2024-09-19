package com.jeanespin.flightsfx.utils;

import com.jeanespin.flightsfx.model.Flight;

import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class FileUtils {
    public static List<Flight> loadFlights() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");

        try {

            return Files.lines(Paths.get("flights.txt"))
                    .map(line -> new Flight(
                            line.split(";")[0],
                            line.split(";")[1],
                            LocalDateTime.parse(line.split(";")[2],dateTimeFormatter),
                            LocalTime.parse(line.split(";")[3],timeFormatter)))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void saveFlights(List<Flight> vuelos) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");

        try (PrintWriter pw = new PrintWriter("flights.txt")) {
            vuelos.stream().forEach(v -> pw.println(v.getNumVuelo() + ";" +
                    v.getDestino() + ";" +
                    v.getSalida().format(dateTimeFormatter) + ";" +
                    v.getDuracion().format(timeFormatter) + ";"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
