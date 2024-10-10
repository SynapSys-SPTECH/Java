
package sptech.school;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import sptech.school.client.S3Provider;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        // Instanciando o cliente S3 via S3Provider
        S3Client s3Client = new S3Provider().getS3Client();
        String bucketName = "s3-raw-lab-bryan";

        // *************************************
        // *   Criando um novo bucket no S3    *
        // *************************************
        try {
            CreateBucketRequest createBucketRequest = CreateBucketRequest.builder()
                    .bucket(bucketName)
                    .build();
            s3Client.createBucket(createBucketRequest);
            System.out.println("Bucket criado com sucesso: " + bucketName);
        } catch (S3Exception e) {
            System.err.println("Erro ao criar o bucket: " + e.getMessage());
        }

        // *************************************
        // *   Listando todos os buckets       *
        // *************************************
        try {
            List<Bucket> buckets = s3Client.listBuckets().buckets();
            System.out.println("Lista de buckets:");
            for (Bucket bucket : buckets) {
                System.out.println("- " + bucket.name());
            }
        } catch (S3Exception e) {
            System.err.println("Erro ao listar buckets: " + e.getMessage());
        }

        // *************************************
        // *   Listando objetos do bucket      *
        // *************************************
        try {
            ListObjectsRequest listObjects = ListObjectsRequest.builder()
                    .bucket("s3-raw-lab-bryan")
                    .build();

            List<S3Object> objects = s3Client.listObjects(listObjects).contents();
            System.out.println("Objetos no bucket " + bucketName + ":");
            for (S3Object object : objects) {
                System.out.println("- " + object.key());
            }
        } catch (S3Exception e) {
            System.err.println("Erro ao listar objetos no bucket: " + e.getMessage());
        }


//        // *************************************
          // *   Fazendo download de arquivos    *
          // * de todos os arquivos em uma pasta *
          // *         chamada logs/             *
          // *************************************
//
//
        try {
            String folderPrefix = "logs/";

            List<S3Object> objects = s3Client.listObjects(ListObjectsRequest.builder()
                    .bucket(bucketName)
                    .prefix(folderPrefix)
                    .build()).contents();
            System.out.println(objects);
            for (S3Object object : objects) {
                GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                        .bucket(bucketName)
                        .key(object.key())
                        .build();

                String localFileName = object.key().substring(folderPrefix.length());

                InputStream inputStream = s3Client.getObject(getObjectRequest, ResponseTransformer.toInputStream());
                //para baixar de fora de pastas.
                //Files.copy(inputStream, new File(object.key()).toPath());
                //para entrar no repositorio e baixar os arquivos
                Files.copy(inputStream, new File(localFileName).toPath());
                System.out.println("Arquivo baixado: " + object.key());
            }
        } catch (IOException | S3Exception e) {
            System.err.println("Erro ao fazer download dos arquivos: " + e.getMessage());
        }

        // *************************************
        // *   Deletando um objeto do bucket   *
        // *  Está funcionando e verificando   *
        // *       se existe no Bucket         *
        // *************************************
        boolean verified = true;
        try {
            String objectKeyToDelete = "arquivo1.txt";
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKeyToDelete)
                    .build();

            System.out.println(deleteObjectRequest);
            try {
                ListObjectsRequest listObjects = ListObjectsRequest.builder()
                        .bucket(bucketName)
                        .build();
                List<S3Object> objects = s3Client.listObjects(listObjects).contents();
                // Verificar arquivo Existente
                for (S3Object object : objects) {
                    if (object.key().equals(objectKeyToDelete)) {
                        s3Client.deleteObject(deleteObjectRequest);
                        System.out.println("Objeto encontrado!!");
                        verified = true;
                        System.out.println("Objeto deletado com sucesso: " + objectKeyToDelete);
                        return;
                    }else {
                        verified = false;
                    }
                }
                if (!verified){
                    System.out.println("Não encontrado Objeto!!");
                }
            }
            catch (S3Exception e) {
                System.err.println("Erro ao listar objetos no bucket: " + e.getMessage());
            }
        } catch (S3Exception e) {
            System.err.println("Erro ao deletar objeto: " + e.getMessage());
        }
    }
}
