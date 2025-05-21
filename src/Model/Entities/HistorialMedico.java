package Model.Entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HistorialMedico {

    private String identificacionPreso;
    private List<RegistroMedico> registros;

    public HistorialMedico(String identificacionPreso) {
        this.identificacionPreso = identificacionPreso;
        this.registros = new ArrayList<>();
    }

    public void agregarRegistro(String diagnostico, LocalDateTime fecha, String enfermera, String motivo) {
        registros.add(new RegistroMedico(diagnostico, fecha, enfermera, motivo));
    }

    public List<RegistroMedico> getRegistros() {
        return new ArrayList<>(registros);
    }

    public String getIdentificacionPreso() {
        return identificacionPreso;
    }

    public static class RegistroMedico {

        private String diagnostico;
        private LocalDateTime fecha;
        private String enfermera;
        private String motivo;

        public RegistroMedico(String diagnostico, LocalDateTime fecha, String enfermera, String motivo) {
            this.diagnostico = diagnostico;
            this.fecha = fecha;
            this.enfermera = enfermera;
            this.motivo = motivo;
        }

        public String getDiagnostico() {
            return diagnostico;
        }

        public LocalDateTime getFecha() {
            return fecha;
        }

        public String getEnfermera() {
            return enfermera;
        }

        public String getMotivo() {
            return motivo;
        }
    }
}
