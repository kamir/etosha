/*
 * Copyright 2016 kamir.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cloudera.utils;

import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

/**
 *
 * @author kamir
 */
public class StatisticalSummary {
    
    public SummaryStatistics s = new SummaryStatistics();
    public Frequency f = new Frequency();
    
    public void addValue( Double v ) {
        s.addValue(v);
        f.addValue(v);
    }
    
}
