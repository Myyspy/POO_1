package pe.edu.upeu.michiugu.control;

import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.*;
import javafx.fxml.FXML;
import org.springframework.stereotype.Controller;
import pe.edu.upeu.michiugu.modelo.ResultadoMichi;

@Controller
public class MichiUguControl {

    @FXML private TextField txtJugador1, txtJugador2, txtNombrePartida;
    @FXML private Button btnIniciar, btnAnular;
    @FXML private GridPane gridTablero;
    @FXML private TableView<ResultadoMichi> tablaPuntajes;
    @FXML private TableColumn<ResultadoMichi, Number> colNumero;
    @FXML private TableColumn<ResultadoMichi, String> colPartida, colJugador1, colJugador2, colGanador, colEstado;
    @FXML private TableColumn<ResultadoMichi, Integer> colPunto;
    @FXML private Label lblJugador1, lblJugador2, lblTurno;

    private Button[][] botones;
    private List<ResultadoMichi> partidas = new ArrayList<>();
    private ResultadoMichi partidaActual;
    private boolean juegoActivo = false;
    private String turno = "X";

    @FXML
    public void initialize() {
        botones = new Button[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Button btn = (Button) gridTablero.getChildren().get(i * 3 + j);
                int fila = i, columna = j;
                btn.setOnAction(e -> manejarJugada(btn, fila, columna));
                botones[i][j] = btn;
                btn.setDisable(true);
            }
        }

        colNumero.setCellValueFactory(cellData -> new ReadOnlyIntegerWrapper(partidas.indexOf(cellData.getValue()) + 1));
        colPartida.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNombrePartida()));
        colJugador1.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNombreJugador1()));
        colJugador2.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNombreJugador2()));
        colGanador.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getGanador()));
        colEstado.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEstado()));
        colPunto.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getPunto()).asObject());
    }

    @FXML
    private void iniciarPartida() {
        limpiarTablero();

        String jugador1 = txtJugador1.getText().trim();
        String jugador2 = txtJugador2.getText().trim();
        String nombrePartida = txtNombrePartida.getText().trim();

        if (jugador1.isEmpty() || jugador2.isEmpty() || nombrePartida.isEmpty()) {
            mostrarAlerta("Completa todos los campos antes de iniciar.");
            return;
        }

        partidaActual = new ResultadoMichi(nombrePartida, jugador1, jugador2);
        partidas.add(partidaActual);

        lblJugador1.setText(jugador1);
        lblJugador2.setText(jugador2);
        lblTurno.setText("Turno de: " + jugador1 + " (X)");

        actualizarTabla();
        activarTablero();
        btnIniciar.setDisable(true);
        btnAnular.setDisable(false);
        turno = "X";
        juegoActivo = true;
    }

    @FXML
    private void anularPartida() {
        if (partidaActual != null) {
            partidaActual.setEstado("Anulado");
            partidaActual.setGanador("");
            partidaActual.setPunto(0);
        }
        juegoActivo = false;
        desactivarTablero();
        btnIniciar.setDisable(false);
        lblTurno.setText("Partida Anulada");
        actualizarTabla();
    }

    private void manejarJugada(Button boton, int fila, int col) {
        if (!juegoActivo || !boton.getText().isEmpty()) return;

        boton.setText(turno);
        if (verificarGanador()) {
            String ganador = turno.equals("X") ? partidaActual.getNombreJugador1() : partidaActual.getNombreJugador2();
            partidaActual.setGanador(ganador);
            partidaActual.setPunto(1);
            partidaActual.setEstado("Terminado");
            juegoActivo = false;
            desactivarTablero();
            btnIniciar.setDisable(false);
            lblTurno.setText("¡Ganó " + ganador + "!");
        } else if (empate()) {
            partidaActual.setGanador("Empate");
            partidaActual.setPunto(0);
            partidaActual.setEstado("Terminado");
            juegoActivo = false;
            desactivarTablero();
            btnIniciar.setDisable(false);
            lblTurno.setText("Empate.");
        } else {
            turno = turno.equals("X") ? "O" : "X";
            lblTurno.setText("Turno de: " + (turno.equals("X") ? partidaActual.getNombreJugador1() + " (X)" : partidaActual.getNombreJugador2() + " (O)"));
        }
        actualizarTabla();
    }

    private boolean verificarGanador() {
        for (int i = 0; i < 3; i++) {
            if (!botones[i][0].getText().isEmpty() &&
                botones[i][0].getText().equals(botones[i][1].getText()) &&
                botones[i][1].getText().equals(botones[i][2].getText())) return true;

            if (!botones[0][i].getText().isEmpty() &&
                botones[0][i].getText().equals(botones[1][i].getText()) &&
                botones[1][i].getText().equals(botones[2][i].getText())) return true;
        }

        if (!botones[0][0].getText().isEmpty() &&
            botones[0][0].getText().equals(botones[1][1].getText()) &&
            botones[1][1].getText().equals(botones[2][2].getText())) return true;

        if (!botones[0][2].getText().isEmpty() &&
            botones[0][2].getText().equals(botones[1][1].getText()) &&
            botones[1][1].getText().equals(botones[2][0].getText())) return true;

        return false;
    }

    private boolean empate() {
        for (Button[] fila : botones)
            for (Button btn : fila)
                if (btn.getText().isEmpty()) return false;
        return true;
    }

    private void desactivarTablero() {
        for (Button[] fila : botones)
            for (Button btn : fila) {
                btn.setDisable(true);
                btn.setText("");
            }
    }

    private void activarTablero() {
        for (Button[] fila : botones)
            for (Button btn : fila) {
                btn.setDisable(false);
                btn.setText("");
            }
    }

    private void limpiarTablero() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                botones[i][j].setText("");
                botones[i][j].setDisable(false);
            }
        }
    }

    private void actualizarTabla() {
        tablaPuntajes.getItems().setAll(partidas);
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Atención");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
