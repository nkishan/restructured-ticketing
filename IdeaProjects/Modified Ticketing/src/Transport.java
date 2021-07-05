import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class Transport {
    SeatingArrangement type;
    private String name;
    private String source;
    private String destination;
    private String mode;
    protected Map<Integer, Seat> bookings = new MyHashMap<>();

    public Transport(String name, String source, String destination, String mode) {
        this.name = name;
        this.source = source;
        this.destination = destination;
        this.mode = mode;
    }

    public void setType(SeatingArrangement type) {
        this.type = type;
    }

    Integer bookTicket(Passenger info) {
        Seat a=type.bookTicket(info);
        if(a==null){
            return null;
        }
        Integer refId=(int)(Math.random()*10000000);
        while(bookings.containsKey(refId)){
            refId=(int)(Math.random()*10000000);
        }
        bookings.put(refId,a);
//        System.out.println("After booking"+bookings.size());
        return refId;
    }

    boolean modifyTicket(Integer refId,Passenger info){
        if(bookings.containsKey(refId)){
            Seat a=bookings.get(refId);
            a.setInfo(info);
            return true;
        }
        return false;

    }

    boolean cancelTicket(Integer refId){
        if(bookings.containsKey(refId)){
            Seat a=bookings.get(refId);
            bookings.remove(refId);
            a.cancelSeat();
            a.setInfo(null);
//            System.out.println("After Cancellation"+bookings.size());
            return true;
        }
        return false;
    }

    void getDetails(Integer bookingId){
        if(bookings.containsKey(bookingId)){
//            System.out.println("Mode: "+mode+"\nName: "+name+"\nSource: "+source+"\nDestination: "+destination);
            bookings.get(bookingId).printDetails();
        }
    }

    public boolean isAvailable() {
        return type.isAvailable();
    }

    public void checkAvailability() {
        type.checkAvailability();
    }

    public void checkLinkedHash(){
        MyLinkedHashMap<Integer,Seat> a= (MyLinkedHashMap<Integer, Seat>) bookings;
        Iterator <Integer> b= a.myIterator();
        for (Iterator<Integer> it = b; it.hasNext(); ) {
            Integer i = it.next();
//            System.out.println(i);
            getDetails(i);

        }
    }

}
