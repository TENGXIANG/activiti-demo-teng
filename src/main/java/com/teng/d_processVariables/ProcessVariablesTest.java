package com.teng.d_processVariables;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipInputStream;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

public class ProcessVariablesTest {
	//流程引擎
    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	
    /**
     * 部署流程定义(方式二：从InputStream)
     */
    @Test
    public void deploymentProcessDefinition_inputStream(){
    	InputStream inputStreambpmn = this.getClass().getResourceAsStream("/diagrams/processVariables.bpmn");
    	InputStream inputStreampng = this.getClass().getResourceAsStream("/diagrams/processVariables.png");   	
    	Deployment deployment =  processEngine.getRepositoryService()   //与流程定义和部署对象相关的Service，创建一个仓库对象
                .createDeployment()    //创建一个部署对象
                .name("流程定义")    //添加部署的名称
                .addInputStream("processVariables.bpmn", inputStreambpmn)//使用资源文件的名称（要求：与资源文件的名称要一致）
                .addInputStream("processVariables.png", inputStreampng)//使用资源文件的名称（要求：与资源文件的名称要一致）
                .deploy(); //完成部署
       System.out.println("部署ID ： " + deployment.getId());  //
       System.out.println("部署名称 ： " + deployment.getName());  //流程定义	
    }
    
    /**
     * 启动流程实例
     */
    @Test
    public void startProcessInstance(){
        //流程定义key
        String processDefinitionKey = "processVariables";
        ProcessInstance pi =  processEngine.getRuntimeService()     //与正在执行的流程实例和执行对象相关的Service
                .startProcessInstanceByKey(processDefinitionKey);   //使用流程定义的key启动流程实例，key对应helloworld.bpmn文件中id的属性值，使用key值启动，默认是按照最新版本的流程定义启动
        System.out.println("流程实例ID ： " + pi.getId());   //流程实例ID
        System.out.println("流程定义ID ： " + pi.getProcessDefinitionId());  //流程定义ID
    }

    /**
     * 设置流程变量
     */
    @Test
    public void setVariables() {
    	/**
    	 * 与任务（正在执行）
    	 */
    	TaskService taskService = processEngine.getTaskService();
    	//任务ID
    	String taskId = "52504";
    	/**
    	 * 一、设置流程变量，使用基本数据类型
    	 */												//注：没绑定任务ID的，重新设置，直接覆盖，版本+1
//    	taskService.setVariableLocal(taskId, "请假天数", 5);//（项目开发建议不用Local）Local 表示与当前任务ID绑定,若此任务ID变化，则获取不到
//    	taskService.setVariable(taskId, "请假日期", new Date());
//    	taskService.setVariable(taskId, "请假原因", "回家探亲，一起吃个饭");
    	/**
    	 * 二、设置流程变量，使用javabean类型
    	 */
    	/**
    	 * 当一个javabean（实现序列号）放置到流程变量中，要求javabean的属性不能再发生变化
    	 * 如果发生变化，再获取的时候，抛出异常
    	 * 
    	 * 解决方案：在Person对象中添加：
    	 * 		private static final long serialVersionUID = -1426484919423478323L;
    	 * 		同时实现  Person implements Serializable
    	 */
    	Person p = new Person();
    	p.setId(20);
    	p.setName("小花");
    	taskService.setVariable(taskId, "人员信息（添加固定版本）", p);
    	
    	System.out.println("设置流程变量成功！");
    }
    
    /**
     * 获取流程变量
     */
    @Test
    public void getVariables() {
    	/**
    	 * 与任务（正在执行）
    	 */
    	TaskService taskService = processEngine.getTaskService();
    	//任务ID
    	String taskId = "52504";
    	/**
    	 * 一、设置流程变量，使用基本数据类型
    	 */ 
//    	Integer days = (Integer)taskService.getVariable(taskId, "请假天数");
//    	Date date = (Date)taskService.getVariable(taskId, "请假日期");    	
//    	String reason = (String)taskService.getVariable(taskId, "请假原因");   	
//    	System.out.println("请假天数：" + days);
//    	System.out.println("请假日期：" + date);
//    	System.out.println("请假原因：" + reason);
    	
    	Person p = (Person)taskService.getVariable(taskId, "人员信息（添加固定版本）");
    	System.out.println(p.getId() + "    " + p.getName());
    	
    	
    }
    
