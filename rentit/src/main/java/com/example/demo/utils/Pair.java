package com.example.demo.utils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@NoArgsConstructor(force=true,access= AccessLevel.PRIVATE)
@AllArgsConstructor
public class Pair<Fst, Snd> {

    Fst first;

    Snd second;

    @Override public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Pair)) return false;
        Pair other = (Pair) o;
        return first.equals(other.first) && second.equals(other.second);
    }

}
