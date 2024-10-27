import java.time.LocalDate;

public class BaseClima {

    private LocalDate data;
    private String hora;
    private Integer direcaoVento;
    private Double ventoRajada;
    private Double ventoVelocidade;
    private String cidade;
    private String estado;

    public BaseClima() {
    }

    public BaseClima(String cidade , String estado, LocalDate data, String hora, Integer direcaoVento, Double ventoRajada, Double ventoVelocidade) {
        this.data = data;
        this.hora = hora;
        this.direcaoVento = direcaoVento;
        this.ventoRajada = ventoRajada;
        this.ventoVelocidade = ventoVelocidade;
        this.cidade = cidade;
        this.estado = estado;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public Integer getDirecaoVento() {
        return direcaoVento;
    }

    public void setDirecaoVento(Integer direcaoVento) {
        this.direcaoVento = direcaoVento;
    }

    public Double getVentoRajada() {
        return ventoRajada;
    }

    public void setVentoRajada(Double ventoRajada) {
        this.ventoRajada = ventoRajada;
    }

    public Double getVentoVelocidade() {
        return ventoVelocidade;
    }

    public void setVentoVelocidade(Double ventoVelocidade) {
        this.ventoVelocidade = ventoVelocidade;
    }

    @Override
    public String toString() {
        return "\n\n" +
                "BaseClima{" +
                "direcaoVento='" + direcaoVento + '\'' +
                ", ventoRajada='" + ventoRajada + '\'' +
                ", ventoVelocidade='" + ventoVelocidade + '\'' +
                ", hora='" + hora + '\'' +
                ", data='" + data + '\'' +
                " cidade='" + cidade + '\'' +
                ", estado='" + estado + '\'' +
                '}';
    }
}
