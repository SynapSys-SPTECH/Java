import java.io.IOException;
import java.io.InputStream;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class LeitorExcel {

    public List<BaseClima> extrairClima(String nomeArquivo, InputStream arquivo) {

        Logger log = Logger.getLogger(Main.class.getName());
        List<BaseClima> listaClima = new ArrayList<>();
            // Utiliza try-with-resources para garantir o fechamento correto do Workbook
            try (Workbook workbook = nomeArquivo.endsWith(".xlsx") ? new XSSFWorkbook(arquivo) : new HSSFWorkbook(arquivo)) {
                log.info("Iniciando leitura do arquivo %s".formatted(nomeArquivo));

                Sheet sheet = workbook.getSheetAt(0);

                int j = 0;

                String Municipio = "";

                String Estado = "";

                Double latitude = 0.0;

                Double longitude = 0.0;


                log.info("iniciando a leitura da primeira parte do arquivo");

                // Itera sobre as linhas da planilha
                int ultimaLinhaTb1 = 0;
                for (Row row : sheet) {
                    ultimaLinhaTb1 = row.getRowNum();
                  //  System.out.println("Lendo linha " + row.getRowNum());
                    BaseClima lido = new BaseClima();

                    // Controla a leitura das linhas 0 e 2
                    switch (j) {
                        case 1:
                            // Verifica se a célula existe e se não é nula
                            if (row.getCell(1) != null) {
                                lido.setEstado(row.getCell(1).getStringCellValue());
                                Estado = lido.getEstado();
                            } else {
                                log.warning("Célula de Estado Sigla vazia na linha 0.");
                            }

                            break;
                        case 2:
                            // Verifica se a célula existe e se não é nula
                            if (row.getCell(1) != null) {
                                lido.setMunicipio(row.getCell(1).getStringCellValue());
                                Municipio = lido.getMunicipio();
                            } else {
                                log.warning("Célula de Municipio vazia na linha 2.");
                            }

                            break;
                        case 4:
                            if (row.getCell(1) != null) {
                                lido.setLatitude(row.getCell(1).getNumericCellValue());
                                latitude = lido.getLatitude();
                            } else {
                                log.warning("Célula de Latitude vazia na linha 2.");
                            }break;
                        case 5:
                            if (row.getCell(1) != null) {
                                lido.setLongitude(row.getCell(1).getNumericCellValue());
                                longitude = lido.getLongitude();
                            } else {
                                log.warning("Célula de Longitude vazia na linha 2.");
                            }break;

                        default:
                            break;
                    }

                    // Para a leitura na linha 7
                    if (row.getRowNum() == 7) {
                        break;
                    }
                    j++;
                }
                
                log.info("Linhas lidas:" + ultimaLinhaTb1);

                log.info("Iniciando a leitura da segunda parte do arquivo");

                // Iterando sobre as linhas da planilha
                int cont = 0;
                int ultimaLinhaTb2 = 0;
                for (Row row : sheet) {

                    // Lê o cabeçalho na linha 8
                    if (cont > 8) {
                        if (row.getRowNum() == 8) {
                            log.info("\nLendo dados da tabela");
                            for (int i = 0; i < 19; i++) {
                                if (row.getCell(i) != null) {
                                    String coluna = row.getCell(i).getStringCellValue();
                                }
                            }
                            continue;
                        }
                        ultimaLinhaTb2 = row.getRowNum();
                        // Extraindo valor das células e criando o objeto BaseClima
                        BaseClima clima = new BaseClima();

                        if (row.getCell(0) != null) {
                            LocalDate data = converterDate(row.getCell(0).getDateCellValue());
                            clima.setData(data);
                        }

                        if (row.getCell(1) != null) {
                            String hora = TimeFormatter(row.getCell(1).getStringCellValue());
                            clima.setHora(hora);
                        }

                        if (row.getCell(16) != null) {
                            clima.setDirecaoVento((int) row.getCell(16).getNumericCellValue());
//                            System.out.println(clima.getDirecaoVento());
                        }

                        if (row.getCell(17) != null) {
                            clima.setVentoVelocidade(row.getCell(17).getNumericCellValue());
                        }

                        if (row.getCell(18) != null) {
                            clima.setVentoRajada(row.getCell(18).getNumericCellValue());
                        }

                        clima.setLatitude(latitude);
                        clima.setLongitude(longitude);
                        clima.setMunicipio(Municipio);
                        clima.setEstado(Estado);

                        if (clima.getVentoRajada() != null && clima.getVentoVelocidade() != null && clima.getDirecaoVento() != null) {
                            listaClima.add(clima);
                        }

                    } else {
                        cont++;
                    }
                }

                log.info("Leitura do arquivo finalizada");
                log.info("linhas Lidas:" + ultimaLinhaTb2 + "\n");
                return listaClima;

            } catch (IOException e) {
                // Caso ocorra algum erro durante a leitura do arquivo uma exceção será lançada
                log.warning("Erro na leitura do arquivo:" + e.getMessage());
                throw new RuntimeException(e);
            }

    }

    private LocalDate converterDate(Date data) {
        return data.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
    private LocalTime converterHora(Time time){
        return time.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
    }
    private String TimeFormatter(String time) {
        String timeStr = time;
        String timeMysql = timeStr.substring(0, 2) + ":" + timeStr.substring(2, 4) + ":00";
        return timeMysql;
    }
}
