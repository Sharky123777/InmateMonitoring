
package Model.Entities;

import java.time.LocalDate;
import java.time.Period;

 public class Sentencia {
    private int años;
    private int meses;
    private LocalDate fechaIngreso;
    private LocalDate fechaSalidaCalculada;

    public Sentencia(int años, int meses, LocalDate fechaIngreso) {
        if (años < 0 || meses < 0) {
            throw new IllegalArgumentException("Los años y meses no pueden ser negativos");
        }
        if (fechaIngreso == null) {
            throw new IllegalArgumentException("La fecha de ingreso no puede ser nula");
        }
        this.años = años;
        this.meses = meses;
        this.fechaIngreso = fechaIngreso;
        calcularFechaSalida();
    }
    
   public void sumarSentencia(Sentencia otra) {
    this.años += otra.getAños();
    this.meses += otra.getMeses();

    this.años += this.meses / 12;
    this.meses = this.meses % 12;

    calcularFechaSalida();
}


    private void calcularFechaSalida() {
        Period periodo = Period.of(años, meses, 0);
        this.fechaSalidaCalculada = fechaIngreso.plus(periodo);
    }

    public int getAños() { return años; }
    public int getMeses() { return meses; }
    public LocalDate getFechaIngreso() { return fechaIngreso; }
    public LocalDate getFechaSalidaCalculada()
    { return fechaSalidaCalculada; }

    public String getSentenciaFormateada() {
        StringBuilder sb = new StringBuilder();
        if (años > 0) sb.append(años).append(años == 1 ? " año " : " años ");
        if (meses > 0) sb.append(meses).append(meses == 1 ? " mes" : " meses");
        return sb.toString().trim();
    }

    public String getFormatoAlmacenamiento() {
        return años + "|" + meses + "|" + fechaIngreso.toString();
    }

    public static Sentencia fromString(String str) {
        String[] partes = str.split("\\|");
        int años = Integer.parseInt(partes[0]);
        int meses = Integer.parseInt(partes[1]);
        LocalDate fechaIngreso = LocalDate.parse(partes[2]);
        return new Sentencia(años, meses, fechaIngreso);
    }
}
