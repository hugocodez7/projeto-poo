package br.edu.ifpb.ads.foodjava.exception;

public class ItemNaoEncontradoException extends Exception{

    public ItemNaoEncontradoException() {
        super("Item não encontrado");
    }

    public ItemNaoEncontradoException(String id) {
        super("Item não encontrado" + " ID buscado: " + id);
    }
}
