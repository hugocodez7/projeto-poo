package br.edu.ifpb.ads.foodjava.repository;
import java.util.List;

public interface Persistivel<T> {

    public void salvar(List<T> list);
    public List<T> carregar();

}
