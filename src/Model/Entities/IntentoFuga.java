
package Model.Entities;

import java.time.LocalDate;

public class IntentoFuga {
    private String identificacionPreso;
    private LocalDate fechaFuga;
    private LocalDate fechaReingreso; // Puede ser null

    public IntentoFuga() {}

    public IntentoFuga(String identificacionPreso, LocalDate fechaFuga, LocalDate fechaReingreso) {
        this.identificacionPreso = identificacionPreso;
        this.fechaFuga = fechaFuga;
        this.fechaReingreso = fechaReingreso;
    }

    public String getIdentificacionPreso() { return identificacionPreso; }
    public LocalDate getFechaFuga() { return fechaFuga; }
    public LocalDate getFechaReingreso() { return fechaReingreso; }

    public void setFechaReingreso(LocalDate fechaReingreso) {
        this.fechaReingreso = fechaReingreso;
    }
}
