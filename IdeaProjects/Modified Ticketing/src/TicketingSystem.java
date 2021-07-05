public interface TicketingSystem {

    Integer bookTicket(Passenger info);

    boolean modifyTicket(Integer bookingId,Passenger info);

    boolean cancelTicket(Integer bookingId);

}
