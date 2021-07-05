import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class MyHashMap<K,V> implements Map<K,V> {
    class Node<K,V> implements Entry<K,V>{
        K key;
        V value;
        Node next;
        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.next=null;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        public Node getNext() {
            return next;
        }

        public void setNext(Node next) {
            this.next = next;
        }


        @Override
        public V setValue(V value) {
            V old=this.value;
            this.value=value;
            return old;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node<?, ?> node = (Node<?, ?>) o;
            return Objects.equals(key, node.key);
        }

//        Node insert(Node head,Node toInsert){
//            if(head==null){
//                head=toInsert;
//                return head;
//            }
//            head.next=insert(head.next,toInsert);
//            return head;
//        }
//
//        Node remove(Node head,Node toRemove){
//            if(head.equals(toRemove)){
//                head=head.next;
//                return head;
//            }
//            head.next=remove(head.next,toRemove);
//            return head;
//        }



    }

    static private Object sharedLock=new Object();
    volatile protected Integer size=0;
    volatile int numBuckets=4;
    float loadFactor= 0.75F;

    Node<K,V>[] keys;
    Lock lock = new ReentrantLock();

    private int nearestNumBuckets(int n){
        int count=1;
        System.out.println(n);
        while(n/2>1){
            count++;
            n/=2;
        }
        System.out.println(count);
        return 2^count;
    }
   public MyHashMap(){
       keys=(Node<K,V>[]) new Node[numBuckets];
       for (int i = 0; i < numBuckets; i++) {
           keys[i]=new Node<K,V>(null,null);
       }
   }
    public MyHashMap(int numBuckets){
        this.numBuckets=nearestNumBuckets(numBuckets);
        keys=(Node<K,V>[]) new Node[numBuckets];
        for (int i = 0; i < numBuckets; i++) {
            keys[i]=new Node<K,V>(null,null);
        }
    }
    public MyHashMap(int numBuckets, float loadFactor){
        this.numBuckets=nearestNumBuckets(numBuckets);
        this.loadFactor=loadFactor;
        keys=(Node<K,V>[]) new Node[numBuckets];
        for (int i = 0; i < numBuckets; i++) {
            keys[i]=new Node<K,V>(null,null);
        }
    }

    synchronized protected int hashFunction(Object key,int numBuckets){
        return key.hashCode() & (numBuckets-1);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size==0;
    }

    @Override
    public boolean containsKey(Object key) {
//        System.out.println("Here");
        int bucketNumber=hashFunction(key,numBuckets);
//        int a=numBuckets;
//        while(bucketNumber>=keys.length){
//            bucketNumber=hashFunction(key);
//        }
        Node toLook= keys[bucketNumber];

//        System.out.println("Inside containsKey");
//        System.out.println(bucketNumber);
//        if(keys[bucketNumber].next!=null) {
//            System.out.println(keys[bucketNumber].next.getKey());
//            System.out.println(keys[bucketNumber].next.getValue());
//        }
//        System.out.println("Exit containsKey");
//        if(toLook==null){
//            return false;
//        }
//        else{
        while(toLook!=null){
//            System.out.println(toLook.key);
            if(key.equals(toLook.key)){
                return true;
            }
            toLook=toLook.next;
        }
//        }
        return false;

    }

    @Override
    public boolean containsValue(Object value) {
        for (int i = 0; i < numBuckets; i++) {
            Node toLook=keys[i];
            while(toLook!=null) {
                if (value.equals(toLook.value)) {
                    return true;
                }
                toLook = toLook.next;
            }
        }
        return false;
    }

    @Override
    public V get(Object key) {
        int bucketNumber=hashFunction(key,numBuckets);
        Node toLook= keys[bucketNumber];

        while (toLook != null) {
            if (key.equals(toLook.key)) {
                return (V) toLook.value;
            }
            toLook = toLook.next;
        }
//        }
//        if(toLook==null){
//            return null;
//        }
//        else{
////            synchronized (keys[bucketNumber]) {
//                while (toLook != null) {
//                    if (key.equals(toLook.key)) {
////                        synchronized (toLook) {
//                            return (V) toLook.value;
////                        }
//                    }
//                    toLook = toLook.next;
//                }
////            }
//        }
        return null;
    }

    void rehash(){
//       synchronized (myHashMap.class) {
//           System.out.println(g);
//           numBuckets *= 2;
//           numBuckets+=1;
        Node<K, V>[] newKeys = (Node<K, V>[]) new Node[numBuckets*2];
        for (int i = 0; i < numBuckets; i++) {
            newKeys[i]=keys[i];
            newKeys[i+numBuckets]=keys[i];
        }


//        int copynumBuckets=numBuckets;

        for (int i = 0; i <numBuckets; i++) {
           synchronized (keys[i]) {
               newKeys[numBuckets+i]=new Node<>(null,null);
               newKeys[i]=new Node<>(null,null);
               Node head = keys[i].next;
               while (head != null) {
                   int bucketNum = hashFunction(head.key,  2*numBuckets);
                   Node toLook = newKeys[bucketNum];
    //                   if (toLook == null) {
    //                       newKeys[bucketNum] = new Node(head.key, head.value);
    //                       break;
    //                   }
                   while (toLook.next != null) {
                       toLook = toLook.next;
                   }
                   toLook.next = new Node(head.key, head.value);
                   head = head.next;
               }
           }

       }
        keys = newKeys;
        numBuckets*=2;
        System.out.println(numBuckets+" "+size);


//       }
    }
    @Override
    public V put(K key, V value) {
        int bucketNumber=hashFunction(key,numBuckets);
        synchronized (keys[bucketNumber]){
            Node toLook=keys[bucketNumber];
            while (toLook.next != null) {
                if (key.equals(toLook.key)) {
                    V old;
                    old = (V) toLook.value;
                    toLook.value =  value;
                    return old;
                }
                toLook = toLook.next;
            }
            if (key.equals(toLook.key)) {
                V old;
                //                    synchronized (toLook) {
                old = (V) toLook.value;
                toLook.value =  value;
                //                    }
                return old;
            }
            toLook.next = new Node( key, value);

            size=size+1;
        }
//        if(keys[bucketNumber]==null){//what if two threads are here?
//            synchronized (sharedLock) {
//                if(keys[bucketNumber]!=null){
//                    synchronized (keys[bucketNumber]){//Cannot enter synchronized block because "this.keys[bucketNumber]" is null
//                        Node toLook=keys[bucketNumber];
//                        if(toLook==null){
//                            keys[bucketNumber] = new Node(key, value);
////                size++;
//                        }
//                        else {
//                            while (toLook.next != null) {
//                                if (key.equals(toLook.key)) {
//                                    V old;
//                                    //synchronized (toLook) {//what happens if the node is removed before the statement?????
//                                    old = (V) toLook.value;
//                                    toLook.value = (V) value;
//                                    //}
//                                    return old;
//                                }
//                                toLook = toLook.next;
//                            }
//                            if (key.equals(toLook.key)) {
//                                V old;
//                                //                    synchronized (toLook) {
//                                old = (V) toLook.value;
//                                toLook.value = (V) value;
//                                //                    }
//                                return old;
//                            }
//                            toLook.next = new Node((K) key, (V) value);
//                        }
//                        size++;
//                    }
//                }
//                else {
//                    keys[bucketNumber] = new Node(key, value);
//                    size++;
//                }
//            }
//            if((float)size/numBuckets>loadFactor){
//                rehash();
//            }
//            return null;
//        }
//        synchronized (keys[bucketNumber]) {
//            Node toLook=keys[bucketNumber];
//            if(toLook==null){
//                keys[bucketNumber] = new Node(key, value);
////                size++;
//            }
//            else {
//                while (toLook.next != null) {
//                    if (key.equals(toLook.key)) {
//                        V old;
//                        System.out.println("dkwejd");
//                        //synchronized (toLook) {//what happens if the node is removed before the statement?????
//                        old = (V) toLook.value;
//                        toLook.value = (V) value;
//                        //}
//                        return old;
//                    }
//                    toLook = toLook.next;
//                }
//                if (key.equals(toLook.key)) {
//                    V old;
//                    //                    synchronized (toLook) {
//                    old = (V) toLook.value;
//                    toLook.value = (V) value;
//                    //                    }
//                    return old;
//                }
//                toLook.next = new Node((K) key, (V) value);
//            }
//            size++;
//        }
        if ((float) size / numBuckets > loadFactor) {
            if (lock.tryLock()) {
                try {
                    rehash();
                } finally {
                    lock.unlock();
                }
            }
        }
//        if ((float) size / numBuckets > loadFactor) {
//            synchronized (sharedLock) {
//                if ((float) size / numBuckets > loadFactor) {
//                    rehash();
//                }
//            }
//        }
        return null;
    }

    @Override
    public V remove(Object key) {
        int bucketNumber = hashFunction(key,numBuckets);

        Node toLook = keys[bucketNumber];
        synchronized (keys[bucketNumber]) {
//            if (key.equals(toLook.key)) {
//                keys[bucketNumber] = toLook.next;
//                size--;
//                return (V) toLook.value;
//            }
            while (toLook.next != null) {
                if (toLook.next.key.equals(key)) {
                    V old = (V) toLook.next.value;
                    toLook.next = toLook.next.next;
//                toLook.v=(V)value;
                    size--;
                    return old;
                }
                toLook=toLook.next;
            }
        }
        return null;
//        if(containsKey(key)){
//            int bucketNumber = hashFunction(key);
//            V val;
//
//            synchronized (sharedLock) {
//                val = get(key);
//                Node a = new Node(key, val);
//                keyBuckets[bucketNumber].remove(a);
//                size--;
//            }
//            return val;
//        }

    }

    @Override
    public void putAll(Map m) {

    }

    @Override
    public void clear() {
        for (int i = 0; i < numBuckets; i++) {
            synchronized (keys[i]) {
                keys[i].next = null;
            }
        }
        size=0;
    }

    @Override
    public Set keySet() {
       Set <K> keySet=new HashSet<>();
        for (int i = 0; i < numBuckets; i++) {
            Node<K,V> toLook=keys[i];
            while(toLook!=null){
                keySet.add(toLook.key);
                toLook=toLook.next;
            }

//            keySet.add()
        }
        return keySet;
//        return null;
    }

    @Override
    public Collection<V> values() {
        Collection<V> list=new HashSet<>();
        for (int i = 0; i < numBuckets; i++) {
            Node<K,V> toLook=keys[i];
            while(toLook!=null){
                list.add(toLook.getValue());
                toLook=toLook.next;
            }

//            keySet.add()
        }
        return null;
    }

    @Override
    public Set<Entry<K,V>> entrySet() {
        Set <Entry<K,V>> keySet=new HashSet<>();
        for (int i = 0; i < numBuckets; i++) {
            Node<K,V> toLook=keys[i];
            while(toLook!=null){
                keySet.add(new Node<>(toLook.getKey(),toLook.getValue()));
                toLook=toLook.next;
            }

//            keySet.add()
        }
        return keySet;
//        return null;
    }
}
