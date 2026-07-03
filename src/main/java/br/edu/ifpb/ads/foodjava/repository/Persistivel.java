package br.edu.ifpb.ads.foodjava.repository;

import br.edu.ifpb.ads.foodjava.exception.ArquivoImportacaoException;

import java.util.List;

/*
Nesse Persistivel que construimos, vai definir um padrão para os repositorios.
Toda classe que implementa Persistivel precisa ter os metodos salvar e carregar
 */

public interface Persistivel<T> {

    void salvar(List<T> lista);

    List<T> carregar() throws ArquivoImportacaoException;
}