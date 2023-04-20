package com.foxmo.rpc.protocol;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class URL implements Serializable {
    private String hostname;
    private Integer port;
}
