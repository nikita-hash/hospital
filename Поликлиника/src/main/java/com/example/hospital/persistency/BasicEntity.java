package com.example.hospital.persistency;

import java.io.Serializable;

public interface BasicEntity <T extends Serializable>{

    T getId();

    void setId(T id);
}
