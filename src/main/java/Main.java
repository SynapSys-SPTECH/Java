import java.util.ArrayList;
import java.util.List;
import java.util.logging.*;

public class Main {
    public static void main(String[] args) throws Exception{

        Slack.notificar("Aplicação Iniciada...");
        Logger log = Logger.getLogger(Main.class.getName());

        log.setUseParentHandlers(false);

        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new Main.CustomFormatter());
        log.addHandler(consoleHandler);

        log.info("Conetando com S3");
//        S3Client s3Client = new S3Provider().getS3Client();
        String bucketName = "bucket-synapsys";

        Bucket function = new Bucket();

        function.criarBucket(bucketName);
//
        function.baixarArquivos(bucketName);

        String diretorio = "./arquivos-Excel";

        List<List<BaseClima>> climasExtraidos = new ArrayList<>();

        ApachePoi lerArq = new ApachePoi();

        lerArq.listar(diretorio,climasExtraidos);

        BDJava banco = new BDJava();

        if(!climasExtraidos.isEmpty()) {
            banco.inserirBanco(climasExtraidos);
        }else {
            Slack.notificar("Erro ao inserir os dados, sem dados para inserir");
            log.warning("Erro ao inserir os dados, sem dados para inserir");
        }

        Slack.notificar("Aplicação finalizada!");
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
