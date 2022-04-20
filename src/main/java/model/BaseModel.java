package model;

import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor

public abstract class BaseModel {

    protected UUID id;
    protected String name;
    protected Date createdDate;
    protected Date updatedDate;
    protected UUID createdBy; //
    protected UUID updatedBy;
    protected boolean isActive;

    {
        this.id = UUID.randomUUID();
        this.createdDate = new Date();
    }


}
