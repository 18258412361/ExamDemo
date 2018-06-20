package com.migu.schedule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.migu.schedule.constants.ReturnCodeKeys;
import com.migu.schedule.info.TaskInfo;

/*
*类名和方法不能修改
 */
public class Schedule
{
    
    private TreeMap<Integer, TreeMap<Integer, Integer>> nodes;
    
    private TreeMap<Integer, Integer> waiting;
    
    private TreeMap<Integer, Integer> running;
    
    public int init()
    {
        // TODO 方法未实现
        nodes = new TreeMap<Integer, TreeMap<Integer, Integer>>(
            new Comparator<Integer>()
            {
                
                public int compare(Integer o1, Integer o2)
                {
                    return o2.compareTo(o1);
                }
            });
        waiting = new TreeMap<Integer, Integer>();
        running = new TreeMap<Integer, Integer>();
        return ReturnCodeKeys.E001;
    }
    
    public int registerNode(int nodeId)
    {
        // TODO 方法未实现
        if (nodeId <= 0)
        {
            return ReturnCodeKeys.E004;
        }
        if (nodes.containsKey(nodeId))
        {
            return ReturnCodeKeys.E005;
        }
        nodes.put(nodeId, null);
        return ReturnCodeKeys.E003;
    }
    
    public int unregisterNode(int nodeId)
    {
        // TODO 方法未实现
        if (nodeId <= 0)
        {
            return ReturnCodeKeys.E004;
        }
        if (!nodes.containsKey(nodeId))
        {
            return ReturnCodeKeys.E007;
        }
        if (null != nodes.get(nodeId))
        {
            for (Entry<Integer, Integer> entry : nodes.get(nodeId).entrySet())
            {
                waiting.put(entry.getKey(), entry.getValue());
            }
        }
        nodes.remove(nodeId);
        return ReturnCodeKeys.E006;
    }
    
    public int addTask(int taskId, int consumption)
    {
        // TODO 方法未实现
        if (taskId <= 0)
        {
            return ReturnCodeKeys.E009;
        }
        if (waiting.containsKey(taskId) || running.containsKey(taskId))
        {
            return ReturnCodeKeys.E010;
        }
        
        waiting.put(taskId, consumption);
        return ReturnCodeKeys.E008;
    }
    
    public int deleteTask(int taskId)
    {
        // TODO 方法未实现
        if (taskId <= 0)
        {
            return ReturnCodeKeys.E009;
        }
        if (!waiting.containsKey(taskId) && !running.containsKey(taskId))
        {
            return ReturnCodeKeys.E012;
        }
        return ReturnCodeKeys.E011;
    }
    
    public int scheduleTask(int threshold)
    {
        // TODO 方法未实现
        if (threshold <= 0)
        {
            return ReturnCodeKeys.E002;
        }
        if (!waiting.isEmpty())
        {
            List<Map.Entry<Integer, Integer>> list =
                new ArrayList<Map.Entry<Integer, Integer>>(waiting.entrySet());
            Collections.sort(list, new Comparator<Map.Entry<Integer, Integer>>()
            {
                public int compare(Entry<Integer, Integer> o1,
                    Entry<Integer, Integer> o2)
                {
                    
                    // 升序排序
                    return o1.getValue().compareTo(o2.getValue());
                }
            });
            List<Integer> nodeList = new ArrayList<Integer>(nodes.keySet());
            int index = 0;
            for (Entry<Integer, Integer> entry : list)
            {
                TreeMap<Integer, Integer> tasks;
                
                int lastIndex = index % nodes.size();
                if (null != nodes.get(nodeList.get(lastIndex)))
                {
                    tasks = nodes.get(nodeList.get(lastIndex));
                }
                else
                {
                    tasks = new TreeMap<Integer, Integer>();
                }
                tasks.put(entry.getKey(), entry.getValue());
                nodes.put(nodeList.get(lastIndex), tasks);
                index++;
            }
            running.putAll(waiting);
            scheduleTask(nodes, threshold);
        }
        return ReturnCodeKeys.E013;
    }
    
    public int queryTaskStatus(List<TaskInfo> tasks)
    {
        // TODO 方法未实现
        if(null == tasks)
        {
            return ReturnCodeKeys.E016;
        }
        for(Integer nodeId: nodes.keySet())
        {
            TreeMap<Integer, Integer> map =  nodes.get(nodeId);
            for(Integer taskId: map.keySet())
            {
                TaskInfo info =  new TaskInfo();
                info.setNodeId(nodeId);
                info.setTaskId(taskId);
                tasks.add(info);
            }
        }
        return ReturnCodeKeys.E015;
    }
    
    private void scheduleTask(TreeMap<Integer, TreeMap<Integer, Integer>> nodes, int threshold)
    {
        
    }
}
