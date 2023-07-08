package socks.shop.coursework3.models;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Operations {

    private OperationType type;
    private LocalDateTime dateTime;
    private int quantity;
    private Size size;
    private int cottonPart;
    private Color color;
}
