/*******************************************************************************
 * Copyright (c) 2004, 2007 Mylyn project committers and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package com.itsolut.mantis.tests;

import junit.framework.TestCase;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.mylyn.commons.net.AuthenticationCredentials;
import org.eclipse.mylyn.commons.net.AuthenticationType;
import org.eclipse.mylyn.internal.tasks.core.TaskRepositoryManager;
import org.eclipse.mylyn.internal.tasks.core.TaskTask;
import org.eclipse.mylyn.internal.tasks.ui.TasksUiPlugin;
import org.eclipse.mylyn.tasks.core.ITask;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.AbstractTaskDataHandler;

import com.itsolut.mantis.core.IMantisClient;
import com.itsolut.mantis.core.MantisCorePlugin;
import com.itsolut.mantis.core.MantisRepositoryConnector;
import com.itsolut.mantis.core.IMantisClient.Version;

/**
 * 
 */
public class MantisTaskDataHandlerTest extends TestCase {
	
	private String defaultRepositoryUrl = "http://www.mantisbt.org/bugs/api/soap/mantisconnect.php";

	private MantisRepositoryConnector connector;

	private TaskRepository repository;

	private TaskRepositoryManager manager;

	private AbstractTaskDataHandler taskDataHandler;

	public MantisTaskDataHandlerTest() {
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		manager = TasksUiPlugin.getRepositoryManager();
		manager.clearRepositories(TasksUiPlugin.getDefault().getRepositoriesFilePath());

		connector = (MantisRepositoryConnector) manager.getRepositoryConnector(MantisCorePlugin.REPOSITORY_KIND);
//		TasksUiPlugin.getSynchronizationScheduler().synchronize(
//				manager.getDefaultRepository(MantisCorePlugin.REPOSITORY_KIND));

		taskDataHandler = connector.getTaskDataHandler();
	}

	protected void init(String url, Version version) {

		AuthenticationCredentials creds = new AuthenticationCredentials("reporter", "reporter");

		repository = new TaskRepository(MantisCorePlugin.REPOSITORY_KIND, url);
		repository.setCredentials(AuthenticationType.REPOSITORY, creds, false);
		repository.setTimeZoneId(IMantisClient.TIME_ZONE);
		repository.setCharacterEncoding(IMantisClient.CHARSET);
		repository.setVersion(version.name());

		manager.addRepository(repository);
	}

//	public void testGetSubTasks() throws CoreException {
//		
//		init(MantisTestConstants.TEST_MANTIS_HTTP_URL, Version.MC_1_0a5);
//		
//		TaskData taskData = connector.getTaskData(repository, "71", new NullProgressMonitor());
//		
//		Set<String> subTaskids = new HashSet<String>();
//		subTaskids.add("72");
//		subTaskids.add("57");
//		
//		assertEquals("Failed retrieving sub task ids", subTaskids, taskDataHandler.getSubTaskIds(taskData));
//	}

//	private void getChangedSinceLastSync() throws Exception {
//		
//		TasksUiPlugin.getSynchronizationScheduler().synchronize(repository);
//		TaskData taskData = TasksUiPlugin.getTaskDataManager().setNewTaskData(taskData); getNewTaskData(task.getRepositoryUrl(),
//				task.getTaskId());
//
//		int lastModified = Integer.parseInt(taskData.getLastModified());
//
//		Set<AbstractTask> tasks = new HashSet<AbstractTask>();
//		tasks.add(task);
//
//		assertEquals(null, repository.getSynchronizationTimeStamp());
//		boolean changed = connector.markStaleTasks(repository, tasks, new NullProgressMonitor());
//		assertTrue(changed);
//		assertTrue(task.isStale());
//
//		// always returns the ticket because time comparison mode is >=
//		task.setStale(false);
//		repository.setSynchronizationTimeStamp(lastModified + "");
//		changed = connector.markStaleTasks(repository, tasks, new NullProgressMonitor());
//		assertTrue(changed);
//		assertTrue(task.isStale());
//
//		task.setStale(false);
//		repository.setSynchronizationTimeStamp((lastModified + 1) + "");
//		changed = connector.markStaleTasks(repository, tasks, new NullProgressMonitor());
//		assertTrue(changed);
//		assertFalse(task.isStale());
//
//		// change ticket making sure it gets a new change time
//		Thread.sleep(1000);
//		IMantisClient client = connector.getClientManager().getRepository(repository);
//		MantisTicket ticket = client.getTicket(1);
//		if (ticket.getValue(Key.DESCRIPTION).equals(lastModified + "")) {
//			ticket.putBuiltinValue(Key.DESCRIPTION, lastModified + "x");
//		} else {
//			ticket.putBuiltinValue(Key.DESCRIPTION, lastModified + "");
//		}
//		client.updateTicket(ticket, "comment");
//
//		task.setStale(false);
//		repository.setSynchronizationTimeStamp((lastModified + 1) + "");
//		changed = connector.markStaleTasks(repository, tasks, new NullProgressMonitor());
//		assertTrue(changed);
//		assertTrue(task.isStale());
//	}

