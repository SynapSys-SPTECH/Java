import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;

public class ApachePoi {

    Logger log = Logger.getLogger(Main.class.getName());

    public void listar(String diretorio, List<List<BaseClima>> climasExtraidos) throws Exception {

        try {
            Path pasta = Paths.get(diretorio);

            try (DirectoryStream<Path> stream = Files.newDirectoryStream(pasta, "INMET_NE_BA*.xlsx")) {
                for (Path caminhoArquivo : stream) {
                    log.info("Processando arquivo: " + caminhoArquivo.getFileName());

                    InputStream arquivo = Files.newInputStream(caminhoArquivo);

                    LeitorExcel leitorExcel = new LeitorExcel();
                    climasExtraidos.add(leitorExcel.extrairClima(caminhoArquivo.getFileName().toString(), arquivo));

                    log.info("Dados extraídos do arquivo: " + caminhoArquivo.getFileName() + "\n");
                }
            }
        } catch (IOException e) {
            Slack.notificar("Erro ao processar os arquivos: " + e.getMessage());
            log.warning("Erro ao processar os arquivos: " + e.getMessage());
        }
    }
}
