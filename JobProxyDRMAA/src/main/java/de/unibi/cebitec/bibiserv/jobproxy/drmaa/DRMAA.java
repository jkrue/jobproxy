/*
 * Copyright 2016 Jan Krueger.
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
package de.unibi.cebitec.bibiserv.jobproxy.drmaa;

import de.unibi.cebitec.bibiserv.jobproxy.model.JobProxyInterface;
import de.unibi.cebitec.bibiserv.jobproxy.model.exceptions.FrameworkException;
import de.unibi.cebitec.bibiserv.jobproxy.model.framework.URLProvider;
import de.unibi.cebitec.bibiserv.jobproxy.model.state.State;
import de.unibi.cebitec.bibiserv.jobproxy.model.state.States;
import de.unibi.cebitec.bibiserv.jobproxy.model.task.Task;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.ggf.drmaa.DrmaaException;
import org.ggf.drmaa.JobTemplate;
import org.ggf.drmaa.Session;

/**
 * Batch grid systems like [S|O|U] Grid Engine or Torque are still popular and
 * often available/used on non "cloud" compute clusters to distribute job
 * requests. Most batch grid systems supports the DRMAA API for a general -
 * framework independent - way of job control. Since the latest DRMAA
 * specification (version 2) is only supported by the Univa Grid Engine (June
 * 2016) this plugin should use the widely spread version 1.
 *
 *
 * @author Jan Krueger -jkrueger(at)cebitec.uni-bielefeld.de
 */
public class DRMAA extends JobProxyInterface {

    Session session;
    JobTemplate jobTemplate;

    Map<String, Task> taskhash = new HashMap<>();

    public DRMAA(URLProvider urlprovider) throws DrmaaException {
        super(urlprovider);
        session = DRMAASession.getInstance();

    }

    @Override
    public String addTask(Task t) throws FrameworkException {

        StringBuilder nativeSpecs = new StringBuilder();
        List<String> complexes = new ArrayList<>();

        try {
            // build jobTemplate from Task
            jobTemplate = session.createJobTemplate();
            jobTemplate.setOutputPath(t.getStdout());
            jobTemplate.setErrorPath(t.getStderr());
            // use grid engine parallel environment if task specifies more than 1 cpu 
            if (t.getCores() != null && t.getCores() > 2) {
                nativeSpecs.append(" -pe multislot ").append(t.getCores());
            }
            if (t.getMemory() != null) {
                nativeSpecs.append(" -l vf=").append(t.getMemory()).append("G");
            }
            if (t.getCputime() != null) {
                nativeSpecs.append(" -l h_cpu=");
                if (t.getCputime() < 10) {
                    nativeSpecs.append("0").append(t.getCputime());
                } else {
                    nativeSpecs.append(t.getCputime());
                }
                nativeSpecs.append(":00:00");
            }

            // set nativeSpecs
            if (nativeSpecs.length() > 0) {
                jobTemplate.setNativeSpecification(nativeSpecs.toString());
            }

            // submit jobTemplate
            String id = session.runJob(jobTemplate);
            // store jobid 
            taskhash.put(id, t);
            // remove job template since it is not longer needed
            session.deleteJobTemplate(jobTemplate);
            // and return job id
            return id;

        } catch (DrmaaException e) {
            throw new FrameworkException("DRMAA exception occured while call 'addTask'", e);
        }
    }

    @Override
    public Task getTask(String id) throws FrameworkException {
        return taskhash.get(id);
    }

    @Override
    public void delTask(String id) throws FrameworkException {
        taskhash.remove(id);
        try {
            session.control(id, Session.TERMINATE);
        } catch (DrmaaException e) {
            throw new FrameworkException("DRMAA exception occurred while call 'delTask'", e);
        }
    }

    @Override
    public State getState(String id) throws FrameworkException {

        try {
            State state = new State();
            state.setId(id);
            state.setCode(Integer.toString(session.getJobProgramStatus(id)));
            state.setDescription(statustoString(session.getJobProgramStatus(id)));
            return state;
        } catch (DrmaaException e) {
            throw new FrameworkException("DRMAA exception occurred while call 'getState'", e);
        }

    }

    @Override
    public States getState() throws FrameworkException {   
        States states = new States();
        for (String id : taskhash.keySet()) {
            states.getState().add(getState(id));
        }
        return states;
    }

    public String statustoString(int jobstatus) {
        switch (jobstatus) {
            case Session.QUEUED_ACTIVE:
                return "Job is pending";
            case Session.SYSTEM_ON_HOLD:
                return "Job is on hold by system.";
            case Session.USER_ON_HOLD:
                return "Job is on hold by user.";
            case Session.USER_SYSTEM_ON_HOLD:
                return "Job is on hold by system or user.";
            case Session.RUNNING:
                return "Job is running";
            case Session.SYSTEM_SUSPENDED:
                return "Job is suspended by system.";
            case Session.USER_SUSPENDED:
                return "Job is suspended by user.";
            case Session.USER_SYSTEM_SUSPENDED:
                return "Job is suspended by system or user.";
            case Session.DONE:
                return "Job has completed";
            case Session.FAILED:
                return "Job has failed.";
        }
        return "Job status is unknown";

    }
}