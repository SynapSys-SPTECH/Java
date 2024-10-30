import org.springframework.jdbc.core.JdbcTemplate;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import client.S3Provider;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.*;

public class Main {
    public static void main(String[] args) {

        Logger log = Logger.getLogger(Main.class.getName());

        // Configurando o logger
        log.setUseParentHandlers(false); // Remove o console handler padrão

        // Criando e configurando um ConsoleHandler customizado
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new Main.CustomFormatter());
        log.addHandler(consoleHandler);

        System.out.println("Conetando com S3");
        S3Client s3Client = new S3Provider().getS3Client();
        String bucketName = "bucket-synapsys";

//        Criar Bucket Se ele já não Existir
        try {
            CreateBucketRequest createBucketRequest = CreateBucketRequest.builder()
                    .bucket(bucketName)
                    .build();
            s3Client.createBucket(createBucketRequest);
            log.fine("Bucket created: " + bucketName);
//            System.out.println("Bucket criado com sucesso: " + bucketName);
        } catch (S3Exception e) {
            log.warning("Erro ao criar o bucket: " + e.getMessage());
//            System.err.println("Erro ao criar o bucket: " + e.getMessage());
        }

        try {
            String diretorioArquivos = "arquivos-Excel/";

            List<S3Object> objects = s3Client.listObjects(ListObjectsRequest.builder()
                    .bucket(bucketName)
                    .prefix(diretorioArquivos)
                    .build()).contents();
            System.out.println("Objetos encontrados: "+ objects);
            for (S3Object object : objects) {
                System.out.println("Objeto: " + object.key());
                GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                        .bucket(bucketName)
                        .key(object.key())
                        .build();
                String localFileName = object.key().substring(diretorioArquivos.length());

                InputStream inputStream = s3Client.getObject(getObjectRequest, ResponseTransformer.toInputStream());

                Path caminhoDestino = Paths.get(diretorioArquivos + localFileName);

                Files.createDirectories(caminhoDestino.getParent());

                Files.copy(inputStream, caminhoDestino);

                log.fine("Arquivo baixado: " + object.key());
            }
        } catch (IOException | S3Exception e) {
            e.printStackTrace();
            log.warning("Erro ao fazer download dos arquivos: " + e.getMessage());
        }

        // *************************************************
        // *      EXECUÇÃO DA PARTE DO APACHE POI          *
        // *************************************************

        // Diretório onde os arquivos estão localizados
        String diretorio = "./arquivos-Excel";
        List<List<BaseClima>> climasExtraidos = new ArrayList<>();

        try {
            // Obtendo o caminho do diretório
            Path pasta = Paths.get(diretorio);

            // Utilizando DirectoryStream para buscar arquivos que começam com "INMET_NE" e terminam com ".xlsx"
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(pasta, "INMET_NE*.xlsx")) {
                for (Path caminhoArquivo : stream) {
                    log.info("Processando arquivo: " + caminhoArquivo.getFileName());
//                    System.out.println("Processando arquivo: " + caminhoArquivo.getFileName());

                    // Abrindo o arquivo
                    InputStream arquivo = Files.newInputStream(caminhoArquivo);

                    // Criando o leitor de Excel e extraindo os dados
                    LeitorExcel leitorExcel = new LeitorExcel();
                    climasExtraidos.add(leitorExcel.extrairClima(caminhoArquivo.getFileName().toString(), arquivo));
                    // Aqui você pode fazer algo com a lista de climas extraídos (exibir, salvar em BD, etc.)

                    log.info("Dados extraídos do arquivo: " + caminhoArquivo.getFileName());
//                    System.out.println("Dados extraídos do arquivo: " + caminhoArquivo.getFileName());

                }

            }

        } catch (IOException e) {
            log.warning("Erro ao processar os arquivos: " + e.getMessage());
        }

        DBConnectionProvider dbConnectionProvider = new DBConnectionProvider();
        JdbcTemplate connection = dbConnectionProvider.getConnection();
        if(!climasExtraidos.isEmpty()) {
            log.info("Iniciando a Inserção de dados");
            int i = 0;
            for (List<BaseClima> dados : climasExtraidos) {
                log.info("Lendo dados da cidade " + dados.get(i).getCidade());

                for (BaseClima baseClima : dados) {
                    String data = baseClima.getData().toString();
                    String cidade = baseClima.getCidade();
                    String hora = baseClima.getHora();
                    String estado = baseClima.getEstado();
                    Integer direcaoVento = baseClima.getDirecaoVento();
                    Double rajadaMax = baseClima.getVentoRajada();
                    Double velocidadeMax = baseClima.getVentoVelocidade();

                    connection.update(
                            "INSERT INTO leitura (dia, hora, direcaoVento, rajadaMax, velocidadeHoraria, cidade, estado) VALUES (? , ?, ?, ?, ?, ?, ?)",
                            data, hora, direcaoVento, rajadaMax, velocidadeMax, cidade, estado);
                }
                log.info("inserido dados da cidade " + dados.get(i).getCidade());
//                System.out.println("inserido dados da cidade " + dados.get(i).getCidade());
                i++;
            }
            log.fine("Finalizado inserção dos dados!");
//            System.out.println("Finalizado inserção dos dados!");
        }else {
            log.warning("Erro ao inserir os dados");
        }


        
    }

    static class CustomFormatter extends Formatter {
        // Códigos ANSI para cores
        private static final String RESET = "\u001B[0m";
        private static final String RED = "\u001B[31m";
        private static final String YELLOW = "\u001B[33m";
        private static final String GREEN = "\u001B[32m";

        @Override
        public String format(LogRecord record) {
            String color;

            // Define a cor com base no nível de log
            if (record.getLevel().intValue() >= Level.WARNING.intValue()) {
                color = RED; // Para WARNING e SEVERE
            } else if (record.getLevel().intValue() == Level.INFO.intValue()) {
                color = GREEN; // Para INFO
            } else {
                color = YELLOW; // Para outros níveis
            }

            return color +
                    String.format("%1$tF %1$tT %2$s %3$s: %4$s%n",
                            record.getMillis(),
                            record.getSourceClassName(),
                            record.getLevel().getLocalizedName(),
                            record.getMessage()) +
                    RESET;
        }
    }


}
