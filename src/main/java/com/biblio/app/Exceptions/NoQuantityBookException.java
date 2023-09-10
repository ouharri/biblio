package com.biblio.app.Exceptions;

public class NoQuantityBookException extends Exception{

        public NoQuantityBookException() {
            System.out.println("le livre n'est pas disponible");
        }
}
