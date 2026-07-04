package br.edu.ifpb.ads.foodjava.exception;

public class ItemNaoEncontradoException extends Exception{

    public ItemNaoEncontradoException() {
        super("Item não encontrado");
    }
}
