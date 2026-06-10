package br.edu.ifpb.ads.foodjava.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonUtil {

    private static final Gson INSTANCIA = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    public static Gson getInstancia() {
        return INSTANCIA;
    }
}
