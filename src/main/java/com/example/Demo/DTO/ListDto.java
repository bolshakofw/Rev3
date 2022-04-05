package com.example.Demo.DTO;

import java.util.List;

public class ListDto {
    private final int count;
    private final List<?> spisok;

    public ListDto(List<?> spisok){
        count = spisok.size();
        this.spisok = spisok;
    }

}
