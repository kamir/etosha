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

import java.io.OutputStream;
import java.util.Vector;

/**
 * 
 * @author kamir
 */
public class RuntimeTracker {
    
    long t0=0;
    long t1=0;
    
    public Vector<RTPoint> points = new Vector<RTPoint>(); 

    public void start() {
        t0=System.currentTimeMillis();
        points.add( new RTPoint( t0, "Start", "OK") );
    }
    
    public void stop() {
        t1=System.currentTimeMillis();
        points.add( new RTPoint( t1, "Stop", "OK") );
    }
    
    public void track( String event, String stats ) {
        points.add( new RTPoint( System.currentTimeMillis(), event, stats ) );
    }
    
    public void dump() {
        for( RTPoint p : points ) {
            System.out.println( "t: " + p.time + " => " + p.event + " : " + p.mssg );
        }
        System.out.println( "Total runtime: " + ( (t1- t0) / 1000 ) + " s" );
    }
    
}

    class RTPoint {

        long time;
        String event;
        String mssg;
        
        public RTPoint(long time, String event, String mssg) {     
            this.time = time;
            this.event = event;
            this.mssg = mssg;
        }
    }


