package Model;

public class Seccion {

    private int id;
    private char tipo;
    private int numeroDeCeldas;

    public Seccion(int id, char tipo, int numeroDeCeldas) {
        this.id = id;
        this.tipo = tipo;
        this.numeroDeCeldas = numeroDeCeldas;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public char getTipo() {
        return tipo;
    }

    public void setTipo(char tipo) {
        this.tipo = tipo;
    }

    public int getNumeroDeCeldas() {
        return numeroDeCeldas;
    }

    public void setNumeroDeCeldas(int numeroDeCeldas) {
        this.numeroDeCeldas = numeroDeCeldas;
    }
}
