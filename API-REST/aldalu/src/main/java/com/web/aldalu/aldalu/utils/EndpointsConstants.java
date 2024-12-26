package com.web.aldalu.aldalu.utils;

public class EndpointsConstants {
    private EndpointsConstants() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    //Los pattern son usados para la configuracion de spring security
    public static final String ENDPOINT_BASE_API = "/api/v1";
    public static final String ENDPOINT_AUTH = ENDPOINT_BASE_API + "/auth";
    public static final String ENDPOINT_AUTH_PATTERN = ENDPOINT_AUTH + "/**";
    public static final String ENDPOINT_LOGOUT = ENDPOINT_AUTH + "/logout";
    public static final String ENDPOINT_VENDEDORES = ENDPOINT_BASE_API + "/vendedores";
    public static final String ENDPOINT_VENDEDORES_PATTERN = ENDPOINT_VENDEDORES + "/**";
    public static final String ENDPOINT_CLIENTES = ENDPOINT_BASE_API + "/clientes";
    public static final String ENDPOINT_CLIENTES_PATTERN = ENDPOINT_CLIENTES + "/**";
    public static final String ENDPOINT_PRODUCTOS = ENDPOINT_BASE_API + "/productos";
    public static final String ENDPOINT_PRODUCTOS_PATTERN = ENDPOINT_PRODUCTOS + "/**";
    public static final String ENDPOINT_TIENDAS = ENDPOINT_BASE_API + "/tiendas";
    public static final String ENDPOINT_TIENDAS_PATTERN = ENDPOINT_PRODUCTOS + "/**";
 
}
