import client.S3Provider;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;

public class Bucket {

    Logger log = Logger.getLogger(Main.class.getName());
    S3Client s3Client = new S3Provider().getS3Client();

    public void criarBucket(String bucketName) {
        try {
            CreateBucketRequest createBucketRequest = CreateBucketRequest.builder()
                    .bucket(bucketName)
                    .build();
            s3Client.createBucket(createBucketRequest);
            log.fine("Bucket created: " + bucketName);
        } catch (S3Exception e) {
            log.warning("Erro ao criar o bucket: " + e.getMessage());
        }
    }

    public void baixarArquivos(String bucketName) {
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
    }
}


