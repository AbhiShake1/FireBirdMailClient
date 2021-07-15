package com.abhi.model;

public final record SizeInteger(int size) implements Comparable<SizeInteger> {
//records are not meant to be altered

    @Override
    public final String toString() {
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
    public final int compareTo(SizeInteger sizeInteger) { //0, 1 or -1
        return Integer.compare(size, sizeInteger.size);
    }
}
