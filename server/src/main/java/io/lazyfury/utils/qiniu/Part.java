package io.lazyfury.utils.qiniu;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Part {
    public String etag;
    public long partNumber;
}
