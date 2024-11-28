import java.time.LocalDate;

public class BaseClima {

    private LocalDate data;
    private String hora;
    private Double latitude;
    private Double longitude;
    private Integer direcaoVento;
    private Double ventoRajada;
    private Double ventoVelocidade;
    private String estado;
    private String municipio;

    public BaseClima() {
    }

    public BaseClima(Double latitude, Double longitude,String municipio , String estado, LocalDate data, String hora, Integer direcaoVento, Double ventoRajada, Double ventoVelocidade) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.data = data;
        this.hora = hora;
        this.direcaoVento = direcaoVento;
        this.ventoRajada = ventoRajada;
        this.ventoVelocidade = ventoVelocidade;
        this.municipio = municipio;
        this.estado = estado;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String Municipio) {
        this.municipio = Municipio;
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

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
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
                " Municipio='" + municipio + '\'' +
                ", estado='" + estado + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
