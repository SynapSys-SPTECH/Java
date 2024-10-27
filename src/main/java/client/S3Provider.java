package client;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

public class S3Provider {

    public S3Client getS3Client() {
        return S3Client.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }
    //aws_access_key_id=ASIAZ5NM557B5OCONWUK
    //aws_secret_access_key=KBcWp+O/wTv2oHOBhT/UWKZ+iDaRgBB8uhx6Py7Q
    //aws_session_token=IQoJb3JpZ2luX2VjEKn//////////wEaCXVzLXdlc3QtMiJHMEUC
    // IQDU1IfYFPxrAm+qkAOq+nQX5agGFF/HiOJzLjXrqA6XnwIgcSq1MvW7IdiQ6eoMKEl
    // xa4I1SSgWA6rUCOMUQbTj+3IqugIIIhABGgw2ODE2NTE4NTkzOTUiDKJvipxCgtU1xk
    // vqCSqXAlJAW1gly7s5jt+tGDldk9pXY6vKfb7xALoq1dRrA+RXD7q88dD2+uMlWyccq
    // jrsez6XMLdrO8fWbUrVDHSegpPnLH3iW7ATB5zAUmMGLehVyeb33iy0UxqjujpE4IUl
    // 90V0l6T+OT4Htp+1qtdqC7lL0U/raSz5xf2oXqiUNi5i5weudtYZGhjJPa3ZEdxBVJZ
    // cyvl5WJGWt50xH/nVUcwwYCMlCz79T8yOdxFXvr05zwPLos/TIWApoKbMyW1+EyHpI7
    // P4XJnhfR3aDk+pOGPydAyBaODKQKOx6zEKVlLZPS1aGwBqMMjtPg7R2f7gs8BKwaOSE
    // AQHI2ttEZpRiJ6do4/JswE/EMi/B6CQS24s1vEBzHhTkTDuofa4BjqdAbupAgO7jezf
    // O5vS0uS2exHjXdqKVBxq7+kfKRXmtiDUsrurrEabciznNpe5YEEjpPnz61dfDQdzoa/
    // 9Slz7fN3zs/Jm2YUlBp9ZbbPJJpesSWbjYmF7OdgtOJzJ2GeQViTU3lHEfPxTdrKqhW
    // LWCAxkog/j6xuW/eBjtFHX/TcwE7uhcXfbhy4MqAq9kOyzNbtEXQXr799eaKlUUw0=

//    private final AwsSessionCredentials credentials;
//    private final AwsSessionCredentials credentials;
//
//
//    public S3Provider() {
//        this.credentials = AwsSessionCredentials.create(
//                System.getenv("AWS_ACCESS_KEY_ID"),
//                System.getenv("AWS_SECRET_ACCESS_KEY"),
//                System.getenv("AWS_SESSION_TOKEN")
//        );
//    }
//
//    public S3Client getS3Client() {
//        return S3Client.builder()
//                .region(Region.US_EAST_1)
//                .credentialsProvider(() -> credentials)
//                .build();
//    }
//    public S3Provider() {
//    // Defina as suas credenciais diretamente aqui
//    String accessKey = "your_access_key_here"; // Coloque sua chave de acesso AWS
//    String secretKey = "your_secret_key_here"; // Coloque sua chave secreta AWS
//    String sessionToken = "your_session_token_here"; // Coloque seu token de sessão AWS (se aplicável)
//
//    this.credentials = AwsSessionCredentials.create(accessKey, secretKey, sessionToken);
//}

}

