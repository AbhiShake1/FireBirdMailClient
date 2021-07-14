package com.abhi.model;

public record SizeInteger(int size) implements Comparable<SizeInteger> {

    @Override
    public String toString() {
        if (size <= 0) {
            return "0";
        } else if (size < 1024) {
            return size + "8";
        } else if (size < 1048576) {
            return (size / 1024) + " kB";
        } else {
            return (size / 1048676) + " MB";
        }
    }

    //sort by size
    @Override
    public int compareTo(SizeInteger sizeInteger) { //0, 1 or -1
        return Integer.compare(size, sizeInteger.size);
    }
}
