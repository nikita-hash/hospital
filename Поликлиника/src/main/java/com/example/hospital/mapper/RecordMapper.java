package com.example.hospital.mapper;

import com.example.hospital.dto.DoctorRecordsListDto;
import com.example.hospital.model.Record;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

@Mapper
public interface RecordMapper {

    RecordMapper INSTANCE= Mappers.getMapper(RecordMapper.class);

    @Mapping(target = "id",source = "id")
    @Mapping(target = "dayOfWeek", expression = "java(customMappingDayOfWeek(records))")
    DoctorRecordsListDto  recordToDoctorRecordsListDto(Record records);

    default String customMappingDayOfWeek(Record record){
        String res=record.getDate().getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("ru"))+" "+record.getTime();
        return res;
    }
}
