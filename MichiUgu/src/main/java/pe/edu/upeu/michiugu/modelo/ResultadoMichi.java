package pe.edu.upeu.michiugu.modelo;

public class ResultadoMichi {
    private String nombrePartida;
    private String nombreJugador1;
    private String nombreJugador2;
    private String ganador;
    private int punto;
    private String estado;

    public ResultadoMichi(String nombrePartida, String nombreJugador1, String nombreJugador2) {
        this.nombrePartida = nombrePartida;
        this.nombreJugador1 = nombreJugador1;
        this.nombreJugador2 = nombreJugador2;
        this.ganador = "";
        this.punto = 0;
        this.estado = "Jugando";
    }

    // Getters y setters
    public String getNombrePartida() {
        return nombrePartida;
    }
    public String getNombreJugador1() {
        return nombreJugador1;
    }
    public String getNombreJugador2() {
        return nombreJugador2;
    }
    public String getGanador() {
        return ganador;
    }
    public int getPunto() {
        return punto;
    }
    public String getEstado() {
        return estado;
    }

    public void setGanador(String ganador) {
        this.ganador = ganador;
    }
    public void setPunto(int punto) {
        this.punto = punto;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
}
