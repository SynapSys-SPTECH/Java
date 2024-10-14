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

    public List<BaseClima> extrarClima(String nomeArquivo, InputStream arquivo){
        try {
            System.out.println("\nIniciando leitura do arquivo %s\n".formatted(nomeArquivo));

            // Criando um objeto Workbook a partir do arquivo recebido
            Workbook workbook;
            if (nomeArquivo.endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(arquivo);
            } else {
                workbook = new HSSFWorkbook(arquivo);
            }

            Sheet sheet = workbook.getSheetAt(0);

            List<BaseClima> listaClima = new ArrayList<>();
            List<Leitura> listaLeitura = new ArrayList<>();
            int j = 0;
            System.out.println("""
                    
                    Iniciando a leitura das primeira parte do arquivo
                    
                    """);
            for (Row row : sheet) {

                System.out.println("Lendo linha " + row.getRowNum());
                Leitura lido = new Leitura();
                switch (j){
                    case 0:
                        lido.setEstado(row.getCell(1).getStringCellValue());
                        System.out.println("adicionado Estado Sigla");
                        listaLeitura.add(lido);
                        break;
                    case 2:
                        lido.setCidade(row.getCell(1).getStringCellValue());
                        System.out.println("Adicionado Cidade");
                        listaLeitura.add(lido);
                        break;
                }
                if (row.getRowNum() == 7) {
                    break;
                }
                j++;
//                sheet.removeRow(row);
            }
            System.out.println("-----------------\n");
            System.out.println(listaLeitura);
            System.out.println("-----------------\n");

            System.out.println("""
                    
                    Iniciando a segunda parte da leitura do arquivo
                    
                    """);
            // Iterando sobre as linhas da planilha
            int teste = 0;
            for (Row row : sheet) {

                System.out.println("Lendo linha " + row.getRowNum());
                if (teste > 8) {
                    if (row.getRowNum() == 8) {
                        System.out.println("\nLendo cabeçalho 2");

                        for (int i = 0; i < 19; i++) {
                            String coluna = row.getCell(i).getStringCellValue();
                            System.out.println("Coluna " + i + ": " + coluna);
                        }
                        System.out.println("--------------------");
                        continue;
                    }
                    // Extraindo valor das células e criando objeto Livro

                    BaseClima clima = new BaseClima();
                    System.out.println("Criando o Objeto");

                    System.out.println(row.getCell(0));
                    LocalDate data = converterDate(row.getCell(0).getDateCellValue());
                    System.out.println("Data: " + data);
                    clima.setData(data);


                    System.out.println(row.getCell(1).getCellType());
                    String hora = row.getCell(1).getStringCellValue();
                    System.out.println("Hora: " + hora);
                    clima.setHora(hora);
                    if (row.getCell(16) != null) {
                        System.out.println(row.getCell(16));
                        clima.setDirecaoVento((int) row.getCell(16).getNumericCellValue());
                    }
                    System.out.println(row.getCell(17));
                    if (row.getCell(17) != null) {
                        clima.setVentoVelocidade(row.getCell(17).getNumericCellValue());
                    }
                    if (row.getCell(18) != null) {
                        System.out.println(row.getCell(18));
                        clima.setVentoRajada(row.getCell(18).getNumericCellValue());
                    }
                    listaClima.add(clima);
                }else {
                    teste++;
                }
            }
            // Fechando o workbook após a leitura
            workbook.close();

            System.out.println("\nLeitura do arquivo finalizada\n");
//            System.out.println(listaClima);
            return listaClima;

        } catch (IOException e) {
            // Caso ocorra algum erro durante a leitura do arquivo uma exceção será lançada
            throw new RuntimeException(e);
        }
    }

    public List<Livro> extrarLivros(String nomeArquivo, InputStream arquivo) {
        try {
            System.out.println("\nIniciando leitura do arquivo %s\n".formatted(nomeArquivo));

            // Criando um objeto Workbook a partir do arquivo recebido
            Workbook workbook;
            if (nomeArquivo.endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(arquivo);
            } else {
                workbook = new HSSFWorkbook(arquivo);
            }

            Sheet sheet = workbook.getSheetAt(0);

            List<Livro> livrosExtraidos = new ArrayList<>();

            // Iterando sobre as linhas da planilha
            for (Row row : sheet) {

                if (row.getRowNum() == 0) {
                    System.out.println("\nLendo cabeçalho");

                    for (int i = 0; i < 4; i++) {
                        String coluna = row.getCell(i).getStringCellValue();
                        System.out.println("Coluna " + i + ": " + coluna);
                    }

                    System.out.println("--------------------");
                    continue;
                }

                // Extraindo valor das células e criando objeto Livro
                System.out.println("Lendo linha " + row.getRowNum());

                Livro livro = new Livro();
                livro.setId((int) row.getCell(0).getNumericCellValue());
                livro.setTitulo(row.getCell(1).getStringCellValue());
                livro.setAutor(row.getCell(2).getStringCellValue());
                livro.setDataLancamento(converterDate(row.getCell(3).getDateCellValue()));

                livrosExtraidos.add(livro);
            }

            // Fechando o workbook após a leitura
            workbook.close();

            System.out.println("\nLeitura do arquivo finalizada\n");

            return livrosExtraidos;
        } catch (IOException e) {
            // Caso ocorra algum erro durante a leitura do arquivo uma exceção será lançada
            throw new RuntimeException(e);
        }
    }

    private LocalDate converterDate(Date data) {
        return data.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
    private LocalTime converterHora(Time time){
        return time.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
    }
}
