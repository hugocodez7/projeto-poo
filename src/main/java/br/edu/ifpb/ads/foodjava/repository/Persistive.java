package br.edu.ifpb.ads.foodjava.repository;

import java.lang.reflect.Type;
import java.util.List;

public interface Persistive {

    public void salvar(List<Type> list);
    public List<Type> carregar();

}
