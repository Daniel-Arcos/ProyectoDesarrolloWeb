package com.web.aldalu.aldalu.utils;

public class ExceptionMessageConstants {
     // Constructor privado para evitar la instanciación
     private ExceptionMessageConstants() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * AUTH
     */
    public static final String UNAUTHORIZED_MSG = "You are not authorized";
    public static final String WRONG_PASSWORD_MSG = "Wrong password";
    public static final String WRONG_CONF_PASSWORD_MSG = "Password are not the same";
    public static final String EXPIRED_TOKEN_MSG = "Token has expired";
    public static final String INVALID_TOKEN_MSG = "Token is invalid";
    public static final String USER_NOT_FOUND_MSG = "User not found";
    public static final String ROLE_NOT_FOUND_MSG = "Role not found";

    /**
    *   API RESPONSES
     */
    public static final String INTERNAL_SERVER_ERROR_MSG =     "An unexpected error occurred";

    /**
     * VENDEDOR
     */
    public static final String VENDEDOR_NOT_FOUND = "Vendedor no encontrado";

    /**
     * TIENDA
     */
    public static final String TIENDA_NOT_FOUND = "Tienda no encontrado";

    /**
     * PRODUCTOS
     */
    public static final String PRODUCTO_NOT_FOUND = "Producto no encontrado";
}
