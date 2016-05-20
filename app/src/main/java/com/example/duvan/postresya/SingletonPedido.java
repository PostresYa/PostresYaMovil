package com.example.duvan.postresya;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by duvan on 17/05/16.
 */
public class SingletonPedido {


    private static  SingletonPedido instance=null;

    private String direccion;



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

    public boolean existReposteria(String nit){
        boolean respuesta=true;
        if(pedido.get(nit)==null){
            respuesta=false;
        }
        return respuesta;
    }
    public HashMap<String,JSONArray> getPedido(){
        return pedido;
    }

    public void removePostre(String nit,JSONObject postre) throws JSONException {
        int pos=-1;
        for(int i=0;i< pedido.get(nit).length();i++){
            if(pedido.get(nit).get(i).equals(postre)){
                pos=i;
            }

        }

        if((pedido.get(nit).length()==1 && pos==0)|| pedido.get(nit).length()==0){
            pedido.remove(nit);

        }else{
            pedido.get(nit).remove(pos);
        }

    }
    public void removeReposteriaPedido(String nit){
        pedido.remove(nit);
    }

    public void removeAll(){
        pedido=new HashMap<>();
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }


}
