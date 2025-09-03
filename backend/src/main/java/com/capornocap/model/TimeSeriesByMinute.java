package com.capornocap.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimeSeriesByMinute {
    public long ts;
    public long logins;
    public long logouts;
    public long quizStarted;
}
