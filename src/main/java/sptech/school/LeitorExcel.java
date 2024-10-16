package sptech.school;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class LeitorExcel {

    public List<BaseClima> extrairClima(String nomeArquivo, InputStream arquivo) {

        List<BaseClima> listaClima = new ArrayList<>();
            // Utiliza try-with-resources para garantir o fechamento correto do Workbook
            try (Workbook workbook = nomeArquivo.endsWith(".xlsx") ? new XSSFWorkbook(arquivo) : new HSSFWorkbook(arquivo)) {
                System.out.println("\nIniciando leitura do arquivo %s\n".formatted(nomeArquivo));

                Sheet sheet = workbook.getSheetAt(0);

                int j = 0;

                String Cidade = "";

                String Estado = "";

                System.out.println("""
                
                Iniciando a leitura da primeira parte do arquivo
                
                """);

                // Itera sobre as linhas da planilha
                for (Row row : sheet) {
                    System.out.println("Lendo linha " + row.getRowNum());
                    BaseClima lido = new BaseClima();

                    // Controla a leitura das linhas 0 e 2
                    switch (j) {
                        case 1:
                            // Verifica se a célula existe e se não é nula
                            if (row.getCell(1) != null) {
                                lido.setEstado(row.getCell(1).getStringCellValue());
                                System.out.println("Adicionado Estado Sigla: " + lido.getEstado());
                                Estado = lido.getEstado();
                            } else {
                                System.out.println("Célula de Estado Sigla vazia na linha 0.");
                            }

                            break;
                        case 2:
                            // Verifica se a célula existe e se não é nula
                            if (row.getCell(1) != null) {
                                lido.setCidade(row.getCell(1).getStringCellValue());
                                System.out.println("Adicionado Cidade: " + lido.getCidade());
                                Cidade = lido.getCidade();
                            } else {
                                System.out.println("Célula de Cidade vazia na linha 2.");
                            }

                            break;
                        default:
                            // Pode adicionar mais casos conforme necessário
                            break;
                    }

                    // Para a leitura na linha 7
                    if (row.getRowNum() == 7) {
                        break;
                    }
                    j++;
                }

                System.out.println("""
                
                Iniciando a leitura do arquivo
                
                """);

                // Iterando sobre as linhas da planilha
                int teste = 0;
                for (Row row : sheet) {
                    System.out.println("Lendo linha " + row.getRowNum());

                    // Lê o cabeçalho na linha 8
                    if (teste > 8) {
                        if (row.getRowNum() == 8) {
                            System.out.println("\nLendo cabeçalho 2");
                            for (int i = 0; i < 19; i++) {
                                if (row.getCell(i) != null) {
                                    String coluna = row.getCell(i).getStringCellValue();
                                    System.out.println("Coluna " + i + ": " + coluna);
                                }
                            }
                            System.out.println("--------------------");
                            continue;
                        }

                        // Extraindo valor das células e criando o objeto BaseClima
                        BaseClima clima = new BaseClima();
                        System.out.println("Criando o Objeto");

                        if (row.getCell(0) != null) {
                            System.out.println(row.getCell(0));
                            LocalDate data = converterDate(row.getCell(0).getDateCellValue());
                            clima.setData(data);
                        }

                        if (row.getCell(1) != null) {
                            System.out.println(row.getCell(1).getCellType());
                            String hora = TimeFormatter(row.getCell(1).getStringCellValue());
                            clima.setHora(hora);
                        }

                        if (row.getCell(16) != null) {
                            System.out.println(row.getCell(16));
                            clima.setDirecaoVento((int) row.getCell(16).getNumericCellValue());
                        }

                        if (row.getCell(17) != null) {
                            System.out.println(row.getCell(17));
                            clima.setVentoVelocidade(row.getCell(17).getNumericCellValue());
                        }

                        if (row.getCell(18) != null) {
                            System.out.println(row.getCell(18));
                            clima.setVentoRajada(row.getCell(18).getNumericCellValue());
                        }

                        clima.setCidade(Cidade);
                        clima.setEstado(Estado);
                        listaClima.add(clima);
                    } else {
                        teste++;
                    }
                }

                System.out.println("\nLeitura do arquivo finalizada\n");
                return listaClima;

            } catch (IOException e) {
                // Caso ocorra algum erro durante a leitura do arquivo uma exceção será lançada
                throw new RuntimeException(e);
            }

    }


//    public List<BaseClima> extrairCidades(String nomeArquivo, InputStream arquivo) {
//        // Utiliza try-with-resources para garantir o fechamento correto do Workbook
//
//        List<BaseClima> listaLeitura = new ArrayList<>();
//
//        try (Workbook workbook = nomeArquivo.endsWith(".xlsx") ? new XSSFWorkbook(arquivo) : new HSSFWorkbook(arquivo)) {
//            System.out.println("\nIniciando leitura do arquivo %s\n".formatted(nomeArquivo));
//
//            Sheet sheet = workbook.getSheetAt(0);
//
//            int j = 0;
//
//            System.out.println("""
//
//                Iniciando a leitura da primeira parte do arquivo
//
//                """);
//
//            // Itera sobre as linhas da planilha
//            for (Row row : sheet) {
//                System.out.println("Lendo linha " + row.getRowNum());
//                BaseClima lido = new BaseClima();
//
//                // Controla a leitura das linhas 0 e 2
//                switch (j) {
//                    case 0:
//                        // Verifica se a célula existe e se não é nula
//                        if (row.getCell(1) != null) {
//                            lido.setEstado(row.getCell(1).getStringCellValue());
//                            System.out.println("Adicionado Estado Sigla: " + lido.getEstado());
//                        } else {
//                            System.out.println("Célula de Estado Sigla vazia na linha 0.");
//                        }
//                        listaLeitura.add(lido);
//                        break;
//                    case 2:
//                        // Verifica se a célula existe e se não é nula
//                        if (row.getCell(1) != null) {
//                            lido.setCidade(row.getCell(1).getStringCellValue());
//                            System.out.println("Adicionado Cidade: " + lido.getCidade());
//                        } else {
//                            System.out.println("Célula de Cidade vazia na linha 2.");
//                        }
//                        listaLeitura.add(lido);
//                        break;
//                    default:
//                        // Pode adicionar mais casos conforme necessário
//                        break;
//                }
//
//                // Para a leitura na linha 7
//                if (row.getRowNum() == 7) {
//                    break;
//                }
//                j++;
//            }
//
//            return listaLeitura;
//
//        } catch (IOException e) {
//            // Caso ocorra algum erro durante a leitura do arquivo, uma exceção será lançada
//            throw new RuntimeException("Erro ao ler o arquivo: " + nomeArquivo, e);
//        }
//    }

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
