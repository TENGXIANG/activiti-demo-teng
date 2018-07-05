package com.teng.g_exclusiveGateWay;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

public class ExclusiveGateWayTest {

	//流程引擎
    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	
    /**
     * 部署流程定义(从InputStream)
     */
    @Test
    public void deploymentProcessDefinition_inputStream(){
    	InputStream inputStreamBpmn = this.getClass().getResourceAsStream("exclusiveGateWay.bpmn");
    	InputStream inputStreamPng = this.getClass().getResourceAsStream("exclusiveGateWay.png");   	
    	Deployment deployment =  processEngine.getRepositoryService()   //与流程定义和部署对象相关的Service，创建一个仓库对象
                .createDeployment()    //创建一个部署对象
                .name("排他网关")    //添加部署的名称
                .addInputStream("exclusiveGateWay.bpmn", inputStreamBpmn)
                .addInputStream("exclusiveGateWay.png", inputStreamPng).deploy(); //完成部署

       System.out.println("部署ID ： " + deployment.getId());  //12501
       System.out.println("部署名称 ： " + deployment.getName());  //流程定义	
    }
    
    /**
     * 启动流程实例
     */
    @Test
    public void startProcessInstance(){
        //流程定义key
        String processDefinitionKey = "exclusiveGateWay";
        ProcessInstance pi =  processEngine.getRuntimeService()     //与正在执行的流程实例和执行对象相关的Service
                .startProcessInstanceByKey(processDefinitionKey);   //使用流程定义的key启动流程实例，key对应helloworld.bpmn文件中id的属性值，使用key值启动，默认是按照最新版本的流程定义启动
        System.out.println("流程实例ID ： " + pi.getId());   //流程实例ID    2501
        System.out.println("流程定义ID ： " + pi.getProcessDefinitionId());  //流程定义ID    helloworld:1:4
    }
    
    /**
     * 查询当前人的个人任务
     */
    @Test
    public void findMyPersonalTask(){
        String assignee = "王小五"; //张三 > 李四 > 王五
        List<Task> list = processEngine.getTaskService()  //与正在执行的任务管理相关的Service
                .createTaskQuery()  //创建任务查询对象
                .taskAssignee(assignee)     //指定个人任务查询，指定办理人，相当于查询act_ru_task 
//              .taskCandidateUser(candidateUser)	//组任务的办理人查询
//              .processDefinitionId(processDefinitionId)//使用流程定义ID查询
//              .processInstanceId(processInstanceId)//使用流程实例ID查询
//              .executionId(executionId)//使用执行对象ID查询
                /**
                 * 排序
                 */
                .orderByTaskCreateTime().asc()//使用创建时间的升序排列
                /**
                 * 返回结果集
                 */
//              .singleResult()//返回唯一结果集
//              .count()//返回结果集的数量
//              .listPage(firstResult, maxResults)//分页查询
                .list();//返回列表

        if(list != null && list.size() > 0){
            for(Task task : list){
                System.out.println("任务ID ： " + task.getId()); //2504 > 5002 > 7502
                System.out.println("任务名称 ： " + task.getName()); //提交申请
                System.out.println("任务的创建时间 ： " + task.getCreateTime()); //Tue Jun 05 15:57:11 CST 2018
                System.out.println("任务的办理人 ： " + task.getAssignee()); //张三
                System.out.println("流程实例ID ： " + task.getProcessInstanceId()); //2501
                System.out.println("执行对象ID ： " + task.getExecutionId()); //2501
                System.out.println("流程定义ID ： " + task.getProcessDefinitionId()); //helloworld:1:4
                System.out.println("#######################################");
            }
        }
    }
    
    /**
     * 完成我的任务
     */
    @Test
    public void completeMyPersonalTask(){
        String taskId = "115004";
        //完成任务的同时，设置流程变量，使用流程变量用来指定完成任务后，下一个连线，对应exclusiveGateWay.bpmn文件中${money > 1000}
        Map<String,Object> variables = new HashMap<>();
        variables.put("money", 200);
        processEngine.getTaskService()	//与正在执行的任务管理相关的Service （同act_ru_task数据）
                     .complete(taskId, variables);
        System.out.println("完成任务，任务ID ： " + taskId);
    }

}

