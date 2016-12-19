package com.comsysto.metagrapher.annotations;

import java.util.Arrays;

public enum MetagrapherServiceType {
    IMPORT, EXPORT, CONTEXT_DEPENDENT;

    public MetagrapherServiceType merge(MetagrapherServiceType type){
        if(this == type){
            return type;
        }
        if(this == CONTEXT_DEPENDENT){
            return type;
        }

        if(type == CONTEXT_DEPENDENT){
            return this;
        }

        throw new IllegalArgumentException("Incompatible service types: " + Arrays.asList(type, this));
    }

}
