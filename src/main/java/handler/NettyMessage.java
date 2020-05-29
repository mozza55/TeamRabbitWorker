package handler;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class NettyMessage {

    private byte messageType;
    private byte taskType;
    private int length;
    private String body;
}
