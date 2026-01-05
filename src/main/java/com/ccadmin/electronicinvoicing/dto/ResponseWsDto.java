package com.ccadmin.electronicinvoicing.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class ResponseWsDto<T> {
    public boolean ok;
    public String message;
    public String code;
    public T data;
    public List<String> errors = new ArrayList<>();
}
