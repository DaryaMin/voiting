package ru.javaops.topjava.to;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class MenuTo extends NamedTo {

    int price;

    public MenuTo(Integer id, String name, int price) {
        super(id, name);
        this.price = price;
    }
}
