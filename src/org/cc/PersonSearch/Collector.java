package org.cc.PersonSearch;

import java.util.IdentityHashMap;
import java.util.Map;

/**
 * Created by Cristian on 26/04/16.
 */

/*
                    !!!!!!WARNING!!!!!!
This class is not for general-purpose
This class is designed for use only in the rare cases wherein reference-equality semantics are required.

 */
public class Collector<K,V extends Comparable> extends IdentityHashMap<K,V> {

    int max; //max number of object in collector
    int currentSize; //current number of objects added
    K currentMinObject; //current object with smallest value
    V currentMinValue; //current smallest value
    public Collector(int max) {


        super(max+2); //when does reshasing take place? javadoc unclear, check code
        this.currentSize = 0;
        this.max = max;
        this.currentMinObject = null;
        this.currentMinValue = null;
    }


    public boolean offer(K k, V v) {

        //if(super.containsKey(k)) return false; // it is an identity map so..

        if(currentSize >= max) {
            //maybe we add, maybe we don't!

            if(v.compareTo(currentMinValue) == 1 ) {

                super.remove(currentMinObject);
                super.put(k,v);

                //smallest now?
                this.currentMinObject = k; //temporary, not necessarily true
                this.currentMinValue = v; //temporary, not necessarily true


                for (Map.Entry<K, V> entry : this.entrySet()) {
                    K key = entry.getKey();
                    V value = entry.getValue();

                    if(value.compareTo(currentMinValue) == -1) {currentMinObject = key; currentMinValue = value; }
                }

                return true;

            } else {

                return false;
            }




        } else {
            //we will add, but must check which object/value now is the smallest

            if(currentMinValue == null) { currentMinValue= v; currentMinObject = k; } else {

                if(v.compareTo( currentMinValue ) == -1) { currentMinValue = v; currentMinObject = k; }


            }

            super.put(k, v);
            currentSize++;
            return true;
        }
    }




}
