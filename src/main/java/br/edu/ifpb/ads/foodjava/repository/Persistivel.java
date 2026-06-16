package br.edu.ifpb.ads.foodjava.repository;
import br.edu.ifpb.ads.foodjava.exception.ArquivoImportacaoException;

import java.util.List;

public interface Persistivel<T> {

    public void salvar(List<T> list);
    public List<T> carregar() throws ArquivoImportacaoException;

}
