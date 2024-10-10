package sptech.school;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

    public class MyS3Service {

        // Cria o logger para esta classe
        private static final Logger logger = LoggerFactory.getLogger(MyS3Service.class);

        public static void main(String[] args) {
            // Exemplo de logging
            logger.info("Iniciando a aplicação");

            try {
                // Exemplo de código que pode gerar uma exceção
                logger.debug("Tentando conectar ao S3...");
                // Código para usar S3 SDK aqui
                logger.info("Conexão ao S3 estabelecida com sucesso");
            } catch (Exception e) {
                // Log de erro em caso de exceção
                logger.error("Falha ao conectar ao S3", e);
            }

            logger.info("Finalizando a aplicação");
        }
    }