    /**
     * 模拟设置和获取流程变量的场景
     */
    public void setAndGetVariables() {
    	/**
    	 * 与流程实例，执行对象（正在执行）
    	 */
    	RuntimeService runtimeService = processEngine.getRuntimeService();
    	/**
    	 * 与任务（正在执行）
    	 */
    	TaskService taskService = processEngine.getTaskService();
    	
    	/**
    	 * 设置流程变量
    	 */
//    	runtimeService.setVariable(executionId, variableName, value);//表示使用执行对象ID，和流程能量的名称，设置流程变量的值（一次只能设置一个值）
//    	runtimeService.setVariables(executionId, variables);//表示使用执行对象ID，和Map集合设置流程变量，map集合的key就是流程变量的名称，map集合的value就是流程变量的值（一次设置多个值）
    	
//    	taskService.setVariable(taskId, variableName, value);//表示使用任务ID，和流程能量的名称，设置流程变量的值（一次只能设置一个值）
//    	taskService.setVariables(taskId, variables);//表示使用任务ID，和Map集合设置流程变量，map集合的key就是流程变量的名称，map集合的value就是流程变量的值（一次设置多个值）
    	
//    	runtimeService.startProcessInstanceByKey(processDefinitionKey, variables);//启动流程实例的同时，可以设置流程变量，用Map集合
//    	taskService.complete(taskId, variables);//完成任务的同时，设置流程变量，用Map集合
    	
    	/**
    	 * 获取流程变量
    	 */
//    	runtimeService.getVariable(executionId, variableName);//使用执行对象ID和流程变量的名称，获取流程变量的值
//    	runtimeService.getVariables(executionId);//使用执行对象ID，获取所有的流程变量，将流程变量放置到Map集合中，map集合的key就是流程变量的名称，map集合的value就是流程变量的值
//    	runtimeService.getVariables(executionId, variableNames)//使用执行对象ID，获取流程变量的值，通过设置流程变量的名称存放到集合中，获取指定流程变量名称的流程变量的值，值存放到Map集合中

//    	taskService.getVariable(taskId, variableName);//使用任务ID和流程变量的名称，获取流程变量的值
//    	taskService.getVariables(taskId);//使用任务ID，获取所有的流程变量，将流程变量放置到Map集合中，map集合的key就是流程变量的名称，map集合的value就是流程变量的值
//    	taskService.getVariables(taskId, variableNames)//使用任务ID，获取流程变量的值，通过设置流程变量的名称存放到集合中，获取指定流程变量名称的流程变量的值，值存放到Map集合中

    }
    
    /**
     * 完成我的任务
     */
    @Test
    public void completeMyPersonalTask(){
        //任务ID
    	String taskId = "65002";
        processEngine.getTaskService()
                     .complete(taskId);
        System.out.println("完成任务，任务ID ： " + taskId);
    }
 
    /**
     * 查询流程变量的历史表
     */
    @Test
    public void findHistoryProcessVariables() {
    	List<HistoricVariableInstance> list = processEngine.getHistoryService()
    			.createHistoricVariableInstanceQuery()
    			.variableName("请假天数")
    			.list();
    	if(list != null && list.size() > 0) {
    		for(HistoricVariableInstance hvi : list) {
    			System.out.println(hvi.getId() + "     " + hvi.getProcessInstanceId() + "     " + hvi.getVariableName() + "     " + hvi.getVariableTypeName() + "    " + hvi.getValue());
    			System.out.println("##################################################");
    		}
    	}
    }
 
}
