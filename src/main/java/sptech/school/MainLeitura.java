package sptech.school;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class MainLeitura {

    public static void main(String[] args) throws IOException {
        String nomeArquivo = "INMET_NE_BA_A401_SALVADOR_01-01-2024_A_31-07-2024.xlsx";

        // Carregando o arquivo excel
        Path caminho = Path.of(nomeArquivo);
        InputStream arquivo = Files.newInputStream(caminho);

        // Extraindo os livros do arquivo
        LeitorExcel leitorExcel = new LeitorExcel();
        List<BaseClima> climasExtraidos = leitorExcel.extrarClima(nomeArquivo, arquivo);

        // Fechando o arquivo após a extração
        arquivo.close();

        System.out.println("Leituras extraídos:");
        for (BaseClima clima : climasExtraidos) {
//            System.out.println(clima);
        }
    }
}