import java.util.List;

public class test extends Thread{
    Transport myTrain;
    static int child,child1,child2,child3;
    public static void main(String[] args) {
        Transport myTrain = new Train("Shatabdi", "Pune", "Mumbai");
        myTrain.setType(new TrainSeatingArrangement(250));

        for (int i = 0; i < 1000; i++) {
            trial(i);
        }
//
//        System.out.println(myTrain.isAvailable());
//        myTrain.checkAvailability();
//        Passenger dummy=new Passenger("Kishan", "Male", 21,"Upper");
//        Integer c = myTrain.bookTicket(dummy);
//        System.out.println(c);
//        myTrain.getDetails(c);
//        myTrain.cancelTicket(c);
//        myTrain.getDetails(c);
//        System.out.println(myTrain.isAvailable());
//        Integer d = myTrain.bookTicket(new Passenger("Kishan", "Male", 21,"Upper"));
        for (int i = 0; i < 20;  i++) {
            Passenger dummy=new Passenger("Kishan"+i, "Male", 21,"Upper");
            Integer c=myTrain.bookTicket(dummy);
//            myTrain.checkLinkedHash();
//            if(c!=null)
//                System.out.println(i);
        }
//        myTrain.checkLinkedHash();
//////
//        myTrain.getDetails(d);
//        myTrain.cancelTicket(d);
////        if(myTrain.cancelTicket(c)){
////            System.out.println("Failed");
////        }
////        System.out.println(c);
//        myTrain.checkAvailability();


    }

    public static void trial(int i){
        Transport myTrain = new Train("Shatabdi"+i, "Pune", "Mumbai");
        Thread a=new Thread("child"){
            public void run(){
                Passenger dummy=new Passenger("Kishan", "Male", 21,"Upper");
//                myTrain.bookTicket(dummy);
                Integer c;
//                child=0;
                for (int i = 0; i < 2;  i++) {
                    c=myTrain.bookTicket(dummy);
//                    System.out.println(" "+getName());
                    if(c!=null) {
                        child++;
//                        myTrain.cancelTicket(c);
//                        if(!myTrain.isAvailable()){
////                            System.out.println("dkdkj");
//                        }
                    }
                }
//                System.out.println(" "+getName());
            }
        };
        Thread b=new Thread("child 1"){
            public void run(){
                Passenger dummy=new Passenger("Kishan", "Male", 21,"Upper");
//                myTrain.bookTicket(dummy);
                Integer c;
                int count=0;
                for (int i = 0; i < 2;  i++) {
                    c=myTrain.bookTicket(dummy);
//                    System.out.println(" "+getName());
                    if(c!=null) {
                        child1++;
//                        myTrain.cancelTicket(c);
//                        System.out.println(" "+getName());
                    }
                }

            }
        };
        Thread c=new Thread("child 2"){
            public void run(){
                Passenger dummy=new Passenger("Kishan", "Male", 21,"Upper");
//                myTrain.bookTicket(dummy);
                Integer c;
                int count=0;
                for (int i = 0; i < 2;  i++) {
                    c=myTrain.bookTicket(dummy);
//                    System.out.println(" "+getName());
                    if(c!=null){
                        child2++;
//                        myTrain.cancelTicket(c);
                    }

                }
//                System.out.println(" "+getName());
            }
        };
        Thread d=new Thread("child 3"){
            public void run(){
                Passenger dummy=new Passenger("Kishan", "Male", 21,"Upper");
//                myTrain.bookTicket(dummy);
                Integer c;
                int count=0;
                for (int i = 0; i < 2;  i++) {
                    c=myTrain.bookTicket(dummy);
//                    System.out.println(" "+getName());
                    if(c!=null){
                        child3++;

//                        myTrain.cancelTicket(c);
                    }
//                    System.out.println(" "+getName());
                }

            }
        };

        myTrain.setType(new TrainSeatingArrangement(30000));
        a.start();
        b.start();
        c.start();
        d.start();
        try {
            d.join();
            a.join();
            b.join();
            c.join();

//            sleep(1000);
            System.out.println(child+" "+child1+" "+child2+" "+child3+" "+(child+child3+child2+child1));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        myTrain=null;

    }

}


