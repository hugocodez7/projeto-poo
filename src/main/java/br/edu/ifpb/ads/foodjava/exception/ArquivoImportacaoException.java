package br.edu.ifpb.ads.foodjava.exception;

public class ArquivoImportacaoException extends Exception{

    public ArquivoImportacaoException(String nomeArquivo, Throwable causa) {
        super("Arquivo corrompido ou inválido: " + nomeArquivo, causa);
    }
}
