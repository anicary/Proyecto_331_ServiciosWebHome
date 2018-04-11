package mx.edu.ittepic.proyecto_331_serviciosweb.Clases;

public class Alumno {
    public String getIdalumno() {
        return idalumno;
    }

    public void setIdalumno(String idalumno) {
        this.idalumno = idalumno;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    String idalumno,nombre,direccion;

    public Alumno(String idalumno, String nombre, String direccion) {
        this.idalumno = idalumno;
        this.nombre = nombre;
        this.direccion = direccion;
    }
}
