import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MyLinkedHashMap<K,V> extends MyHashMap<K,V> implements Map<K,V> {
    Entry<K,V> next,current;
    public Iterator<K> myIterator(){
        next= (Entry<K, V>) head;
        current=null;
        return (new Iterator<>() {
            @Override
            public boolean hasNext() {
                return next!=null;
            }

            @Override
            public K next() {
                Entry<K,V> temp=(Entry<K, V>) current;
                current=next;
//                temp=(Entry<K, V>) next;
                next=(Entry<K, V>) next.nextEntry;
                return current.getKey();
            }

        });
    }
//    public Iterator<Map.Entry> iterator(){
//        next= head;
//        current=null;
//        return (new Iterator<>() {
//            @Override
//            public boolean hasNext() {
//                return next!=null;;
//            }
//
//            @Override
//            public Map.Entry next() {
//                Entry<K,V> temp=(Entry<K, V>) current;
//                current=temp.nextEntry;
//                temp=(Entry<K, V>) temp.nextEntry;
//                next=temp.nextEntry;
//                return current;
//            }
//            public void remove() {
//                if(current==null){
//                    return;
//                }
//                remove(current.key);
//                current=null;
//            }
//        });
//    }



    class Entry<K,V> extends Node<K,V>{
        Node<K,V> nextEntry;
        Node<K,V> prevEntry;

        Entry(K key,V value,Node prev,Node next){
            super(key,value);
            this.prevEntry=prev;
            this.nextEntry=next;
        }

        Node getPrev(Node a){
            Entry x=(Entry) a;
            return x.prevEntry;
        }

        Node getNext(Node a){
            Entry x=(Entry) a;
            return x.nextEntry;
        }


    }
    Lock lock = new ReentrantLock();
    final Object sharedLock=new Object();
    Node<K,V> head;
    Node<K,V> tail=new Entry<>(null,null,null,null);
//    Node<K,V>[] keys=new Entry[numBuckets];

    MyLinkedHashMap(){
        super();
        keys=(Node<K, V>[]) new Entry[numBuckets];
        for (int i = 0; i < numBuckets; i++) {
            keys[i]=new Entry<K,V>(null,null,null,null);
        }
    }
    MyLinkedHashMap(int numBuckets){
        super(numBuckets);
    }
    MyLinkedHashMap(int numBuckets,float loadFactor){
        super(numBuckets,loadFactor);
    }

    @Override
    public V put(K key, V value) {
        int numBucket=hashFunction(key,numBuckets);
        Node<K,V> toLook=keys[numBucket];
        synchronized (keys[numBucket])
        {
            while (toLook.next != null) {
//                toLook = toLook.next;
                if (key.equals(toLook.getKey())) {
                    V old = toLook.getValue();
                    toLook.setValue(value);
                    return old;
                }
                toLook=toLook.next;
            }
            synchronized (tail) {
                Node<K, V> toAdd = new Entry(key, value, tail, null);
                Entry<K,V> temp=(Entry<K, V>)tail;
                Entry<K,V> temp1=(Entry<K, V>)toAdd;
                Node<K,V> x=temp1.prevEntry;
                temp.nextEntry=toAdd;
                tail = toAdd;
                toLook.next = toAdd;
                if(head==null){
                    head=toAdd;
                }
            }
//            System.out.println(head.getKey());
//            System.out.println(tail.getKey());
//            Entry<K,V> temp=(Entry<K, V>) head;
//            while (temp!=null){
//                System.out.println(temp.getKey());
//                temp=(Entry<K, V>) temp.nextEntry;
//            }
        }
        size++;
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

//        System.out.println("Inside put"+numBucket);
//        System.out.println(keys[numBucket].next.getKey());
//        System.out.println(keys[numBucket].next.getValue());
//        System.out.println("Exit put");
        return null;
    }

    @Override
    public V remove(Object key) {
        int numBucket=hashFunction(key,numBuckets);
        Entry<K,V> toLook=(Entry<K, V>) keys[numBucket];

        synchronized (keys[numBucket]){
            while (toLook.next != null) {
                if (toLook.next.key.equals(key)) {
                    V old = (V) toLook.next.value;
                    toLook.next = toLook.next.next;
                    size--;

                    if(toLook.prevEntry==null){
                        head=toLook.nextEntry;
                        return old;
                    }
                    synchronized (toLook.prevEntry){
                        toLook.prevEntry.setNext(toLook.nextEntry);
                        if(toLook==tail){
                            tail=toLook.prevEntry;
                        }
                    }


                    return old;
                }
                toLook=(Entry<K, V>)toLook.getNext();
            }
        }
        return null;
    }

    void rehash(){
//       synchronized (myHashMap.class) {
//           System.out.println(g);
//           numBuckets *= 2;
//           numBuckets+=1;
        Node<K, V>[] newKeys = (Entry<K, V>[]) new Entry[numBuckets*2];
        for (int i = 0; i < numBuckets; i++) {
            newKeys[i]=keys[i];
            newKeys[i+numBuckets]=keys[i];
        }

        for (int i = 0; i < numBuckets; i++) {
            synchronized (keys[i]) {
                Entry<K,V> temp=(Entry<K, V>) keys[i].next;
                newKeys[numBuckets+i]=new Entry<>(null,null,null,null);
                newKeys[i]=new Entry<>(null,null,null,null);
                Entry<K,V> head = temp;
                while (head != null) {

                    int bucketNum = hashFunction(head.key,  2*numBuckets);
                    Node toLook = (Entry<K, V>) newKeys[bucketNum];
                    //                   if (toLook == null) {
                    //                       newKeys[bucketNum] = new Node(head.key, head.value);
                    //                       break;
                    //                   }
                    while (toLook.getNext() != null) {
                        toLook = toLook.getNext();
                    }
//                    Entry<K,V> copy=(Entry<K, V>) head;
                    toLook.next = head;

                    toLook = head.getNext();
                    head.next=null;
                    head=(Entry<K, V>) toLook;
                }
            }

        }
        keys = newKeys;
        numBuckets*=2;
        System.out.println(numBuckets+" "+size);
//       }
    }


}
