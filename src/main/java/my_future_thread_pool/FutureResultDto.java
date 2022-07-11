package my_future_thread_pool;

import lombok.Data;

@Data
public class FutureResultDto {

    private String msg;

    public FutureResultDto(String msg) {
        this.msg = msg;
    }
}
