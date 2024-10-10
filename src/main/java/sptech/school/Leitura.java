package school.sptech;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Leitura {

    private Integer idLeitura;
    private Integer data;
    private Integer hora;
    private Integer direcaoVento;
    private Double rajadaMax;
    private Double velocidadeHoraria;
    private String cidade;
    private String estado;
    private LocalDateTime creatAt;

//    public Leitura(){
//
//    }

    public Leitura(Integer idLeitura , Integer data, Integer hora, Integer direcaoVento, Double rajadaMax, Double velocidadeHoraria, String cidade, String estado, LocalDateTime creatAt) {
        this.idLeitura = idLeitura;
        this.data = data;
        this.hora = hora;
        this.direcaoVento = direcaoVento;
        this.rajadaMax = rajadaMax;
        this.velocidadeHoraria = velocidadeHoraria;
        this.cidade = cidade;
        this.estado = estado;
        this.creatAt = creatAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getData() {
        return data;
    }

    public void setData(Integer data) {
        this.data = data;
    }

    public Integer getHora() {
        return hora;
    }

    public void setHora(Integer hora) {
        this.hora = hora;
    }

    public Integer getDirecaoVento() {
        return direcaoVento;
    }

    public void setDirecaoVento(Integer direcaoVento) {
        this.direcaoVento = direcaoVento;
    }

    public Double getRajadaMax() {
        return rajadaMax;
    }

    public void setRajadaMax(Double rajadaMax) {
        this.rajadaMax = rajadaMax;
    }

    public Double getVelocidadeHoraria() {
        return velocidadeHoraria;
    }

    public void setVelocidadeHoraria(Double velocidadeHoraria) {
        this.velocidadeHoraria = velocidadeHoraria;
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

    public LocalDateTime getCreatAt() {
        return creatAt;
    }

    public void setCreatAt(LocalDateTime creatAt) {
        this.creatAt = creatAt;
    }

    @Override
    public String toString() {
        return "Leitura{" +
                "id=" + id +
                ", data=" + data +
                ", hora=" + hora +
                ", direcaoVento=" + direcaoVento +
                ", rajadaMax=" + rajadaMax +
                ", velocidadeHoraria=" + velocidadeHoraria +
                ", cidade='" + cidade + '\'' +
                ", estado='" + estado + '\'' +
                ", creatAt=" + creatAt +
                '}';
    }
}
