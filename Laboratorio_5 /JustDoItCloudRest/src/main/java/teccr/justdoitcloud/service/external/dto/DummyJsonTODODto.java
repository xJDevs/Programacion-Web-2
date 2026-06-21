package teccr.justdoitcloud.service.external.dto;

import lombok.Data;

@Data
public class DummyJsonTODODto {
    private Integer id;
    private String todo;
    private Boolean completed;
    private Integer userId;
}