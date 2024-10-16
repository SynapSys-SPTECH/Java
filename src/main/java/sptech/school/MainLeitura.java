package sptech.school;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.*;


public class MainLeitura {
//
//    public static void main(String[] args) throws IOException {
//        String nomeArquivo = "./arquivos-CSV/INMET_NE_BA_A401_SALVADOR_01-01-2024_A_31-07-2024.xlsx";
//
//        // Carregando o arquivo excel
//        Path caminho = Path.of(nomeArquivo);
//        InputStream arquivo = Files.newInputStream(caminho);
//
//        // Extraindo os livros do arquivo
//        LeitorExcel leitorExcel = new LeitorExcel();
//        List<BaseClima> climasExtraidos = leitorExcel.extrarClima(nomeArquivo, arquivo);
//
//        // Fechando o arquivo após a extração
//        arquivo.close();
//
//        System.out.println("Leituras extraídos:");
//        for (BaseClima clima : climasExtraidos) {
//            System.out.println(clima);
//        }
//    }
//}
    public static void main(String[] args) {
        // Diretório onde os arquivos estão localizados
        String diretorio = "./arquivos-Excel";

        try {
            // Obtendo o caminho do diretório
            Path pasta = Paths.get(diretorio);

            // Utilizando DirectoryStream para buscar arquivos que começam com "INMET_NE" e terminam com ".xlsx"
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(pasta, "INMET_NE*.xlsx")) {

                List<List<BaseClima>> cidadesExtraidas = new ArrayList<>();
                List<List<BaseClima>> climasExtraidos = new ArrayList<>();
                for (Path caminhoArquivo : stream) {
                    System.out.println("Processando arquivo: " + caminhoArquivo.getFileName());

                    // Abrindo o arquivo
                    InputStream arquivo = Files.newInputStream(caminhoArquivo);

                    // Criando o leitor de Excel e extraindo os dados
                    LeitorExcel leitorExcel = new LeitorExcel();
                    climasExtraidos.add(leitorExcel.extrairClima(caminhoArquivo.getFileName().toString(), arquivo));
                    // Aqui você pode fazer algo com a lista de climas extraídos (exibir, salvar em BD, etc.)


                    System.out.println("Dados extraídos do arquivo: " + caminhoArquivo.getFileName());
                    System.out.println(climasExtraidos);
                }



            }

        } catch (IOException e) {
            System.err.println("Erro ao processar os arquivos: " + e.getMessage());
        }
    }
}