	public void testNonNumericTaskId() {
		try {
			connector.getTaskData(repository, "abc", new NullProgressMonitor());
			fail("Expected CoreException");
		} catch (CoreException e) {
		}
	}
	
	public void testUnableToCloneNullTask() {
		
		assertFalse(taskDataHandler.canInitializeSubTaskData(repository, null));
	}
	
	public void testUnableToCloneTaskWithoutProperKey() {
		
		assertFalse(taskDataHandler.canInitializeSubTaskData(repository, newDummyTask()));
	}

	private ITask newDummyTask() {
		
		return new TaskTask(MantisCorePlugin.REPOSITORY_KIND, defaultRepositoryUrl, "1");
	}
	
	public void testAbleToCloneTaskWithProperKey() {
		
		ITask task = newDummyTask();
		task.setAttribute(MantisRepositoryConnector.TASK_KEY_SUPPORTS_SUBTASKS, Boolean.TRUE.toString());
		assertTrue(taskDataHandler.canInitializeSubTaskData(repository, task));
	}
	
//	public void testPostTaskDataInvalidCredentials() throws Exception {
//		init(MantisTestConstants.TEST_MANTIS_HTTP_URL, Version.MC_1_0a5);
//		postTaskDataInvalidCredentials();
//	}		

//	private void postTaskDataInvalidCredentials() throws Exception {
//		MantisTask task = (MantisTask) connector.createTaskFromExistingId(repository, 60 + "",
//				new NullProgressMonitor());
//		TasksUiPlugin.getSynchronizationScheduler().synchronize(repository);
//		TaskData taskData = TasksUiPlugin.getTaskDataManager().getNewTaskData(task.getRepositoryUrl(),
//				task.getTaskId());
//		
//		taskData.setNewComment("new comment");
//		repository.setAuthenticationCredentials("foo", "bar");
//		try {
//			taskDataHandler.postTaskData(repository, taskData, new NullProgressMonitor());
//		} catch (CoreException expected) {
//			assertEquals(RepositoryStatus.ERROR_REPOSITORY_LOGIN, expected.getStatus().getCode());
//		}
//		assertEquals("new comment", taskData.getNewComment());
//	}

//	public void testAttachmentURL() throws CoreException {
//		
//		init(MantisTestConstants.TEST_MANTIS_HTTP_URL, Version.MC_1_0a5);
//		
//		TaskData taskData = connector.getTaskData(repository, "77", new NullProgressMonitor());
//	    MantisAttachmentHandler attachment = new MantisAttachmentHandler(connector);
//	    attachment.
//		assertEquals(1, taskData.getRoot().getAttribute(TaskAttribute.) .getAttachments().size());
//		assertEquals(MantisTestConstants.TEST_MANTIS_WEB_URL + "file_download.php?type=bug&file_id=24", taskData.getAttachments().get(0).getUrl());
//		
//	}

}
