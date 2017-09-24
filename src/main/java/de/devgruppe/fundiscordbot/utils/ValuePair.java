package de.whynotminecraft.timerankupdater.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ValuePair<K, V> {
    private K key;
    private V value;
}
