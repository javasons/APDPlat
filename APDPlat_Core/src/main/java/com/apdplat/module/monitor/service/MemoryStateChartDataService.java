/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.apdplat.module.monitor.service;

import com.apdplat.module.monitor.model.MemoryState;
import com.apdplat.module.monitor.model.ProcessTime;
import com.apdplat.platform.log.APDPlatLogger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 *
 * @author ysc
 */
public class MemoryStateChartDataService {
    protected static final APDPlatLogger log = new APDPlatLogger(MemoryStateChartDataService.class);

    public static List<MemoryState> getSequenceDataHH(List<MemoryState> models){    
        return getSequenceTimeData(models,"yyyy-MM-dd HH");
    }
    public static List<MemoryState> getSequenceDataDD(List<MemoryState> models){    
        return getSequenceTimeData(models,"yyyy-MM-dd");
    }
    public static List<MemoryState> getSequenceDataMonth(List<MemoryState> models){    
        return getSequenceTimeData(models,"yyyy-MM");
    }
    private static List<MemoryState> getSequenceTimeData(List<MemoryState> models,String format){        
        LinkedHashMap<String,MemoryState> temp=new LinkedHashMap<>();
        //将日志数据转换为统计报表数据
        for(MemoryState item : models){
            String key=new SimpleDateFormat(format).format(item.getRecordTime());
            MemoryState value=temp.get(key);
            if(value==null){
                value=item;
            }else{
                //几次采集的内存数据，根据 已分配内存 来判断，谁大说明谁的内存使用情况最糟糕
                value=value.getTotalMemory()>item.getTotalMemory()?value:item;
            }
            
            temp.put(key,value);
        } 
        List<MemoryState> list=new ArrayList<>();
        for(MemoryState value : temp.values()){
            list.add(value);
        }
        return list;
    }

    /**
     * 同一命令只留最耗时的命令
     * @param models
     * @return 
     */
    private static List<ProcessTime> mini(List<ProcessTime> models) {
        LinkedHashMap<String,ProcessTime> LinkedHashMap=new LinkedHashMap<>();
        for(ProcessTime item : models){
            ProcessTime value=LinkedHashMap.get(item.getResource());
            if(value==null){
                value=item;
            }else{
                value=value.getProcessTime()>item.getProcessTime()?value:item;
            }
            LinkedHashMap.put(item.getResource(), value);
        }
        List<ProcessTime> list=new ArrayList<>();
        for(ProcessTime item : LinkedHashMap.values()){
            list.add(item);
        }
        return list;
    }
}