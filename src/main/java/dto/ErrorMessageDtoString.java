package dto;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor

public class ErrorMessageDtoString {
    private String timestamp;
    private int status;
    private String error;
    private Object message;
    private String path;
}