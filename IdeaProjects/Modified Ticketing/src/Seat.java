public abstract class Seat {
    private String seatType;
    private Integer number;
    private boolean isBooked = false;
    private Passenger info;

    public Seat(int number, String seatType) {
        this.number = number;
        this.seatType = seatType;
    }
    public boolean isBooked() {
        return isBooked;
    }

    public void BookSeat() {
        isBooked = true;
    }
    public void cancelSeat() {
        isBooked = false;
    }

    public void setInfo(Passenger info) {
        this.info = info;
    }

    public String getSeatType() {
        return seatType;
    }

    public Integer getNumber() {
        return number;
    }
    public void printDetails(){
//        System.out.println("Seat Number: "+number+" Seat Type: "+seatType);
        info.printDetails();
    }
}
