package com.laplacitacolombiana.springboot.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProveedor;

    @NotBlank(message = "La razón social es obligatorio")
    @Size(min = 2, max = 100, message = "La razón social debe tener entre 2 y 100 caracteres")
    private String razonSocial;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombreProveedor;

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(
            regexp = "^[+\\d][\\d\\s\\-()]{6,19}$",
            message = "El teléfono contiene un formato inválido")
    private String telefonoProveedor;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Formato de email inválido")
    private String emailProveedor;

    @NotBlank(message = "La dirección es obligatoria")
    @Size(max = 150, message = "La dirección no debe exceder 150 caracteres")
    private String direccionProveedor;

    public Long getId() {
        return idProveedor;
    }

    public void setId(Long idProveedor) {
        this.idProveedor = idProveedor;
    }


    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getNombreProveedor() {
        return nombreProveedor;
    }

    public void setNombreProveedor(String nombreProveedor) {
        this.nombreProveedor = nombreProveedor;
    }

    public String getTelefonoProveedor() {
        return telefonoProveedor;
    }

    public void setTelefonoProveedor(String telefonoProveedor) {
        this.telefonoProveedor = telefonoProveedor;
    }

    public String getEmailProveedor() {
        return emailProveedor;
    }

    public void setEmailProveedor(String emailProveedor) {
        this.emailProveedor = emailProveedor;
    }

    public String getDireccionProveedor() {
        return direccionProveedor;
    }

    public void setDireccionProveedor(String direccionProveedor) {
        this.direccionProveedor = direccionProveedor;
    }
}

