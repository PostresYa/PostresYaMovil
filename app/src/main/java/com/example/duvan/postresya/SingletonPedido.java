package com.example.duvan.postresya;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by duvan on 17/05/16.
 */
public class SingletonPedido {


    private static  SingletonPedido instance=null;


    private HashMap<String,JSONArray> pedido= new HashMap<>();

    public SingletonPedido(){

    }

    public static SingletonPedido getInstance(){
        if(instance==null){
            instance=new SingletonPedido();
        }
        return instance;
    }

    public void addPedido(String nit, JSONObject postre){
        if(pedido.get(nit)==null){
            JSONArray postres= new JSONArray();
            postres.put(postre);
           pedido.put(nit,postres);
        }else{
            pedido.get(nit).put(postre);
        }
    }
    public HashMap<String,JSONArray> getPedido(){
        return pedido;
    }

}
