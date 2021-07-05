import java.util.*;


public abstract class SeatingArrangement<V extends Iterable<Seat> > {
    V seats;
    Set<String> types=new HashSet<>();
    public SeatingArrangement(){
        this.seats=getSeats();

        for (Seat i:seats){
            if(!types.contains(i.getSeatType())){
                types.add(i.getSeatType());
            }
        }

    }

    public SeatingArrangement(int numSeats){



        this.seats=getSeats(numSeats);
        for (Seat i:seats){
            if(!types.contains(i.getSeatType())){
                types.add(i.getSeatType());
            }
        }
//        this.types=getTypes();
    }

    abstract V getSeats(int numSeats);

    abstract V getSeats();


//    abstract List<String> getTypes();

    Seat bookTicket(Passenger info) {
//        return null;
        String preference=info.getPreference();
        if (isAvailable(preference)) {
            for (Seat i : seats) {

                if (i.getSeatType().equals(preference) && !i.isBooked()) {
                    synchronized (i) {
                        if (!i.isBooked()) {
                            i.BookSeat();
                            i.setInfo(info);
                            return i;
                        }
                    }

                }

            }
        }
        if (isAvailable()) {
            for (Seat i : seats) {

                if (!i.isBooked()) {
                    synchronized (i) {
                        if (!i.isBooked()) {
                            i.BookSeat();
                            i.setInfo(info);
                            return i;
                        }
                    }

                }

            }
        }
        return null;
    }
    //
//
    public boolean isAvailable(String type) {

        for (Seat i : seats) {
            if (i.getSeatType().equals(type)&&!i.isBooked()) {
                return true;
            }
        }
        return false;
    }
    //
    public boolean isAvailable() {
        Iterator<Seat> iterateOver=seats.iterator();
        while (iterateOver.hasNext()){
            if(!iterateOver.next().isBooked()){
                return true;
            }

        }
        return false;
    }
    //
    public void checkAvailability() {
        Map<String, Integer> availability = new TreeMap<>();
        for (String i : types) {
            availability.put(i, 0);
        }
        for (Seat i : seats) {
            if (!i.isBooked()) {
                Integer count = availability.get(i.getSeatType()) + 1;
                availability.put(i.getSeatType(), count);
            }
        }
        for (Map.Entry m : availability.entrySet())
            System.out.println(m.getKey() + " : " + m.getValue());
    }
}
