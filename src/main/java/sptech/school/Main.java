package sptech.school;

import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import sptech.school.client.S3Provider;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        S3Client s3Client = new S3Provider().getS3Client();
        String bucketName = "bucket-synapsys";


//        Criar Bucket Se ele já não Existir
        try {
            CreateBucketRequest createBucketRequest = CreateBucketRequest.builder()
                    .bucket(bucketName)
                    .build();
            s3Client.createBucket(createBucketRequest);
            System.out.println("Bucket criado com sucesso: " + bucketName);
        } catch (S3Exception e) {
            System.err.println("Erro ao criar o bucket: " + e.getMessage());
        }

//          Listar Todos os Buckets dessa conta:
//        try {
//            List<Bucket> buckets = s3Client.listBuckets().buckets();
//            System.out.println("Lista de buckets:");
//            for (Bucket bucket : buckets) {
//                System.out.println("- " + bucket.name());
//            }
//        } catch (S3Exception e) {
//            System.err.println("Erro ao listar buckets: " + e.getMessage());
//        }

////        Listar objetos dentro do Bucket Criado/Escolhido
//        try {
//            ListObjectsRequest listObjects = ListObjectsRequest.builder()
//                    .bucket(bucketName)
//                    .build();
//
//            List<S3Object> objects = s3Client.listObjects(listObjects).contents();
//            System.out.println("Objetos no bucket " + bucketName + ":");
//            for (S3Object object : objects) {
//                System.out.println("- " + object.key());
//            }
//        } catch (S3Exception e) {
//            System.err.println("Erro ao listar objetos no bucket: " + e.getMessage());
//        }


//         Baixar os arquivos de Base
        try {
            String diretorioArquivos = "arquivos-Excel/";

            List<S3Object> objects = s3Client.listObjects(ListObjectsRequest.builder()
                    .bucket(bucketName)
                    .prefix(diretorioArquivos)
                    .build()).contents();
            System.out.println(objects);
            for (S3Object object : objects) {
                GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                        .bucket(bucketName)
                        .key(object.key())
                        .build();
                String localFileName = object.key().substring(diretorioArquivos.length());
                InputStream inputStream = s3Client.getObject(getObjectRequest, ResponseTransformer.toInputStream());
                Path caminhoDestino =Paths.get(diretorioArquivos + localFileName);
                Files.createDirectories(caminhoDestino.getParent());
                Files.copy(inputStream, caminhoDestino);
                System.out.println("Arquivo baixado: " + object.key());
            }
        } catch (IOException | S3Exception e) {
            System.err.println("Erro ao fazer download dos arquivos: " + e.getMessage());
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

        System.out.println("for Each");

        for (List<BaseClima> dados : climasExtraidos){
            System.out.println(dados);
            for (BaseClima baseClima : dados){
                System.out.println(baseClima.getCidade());
                System.out.println(baseClima.getHora());
                System.out.println(baseClima.getEstado());
                System.out.println(baseClima.getDirecaoVento());
                System.out.println(baseClima.getVentoRajada());
                System.out.println(baseClima.getVentoVelocidade());
            }


        }

        System.out.println("Finalizei o FOr Each");

//        Aqui iremos ler o arquivo com Apache POI
//           *************************************
//        // *   Deletando um objeto do bucket   *
//        // *  Está funcionando e verificando   *
//        // *       se existe no Bucket         *
//        // *************************************
//        boolean verified = true;
//        try {
//            String objectKeyToDelete = "arquivo1.txt";
//            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
//                    .bucket(bucketName)
//                    .key(objectKeyToDelete)
//                    .build();
//
//            System.out.println(deleteObjectRequest);
//            try {
//                ListObjectsRequest listObjects = ListObjectsRequest.builder()
//                        .bucket(bucketName)
//                        .build();
//                List<S3Object> objects = s3Client.listObjects(listObjects).contents();
//                // Verificar arquivo Existente
//                for (S3Object object : objects) {
//                    if (object.key().equals(objectKeyToDelete)) {
//                        s3Client.deleteObject(deleteObjectRequest);
//                        System.out.println("Objeto encontrado!!");
//                        verified = true;
//                        System.out.println("Objeto deletado com sucesso: " + objectKeyToDelete);
//                        return;
//                    }else {
//                        verified = false;
//                    }
//                }
//                if (!verified){
//                    System.out.println("Não encontrado Objeto!!");
//                }
//            }
//            catch (S3Exception e) {
//                System.err.println("Erro ao listar objetos no bucket: " + e.getMessage());
//            }
//        } catch (S3Exception e) {
//            System.err.println("Erro ao deletar objeto: " + e.getMessage());
//        }

        
    }
}
