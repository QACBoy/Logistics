package com.example.scandemo5;

import java.util.ArrayList;

/**
 * Created by Sam on 2017/8/22.
 */

public class JMap<K,V> {

        private class Item<K,V>{
            K key;
            V value;

            public Item(K key , V value){
                this.key = key;
                this.value = value;
            }

            public K getKey(){
                return key;
            }
            public V getValue(){
                return value;
            }
        }

        private ArrayList<Item> items;


        public JMap(){
            items = new ArrayList<>();

        }


        public void add(K key,V value){
            if(!isKeyExist(key)) {
                Item<K, V> item = new Item<>(key, value);
                items.add(item);
            }else {
                set(key,value);
            }
        }

        public V get(K key){
            for (int i = 0 ;i<items.size();i++){
                Item<K,V> item = items.get(i);
                if(item.getKey().equals(key)){
                    return item.getValue();
                }
            }
            return null;
        }

        private void set(K key,V value){
            for (int i = 0 ;i<items.size();i++){
                Item<K,V> item = items.get(i);
                if(item.getKey().equals(key)){
                    item.value = value;
                }
            }
        }

        public boolean isKeyExist(K key){
            for (int i = 0 ;i<items.size();i++){
                Item<K,V> item = items.get(i);
                if(key.equals(item.getKey())){
                    return true;
                }
            }
            return false;
        }

        public int size(){
            return items.size();
        }



}
