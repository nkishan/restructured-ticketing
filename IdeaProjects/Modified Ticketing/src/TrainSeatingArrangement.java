import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TrainSeatingArrangement extends SeatingArrangement {
    private int numSeats=72;

    public TrainSeatingArrangement(int numSeats) {
        super(numSeats);
    }
    public TrainSeatingArrangement(){

    }
//
//        super(getSeats(),getTypes());
//        this.numSeats=numSeats;
//        getSeats();
//
//  }

//     List <Seat> getSeats(){
//        List <Seat> seats=new ArrayList<Seat>();
//        for (int i = 0; i < 1; i++) {
//            String type;
//            int remainder = (i + 1) % 8;
//            if (remainder == 1 || remainder == 4 || remainder == 7) {
//                type = "Lower";
//            } else if (remainder == 2 || remainder == 5) {
//                type = "Middle";
//            } else {
//                type = "Upper";
//            }
//            seats.add(new Seat(i + 1, type));
//        }
//        return (Iterator) seats;
//    }

    @Override
    Iterable<Seat> getSeats() {
//        Seat[] seats=new Seat[72];
        numSeats=72;
        List <Seat> seats=new ArrayList<Seat>();
        for (int i = 0; i < numSeats; i++) {
            String type;
            int remainder = (i + 1) % 8;
            if (remainder == 1 || remainder == 4 || remainder == 7) {
                type = "Lower";
            } else if (remainder == 2 || remainder == 5) {
                type = "Middle";
            } else {
                type = "Upper";
            }
            Seat a=new TrainSeat(i + 1, type);
            seats.add(a);
//            seats[i]=new Seat(i+1,type);
        }
        return (Iterable<Seat>) seats;
    }

    Iterable<Seat> getSeats(int numSeats) {
        this.numSeats=numSeats;
        List <Seat> seats=new ArrayList<Seat>();
        for (int i = 0; i < numSeats; i++) {
            String type;
            int remainder = (i + 1) % 8;
            if (remainder == 1 || remainder == 4 || remainder == 7) {
                type = "Lower";
            } else if (remainder == 2 || remainder == 5) {
                type = "Middle";
            } else {
                type = "Upper";
            }
            Seat a=new TrainSeat(i + 1, type);
            seats.add(a);
        }
        return (Iterable<Seat>) seats;
    }

//     List<String> getTypes(){
//         List<String> types=new ArrayList<String>();
//         types.add("Lower");
//         types.add("Middle");
//         types.add("Upper");
//         return types;
//    }
}
