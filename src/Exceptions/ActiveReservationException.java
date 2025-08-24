
package Exceptions;


public class ActiveReservationException extends Exception{

    public ActiveReservationException() {
        super("No se puede eliminar un cliente con reservas activas");
    }


    
}  
   


