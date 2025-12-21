/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaapplication53;

/**
 *
 * @author Mohamed Helal
 */

import java.util.Iterator;
import java.util.NoSuchElementException;

public class PermutationIterator implements Iterator<int[]> {
    private final int[] current = new int[5];
    private boolean hasNext = true;

    public PermutationIterator() {
        for (int i = 0; i < 5; i++) {
            current[i] = 1;
        }
    }

    @Override
    public boolean hasNext() {
        return hasNext;
    }

    @Override
    public int[] next() {
        if (!hasNext) {
            throw new NoSuchElementException();
        }
        int[] result = current.clone();
        increment();
        return result;
    }

    private void increment() {
        for (int i = 4; i >= 0; i--) {
            if (current[i] < 9) {
                current[i]++;
                return;
            } else {
                current[i] = 1;
            }
        }
        hasNext = false;
    }
}